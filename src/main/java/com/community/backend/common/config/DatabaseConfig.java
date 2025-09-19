package com.community.backend.common.config;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DatabaseConfig {

	@Value("${spring.datasource.master.url}")
	private String masterUrl;
	@Value("${spring.datasource.master.username}")
	private String masterUsername;
	@Value("${spring.datasource.master.password}")
	private String masterPassword;

	@Value("${spring.datasource.slave.url}")
	private String slaveUrl;
	@Value("${spring.datasource.slave.username}")
	private String slaveUsername;
	@Value("${spring.datasource.slave.password}")
	private String slavePassword;

	// 1️⃣ Master DataSource
	@Bean
	public DataSource masterDataSource() {
		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl(masterUrl);
		ds.setUsername(masterUsername);
		ds.setPassword(masterPassword);
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
		return ds;
	}

	// 2️⃣ Slave DataSource
	@Bean
	public DataSource slaveDataSource() {
		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl(slaveUrl);
		ds.setUsername(slaveUsername);
		ds.setPassword(slavePassword);
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
		return ds;
	}

	@Bean
	public DataSource routingDataSource(
		@Qualifier("masterDataSource") DataSource masterDataSource,
		@Qualifier("slaveDataSource") DataSource slaveDataSource) {

		AbstractRoutingDataSource routingDataSource = new AbstractRoutingDataSource() {
			@Override
			protected Object determineCurrentLookupKey() {
				String ds = DataSourceContextHolder.getDataSourceType();
				log.info("✅ Current DataSource : {}", ds);
				return ds != null ? ds : "master"; // 기본 master
			}
		};

		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put("master", masterDataSource);
		targetDataSources.put("slave", slaveDataSource);

		routingDataSource.setTargetDataSources(targetDataSources);
		routingDataSource.setDefaultTargetDataSource(masterDataSource);
		return routingDataSource;
	}

	@Primary
	@Bean
	public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
		return new LazyConnectionDataSourceProxy(routingDataSource);
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
		@Qualifier("dataSource") DataSource dataSource) {

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource);
		factory.setPackagesToScan("com.community.backend.domain");
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL8Dialect");
		factory.setJpaVendorAdapter(vendorAdapter);
		return factory;
	}

	@Bean
	public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
		JpaTransactionManager tm = new JpaTransactionManager();
		tm.setEntityManagerFactory(entityManagerFactory.getObject());
		return tm;
	}
}