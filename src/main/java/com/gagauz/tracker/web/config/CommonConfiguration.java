package com.gagauz.tracker.beans.setup;

import com.gagauz.tracker.db.config.DevDataSource;
import com.gagauz.tracker.db.config.DevLocalSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionAttributeSourceAdvisor;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.naming.NamingException;
import javax.sql.DataSource;

@Configuration
@EnableScheduling
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = "com.gagauz.tracker.beans")
public class CommonConfiguration {

    @Bean(autowire = Autowire.BY_NAME)
    public DataSource dataSource() {
        return new DevDataSource();
    }

    @Bean(autowire = Autowire.BY_NAME)
    public LocalSessionFactoryBean sessionFactory() {
        return new DevLocalSessionFactoryBean();
    }

    @Bean(autowire = Autowire.BY_NAME)
    public PlatformTransactionManager transactionManager() {
        HibernateTransactionManager tm = new HibernateTransactionManager();
        tm.setNestedTransactionAllowed(true);
        return tm;
    }

    @Bean(autowire = Autowire.BY_NAME)
    public TransactionInterceptor transactionInterceptor() {
        return new TransactionInterceptor();
    }

    @Bean
    public AnnotationTransactionAttributeSource transactionAttributeSource() {
        return new AnnotationTransactionAttributeSource();
    }

    @Bean(autowire = Autowire.BY_NAME)
    public TransactionAttributeSourceAdvisor transactionAttributeSourceAdvisor() {
        return new TransactionAttributeSourceAdvisor();
    }

    public JndiObjectFactoryBean getJndiBean(String jndiName) {
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName(jndiName);
        try {
            bean.afterPropertiesSet();
        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
        return bean;
    }

    public String getJndiValue(String jndiName) {
        return getJndiBean(jndiName).getObject().toString();
    }
}
