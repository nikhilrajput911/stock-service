package com.iiht.stock.query.api.controllers;

import com.iiht.cqrs.core.infrastructure.QueryDispatcher;
import com.iiht.stock.query.api.dto.StockLookupResponse;
import com.iiht.stock.query.api.queries.*;
import com.iiht.stock.query.domain.StockDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1.0/market/stock")
@Api(value = "REST APIs to query stock information")
public class StockLookupController {
    private final Logger logger = Logger.getLogger(StockLookupController.class.getName());

    @Autowired
    private QueryDispatcher queryDispatcher;

    @ApiOperation(value = "${api.find-all-stock.description}", notes = "${api.find-all-stock.notes}")
    @GetMapping(path = "/")
    public ResponseEntity<StockLookupResponse> getAllStocks() {
        try {
            List<StockDetails> stocks = queryDispatcher.send(new FindAllStocksQuery());
            if (stocks == null || stocks.size() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            StockLookupResponse response = StockLookupResponse.builder()
                    .stocks(stocks)
                    .message(MessageFormat.format("Successfully returned {0} stock(s)!", stocks.size()))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            String safeErrorMessage = "Failed to complete get all stocks request!";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new StockLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "${api.find-stocks-by-company-code.description}", notes = "${api.find-stocks-by-company-code.notes}")
    @GetMapping(path = "/byCompanyCode/{companyCode}")
    public ResponseEntity<StockLookupResponse> getStocksByCompanyCode(@PathVariable(value = "companyCode") String companyCode) {
        try {
            List<StockDetails> stocks = queryDispatcher.send(new FindStocksByCompanyCodeQuery(companyCode));
            if (stocks == null || stocks.size() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            StockLookupResponse response = StockLookupResponse.builder()
                    .stocks(stocks)
                    .message(MessageFormat.format("Successfully returned {0} stock(s)!", stocks.size()))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            String safeErrorMessage = "Failed to complete get stocks by company code request!";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new StockLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "${api.find-stocks-by-company-code-and-between-creation-date.description}", notes = "${api.find-stocks-by-company-code-and-between-creation-date.notes}")
    @GetMapping(path = "/get/{companyCode}/{startDate}/{endDate}")
    public ResponseEntity<StockLookupResponse> getStocksByCompanyCodeAndCreatedDateBetween (
            @PathVariable(value = "companyCode") String companyCode,
            @PathVariable(value = "startDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
            @PathVariable(value = "endDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate
    ) {
        try {
            logger.info("companyCode: " + companyCode);
            logger.info("startDate: " + startDate);
            logger.info("endDate: " + endDate);

            List<StockDetails> stocks = queryDispatcher.send(new FindStocksByCompanyCodeAndBetweenCreatedDateQuery(companyCode, startDate, endDate));
            logger.info("Stocks: " + stocks);
            if (stocks == null || stocks.size() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            StockLookupResponse response = StockLookupResponse.builder()
                    .stocks(stocks)
                    .message(MessageFormat.format("Successfully returned {0} stock(s)!", stocks.size()))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            String safeErrorMessage = "Failed to complete get stocks by created date request!";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new StockLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
