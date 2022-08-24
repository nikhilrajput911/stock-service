package com.iiht.stock.command.api.commands;

import com.iiht.cqrs.core.handlers.EventSourcingHandler;
import com.iiht.stock.command.domain.StockAggregate;
import com.iiht.stock.common.events.StockPriceAddedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockCommandHandler implements CommandHandler {
    @Autowired
    private EventSourcingHandler<StockAggregate> eventSourcingHandler;

    @Override
    public void handle(AddStockPriceCommand command) {
        StockAggregate aggregate = new StockAggregate(command);
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(DeleteStockPriceCommand command) {
        StockAggregate aggregate = eventSourcingHandler.getById(command.getId());
        aggregate.deleteStockPrice();
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(DeleteByCompanyCodeStockPriceCommand command) {
        List<StockAggregate> stockAggregateList = eventSourcingHandler.getByEventTypeAndCompanyCode(
                StockPriceAddedEvent.class.getTypeName(), command.getCompanyCode());

        for(StockAggregate aggregate : stockAggregateList) {
            aggregate.deleteStockPriceByCompanyCode(command.getCompanyCode());
            eventSourcingHandler.save(aggregate);
        }
    }
}
