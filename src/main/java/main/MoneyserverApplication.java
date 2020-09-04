package main;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
@ComponentScan("main")
@ComponentScan("controller")
@ComponentScan("service")
@ComponentScan("stockdao")
@ComponentScan("vo")
@MapperScan(value= {"classpath:/mapper/*.xml"})
@SpringBootApplication
public class MoneyserverApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(MoneyserverApplication.class, args);
	}
}


