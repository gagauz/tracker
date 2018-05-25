package com.gagauz.tracker.web.components.ticket;

import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Workflow;
import com.gagauz.tracker.services.dao.WorkflowDao;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

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

    private List<Workflow> comments;

    @Property
    private Workflow comment;

    private Workflow newComment;

    @SessionState(create = false)
    private User securityUser;

    @Inject
    private Request request;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private WorkflowDao workflowDao;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    boolean setupRender() {
        return ticket.getId() > 0;
    }

    Object onSubmitFromCommentForm(int id) {
        if (!commentForm.getHasErrors()) {
            newComment.setId(id);
            newComment.setAuthor(securityUser);
            newComment.setTicket(ticket);
            workflowDao.save(newComment);
            newComment = null;
            if (request.isXHR()) {
                ajaxResponseRenderer.addCallback(new JavaScriptCallback() {
                    @Override
                    public void run(JavaScriptSupport javascriptSupport) {
                        javascriptSupport.addScript("$j('%s').parent('popup').trigger('popupHide');", editZone.getClientId());
                    }
                });

            }
        }

        if (null != zone && request.isXHR()) {
            return zone.getBody();
        }
        return null;
    }

    Object onEdit(Workflow comment) {
        if (request.isXHR()) {
            newComment = comment;
            return editZone.getBody();
        }
        return null;
    }

    Object onDrop(Workflow comment) {
        workflowDao.delete(comment);
        if (null != zone && request.isXHR()) {
            return zone.getBody();
        }
        return null;
    }

    public List<Workflow> getComments() {
        if (null != ticket && null == comments) {
            comments = workflowDao.findCommentsByTicket(ticket);
        }
        return comments;
    }

    public Workflow getNewComment() {
        if (null == newComment) {
            newComment = new Workflow();
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
