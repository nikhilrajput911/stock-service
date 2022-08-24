package com.iiht.stock.query.api.queries;


import com.iiht.cqrs.core.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindStocksByCompanyCodeQuery extends BaseQuery {
    private String companyCode;
}
