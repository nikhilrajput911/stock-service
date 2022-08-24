package com.iiht.stock.command.infrastructure;

import com.iiht.cqrs.core.domain.AggregateRoot;
import com.iiht.cqrs.core.events.BaseEvent;
import com.iiht.cqrs.core.handlers.EventSourcingHandler;
import com.iiht.cqrs.core.infrastructure.EventStore;
import com.iiht.stock.command.domain.StockAggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class StockEventSourcingHandler implements EventSourcingHandler<StockAggregate> {
    @Autowired
    private EventStore eventStore;

//    @Autowired
//    private EventProducer eventProducer;

    @Override
    public void save(AggregateRoot aggregate) {
        eventStore.saveEvents(aggregate.getId(), aggregate.getUncommittedChanges(), aggregate.getVersion());
        aggregate.markChangesAsCommitted();
    }

    @Override
    public StockAggregate getById(String id) {
        StockAggregate aggregate = new StockAggregate();
        List<BaseEvent> events = eventStore.getEvents(id);
        if (events != null && !events.isEmpty()) {
            aggregate.replayEvents(events);
            Optional<Integer> latestVersion = events.stream().map(x -> x.getVersion()).max(Comparator.naturalOrder());
            aggregate.setVersion(latestVersion.get());
        }
        return aggregate;
    }

    @Override
    public List<StockAggregate> getByEventTypeAndCompanyCode(String eventType, String companyCode) {
        List<BaseEvent> eventStream = eventStore.getEventsByEventTypeAndCompanyCode(eventType, companyCode);
        List<StockAggregate> stockAggregateList = new ArrayList<>();
        for(BaseEvent event : eventStream) {
            List<BaseEvent> events = eventStore.getEvents(event.getId());
            StockAggregate aggregate = new StockAggregate();
            if (events != null && !events.isEmpty()) {
                aggregate.replayEvents(events);
                Optional<Integer> latestVersion = events.stream().map(x -> x.getVersion()).max(Comparator.naturalOrder());
                aggregate.setVersion(latestVersion.get());
            }
            stockAggregateList.add(aggregate);
        }
        return stockAggregateList;
    }

    @Override
    public void republishEvents() {
//        var aggregateIds = eventStore.getAggregateIds();
//        for(var aggregateId: aggregateIds) {
//            var aggregate = getById(aggregateId);
//            if (aggregate == null || !aggregate.getActive()) continue;
//            var events = eventStore.getEvents(aggregateId);
//            for(var event: events) {
//                eventProducer.produce(event.getClass().getSimpleName(), event);
//            }
//        }
    }
}
