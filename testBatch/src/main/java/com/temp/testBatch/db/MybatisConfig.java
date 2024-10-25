package com.temp.testBatch.db;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * DataBase 연결 클래스
 */
class MyBatisConfig {
    //Mapper 파일이 있는 위치 지정
    public static final String BASE_PACKAGE = "com.temp";
}

/**
 * Primary DataBase 연결 클래스
 * Mapper 클래스에서 AnotationClass명으로 지정: PrimaryConnection
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = MyBatisConfig.BASE_PACKAGE, annotationClass = PrimaryConnection.class, sqlSessionFactoryRef = "primarySqlSessionFactory")
class PrimaryMyBatisConfig {
    @Primary
    @Bean(name = "primarySqlSessionFactory")
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDataSource") DataSource primaryDataSource) throws Exception {
        return SqlSessionFactoryBuilder.build(primaryDataSource);
    }

    /**
     * DataSource 연결정보를 Application.yml에 정의한 값을 사용해서 연결
     * @return
     */
    @Primary
    @Bean(name = "primaryDataSource", destroyMethod = "")
    @ConfigurationProperties("spring.datasource.primary")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("primaryDataSource") DataSource primaryDataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(primaryDataSource);
        transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;
    }
}

/**
 * Secondary DataBase 연결 클래스
 * Mapper 클래스에서 AnotationClass명으로 지정: SecondaryConnection
 */
@Configuration
@MapperScan(basePackages = MyBatisConfig.BASE_PACKAGE, annotationClass = SecondaryConnection.class, sqlSessionFactoryRef = "secondarySqlSessionFactory")
class SecondaryMyBatisConfig {
    @Bean(name = "secondarySqlSessionFactory")
    public SqlSessionFactory secondarySqlSessionFactory(@Qualifier("secondaryDataSource") DataSource secondaryDataSource) throws Exception {
        return SqlSessionFactoryBuilder.build(secondaryDataSource);
    }

    /**
     * DataSource 연결정보를 Application.yml에 정의한 값을 사용해서 연결
     * @return
     */
    @Bean(name = "secondaryDataSource", destroyMethod = "")
    @ConfigurationProperties("spring.datasource.secondary")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}
