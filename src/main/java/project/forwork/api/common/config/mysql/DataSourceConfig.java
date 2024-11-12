package project.forwork.api.common.config.mysql;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Profile("prod2")
@Configuration
public class DataSourceConfig {

    public static final String MASTER = "master";
    public static final String SLAVE = "slave";
    public static final String MASTER_DATA_SOURCE = "masterDataSource";
    public static final String SLAVE_DATA_SOURCE = "slaveDataSource";
    public static final String ROUTING_DATA_SOURCE = "routingDataSource";

    @DependsOn(ROUTING_DATA_SOURCE)
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

    @Bean(name = MASTER_DATA_SOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = SLAVE_DATA_SOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.slave.hikari") // 공통 prefix 사용
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @DependsOn({MASTER_DATA_SOURCE, SLAVE_DATA_SOURCE})
    @Bean
    public DataSource routingDataSource(
            @Qualifier(MASTER_DATA_SOURCE) DataSource masterDataSource,
            @Qualifier(SLAVE_DATA_SOURCE) DataSource slaveDataSource
    ) {
        RoutingDataSource routingDataSourceImpl = new RoutingDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(MASTER, masterDataSource);
        targetDataSources.put(SLAVE, slaveDataSource);
        routingDataSourceImpl.setTargetDataSources(targetDataSources);
        routingDataSourceImpl.setDefaultTargetDataSource(masterDataSource); // 기본은 마스터
        return routingDataSourceImpl;
    }

    @DependsOn({ROUTING_DATA_SOURCE})
    @Primary
    @Bean
    public DataSource dataSource(@Qualifier(ROUTING_DATA_SOURCE) DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }
}