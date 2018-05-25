package com.gagauz.tracker.utils;

import com.gagauz.tracker.config.Constants;
import com.xl0e.util.IAppProperty;

public enum AppProperties implements IAppProperty<AppProperties> {
    JDBC_USERNAME("b4f"),
    JDBC_PASSWORD("office"),
    JDBC_URL("jdbc:mysql://localhost:3306/tracker"),
    JDBC_DRIVER("com.mysql.jdbc.Driver"),
    JDBC_DIALECT("com.xl0e.hibernate.utils.MySQL5InnoDBDialect2"),
    MAIL_TEMPLATE_DIR(null),
    FILL_TEST_DATA("false"),
    PROFILE(Constants.AppProfiles.PROD);

    private final String defaultValue;

    AppProperties(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        return getString();
    }
}
