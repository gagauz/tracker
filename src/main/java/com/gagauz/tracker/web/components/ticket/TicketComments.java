package com.gagauz.tracker.web.components.ticket;

import com.gagauz.tracker.beans.dao.TicketCommentDao;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TicketComment;
import com.gagauz.tracker.db.model.User;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.gagauz.tapestry.security.api.SecurityUser;

import java.util.List;

public class TicketComments {

    @Component
    private Form commentForm;

    @Parameter
    private Ticket ticket;

    @Parameter
    private Zone zone;

    @Component(parameters = {"id=literal:editZone", "show=popup", "update=popup"})
    private Zone editZone;

    private List<TicketComment> comments;

    @Property
    private TicketComment comment;

    //    @Persist
    //("flash")
    private TicketComment newComment;

    @SessionState(create = false)
    private SecurityUser securityUser;

    @Inject
    private TicketCommentDao ticketCommentDao;

    @Inject
    private Request request;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    Object onSubmitFromCommentForm(int id) {
        if (!commentForm.getHasErrors()) {
            newComment.setId(id);
            newComment.setAuthor((User) securityUser);
            newComment.setTicket(ticket);
            ticketCommentDao.save(newComment);
            newComment = null;
            javaScriptSupport.addInitializerCall("$j('%s').parent('popup').trigger('popupHide');", editZone.getClientId());
        }

        if (null != zone && request.isXHR()) {
            return zone.getBody();
        }
        return null;
    }

    Object onEdit(TicketComment comment) {
        if (request.isXHR()) {
            newComment = comment;
            return editZone.getBody();
        }
        return null;
    }

    Object onDrop(TicketComment comment) {
        ticketCommentDao.delete(comment);
        if (null != zone && request.isXHR()) {
            return zone.getBody();
        }
        return null;
    }

    public List<TicketComment> getComments() {
        if (null != ticket && null == comments) {
            comments = ticketCommentDao.findByTicket(ticket);
        }
        return comments;
    }

    public TicketComment getNewComment() {
        if (null == newComment) {
            newComment = new TicketComment();
        }
        return newComment;
    }

    public boolean isUserComment() {
        return null != securityUser && comment.getAuthor().equals(securityUser);
    }

    public String getParentZoneId() {
        return null != zone ? zone.getClientId() : null;
    }
}
