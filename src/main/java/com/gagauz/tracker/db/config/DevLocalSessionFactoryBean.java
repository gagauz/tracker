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

import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import com.gagauz.tracker.utils.AppProperties;

public class DevLocalSessionFactoryBean extends LocalSessionFactoryBean {
	public DevLocalSessionFactoryBean() {
		setPackagesToScan(com.gagauz.tracker.db.model.User.class.getPackage().getName());
		setAnnotatedPackages(new String[] { com.gagauz.tracker.db.model.User.class.getPackage().getName() });
		Properties properties = new Properties();

		properties.put(DIALECT, AppProperties.JDBC_DIALECT.toString());
		properties.put(SHOW_SQL, false);
		properties.put(FORMAT_SQL, false);
		properties.put(USE_SQL_COMMENTS, false);
		if (AppProperties.FILL_TEST_DATA.getBoolean()) {
			properties.put(HBM2DDL_AUTO, "create");
		} else {
			properties.put(HBM2DDL_AUTO, "false");
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
