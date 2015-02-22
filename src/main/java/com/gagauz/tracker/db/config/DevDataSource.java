package com.gagauz.tracker.db.config;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

import java.sql.Driver;
import java.util.Properties;

public class DevDataSource extends SimpleDriverDataSource implements DataSource {

    public DevDataSource() {
        try {
            setDriverClass((Class<? extends Driver>) Class.forName(System.getProperty("tracker.jdbc-driver")));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        setUrl(System.getProperty("tracker.jdbc-url"));
        setUsername(System.getProperty("tracker.db-username", "b4f"));
        setPassword("office");
        Properties props = new Properties();
        props.setProperty("cacheServerConfiguration", "true");
        props.setProperty("characterSetResults", "UTF-8");
        props.setProperty("useLocalSessionState", "true");
        props.setProperty("statementInterceptors", "com.gagauz.tracker.beans.setup.StatementInterceptor");
        setConnectionProperties(props);
    }
}
