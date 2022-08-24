package com.iiht.stock.query.api.queries;

import com.iiht.cqrs.core.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class FindStocksByCompanyCodeAndBetweenCreatedDateQuery extends BaseQuery {
    private String companyCode;
    private Date startDate;
    private Date endDate;
}
