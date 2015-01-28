package com.gagauz.tracker.db.config;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

import java.util.Properties;

public class DevDataSource extends SimpleDriverDataSource implements DataSource {

    public DevDataSource() {
        setDriverClass(com.mysql.jdbc.Driver.class);
        setUrl("jdbc:mysql://localhost:3306/tracker?autoReconnect=true");
        setUsername("b4f");
        setPassword("office");
        Properties props = new Properties();
        props.setProperty("cacheServerConfiguration", "true");
        props.setProperty("characterSetResults", "UTF-8");
        props.setProperty("useLocalSessionState", "true");
        props.setProperty("statementInterceptors", "com.gagauz.tracker.beans.setup.StatementInterceptor");
        setConnectionProperties(props);
    }

}
