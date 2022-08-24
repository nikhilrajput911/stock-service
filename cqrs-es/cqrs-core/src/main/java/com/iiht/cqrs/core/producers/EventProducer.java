package com.iiht.cqrs.core.producers;

import com.iiht.cqrs.core.events.BaseEvent;

public interface EventProducer {
    void produce(String topic, BaseEvent event);
}