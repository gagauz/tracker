package com.gagauz.tracker.web.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.Ajax;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.gagauz.tapestry.web.services.ToolsService;

import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TicketStatus;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.utils.Comparators;

@Import(module = "bootstrap/dropdown")
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

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    private int endTime;
    private int rowEndTime;
    private int minTime;

    @Property
    private TicketStatus status;

    private Set<TicketStatus> allStatuses;
    private Map<User, List<Ticket>> userTicketMap;
    private Map<User, Map<TicketStatus, Integer>> userTicketStatus;

    private Map<User, Integer[]> userTotalTimes;

    @Cached
    public Collection<User> getUsers() {
        if (null == userTicketMap) {
            allStatuses = CollectionFactory.newSet();
            userTicketMap = CollectionFactory.newMap();
            userTicketStatus = CollectionFactory.newMap();
            userTotalTimes = CollectionFactory.newMap();
            minTime = Integer.MAX_VALUE;
            for (Ticket ticket : ticketDao.findByVersion(version)) {
                allStatuses.add(ticket.getStatus());
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

    public Collection<TicketStatus> getFilterStatuses() {
        getUsers();
        return allStatuses;
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
        featureVersion.getId().setFeature(feature);
        featureVersion.getId().setVersion(version);
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

    void afterRender() {
        javaScriptSupport.require("page/VersionUserMap").invoke("init").with(getEventUrl());
    }

    void onChange(@RequestParameter(value = "user") Integer userId, @RequestParameter(value = "ticket") Integer ticketId) {
        User user = userDao.findById(userId);
        Ticket ticket = ticketDao.findById(ticketId);
        if (null != ticket) {
            ticket.setOwner(user);
        }
    }

    @Ajax
    void onViewTicket(Ticket ticket) {
        viewTicket = ticket;
        ajaxResponseRenderer
                .addRender(Layout.MODAL_BODY_ID, ticketZone.getBody())
                .addCallback(new JavaScriptCallback() {
                    @Override
                    public void run(JavaScriptSupport javascriptSupport) {
                        javascriptSupport.require("modal").invoke("showModal").with(Layout.MODAL_ID);
                    }
                });
    }

}
