package com.gagauz.tracker.web.components.ticket;

import java.util.stream.Collectors;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.web.services.model.CollectionGridDataSourceRowTypeFix;

import com.gagauz.tracker.beans.dao.TicketStatusDao;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.TicketStatus;

public class TicketStatusEdit {
    @Parameter(autoconnect = true)
    @Property
    private TicketStatus object;

    @Parameter(autoconnect = true)
    private Project project;

    @Property
    private TicketStatus row;

    @Inject
    private TicketStatusDao ticketStatusDao;

    @Inject
    private SelectModelFactory selectModelFactory;

    void onEdit(TicketStatus status) {
        this.object = status;
    }

    public GridDataSource getStatuses() {
        return new CollectionGridDataSourceRowTypeFix<>(ticketStatusDao.findByProject(project), TicketStatus.class);
    }

    public SelectModel getModel() {
        return selectModelFactory.create(
                ticketStatusDao.findByProject(project).stream().filter(st -> !st.equals(object)).collect(Collectors.toList()), "name");
    }
}
