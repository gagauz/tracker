package com.gagauz.tracker.web.config;

import javax.sql.DataSource;

import org.gagauz.hibernate.config.AbstractHibernateConfig;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gagauz.tracker.db.config.DevDataSource;
import com.gagauz.tracker.db.config.DevLocalSessionFactoryBean;

@Configuration
@EnableScheduling
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = "com.gagauz.tracker.beans")
public class CommonConfiguration extends AbstractHibernateConfig {

    @Override
    @Bean(autowire = Autowire.BY_NAME)
    public DataSource dataSource() {
        return new DevDataSource();
    }

    @Override
    @Bean(autowire = Autowire.BY_NAME)
    public FactoryBean<SessionFactory> sessionFactory() {
        return new DevLocalSessionFactoryBean();
    }
}
