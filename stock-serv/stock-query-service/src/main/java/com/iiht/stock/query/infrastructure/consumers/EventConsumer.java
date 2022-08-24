package com.iiht.stock.query.infrastructure.consumers;

import com.iiht.stock.common.events.StockPriceAddedEvent;
import com.iiht.stock.common.events.StockPriceDeletedByCompanyCodeEvent;
import com.iiht.stock.common.events.StockPriceDeletedEvent;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {
    void consume(@Payload StockPriceAddedEvent event, Acknowledgment ack);
    void consume(@Payload StockPriceDeletedEvent event, Acknowledgment ack);
    void consume(@Payload StockPriceDeletedByCompanyCodeEvent event, Acknowledgment ack);
}
