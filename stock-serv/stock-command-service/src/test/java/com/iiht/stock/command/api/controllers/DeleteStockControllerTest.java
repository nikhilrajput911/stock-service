package com.iiht.stock.command.api.controllers;

import com.iiht.cqrs.core.events.BaseEvent;
import com.iiht.cqrs.core.events.EventModel;
import com.iiht.cqrs.core.infrastructure.CommandDispatcher;
import com.iiht.stock.command.domain.EventStoreRepository;
import com.iiht.stock.command.domain.StockAggregate;
import com.iiht.stock.common.events.StockPriceAddedEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebMvcTest(DeleteStockController.class)
public class DeleteStockControllerTest {
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
    void deleteStockByCompanyCodeTest() throws Exception {
        BaseEvent event = StockPriceAddedEvent.builder()
                .id("76c86c54-8c88-4dba-9609-c41d54778611")
                .companyCode("CTS")
                .stockPrice(123.12)
                .createdDate(new Date())
                .version(0)
                .build();

        EventModel persistedEvent = EventModel.builder()
                .id("63037e17b6b1947c1573920c")
                .timeStamp(new Date())
                .aggregateIdentifier("76c86c54-8c88-4dba-9609-c41d54778611")
                .aggregateType(StockAggregate.class.getTypeName())
                .version(0)
                .eventType(event.getClass().getTypeName())
                .eventData(event)
                .build();

        List<EventModel> eventStream = new ArrayList<>();
        eventStream.add(persistedEvent);

        ListenableFuture<SendResult<String, Object>> responseFuture = Mockito.mock(ListenableFuture.class);

        Mockito.when(eventStoreRepository.findByEventTypeAndCompanyCode(Matchers.anyString(), Matchers.anyString())).thenReturn(eventStream);
        Mockito.when(mongoTemplate.find(Matchers.any(Query.class), Matchers.eq(EventModel.class), Matchers.anyString())).thenReturn(eventStream);
        Mockito.when(eventStoreRepository.findByAggregateIdentifier(Matchers.anyString())).thenReturn(eventStream);
        Mockito.when(eventStoreRepository.save(Matchers.any(EventModel.class))).thenReturn(persistedEvent);
        Mockito.when(kafkaTemplate.send(Matchers.anyString(), Matchers.any(BaseEvent.class))).thenReturn(responseFuture);

        RequestBuilder request = MockMvcRequestBuilders
                .delete("/api/v1.0/market/stock/delete/byCompanyCode/CTS")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic YWRtaW46YWRtaW4=");

        MvcResult result = mockMvc.perform(request).andReturn();
        String responseAsString = result.getResponse().getContentAsString();
        System.out.println("Response: " + responseAsString);

        String expectedStr = "{\"message\":\"Stock delete request successfully completed!\"}";

        JSONAssert.assertEquals(String.valueOf(200), String.valueOf(result.getResponse().getStatus()),true);
        JSONAssert.assertEquals(expectedStr, responseAsString, false);
    }

    @Test
    void deleteStockByCompanyCodeTest_badRequest() throws Exception {
        Mockito.when(eventStoreRepository.findByEventTypeAndCompanyCode(Matchers.anyString(), Matchers.anyString())).thenThrow(new IllegalStateException());

        RequestBuilder request = MockMvcRequestBuilders
                .delete("/api/v1.0/market/stock/delete/byCompanyCode/CTS")
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
    void deleteStockByCompanyCodeTest_failedToDeleteStock() throws Exception {
        Mockito.when(eventStoreRepository.findByEventTypeAndCompanyCode(Matchers.anyString(), Matchers.anyString())).thenThrow(new IllegalArgumentException());

        RequestBuilder request = MockMvcRequestBuilders
                .delete("/api/v1.0/market/stock/delete/byCompanyCode/CTS")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic YWRtaW46YWRtaW4=");

        MvcResult result = mockMvc.perform(request).andReturn();
        String responseAsString = result.getResponse().getContentAsString();
        System.out.println("Response: " + responseAsString);

        String expectedStr = "{\"message\":\"Error while processing request to delete stock with company code - CTS.\"}";

        JSONAssert.assertEquals(String.valueOf(500), String.valueOf(result.getResponse().getStatus()),true);
        JSONAssert.assertEquals(expectedStr, responseAsString, false);
    }
}
