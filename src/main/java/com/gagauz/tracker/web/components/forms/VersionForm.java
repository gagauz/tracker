package com.gagauz.tracker.web.components.forms;

import com.gagauz.tracker.beans.dao.VersionDao;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Version;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;

public class VersionForm {

    @Parameter(required = true, allowNull = false)
    @Property
    private Project project;

    @Parameter(required = true)
    @Property
    private Version version;

    @Component(parameters = {"object=version", "exclude=id"})
    private BeanEditForm versionForm;

    @Inject
    private VersionDao versionDao;

    boolean setupRender() {
        return null != version;
    }

    Object onValidateFromVersionForm() {
        return null;
    }

    Object onSuccessFromVersionForm() {
        version.setProject(project);
        versionDao.save(version);
        return null;
    }
}
