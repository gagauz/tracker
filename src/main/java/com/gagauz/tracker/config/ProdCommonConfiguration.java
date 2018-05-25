package com.gagauz.tracker.config;

import static com.gagauz.tracker.utils.AppProperties.*;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gagauz.tracker.db.config.CommonLocalSessionFactoryBean;
import com.gagauz.tracker.services.ServicesRootPackage;
import com.xl0e.hibernate.config.AbstractHibernateConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Profile(Constants.AppProfiles.PROD)
@Configuration
@EnableScheduling
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackageClasses = ServicesRootPackage.class)
public class ProdCommonConfiguration extends AbstractHibernateConfig {

    @Override
    @Bean(autowire = Autowire.BY_NAME)
    public DataSource dataSource() {
        Properties props = new Properties();
        props.setProperty("cacheServerConfiguration", "true");
        props.setProperty("useUnicode", "true");
        props.setProperty("characterEncoding", "UTF-8");
        props.setProperty("characterSetResults", "UTF-8");
        props.setProperty("useLocalSessionState", "true");
        props.setProperty("statementInterceptors", com.xl0e.hibernate.utils.StatementInterceptor.class.getName());
        props.setProperty("includeThreadDumpInDeadlockExceptions", "true");
        props.setProperty("logSlowQueries", "true");
        props.setProperty("logger", "com.mysql.jdbc.log.Slf4JLogger");
        props.setProperty("dumpQueriesOnException", "true");

        HikariConfig config = new HikariConfig(props);

        HikariDataSource hikariDataSource = new HikariDataSource(config);

        hikariDataSource.setJdbcUrl(JDBC_URL.toString());
        hikariDataSource.setUsername(JDBC_USERNAME.toString());
        hikariDataSource.setPassword(JDBC_PASSWORD.toString());
        hikariDataSource.setDriverClassName(JDBC_DRIVER.toString());

        LazyConnectionDataSourceProxy dataSource = new LazyConnectionDataSourceProxy(hikariDataSource);
        return dataSource;

    }

    @Override
    @Bean(autowire = Autowire.BY_NAME)
    public FactoryBean<SessionFactory> sessionFactory() {
        return new CommonLocalSessionFactoryBean();
    }
}
