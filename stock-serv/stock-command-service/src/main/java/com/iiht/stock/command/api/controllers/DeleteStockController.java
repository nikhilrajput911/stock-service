package com.iiht.stock.command.api.controllers;

import com.iiht.cqrs.core.exceptions.AggregateNotFoundException;
import com.iiht.cqrs.core.infrastructure.CommandDispatcher;
import com.iiht.stock.command.api.commands.DeleteByCompanyCodeStockPriceCommand;
import com.iiht.stock.command.api.commands.DeleteStockPriceCommand;
import com.iiht.stock.common.dto.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1.0/market/stock")
@Api(value = "REST APIs to delete stocks")
public class DeleteStockController {
    private final Logger logger = Logger.getLogger(DeleteStockController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    @ApiOperation(value = "${api.delete-stock-by-company-code.description}", notes = "${api.delete-stock-by-company-code.notes}")
    @DeleteMapping(path = "/byCompanyCode/{companyCode}")
    public ResponseEntity<BaseResponse> deleteStockByCompanyCode(@PathVariable(value = "companyCode") String companyCode) {
        try {
            commandDispatcher.send(new DeleteByCompanyCodeStockPriceCommand(companyCode));
            return new ResponseEntity<>(new BaseResponse("Stock delete request successfully completed!"), HttpStatus.OK);
        } catch (IllegalStateException | AggregateNotFoundException e) {
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}.", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            String safeErrorMessage = MessageFormat.format("Error while processing request to delete stock with company code - {0}.", companyCode);
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
