package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.beans.dao.TicketTypeDao;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.TicketType;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TicketTypeEdit {

    private static final Predicate<TicketType> PROJECT_NULL = new Predicate<TicketType>() {

        @Override
        public boolean test(TicketType t) {
            return null == t.getProject();
        }

    };

    @Component
    private BeanEditForm form;

    @Property
    private Project project;

    @Property
    private TicketType ticketTypeRow;

    @Property
    @Persist
    private TicketType ticketType;

    @Inject
    private TicketTypeDao ticketTypeDao;

    void onActivate(Project project) {
        this.project = project;
    }

    void onEdit(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    Object onPassivate() {
        return project;
    }

    @Cached
    public List<TicketType> getTicketType0() {
        return null == project ? ticketTypeDao.findCommon() : ticketTypeDao.findByProject(project);
    }

    @Cached
    public List<TicketType> getTicketType1() {
        return getTicketType0().stream().filter(PROJECT_NULL.negate()).collect(Collectors.<TicketType>toList());
    }

    @Cached
    public List<TicketType> getTicketType2() {
        return getTicketType0().stream().filter(PROJECT_NULL).collect(Collectors.<TicketType>toList());
    }

    void onSubmitFromForm() {
        if (form.isValid()) {
            ticketType.setProject(project);
            ticketTypeDao.save(ticketType);
        }
    }
}
