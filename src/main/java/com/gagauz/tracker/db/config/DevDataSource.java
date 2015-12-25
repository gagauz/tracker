package com.gagauz.tracker.db.config;

import com.gagauz.tracker.db.utils.StatementInterceptor;
import com.gagauz.tracker.utils.SysEnv;
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
            setDriverClass((Class<? extends Driver>) Class.forName(SysEnv.JDBC_DRIVER.toString()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        setUrl(SysEnv.JDBC_URL.toString());
        setUsername(SysEnv.JDBC_USERNAME.toString());
        setPassword(SysEnv.JDBC_PASSWORD.toString());
        Properties props = new Properties();
        props.setProperty("cacheServerConfiguration", "true");
        props.setProperty("characterSetResults", "UTF-8");
        props.setProperty("useLocalSessionState", "true");
        props.setProperty("statementInterceptors", StatementInterceptor.class.getName());
        setConnectionProperties(props);
    }
}
