package com.gagauz.tracker.web.components.ticket;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.web.components.BeanForm;
import org.apache.tapestry5.web.config.Global;
import org.apache.tapestry5.web.services.model.CollectionGridDataSourceRowTypeFix;

import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.UserGroup;
import com.gagauz.tracker.db.model.TicketType;
import com.gagauz.tracker.services.dao.UserGroupDao;
import com.gagauz.tracker.services.dao.TicketTypeDao;

public class TicketTypeEdit {

    @Component
    private BeanForm form;

    @Property
    private TicketType ticketTypeRow;

    @Parameter
    @Property
    private TicketType object;

    @Inject
    private TicketTypeDao ticketTypeDao;

    @Inject
    private UserGroupDao roleGroupDao;

    @Inject
    private SelectModelFactory modelFactory;

    void onEdit(TicketType ticketType) {
        this.object = ticketType;
    }

    public Project getProject() {
        return Global.peek(Project.class);
    }

    @Cached
    public GridDataSource getTicketType1() {
        return new CollectionGridDataSourceRowTypeFix<>(ticketTypeDao.findByProject(getProject()), TicketType.class);
    }

    void onSubmitFromForm() {
        if (form.isValid()) {
            if (null == object.getProject()) {
                object.setProject(getProject());
            }
            ticketTypeDao.save(object);
        }
    }

    @Cached
    public SelectModel getGroupModel() {
        List<OptionModel> list = new ArrayList<>();
        for (UserGroup roleGroup : roleGroupDao.findByProject(getProject())) {
            list.add(new OptionModelImpl(roleGroup.getName(), roleGroup));
        }
        return new SelectModelImpl(list.toArray(new OptionModel[list.size()]));
    }
}
