package com.iiht.stock.query.api.queries;

import com.iiht.cqrs.core.domain.BaseEntity;

import java.util.List;

public interface QueryHandler {
    List<BaseEntity> handle(FindAllStocksQuery query);
    List<BaseEntity> handle(FindStocksByCompanyCodeQuery query);
    List<BaseEntity> handle(FindStocksByCompanyCodeAndBetweenCreatedDateQuery query);
}
