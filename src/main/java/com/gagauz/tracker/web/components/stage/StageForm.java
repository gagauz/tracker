package com.gagauz.tracker.web.components.stage;

import com.gagauz.tracker.beans.dao.StageDao;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Stage;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.ArrayList;
import java.util.List;

public class StageForm {
    @Parameter(required = true, allowNull = false)
    @Property
    private Project project;

    private Stage stage;

    @Component(parameters = {"object=stage", "exclude=id,created,updated"})
    private BeanEditForm stageForm;

    @Inject
    private StageDao stageDao;

    boolean setupRender() {
        return null != project;
    }

    @Cached
    public SelectModel getStageSelectModel() {
        List<OptionModel> list = FactoryX.newArrayList();
        for (Stage stage : stageDao.findByProject(project, getStage().getId())) {
            list.add(new OptionModelImpl(stage.getName(), stage));
        }
        return new SelectModelImpl(null, list);
    }

    Object onValidateFromStageForm() {
        return null;
    }

    Object onSuccessFromStageForm() {
        stage.setProject(project);
        stageDao.save(stage);
        return null;
    }

    public Stage getStage() {
        if (null == stage) {
            stage = new Stage();
            stage.setProject(project);
        }
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
