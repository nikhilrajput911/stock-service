package com.iiht.stock.query.api.controllers;

import com.iiht.cqrs.core.domain.BaseEntity;
import com.iiht.cqrs.core.infrastructure.QueryDispatcher;
import com.iiht.stock.query.domain.StockDetails;
import com.iiht.stock.query.domain.StockRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebMvcTest(StockLookupController.class)
public class StockLookupControllerTest {

    @Autowired
    private QueryDispatcher queryDispatcher;

    @MockBean
    private StockRepository stockRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllStockTest_success() throws Exception {
        List<StockDetails> stockDetails = new ArrayList<>();
        stockDetails.add(new StockDetails("DDDFFDS-123-ERT", "CTS", 120.55, new Date()));

        Mockito.when(stockRepository.findAll()).thenReturn(stockDetails);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1.0/market/stock/")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();
        String responseAsString = result.getResponse().getContentAsString();
        System.out.println("Response: " + responseAsString);

        String expectedStr = "{\"message\":\"Successfully returned 1 stock(s)!\",\"stocks\":[{\"id\":\"DDDFFDS-123-ERT\",\"companyCode\":\"CTS\",\"stockPrice\":120.55}],\"numberOfStock\":1,\"minStockPrice\":120.55,\"maxStockPrice\":120.55,\"avgStockPrice\":120.55}";

        JSONAssert.assertEquals(String.valueOf(200), String.valueOf(result.getResponse().getStatus()),true);
        JSONAssert.assertEquals(expectedStr, responseAsString, false);
    }

    @Test
    void getAllStockTest_noStocksFound() throws Exception {
        List<StockDetails> stockDetails = new ArrayList<>();
        Mockito.when(stockRepository.findAll()).thenReturn(stockDetails);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1.0/market/stock/")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();

        JSONAssert.assertEquals(String.valueOf(204), String.valueOf(result.getResponse().getStatus()),true);
    }

    @Test
    void getAllStockTest_failedToGetStocks() throws Exception {
        Mockito.when(stockRepository.findAll()).thenThrow(new IllegalArgumentException());

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1.0/market/stock/")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();
        String responseAsString = result.getResponse().getContentAsString();
        System.out.println("Response: " + responseAsString);

        String expectedStr = "{\"message\":\"Failed to complete get all stocks request!\",\"stocks\":null,\"numberOfStock\":null,\"minStockPrice\":0.0,\"maxStockPrice\":0.0,\"avgStockPrice\":0.0}";

        JSONAssert.assertEquals(String.valueOf(500), String.valueOf(result.getResponse().getStatus()),true);
        JSONAssert.assertEquals(expectedStr, responseAsString, false);
    }

    @Test
    void getStocksByCompanyCodeTest_success() throws Exception {
        List<BaseEntity> stockDetails = new ArrayList<>();
        stockDetails.add(new StockDetails("DDDFFDS-123-ERT", "CTS", 120.55, new Date()));

        Mockito.when(stockRepository.findByCompanyCode("CTS")).thenReturn(stockDetails);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1.0/market/stock/byCompanyCode/CTS")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();
        String responseAsString = result.getResponse().getContentAsString();
        System.out.println("Response: " + responseAsString);

        String expectedStr = "{\"message\":\"Successfully returned 1 stock(s)!\",\"stocks\":[{\"id\":\"DDDFFDS-123-ERT\",\"companyCode\":\"CTS\",\"stockPrice\":120.55}],\"numberOfStock\":1,\"minStockPrice\":120.55,\"maxStockPrice\":120.55,\"avgStockPrice\":120.55}";

        JSONAssert.assertEquals(String.valueOf(200), String.valueOf(result.getResponse().getStatus()),true);
        JSONAssert.assertEquals(expectedStr, responseAsString, false);
    }

    @Test
    void getStocksByCompanyCodeTest_noStocksFound() throws Exception {
        List<BaseEntity> stockDetails = new ArrayList<>();
        Mockito.when(stockRepository.findByCompanyCode("CTS")).thenReturn(stockDetails);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1.0/market/stock/byCompanyCode/CTS")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();

        JSONAssert.assertEquals(String.valueOf(204), String.valueOf(result.getResponse().getStatus()),true);
    }

