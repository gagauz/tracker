package com.gagauz.tracker.db.config;

import com.gagauz.tracker.utils.SysEnv;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.*;

public class DevLocalSessionFactoryBean extends LocalSessionFactoryBean {
    public DevLocalSessionFactoryBean() {
        setPackagesToScan(com.gagauz.tracker.db.model.User.class.getPackage().getName());
        setAnnotatedPackages(new String[] {com.gagauz.tracker.db.model.User.class.getPackage().getName()});
        Properties properties = new Properties();

        properties.put(DIALECT, SysEnv.JDBC_DIALECT.toString());
        properties.put(SHOW_SQL, false);
        properties.put(FORMAT_SQL, false);
        properties.put(USE_SQL_COMMENTS, false);
        properties.put(HBM2DDL_AUTO, "create");
        properties.put(STATEMENT_BATCH_SIZE, 50);
        properties.put(STATEMENT_BATCH_SIZE, 50);
        properties.put("hibernate.validator.autoregister_listeners", "create");
        properties.put(USE_REFLECTION_OPTIMIZER, true);
        properties.put(DEFAULT_BATCH_FETCH_SIZE, 50);
        properties.put("current_session_context_class", "thread");
        properties.put("connection.characterEncoding", "utf-8");
        properties.put("hibernate.connection.charset", "utf8");
        properties.put("hibernate.connection.characterEncoding", "utf-8");
        properties.put("hibernate.connection.useUnicode", "true");
        setHibernateProperties(properties);
    }
}
