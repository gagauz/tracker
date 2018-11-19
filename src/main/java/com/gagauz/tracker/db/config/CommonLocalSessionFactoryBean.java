package com.gagauz.tracker.db.config;

import static org.hibernate.cfg.AvailableSettings.*;

import java.util.Properties;

import org.hibernate.tool.schema.Action;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import com.gagauz.tracker.utils.AppProperties;

public class CommonLocalSessionFactoryBean extends LocalSessionFactoryBean {
    public CommonLocalSessionFactoryBean() {
        setPackagesToScan(com.gagauz.tracker.db.model.User.class.getPackage().getName());
        setAnnotatedPackages(new String[] { com.gagauz.tracker.db.model.User.class.getPackage().getName() });
        Properties properties = new Properties();

        properties.put(DIALECT, AppProperties.JDBC_DIALECT.toString());
        properties.put(SHOW_SQL, false);
        properties.put(FORMAT_SQL, false);
        properties.put(USE_SQL_COMMENTS, false);
        if (AppProperties.FILL_TEST_DATA.getBoolean()) {
            properties.put(HBM2DDL_AUTO, Action.CREATE_DROP);
        } else {
            properties.put(HBM2DDL_AUTO, Action.NONE);
        }
        properties.put(STATEMENT_BATCH_SIZE, 50);
        properties.put(STATEMENT_BATCH_SIZE, 50);
        properties.put("hibernate.validator.autoregister_listeners", "create");
        properties.put(USE_REFLECTION_OPTIMIZER, true);
        properties.put(DEFAULT_BATCH_FETCH_SIZE, 50);
        properties.put("current_session_context_class", "thread");
        properties.put("connection.characterEncoding", "utf-8");
        properties.put("hibernate.connection.charset", "utf8");
        properties.put("hibernate.connection.characterEncoding", "utf8");
        properties.put("hibernate.connection.useUnicode", "true");
        setHibernateProperties(properties);
    }
}
