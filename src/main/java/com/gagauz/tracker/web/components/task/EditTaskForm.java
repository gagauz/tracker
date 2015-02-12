package com.gagauz.tracker.web.components.task;

import com.gagauz.tapestry.security.api.SecurityUser;
import com.gagauz.tracker.beans.dao.TaskDao;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.db.model.Task;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.utils.Param;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ValueEncoderSource;

import java.util.List;

public class EditTaskForm {

    @Component
    private Zone zone;

    @Component
    private Form form;

    @Parameter(name = "task")
    private Task taskParam;

    private Task task;

    @Inject
    private ComponentResources componentResources;

    @Inject
    private TaskDao taskDao;

    @Inject
    private UserDao userDao;

    @Inject
    private ValueEncoderSource valueEncoderSource;

    @SessionState
    private SecurityUser user;

    boolean setupRender() {
        if (null != taskParam) {
            task = taskParam;
        }
        return task != null;
    }

    void onSubmitFromForm() {
        getTask().setAuthor((User) user);
        taskDao.save(getTask());
    }

    List<User> onProvideCompletions(String username) {
        return userDao.findByQuery("from User u where username like :name or name like :name", Param.param("name", username + '%'));
    }

    @Cached
    public FieldTranslator<User> getUsernameTranslator() {
        final ValueEncoder<User> encoder = valueEncoderSource.getValueEncoder(User.class);
        return new FieldTranslator<User>() {

            @Override
            public String toClient(User value) {
                return encoder.toClient(value);
            }

            @Override
            public void render(MarkupWriter writer) {

            }

            @Override
            public User parse(String input) throws ValidationException {
                return encoder.toValue(input);
            }

            @Override
            public Class<User> getType() {
                return User.class;
            }
        };
    }

    public Task getTask() {
        if (null == task) {
            task = new Task();
        }
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
