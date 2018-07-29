package com.algaworks;

import com.algaworks.config.property.AlgamoneyApiProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(AlgamoneyApiProperty.class)
public class AlgamoneyApiApplication {

	private static ApplicationContext APPLICATION_C0NTEXT;

	public static void main(String[] args) {
		APPLICATION_C0NTEXT = SpringApplication.run(AlgamoneyApiApplication.class, args);
	}

	public static <T> T getBean(Class<T> type) {
		return APPLICATION_C0NTEXT.getBean(type);
	}

}
