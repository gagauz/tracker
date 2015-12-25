package com.gagauz.tracker.web.components;

import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.utils.Param;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.ValueEncoderSource;

import java.util.ArrayList;
import java.util.List;

public class UserAutocompleteField implements Field {

    @Component
    private TextField input;

    @Parameter(required = true, autoconnect = true, principal = true)
    private User user;

    @Inject
    protected ComponentDefaultProvider defaultProvider;

    @Inject
    protected ComponentResources resources;

    @Inject
    private ValueEncoderSource valueEncoderSource;

    @Inject
    private SelectModelFactory selectModelFactory;

    @Inject
    private UserDao userDao;

    @Environmental(false)
    protected FormSupport formSupport;

    List<JSONObject> onProvideCompletions(String username) {
        List<JSONObject> res = new ArrayList<>();
        for (User u : userDao.findByQuery("from User u where username like :name or name like :name or email like :name", Param.param("name", username + '%'))) {
            res.add(new JSONObject("id", u.getId(), "name", u.getName() + " - " + u.getEmail()));
        }
        return res;
    }

    @Cached
    public FieldTranslator<User> getIdTranslator() {
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

    @Override
    public String getClientId() {
        return input.getClientId();
    }

    @Override
    public String getControlName() {
        return input.getControlName();
    }

    @Override
    public String getLabel() {
        return defaultProvider.defaultLabel(resources);
    }

    @Override
    public boolean isDisabled() {
        return input.isDisabled();
    }

    @Override
    public boolean isRequired() {
        return input.isRequired();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
