package com.iiht.stock.query.api.queries;

import com.iiht.cqrs.core.domain.BaseEntity;
import com.iiht.stock.query.domain.StockDetails;
import com.iiht.stock.query.domain.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockQueryHandler implements QueryHandler {
    @Autowired
    private StockRepository stockRepository;

    @Override
    public List<BaseEntity> handle(FindAllStocksQuery query) {
        Iterable<StockDetails> stockDetails = stockRepository.findAll();
        List<BaseEntity> stockDetailsList = new ArrayList<>();
        stockDetails.forEach(stockDetailsList::add);
        return stockDetailsList;
    }

    @Override
    public List<BaseEntity> handle(FindStocksByCompanyCodeQuery query) {
        List<BaseEntity> stockDetails = stockRepository.findByCompanyCode(query.getCompanyCode());
        if (stockDetails.isEmpty()) {
            return null;
        }
        List<BaseEntity> stockDetailsList = new ArrayList<>();
        stockDetails.forEach(stockDetailsList::add);
        return stockDetailsList;
    }

    @Override
    public List<BaseEntity> handle(FindStocksByCompanyCodeAndBetweenCreatedDateQuery query) {
        List<BaseEntity> stockDetails = stockRepository.findByCompanyCodeAndCreatedDateBetween(
                query.getCompanyCode(), query.getStartDate(), query.getEndDate());

        if (stockDetails.isEmpty()) {
            return null;
        }
        List<BaseEntity> stockDetailsList = new ArrayList<>();
        stockDetails.forEach(stockDetailsList::add);
        return stockDetailsList;
    }

}
