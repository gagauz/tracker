package com.gagauz.tracker.web.components;

import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.db.model.*;
import com.gagauz.tracker.utils.Comparators;
import com.gagauz.tracker.web.services.ToolsService;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import java.util.*;
import java.util.Map.Entry;

public class VersionUserMap {

    @Parameter(allowNull = false, required = true, principal = true)
    private Version version;

    @Component(parameters = {"id=literal:ticketZone", "show=popup", "update=popup"})
    private Zone ticketZone;

    @Property
    private Feature feature;

    private User user;

    private Ticket ticket;

    @Property
    private int estimated;

    @Property
    private int progress;

    @Property
    private Ticket viewTicket;

    @Property
    private Entry<TicketStatus, Integer> entry;

    @Inject
    private FeatureVersionDao featureVersionDao;

    @Inject
    private TicketDao ticketDao;

    @Inject
    private UserDao userDao;

    @SessionState
    private User securityUser;

    @Inject
    private ComponentResources resources;

    @Inject
    private ToolsService toolsService;

    private int endTime;
    private int rowEndTime;
    private int minTime;

    private Map<User, List<Ticket>> userTicketMap;
    private Map<User, Map<TicketStatus, Integer>> userTicketStatus;

    private Map<User, Integer[]> userTotalTimes;

    @Cached
    public Collection<User> getUsers() {
        if (null == userTicketMap) {
            userTicketMap = CollectionFactory.newMap();
            userTicketStatus = CollectionFactory.newMap();
            userTotalTimes = CollectionFactory.newMap();
            minTime = Integer.MAX_VALUE;
            for (Ticket ticket : ticketDao.findByVersion(version)) {
                List<Ticket> tickets = userTicketMap.get(ticket.getOwner());

                Integer[] times = userTotalTimes.get(ticket.getOwner());
                if (null == times) {
                    times = new Integer[] {0, 0};
                    userTotalTimes.put(ticket.getOwner(), times);
                }

                times[0] += ticket.getEstimate();
                times[1] += ticket.getProgress();

                Map<TicketStatus, Integer> statusMap = userTicketStatus.get(ticket.getOwner());
                if (null == statusMap) {
                    statusMap = CollectionFactory.newMap();
                    userTicketStatus.put(ticket.getOwner(), statusMap);
                }
                Integer i = statusMap.get(ticket.getStatus());
                if (null == i) {
                    i = 0;
                }
                ++i;
                statusMap.put(ticket.getStatus(), i);

                if (null == tickets) {
                    tickets = new ArrayList<>();
                    userTicketMap.put(ticket.getOwner(), tickets);
                }
                tickets.add(ticket);
                minTime = Math.min(minTime, ticket.getEstimate() - ticket.getProgress());
            }
            minTime = 80 / (minTime + 1);
        }
        List<User> users = new ArrayList<User>(userTicketMap.keySet());
        Collections.sort(users, Comparators.USER_BY_NAME_COMPARATOR);

        return users;
    }

    public int getTicketsWidth() {
        return rowEndTime;
    }

    public Collection<Ticket> getUserTickets() {
        return userTicketMap.get(user);
    }

    public Set<Entry<TicketStatus, Integer>> getStatuses() {
        return userTicketStatus.get(user).entrySet();
    }

    public Integer getTotalEstimate() {
        return userTotalTimes.get(user)[0];
    }

    public Integer getTotalProgress() {
        return userTotalTimes.get(user)[1];
    }

    public String getRemainTime() {
        Integer[] times = userTotalTimes.get(user);
        if (times[0] == 0) {
            return times[1] == 0 ? "0" : "∞";
        }
        return toolsService.getTime(times[0] - times[1]);
    }

    void onCreateFeatureVersion(Feature feature, Version version) {
        FeatureVersion featureVersion = new FeatureVersion();
        featureVersion.setFeature(feature);
        featureVersion.setVersion(version);
        User user = new User();
        int id = securityUser.getId();
        user.setId(id);
        featureVersion.setCreator(user);
        featureVersionDao.save(featureVersion);
    }

    public boolean isDraggable() {
        return true;
    }

    public String getTicketsTime() {
        return toolsService.getTime(rowEndTime);
    }

    public String getTicketTime() {
        return toolsService.getTime(ticket.getEstimate() - ticket.getProgress());
    }

    public String getEventUrl() {
        return resources.createEventLink("change").toRedirectURI();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        endTime = Math.max(rowEndTime, endTime);
        rowEndTime = 0;

        this.user = user;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        rowEndTime += ticket.getEstimate() - ticket.getProgress();
        this.ticket = ticket;
    }

    void onChange(@RequestParameter(value = "user") Integer userId, @RequestParameter(value = "ticket") Integer ticketId) {
        User user = userDao.findById(userId);
        Ticket ticket = ticketDao.findById(ticketId);
        if (null != ticket) {
            ticket.setOwner(user);
        }
    }

    Object onViewTicket(Ticket ticket) {
        viewTicket = ticket;
        return ticketZone.getBody();
    }

}
