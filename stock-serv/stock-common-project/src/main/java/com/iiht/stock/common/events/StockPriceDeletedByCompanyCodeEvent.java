package com.iiht.stock.common.events;

import com.iiht.cqrs.core.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class StockPriceDeletedByCompanyCodeEvent extends BaseEvent {
    private String companyCode;
}
