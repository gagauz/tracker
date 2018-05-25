package com.gagauz.tracker.config;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gagauz.tracker.db.config.DevDataSource;
import com.gagauz.tracker.db.config.CommonLocalSessionFactoryBean;
import com.gagauz.tracker.services.ServicesRootPackage;
import com.xl0e.hibernate.config.AbstractHibernateConfig;

@Profile(Constants.AppProfiles.DEV)
@Configuration
@EnableScheduling
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackageClasses = ServicesRootPackage.class)
public class DevCommonConfiguration extends AbstractHibernateConfig {

    @Override
    @Bean(autowire = Autowire.BY_NAME)
    public DataSource dataSource() {
        return new DevDataSource();
    }

    @Override
    @Bean(autowire = Autowire.BY_NAME)
    public FactoryBean<SessionFactory> sessionFactory() {
        return new CommonLocalSessionFactoryBean();
    }
}
