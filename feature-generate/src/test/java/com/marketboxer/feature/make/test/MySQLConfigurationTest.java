/**
 * ymm56.com Inc.
 * Copyright (c) 2013-2022 All Rights Reserved.
 */
package com.marketboxer.feature.make.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 *
 * @author jamesqin mail:qinjie@amh-group.com
 * @version : MySQLConfiguration.java, v 0.1 2022-09-08 12:40 jamesqin Exp $$
 */
@Configuration
public class MySQLConfigurationTest {

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        try {
            sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sqlSessionFactoryBean;
    }

    @Bean
    public DataSource dataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        // Test credentials stay outside source control and can be overridden in CI.
        druidDataSource.setUrl(getEnv("TEST_DB_URL", "jdbc:mysql://localhost:3306/stock?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&autoReconnect=true&maxReconnects=10"));
        druidDataSource.setUsername(getEnv("TEST_DB_USERNAME", "root"));
        druidDataSource.setPassword(getEnv("TEST_DB_PASSWORD", ""));
        druidDataSource.setKeepAlive(true);
        druidDataSource.setPhyMaxUseCount(100);
        druidDataSource.setMaxActive(10);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setValidationQuery("select 1");
        druidDataSource.setMinIdle(10);
        druidDataSource.setInitialSize(5);
        druidDataSource.setTimeBetweenEvictionRunsMillis(2000);
        druidDataSource.setMinEvictableIdleTimeMillis(600000);
        druidDataSource.setMaxEvictableIdleTimeMillis(900000);
        return druidDataSource;
    }

    private String getEnv(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null ? defaultValue : value;
    }

}