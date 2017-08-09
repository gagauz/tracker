package com.gagauz.tracker.db.config;

import java.sql.Driver;
import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.gagauz.tracker.utils.AppProperties;

public class DevDataSource extends SimpleDriverDataSource implements DataSource {

    public DevDataSource() {
        for (Entry e : System.getProperties().entrySet()) {
            System.out.format("%1$40s = %2$s\n", e.getKey(), e.getValue());
        }
        try {
            setDriverClass((Class<? extends Driver>) Class.forName(AppProperties.JDBC_DRIVER.toString()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        setUrl(AppProperties.JDBC_URL.toString());
        setUsername(AppProperties.JDBC_USERNAME.toString());
        setPassword(AppProperties.JDBC_PASSWORD.toString());
        Properties props = new Properties();
        props.setProperty("cacheServerConfiguration", "true");
        props.setProperty("useUnicode", "true");
        props.setProperty("characterEncoding", "UTF-8");
        props.setProperty("characterSetResults", "UTF-8");
        props.setProperty("useLocalSessionState", "true");
        props.setProperty("statementInterceptors", com.xl0e.hibernate.utils.StatementInterceptor.class.getName());
        props.setProperty("includeThreadDumpInDeadlockExceptions", "true");
        props.setProperty("logSlowQueries", "true");
        // props.setProperty("includeInnodbStatusInDeadlockExceptions", "true");
        props.setProperty("logger", "com.mysql.jdbc.log.Slf4JLogger");
        props.setProperty("dumpQueriesOnException", "true");
        setConnectionProperties(props);
    }
}
