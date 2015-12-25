package com.gagauz.tracker.utils;

public enum SysEnv {
    JDBC_USERNAME("b4f"),
    JDBC_PASSWORD("office"),
    JDBC_URL("jdbc:mysql://localhost:3306/tracker"),
    JDBC_DRIVER("com.mysql.jdbc.Driver"),
    JDBC_DIALECT("org.hibernate.dialect.MySQL5InnoDBDialect"),
    MAIL_TEMPLATE_DIR(null),
    TEST_DATA("false"),
    CONFIG_CLASS("ProductionConfiguration");

    private String value;
    private final String defaultValue;

    SysEnv(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        if (null == value) {
            value = System.getProperty(name(), System.getenv(name()));
            if (null == value) {
                if (null == defaultValue) {
                    throw new IllegalStateException("No system variable with name " + name() + " present!");
                }
                value = defaultValue;
            }
        }
        return value;
    }
}