    @Test
    void getStocksByCompanyCodeTest_failedToGetStocks() throws Exception {
        Mockito.when(stockRepository.findByCompanyCode("CTS")).thenThrow(new IllegalArgumentException());

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1.0/market/stock/byCompanyCode/CTS")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();
        String responseAsString = result.getResponse().getContentAsString();
        System.out.println("Response: " + responseAsString);

        String expectedStr = "{\"message\":\"Failed to complete get stocks by company code request!\",\"stocks\":null,\"numberOfStock\":null,\"minStockPrice\":0.0,\"maxStockPrice\":0.0,\"avgStockPrice\":0.0}";

        JSONAssert.assertEquals(String.valueOf(500), String.valueOf(result.getResponse().getStatus()),true);
        JSONAssert.assertEquals(expectedStr, responseAsString, false);
    }

    @Test
    void getStocksByCompanyCodeAndCreatedDateBetweenTest_success() throws Exception {
        Date today = new Date();
        List<BaseEntity> stockDetails = new ArrayList<>();
        stockDetails.add(new StockDetails("DDDFFDS-123-ERT", "CTS", 120.55, today));

        Mockito.when(stockRepository.findByCompanyCodeAndCreatedDateBetween(Matchers.anyString(), Matchers.any(Date.class), Matchers.any(Date.class)))
                .thenReturn(stockDetails);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1.0/market/stock/get/CTS/2022-09-01/2022-09-10")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();
        String responseAsString = result.getResponse().getContentAsString();
        System.out.println("Response: " + responseAsString);

        String expectedStr = "{\"message\":\"Successfully returned 1 stock(s)!\",\"stocks\":[{\"id\":\"DDDFFDS-123-ERT\",\"companyCode\":\"CTS\",\"stockPrice\":120.55}],\"numberOfStock\":1,\"minStockPrice\":120.55,\"maxStockPrice\":120.55,\"avgStockPrice\":120.55}";

        JSONAssert.assertEquals(String.valueOf(200), String.valueOf(result.getResponse().getStatus()),true);
        JSONAssert.assertEquals(expectedStr, responseAsString, false);
    }

    @Test
    void getStocksByCompanyCodeAndCreatedDateBetweenTest_stockNotFound() throws Exception {
        List<BaseEntity> stockDetails = new ArrayList<>();

        Mockito.when(stockRepository.findByCompanyCodeAndCreatedDateBetween(Matchers.anyString(), Matchers.any(Date.class), Matchers.any(Date.class)))
                .thenReturn(stockDetails);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1.0/market/stock/get/CTS/2022-09-01/2022-09-10")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();
        String responseAsString = result.getResponse().getContentAsString();
        System.out.println("Response: " + responseAsString);

        JSONAssert.assertEquals(String.valueOf(204), String.valueOf(result.getResponse().getStatus()),true);
    }

    @Test
    void getStocksByCompanyCodeAndCreatedDateBetweenTest_failedToGetStocks() throws Exception {
        Mockito.when(stockRepository.findByCompanyCodeAndCreatedDateBetween(Matchers.anyString(), Matchers.any(Date.class), Matchers.any(Date.class)))
                .thenThrow(new IllegalArgumentException());

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1.0/market/stock/get/CTS/2022-09-01/2022-09-10")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andReturn();
        String responseAsString = result.getResponse().getContentAsString();
        System.out.println("Response: " + responseAsString);

        String expectedStr = "{\"message\":\"Failed to complete get stocks by created date request!\",\"stocks\":null,\"numberOfStock\":null,\"minStockPrice\":0.0,\"maxStockPrice\":0.0,\"avgStockPrice\":0.0}";

        JSONAssert.assertEquals(String.valueOf(500), String.valueOf(result.getResponse().getStatus()),true);
        JSONAssert.assertEquals(expectedStr, responseAsString, false);
    }
}
