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
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.Ajax;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.web.services.ToolsService;

import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TicketStatus;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.services.dao.FeatureDao;
import com.gagauz.tracker.services.dao.TicketDao;
import com.gagauz.tracker.services.dao.UserDao;
import com.gagauz.tracker.utils.ColorMap;
import com.gagauz.tracker.utils.Comparators;

@Import(module = "bootstrap/dropdown")
public class VersionUserMap {

    @Parameter(allowNull = false, required = true, principal = true)
    private Version version;

    @Component(parameters = { "id=literal:ticketZone", "show=popup", "update=popup" })
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
    private FeatureDao featureDao;

    @Inject
    private TicketDao ticketDao;

    @Inject
    private Messages messages;

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
        if (null == this.userTicketMap) {
            this.allStatuses = CollectionFactory.newSet();
            this.userTicketMap = CollectionFactory.newMap();
            this.userTicketStatus = CollectionFactory.newMap();
            this.userTotalTimes = CollectionFactory.newMap();
            this.minTime = Integer.MAX_VALUE;
            for (Ticket ticket : this.ticketDao.findByVersion(this.version)) {
                this.allStatuses.add(ticket.getStatus());
                List<Ticket> tickets = this.userTicketMap.get(ticket.getOwner());

                Integer[] times = this.userTotalTimes.get(ticket.getOwner());
                if (null == times) {
                    times = new Integer[] { 0, 0 };
                    this.userTotalTimes.put(ticket.getOwner(), times);
                }

                times[0] += ticket.getEstimate();
                times[1] += ticket.getProgress();

                Map<TicketStatus, Integer> statusMap = this.userTicketStatus.get(ticket.getOwner());
                if (null == statusMap) {
                    statusMap = CollectionFactory.newMap();
                    this.userTicketStatus.put(ticket.getOwner(), statusMap);
                }
                Integer i = statusMap.get(ticket.getStatus());
                if (null == i) {
                    i = 0;
                }
                ++i;
                statusMap.put(ticket.getStatus(), i);

                if (null == tickets) {
                    tickets = new ArrayList<>();
                    this.userTicketMap.put(ticket.getOwner(), tickets);
                }
                tickets.add(ticket);
                this.minTime = Math.min(this.minTime, ticket.getEstimate() - ticket.getProgress());
            }
            this.minTime = 80 / (this.minTime + 1);
        }
        List<User> users = new ArrayList<>(this.userTicketMap.keySet());
        Collections.sort(users, Comparators.USER_BY_NAME_COMPARATOR);

        return users;
    }

    public Collection<TicketStatus> getFilterStatuses() {
        getUsers();
        return this.allStatuses;
    }

    public int getTicketsWidth() {
        return this.rowEndTime;
    }

    public Collection<Ticket> getUserTickets() {
        return this.userTicketMap.get(this.user);
    }

    public Set<Entry<TicketStatus, Integer>> getStatuses() {
        return this.userTicketStatus.get(this.user).entrySet();
    }

    public Integer getTotalEstimate() {
        return this.userTotalTimes.get(this.user)[0];
    }

    public Integer getTotalProgress() {
        return this.userTotalTimes.get(this.user)[1];
    }

    public String getRemainTime() {
        Integer[] times = this.userTotalTimes.get(this.user);
        if (times[0] == 0) {
            return times[1] == 0 ? "0" : "∞";
        }
        return this.toolsService.getTime(times[0] - times[1]);
    }

    void onCreateFeatureVersion(Feature feature, Version version) {
        //        FeatureVersion featureVersion = new FeatureVersion();
        //        featureVersion.setFeature(feature);
        //        featureVersion.setVersion(version);
        //        User user = new User();
        //        int id = this.securityUser.getId();
        //        user.setId(id);
        //        featureVersion.setCreator(user);
        //        this.featureVersionDao.save(featureVersion);
    }

    public boolean isDraggable() {
        return true;
    }

    public String getTicketsTime() {
        return this.toolsService.getTime(this.rowEndTime);
    }

    public String getTicketTime() {
        return this.toolsService.getTime(this.ticket.getEstimate() - this.ticket.getProgress());
    }

    public String getEventUrl() {
        return this.resources.createEventLink("change").toRedirectURI();
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.endTime = Math.max(this.rowEndTime, this.endTime);
        this.rowEndTime = 0;

        this.user = user;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public void setTicket(Ticket ticket) {
        this.rowEndTime += ticket.getEstimate() - ticket.getProgress();
        this.ticket = ticket;
    }

    void afterRender() {
        this.javaScriptSupport.require("page/version_map").invoke("init").with(getEventUrl());
    }

    void onChange(@RequestParameter(value = "target") Integer userId, @RequestParameter(value = "ticket") Integer ticketId) {
        User user = userDao.findById(userId);
        Ticket ticket = ticketDao.findById(ticketId);
        if (null != user && null != ticket) {
            ticket.setOwner(user);
            ticketDao.save(ticket);
        }
    }

    @Ajax
    void onViewTicket(Ticket ticket) {
        this.viewTicket = ticket;
        this.ajaxResponseRenderer
                .addRender(Layout.MODAL_BODY_ID, this.ticketZone.getBody())
                .addCallback((JavaScriptCallback) javascriptSupport -> javascriptSupport.require("modal").invoke("showModal")
                        .with(Layout.MODAL_ID));
    }

    public String getBgColor() {
        return ColorMap.getColor(user.getId());
    }

}
