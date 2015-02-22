package com.gagauz.tracker.db.config;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

import java.sql.Driver;
import java.util.Map.Entry;
import java.util.Properties;

public class DevDataSource extends SimpleDriverDataSource implements DataSource {

    public DevDataSource() {
        for (Entry e : System.getProperties().entrySet()) {
            System.out.format("%1$40s = %2$s\n", e.getKey(), e.getValue());
        }
        try {
            setDriverClass((Class<? extends Driver>) Class.forName(System.getProperty("tracker.jdbc-driver")));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        setUrl(System.getProperty("tracker.jdbc-url"));
        setUsername(System.getProperty("tracker.db-username", "b4f"));
        setPassword(System.getProperty("tracker.db-password", "office"));
        Properties props = new Properties();
        props.setProperty("cacheServerConfiguration", "true");
        props.setProperty("characterSetResults", "UTF-8");
        props.setProperty("useLocalSessionState", "true");
        props.setProperty("statementInterceptors", "com.gagauz.tracker.beans.setup.StatementInterceptor");
        setConnectionProperties(props);
    }
}
