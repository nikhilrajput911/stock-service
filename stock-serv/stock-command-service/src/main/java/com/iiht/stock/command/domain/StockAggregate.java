package com.iiht.stock.command.domain;

import com.iiht.cqrs.core.domain.AggregateRoot;
import com.iiht.stock.command.api.commands.AddStockPriceCommand;
import com.iiht.stock.common.events.StockPriceAddedEvent;
import com.iiht.stock.common.events.StockPriceDeletedByCompanyCodeEvent;
import com.iiht.stock.common.events.StockPriceDeletedEvent;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class StockAggregate extends AggregateRoot {

    public StockAggregate(AddStockPriceCommand command) {
        raiseEvent(StockPriceAddedEvent.builder()
                    .id(command.getId())
                    .companyCode(command.getCompanyCode())
                    .stockPrice(command.getStockPrice())
                    .createdDate(new Date())
                    .build());
    }

    public void apply(StockPriceAddedEvent event) {
        this.id = event.getId();
    }

    public void deleteStockPrice() {
        raiseEvent(StockPriceDeletedEvent.builder()
                    .id(this.id)
                    .build());
    }

    public void apply(StockPriceDeletedEvent event) {
        this.id = event.getId();
    }

    public void deleteStockPriceByCompanyCode(String companyCode) {
        raiseEvent(StockPriceDeletedByCompanyCodeEvent.builder()
                    .id(this.id)
                    .companyCode(companyCode)
                    .build());
    }

    public void apply(StockPriceDeletedByCompanyCodeEvent event) {
        this.id = event.getId();
    }
}
