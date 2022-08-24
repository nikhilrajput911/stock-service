package com.iiht.stock.command;

import com.iiht.cqrs.core.infrastructure.CommandDispatcher;
import com.iiht.stock.command.api.commands.AddStockPriceCommand;
import com.iiht.stock.command.api.commands.CommandHandler;
import com.iiht.stock.command.api.commands.DeleteByCompanyCodeStockPriceCommand;
import com.iiht.stock.command.api.commands.DeleteStockPriceCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

@ComponentScan(basePackages = {"com.iiht.stock.command"})
@SpringBootApplication
public class CommandApplication {

	@Autowired
	private CommandDispatcher commandDispatcher;

	@Autowired
	private CommandHandler commandHandler;

	public static void main(String[] args) {
		SpringApplication.run(CommandApplication.class, args);
	}

	@PostConstruct
	public void registerHandlers() {
		commandDispatcher.registerHandler(AddStockPriceCommand.class, commandHandler::handle);
		commandDispatcher.registerHandler(DeleteStockPriceCommand.class, commandHandler::handle);
		commandDispatcher.registerHandler(DeleteByCompanyCodeStockPriceCommand.class, commandHandler::handle);
	}
}
