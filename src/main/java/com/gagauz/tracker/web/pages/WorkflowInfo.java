package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.TicketStatusDao;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.TicketStatus;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.ArrayList;
import java.util.List;

public class WorkflowInfo {
    @Property
    private Project project;

    @Property
    private TicketStatus entityRow;

    @Property
    @Persist
    private TicketStatus editEntity;

    @Inject
    private TicketStatusDao dao;

    @Inject
    private Messages messages;

    void onEdit(Project project, TicketStatus status) {
        this.project = project;
        if (null != status) {
            this.editEntity = status;
        }
    }

    void onCreate(Project project) {
        editEntity = new TicketStatus();
        editEntity.setProject(project);
    }

    void onReset() {
        editEntity = null;
    }

    void onActivate(Project project) {
        onEdit(project, null);
    }

    void onActivate(Project project, TicketStatus status) {
        onEdit(project, status);
    }

    void onSuccessFromForm() {
        dao.save(editEntity);
        editEntity = null;
    }

    Object onPassivate() {
        return new Object[] {project, editEntity};
    }

    @Cached
    public List<TicketStatus> getEntities() {
        if (null != project) {
            return dao.findByProject(project);
        }
        return dao.findAll();
    }

    @Cached
    public SelectModel getModelFrom() {
        List<TicketStatus> list = dao.findByProject(project);
        List<OptionModel> options = new ArrayList<OptionModel>(list.size());
        for (TicketStatus s : list) {
            options.add(new OptionModelImpl(s.getName(), s.getId()));
        }
        return new SelectModelImpl(null, options);
    }

    @Cached
    public SelectModel getModelTo() {
        List<TicketStatus> list = dao.findByProject(project);
        List<OptionModel> options = new ArrayList<OptionModel>(list.size());
        for (TicketStatus s : list) {
            options.add(new OptionModelImpl(s.getName(), s.getId()));
        }
        return new SelectModelImpl(null, options);
    }

    @Cached
    public String getProjectName() {
        return editEntity.getProject() != null ? editEntity.getProject().getName() : messages.get("any");
    }
}
