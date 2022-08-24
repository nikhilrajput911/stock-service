package com.iiht.stock.command.api.controllers;

import com.iiht.cqrs.core.infrastructure.CommandDispatcher;
import com.iiht.stock.command.api.commands.AddStockPriceCommand;
import com.iiht.stock.command.api.dto.AddStockResponse;
import com.iiht.stock.common.dto.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1.0/market/stock/add")
@Api(value = "REST APIs to add stocks")
public class AddStockController {
    private final Logger logger = Logger.getLogger(AddStockController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    @ApiOperation(value = "${api.add-stock.description}", notes = "${api.add-stock.notes}")
    @PostMapping
    public ResponseEntity<BaseResponse> addStock(@RequestBody AddStockPriceCommand command) {
        String id = UUID.randomUUID().toString();
        command.setId(id);
        try {
            commandDispatcher.send(command);
            return new ResponseEntity<>(new AddStockResponse("Stock creation request completed successfully!", id), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}.", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            String safeErrorMessage = MessageFormat.format("Error while processing request to add new stock for id - {0}.", id);
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new AddStockResponse(safeErrorMessage, id), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
