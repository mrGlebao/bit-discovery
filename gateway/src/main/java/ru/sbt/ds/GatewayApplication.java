package ru.sbt.ds;

import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import ru.sbt.ds.filters.route.VersionFilter;


@EnableEurekaClient
@EnableZuulProxy
@SpringBootApplication
public class GatewayApplication {

	@Autowired
	private EurekaClient eurekaClient;

	@Bean
	public VersionFilter versionFilter() {
		return new VersionFilter(eurekaClient);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(GatewayApplication.class, args);
	}
}
