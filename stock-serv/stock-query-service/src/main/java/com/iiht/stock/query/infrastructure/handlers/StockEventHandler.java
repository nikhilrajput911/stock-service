package com.iiht.stock.query.infrastructure.handlers;

import com.iiht.stock.common.events.StockPriceAddedEvent;
import com.iiht.stock.common.events.StockPriceDeletedByCompanyCodeEvent;
import com.iiht.stock.common.events.StockPriceDeletedEvent;
import com.iiht.stock.query.domain.StockDetails;
import com.iiht.stock.query.domain.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockEventHandler implements EventHandler {
    @Autowired
    StockRepository stockRepository;

    @Override
    public void on(StockPriceAddedEvent event) {
        StockDetails stockDetails = StockDetails.builder()
                .id(event.getId())
                .companyCode(event.getCompanyCode())
                .stockPrice(event.getStockPrice())
                .createdDate(event.getCreatedDate())
                .build();
        stockRepository.save(stockDetails);
    }

    @Override
    public void on(StockPriceDeletedEvent event) {
        stockRepository.deleteById(event.getId());
    }

    @Override
    public void on(StockPriceDeletedByCompanyCodeEvent event) {
        stockRepository.deleteStocksByCompanyCode(event.getCompanyCode());
    }
}
