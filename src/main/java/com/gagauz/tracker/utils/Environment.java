package com.gagauz.tracker.utils;

import com.gagauz.tracker.config.Constants;
import com.xl0e.util.IAppProperty;

public enum Environment implements IAppProperty<Environment> {
    JDBC_USERNAME("b4f"),
    JDBC_PASSWORD("office"),
    JDBC_URL("jdbc:mysql://localhost:3306/tracker?autoReconnect=true&useUnicode=true&createDatabaseIfNotExist=true&characterEncoding=utf-8"),
    JDBC_DRIVER("com.mysql.jdbc.Driver"),
    JDBC_DIALECT("com.xl0e.hibernate.utils.MySQL5InnoDBDialect2"),
    MAIL_TEMPLATE_DIR(null),
    FILL_TEST_DATA("false"),
    PROFILE(Constants.AppProfiles.DEV);

    private final String defaultValue;

    Environment(String defaultValue) {
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
