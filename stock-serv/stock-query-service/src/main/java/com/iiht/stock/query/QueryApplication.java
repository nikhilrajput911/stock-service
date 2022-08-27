package com.iiht.stock.query;

import com.iiht.cqrs.core.infrastructure.QueryDispatcher;
import com.iiht.stock.query.api.queries.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

@EnableEurekaClient
@ComponentScan(basePackages = {"com.iiht.stock.query"})
@SpringBootApplication
public class QueryApplication {
	@Autowired
	private QueryDispatcher queryDispatcher;

	@Autowired
	private QueryHandler queryHandler;

	public static void main(String[] args) {
		SpringApplication.run(QueryApplication.class, args);
	}

	@PostConstruct
	public void registerHandlers() {
		queryDispatcher.registerHandler(FindAllStocksQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindStocksByCompanyCodeQuery.class, queryHandler::handle);
		queryDispatcher.registerHandler(FindStocksByCompanyCodeAndBetweenCreatedDateQuery.class, queryHandler::handle);
	}
}
