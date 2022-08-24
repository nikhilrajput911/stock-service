package com.iiht.stock.query.infrastructure.handlers;

import com.iiht.stock.common.events.StockPriceAddedEvent;
import com.iiht.stock.common.events.StockPriceDeletedByCompanyCodeEvent;
import com.iiht.stock.common.events.StockPriceDeletedEvent;

public interface EventHandler {
    void on(StockPriceAddedEvent event);
    void on(StockPriceDeletedEvent event);
    void on(StockPriceDeletedByCompanyCodeEvent event);
}
