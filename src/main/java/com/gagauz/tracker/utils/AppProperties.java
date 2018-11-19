package com.gagauz.tracker.utils;

import com.xl0e.util.IAppProperty;

public enum AppProperties implements IAppProperty<AppProperties> {
    JDBC_USERNAME("b4f"),
    JDBC_PASSWORD("office"),
    JDBC_URL("jdbc:mysql://localhost:3306/tracker?autoReconnect=true&useUnicode=true&createDatabaseIfNotExist=true&characterEncoding=utf-8"),
    JDBC_DRIVER("com.mysql.jdbc.Driver"),
    JDBC_DIALECT("org.hibernate.dialect.MySQL5InnoDBDialect"),
    MAIL_TEMPLATE_DIR(null),
    FILL_TEST_DATA("false");

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
