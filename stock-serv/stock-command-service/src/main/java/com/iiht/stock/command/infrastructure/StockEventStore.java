package com.iiht.stock.command.infrastructure;

import com.iiht.cqrs.core.events.BaseEvent;
import com.iiht.cqrs.core.events.EventModel;
import com.iiht.cqrs.core.exceptions.AggregateNotFoundException;
import com.iiht.cqrs.core.exceptions.ConcurrencyException;
import com.iiht.cqrs.core.infrastructure.EventStore;
import com.iiht.cqrs.core.producers.EventProducer;
import com.iiht.stock.command.domain.EventStoreRepository;
import com.iiht.stock.command.domain.StockAggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockEventStore implements EventStore {
    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private EventStoreRepository eventStoreRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        List<EventModel> eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (expectedVersion != -1 && eventStream.get(eventStream.size() - 1).getVersion() != expectedVersion) {
            throw new ConcurrencyException();
        }
        int version = expectedVersion;
        for (BaseEvent event: events) {
            version++;
            event.setVersion(version);
            EventModel eventModel = EventModel.builder()
                    .timeStamp(new Date())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(StockAggregate.class.getTypeName())
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .eventData(event)
                    .build();
            EventModel persistedEvent = eventStoreRepository.save(eventModel);
            if (!persistedEvent.getId().isEmpty()) {
                eventProducer.produce(event.getClass().getSimpleName(), event);
            }
        }
    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("aggregateIdentifier").is(aggregateId));
        List<EventModel> eventStream = mongoTemplate.find(query, EventModel.class, "eventStore");
//        List<EventModel> eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);

        if (eventStream == null || eventStream.isEmpty()) {
            throw new AggregateNotFoundException("Incorrect stock ID provided!");
        }
        return eventStream.stream().map(x -> x.getEventData()).collect(Collectors.toList());
    }

    @Override
    public List<String> getAggregateIds() {
        List<EventModel> eventStream = eventStoreRepository.findAll();
        if (eventStream == null || eventStream.isEmpty()) {
            throw new IllegalStateException("Could not retrieve event stream from the event store!");
        }
        return eventStream.stream().map(EventModel::getAggregateIdentifier).distinct().collect(Collectors.toList());
    }

    @Override
    public List<BaseEvent> getEventsByEventTypeAndCompanyCode(String eventType, String companyCode) {
        List<EventModel> eventStream = eventStoreRepository.findByEventTypeAndCompanyCode(eventType, companyCode);
        if (eventStream == null || eventStream.isEmpty()) {
            throw new IllegalStateException("Could not retrieve event stream from the event store!");
        }
        return eventStream.stream().map(x -> x.getEventData()).collect(Collectors.toList());
    }
}
