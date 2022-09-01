package com.example.remya.dynamicDBOne;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.hibernate.EmptyInterceptor;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import com.zaxxer.hikari.HikariDataSource;


@Configuration
@Lazy
@EnableConfigurationProperties({ JpaProperties.class })

@EnableJpaRepositories(basePackages = "com.example.remya.dynamicDBOne",
entityManagerFactoryRef = "entityManagerFactory",
transactionManagerRef= "sqlTransactionManager"
)
@EnableTransactionManagement

public class DynamicDBOneConfigurationSQL {
	@Autowired
	private JpaProperties jpaproperties;

	@Bean
    @Primary
    @ConfigurationProperties("app.datasource.one")
    public DataSourceProperties sqlDataSourceProperties() {
	 System.out.println("sql databasourceProperties");
        return new DataSourceProperties();
    }

    @Bean
   @Primary
    @ConfigurationProperties("app.datasource.one.configuration")
    public DataSource sqlDataSource() {
    	System.out.println("sql databasource");
        return sqlDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }
    
	@Bean(name = "datasourceBasedMultitenantConnectionProvider")
	public MultiTenantConnectionProvider dynamicDataSourceRouter() {
		return new MultiTenanatSchema();
	}

	@Bean(name = "currentTenantIdentifierResolver")
	public CurrentTenantIdentifierResolver tenantResolver() {
		return new CurrentTenentResolverName();
	}
	 @PersistenceContext
    @Primary
    @Bean(name = "entityManagerFactory")
	// @ConditionalOnMissingBean(name="entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("datasourceBasedMultitenantConnectionProvider") MultiTenantConnectionProvider dynamicDataSourceRouter,
			@Qualifier("currentTenantIdentifierResolver") CurrentTenantIdentifierResolver tenantResolver) {
    	System.out.println("sql LocalContainerEntityManagerFactoryBean");
    	Map<String, Object> jpaProperties = new LinkedHashMap<>();
		    //jpaProperties.put("hibernate.default_schema", "remya");
		    //jpaProperties.put("hibernate.default_schema", "dbo");
    	jpaProperties.putAll(this.jpaproperties.getProperties());
    	jpaProperties.put("hibernate.show_sql", true);
    	jpaProperties.put(org.hibernate.cfg.Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
		jpaProperties.put(org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
		jpaProperties.put(org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER, dynamicDataSourceRouter);
		jpaProperties.put("hibernate.ejb.interceptor",hibernateInterceptor() );
		jpaProperties.put("hibernate.default_schema", "dbo");
//    	LocalContainerEntityManagerFactoryBean lfb = factoryBuilder
//                .dataSource(sqlDataSource())
//                .packages(Employee.class)
//                .build();
		LocalContainerEntityManagerFactoryBean lfb = new LocalContainerEntityManagerFactoryBean();
		lfb.setDataSource(sqlDataSource());
    	lfb.setJpaPropertyMap(jpaProperties);
    	lfb.setPersistenceUnitName("Employee");
		lfb.setPackagesToScan("com.example.remya.dynamicDBOne");
		lfb.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		lfb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		
    	return lfb;
//        return builder
//                .dataSource(sqlDataSource())
//                .packages(Employee.class)
//                .build();
    }

   // @Primary
    @Bean
    public PlatformTransactionManager sqlTransactionManager(
            final @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
    	System.out.println("sql PlatformTransactionManager");
    	return new JpaTransactionManager(entityManagerFactory.getObject());
    }
    
//    @Bean
//    public JpaTransactionManager transactionManager(
//            @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory);
//        System.out.println("sql PlatformTransactionManager");
//        return transactionManager;
//    }

    @SuppressWarnings("serial")
	private EmptyInterceptor hibernateInterceptor() {
		
		return new EmptyInterceptor(){
			public String onPrepareStatement(String sql){
				String sqlQ = super.onPrepareStatement(sql);
				System.out.println(sqlQ);
				if(CurrentTenantName.getCurrentTenant() != null && !CurrentTenantName.getCurrentTenant().isEmpty() && CurrentTenantName.getCurrentTenant().equalsIgnoreCase("remya")){
					sqlQ=sqlQ.replace("dbo.", "remya.");
				}
				return sqlQ;
			}
		};
	}
	

}
