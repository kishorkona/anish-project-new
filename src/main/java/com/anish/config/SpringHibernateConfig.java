package com.anish.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class SpringHibernateConfig {

	@Value("${db.schema.value}")
	private String schema_name;
	
	@Value("${db.userName}")
	private String userName;
	
	@Value("${db.password}")
	private String password;
	
	@Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUrl("jdbc:mariadb://localhost:3306/"+schema_name);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        return dataSource;
    }
}
