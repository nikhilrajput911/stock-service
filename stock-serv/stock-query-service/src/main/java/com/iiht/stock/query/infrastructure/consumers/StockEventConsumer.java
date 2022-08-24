package com.iiht.stock.query.infrastructure.consumers;

import com.iiht.stock.common.events.StockPriceAddedEvent;
import com.iiht.stock.common.events.StockPriceDeletedByCompanyCodeEvent;
import com.iiht.stock.common.events.StockPriceDeletedEvent;
import com.iiht.stock.query.infrastructure.handlers.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class StockEventConsumer implements EventConsumer {
    @Autowired
    private EventHandler eventHandler;

    @KafkaListener(topics = "StockPriceAddedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(@Payload StockPriceAddedEvent event, Acknowledgment ack) {
        this.eventHandler.on(event);
        ack.acknowledge();
    }

    @KafkaListener(topics = "StockPriceDeletedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(@Payload StockPriceDeletedEvent event, Acknowledgment ack) {
        this.eventHandler.on(event);
        ack.acknowledge();
    }

    @KafkaListener(topics = "StockPriceDeletedByCompanyCodeEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(@Payload StockPriceDeletedByCompanyCodeEvent event, Acknowledgment ack) {
        this.eventHandler.on(event);
        ack.acknowledge();
    }
}
