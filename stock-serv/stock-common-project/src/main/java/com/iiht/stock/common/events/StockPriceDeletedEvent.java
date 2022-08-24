package com.iiht.stock.common.events;

import com.iiht.cqrs.core.events.BaseEvent;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class StockPriceDeletedEvent extends BaseEvent {
}
