package com.iiht.stock.common.events;

import com.iiht.cqrs.core.events.BaseEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class StockPriceAddedEvent extends BaseEvent {
    private String companyCode;
    private double stockPrice;
    private Date createdDate;
}
