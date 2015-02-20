package com.gagauz.tracker.db.config;

import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.*;

public class DevLocalSessionFactoryBean extends LocalSessionFactoryBean {
    public DevLocalSessionFactoryBean() {
        setPackagesToScan(com.gagauz.tracker.db.model.User.class.getPackage().getName());
        setAnnotatedPackages(new String[] {com.gagauz.tracker.db.model.User.class.getPackage().getName()});
        Properties properties = new Properties();
        properties.put(DIALECT, System.getProperty("tracker.jdbc-dialect"));
        properties.put(SHOW_SQL, false);
        properties.put(FORMAT_SQL, false);
        properties.put(USE_SQL_COMMENTS, false);
        properties.put(HBM2DDL_AUTO, "create");
        properties.put(STATEMENT_BATCH_SIZE, 50);
        properties.put("hibernate.validator.autoregister_listeners", "create");
        properties.put(USE_REFLECTION_OPTIMIZER, true);
        properties.put(DEFAULT_BATCH_FETCH_SIZE, 50);
        properties.put("current_session_context_class", "thread");
        setHibernateProperties(properties);
    }
}
