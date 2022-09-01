package com.example.remya.dynamicDBOne;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication
(exclude={ DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
public class DynamicDbOneApplication {

	public static void main(String[] args) {
		SpringApplication.run(DynamicDbOneApplication.class, args);
	}

}
