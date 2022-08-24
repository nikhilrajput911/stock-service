package com.iiht.cqrs.core.handlers;

import com.iiht.cqrs.core.domain.AggregateRoot;

import java.util.List;

public interface EventSourcingHandler<T> {
    void save(AggregateRoot aggregate);
    T getById(String id);
    List<T> getByEventTypeAndCompanyCode(String eventType, String companyCode);
    void republishEvents();
}
