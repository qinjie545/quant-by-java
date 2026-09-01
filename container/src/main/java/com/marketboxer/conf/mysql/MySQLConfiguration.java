/**
 * ymm56.com Inc.
 * Copyright (c) 2013-2022 All Rights Reserved.
 */
package com.marketboxer.conf.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 *
 * @author jamesqin mail:qinjie@amh-group.com
 * @version : MySQLConfiguration.java, v 0.1 2022-09-08 12:40 jamesqin Exp $$
 */
@Configuration
public class MySQLConfiguration {

    @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/stock?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&autoReconnect=true&maxReconnects=10}")
    private String datasourceUrl;

    @Value("${spring.datasource.username:root}")
    private String datasourceUsername;

    @Value("${spring.datasource.password:}")
    private String datasourcePassword;

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        return sqlSessionFactoryBean;
    }

    @Bean
    public DataSource dataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(datasourceUrl);
        druidDataSource.setUsername(datasourceUsername);
        druidDataSource.setPassword(datasourcePassword);
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

    @Bean
    public SqlSessionTemplate sessionTemplate(SqlSessionFactoryBean sqlSessionFactoryBean){
        try {
            sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
            return new SqlSessionTemplate(sqlSessionFactoryBean.getObject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}