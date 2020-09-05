package com.timsanalytics.crc.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private final Environment environment;

    @Autowired
    public DataSourceConfig(Environment environment) {
        this.environment = environment;
    }

    // AUTH DATA SOURCE

    @Bean(name = "mySqlAuthDataSource")
    public BasicDataSource mySqlAuthDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(this.environment.getProperty("mySql.datasource.driver-class-name"));
        dataSource.setUsername(this.environment.getProperty("mysql.datasource.username"));
        dataSource.setPassword(this.environment.getProperty("mysql.datasource.password"));
        dataSource.setUrl(this.environment.getProperty("mySql.datasource.url.auth"));
        return dataSource;
    }

    @Bean(name = "mySqlAuthJdbcTemplate")
    public JdbcTemplate mySqlAuthJdbcTemplate(@Qualifier("mySqlAuthDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean(name = "mySqlAuthTransactionManager")
    public PlatformTransactionManager mySqlAuthTransactionManager() {
        return new DataSourceTransactionManager(mySqlAuthDataSource());
    }

    // CRC DATA SOURCE

    @Bean(name = "mySqlCrcDataSource")
    public BasicDataSource mySqlCrcDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(this.environment.getProperty("mySql.datasource.driver-class-name"));
        dataSource.setUsername(this.environment.getProperty("mysql.datasource.username"));
        dataSource.setPassword(this.environment.getProperty("mysql.datasource.password"));
        dataSource.setUrl(this.environment.getProperty("mySql.datasource.url.crc"));
        return dataSource;
    }

    @Bean(name = "mySqlCrcJdbcTemplate")
    public JdbcTemplate mySqlCrcJdbcTemplate(@Qualifier("mySqlCrcDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }
}
