package com.iiht.stock.command.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iiht.cqrs.core.events.BaseEvent;
import com.iiht.cqrs.core.events.EventModel;
import com.iiht.cqrs.core.infrastructure.CommandDispatcher;
import com.iiht.stock.command.api.commands.AddStockPriceCommand;
import com.iiht.stock.command.domain.EventStoreRepository;
import com.iiht.stock.command.domain.StockAggregate;
import com.iiht.stock.common.events.StockPriceAddedEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Matchers;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebMvcTest(AddStockController.class)
public class AddStockControllerTest {

    @MockBean
    private EventStoreRepository eventStoreRepository;

    @MockBean
    private KafkaTemplate<String, Object> kafkaTemplate;

    @MockBean
    private MongoTemplate mongoTemplate;

    @Autowired
    private CommandDispatcher commandDispatcher;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addStockTest_success() throws Exception {
        AddStockPriceCommand command = new AddStockPriceCommand();
        command.setCompanyCode("CTS");
        command.setStockPrice(1200.50);

        BaseEvent event = new StockPriceAddedEvent();
        List<EventModel> eventStream = new ArrayList<>();

        EventModel persistedEvent = EventModel.builder()
                .id("dssdfsdfsdasddsad")
                .timeStamp(new Date())
                .aggregateIdentifier("JHGJHVMNVNBVN")
                .aggregateType(StockAggregate.class.getTypeName())
                .version(0)
                .eventType(event.getClass().getTypeName())
                .eventData(event)
                .build();

        Mockito.when(eventStoreRepository.findByAggregateIdentifier(Matchers.anyString())).thenReturn(eventStream);
        Mockito.when(eventStoreRepository.save(Matchers.any(EventModel.class))).thenReturn(persistedEvent);
        Mockito.when(kafkaTemplate.send(Matchers.anyString(), Matchers.any(BaseEvent.class))).thenReturn(Matchers.any());

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(command);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/v1.0/market/stock/add")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic YWRtaW46YWRtaW4=");

        MvcResult result = mockMvc.perform(request).andReturn();
        String responseAsString = result.getResponse().getContentAsString();
        System.out.println("Response: " + responseAsString);

        String expectedStr = "{\"message\":\"Stock creation request completed successfully!\"}";

        JSONAssert.assertEquals(String.valueOf(201), String.valueOf(result.getResponse().getStatus()),true);
        JSONAssert.assertEquals(expectedStr, responseAsString, false);
    }

    @Test
    void addStockTest_badRequest() throws Exception {
        Mockito.when(eventStoreRepository.findByAggregateIdentifier(Matchers.anyString())).thenThrow(new IllegalStateException());

        AddStockPriceCommand command = new AddStockPriceCommand();
        command.setCompanyCode("CTS");
        command.setStockPrice(1200.50);

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(command);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/v1.0/market/stock/add")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic YWRtaW46YWRtaW4=");

        MvcResult result = mockMvc.perform(request).andReturn();
        String responseAsString = result.getResponse().getContentAsString();
        System.out.println("Response: " + responseAsString);

        String expectedStr = "{\"message\":\"java.lang.IllegalStateException\"}";

        JSONAssert.assertEquals(String.valueOf(400), String.valueOf(result.getResponse().getStatus()),true);
        JSONAssert.assertEquals(expectedStr, responseAsString, false);
    }

    @Test
    void addStockTest_failedToAddStock() throws Exception {
        Mockito.when(eventStoreRepository.findByAggregateIdentifier(Matchers.anyString())).thenThrow(new IllegalArgumentException());

        AddStockPriceCommand command = new AddStockPriceCommand();
        command.setCompanyCode("CTS");
        command.setStockPrice(1200.50);

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(command);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/v1.0/market/stock/add")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic YWRtaW46YWRtaW4=");

        MvcResult result = mockMvc.perform(request).andReturn();
        String responseAsString = result.getResponse().getContentAsString();
        System.out.println("Response: " + responseAsString);

        JSONAssert.assertEquals(String.valueOf(500), String.valueOf(result.getResponse().getStatus()),true);
    }
}
