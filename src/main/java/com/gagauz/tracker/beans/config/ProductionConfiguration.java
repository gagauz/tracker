package com.gagauz.tracker.beans.config;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.sql.DataSource;

import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.*;

@Configuration
public class ProductionConfiguration extends CommonConfiguration {
    @Bean
    public DataSource dataSource() {
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

    @Bean(autowire = Autowire.BY_NAME)
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean annotationSessionFactoryBean = new LocalSessionFactoryBean();
        annotationSessionFactoryBean.setPackagesToScan(com.gagauz.tracker.db.model.Bug.class.getPackage().getName());
        annotationSessionFactoryBean.setAnnotatedPackages(new String[] {com.gagauz.tracker.db.model.Bug.class.getPackage().getName()});
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
