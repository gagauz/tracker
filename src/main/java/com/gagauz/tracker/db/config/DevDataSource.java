package com.gagauz.tracker.db.config;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

public class DevDataSource extends BasicDataSource {

    public DevDataSource() {
        setDriverClassName("com.mysql.jdbc.Driver");
        setUrl("jdbc:mysql://localhost:3306/tracker?autoReconnect=true");
        setUsername("b4f");
        setPassword("office");
        setMaxWait(5000);
        setMaxActive(5);
        setMaxIdle(5);
        setMinEvictableIdleTimeMillis(1800000);
        setTimeBetweenEvictionRunsMillis(600000);
        setNumTestsPerEvictionRun(5);
        setTestWhileIdle(true);
        setTestOnBorrow(false);
        setValidationQuery("/* ping */");
        setConnectionProperties("cacheServerConfiguration=true;characterSetResults=UTF-8;useLocalSessionState=true;statementInterceptors=com.gagauz.tracker.beans.setup.StatementInterceptor");
    }

}
