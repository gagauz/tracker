package com.gagauz.tracker.web.services;

import static org.hibernate.cfg.AvailableSettings.DEFAULT_BATCH_FETCH_SIZE;
import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.AvailableSettings.FORMAT_SQL;
import static org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO;
import static org.hibernate.cfg.AvailableSettings.SHOW_SQL;
import static org.hibernate.cfg.AvailableSettings.STATEMENT_BATCH_SIZE;
import static org.hibernate.cfg.AvailableSettings.USE_REFLECTION_OPTIMIZER;
import static org.hibernate.cfg.AvailableSettings.USE_SQL_COMMENTS;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

public class DevelopmentModule {

    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
        // The application version number is incorprated into URLs for some
        // assets. Web browsers will cache assets because of the far future expires
        // header. If existing assets are changed, the version number should also
        // change, to force the browser to download new versions. This overrides Tapesty's default
        // (a random hexadecimal number), but may be further overriden by DevelopmentModule or
        // QaModule.
        configuration.override(SymbolConstants.EXECUTION_MODE, "development");
        configuration.override(SymbolConstants.PRODUCTION_MODE, false);
    }

    public static DataSource buildDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/tracker?autoReconnect=true");
        dataSource.setUsername("b4f");
        dataSource.setPassword("office");
        dataSource.setMaxWait(5000);
        dataSource.setMaxActive(5);
        dataSource.setMaxIdle(5);
        dataSource.setMinEvictableIdleTimeMillis(1800000);
        dataSource.setTimeBetweenEvictionRunsMillis(600000);
        dataSource.setNumTestsPerEvictionRun(5);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setValidationQuery("/* ping */");
        dataSource
                .setConnectionProperties("cacheServerConfiguration=true;characterSetResults=UTF-8;useLocalSessionState=true;statementInterceptors=com.gagauz.tracker.beans.config.StatementInterceptor");
        return dataSource;
    }

    public static LocalSessionFactoryBean buildSessionFactory() {
        LocalSessionFactoryBean annotationSessionFactoryBean = new LocalSessionFactoryBean();
        annotationSessionFactoryBean.setPackagesToScan(com.gagauz.tracker.db.model.User.class.getPackage().getName());
        annotationSessionFactoryBean.setAnnotatedPackages(new String[] {com.gagauz.tracker.db.model.User.class.getPackage().getName()});
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
        annotationSessionFactoryBean.setHibernateProperties(properties);
        return annotationSessionFactoryBean;
    }
}
