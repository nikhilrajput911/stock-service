package com.iiht.stock.command.domain;


import com.iiht.cqrs.core.events.EventModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventStoreRepository extends MongoRepository<EventModel, String> {
    List<EventModel> findByAggregateIdentifier(String aggregateIdentifier);

    @Query("{ 'eventType': ?0, 'eventData.companyCode': ?1 }")
    List<EventModel> findByEventTypeAndCompanyCode(String eventType, String companyCode);
}
