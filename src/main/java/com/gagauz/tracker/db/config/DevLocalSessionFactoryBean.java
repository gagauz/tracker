package com.gagauz.tracker.db.config;

import static org.hibernate.cfg.AvailableSettings.DEFAULT_BATCH_FETCH_SIZE;
import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.AvailableSettings.FORMAT_SQL;
import static org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO;
import static org.hibernate.cfg.AvailableSettings.SHOW_SQL;
import static org.hibernate.cfg.AvailableSettings.STATEMENT_BATCH_SIZE;
import static org.hibernate.cfg.AvailableSettings.USE_REFLECTION_OPTIMIZER;
import static org.hibernate.cfg.AvailableSettings.USE_SQL_COMMENTS;

import java.util.Properties;

import org.hibernate.cfg.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class DevLocalSessionFactoryBean extends LocalSessionFactoryBean {
    public DevLocalSessionFactoryBean() {
        setPackagesToScan(com.gagauz.tracker.db.model.User.class.getPackage().getName());
        setAnnotatedPackages(new String[] {com.gagauz.tracker.db.model.User.class.getPackage().getName()});
        Properties properties = new Properties();
        properties.put(DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect");
        properties.put(SHOW_SQL, false);
        properties.put(FORMAT_SQL, false);
        properties.put(USE_SQL_COMMENTS, false);
        properties.put(HBM2DDL_AUTO, "create");
        properties.put(STATEMENT_BATCH_SIZE, 50);
        properties.put("hibernate.validator.autoregister_listeners", "create");
        properties.put(USE_REFLECTION_OPTIMIZER, true);
        properties.put(DEFAULT_BATCH_FETCH_SIZE, 50);
        properties.put("current_session_context_class", "thread");
        properties.put("current_session_context_class", "thread");
        setHibernateProperties(properties);
        Configuration cfg = new Configuration();
    }
}
