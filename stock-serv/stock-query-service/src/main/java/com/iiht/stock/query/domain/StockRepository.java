package com.iiht.stock.query.domain;

import com.iiht.cqrs.core.domain.BaseEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface StockRepository extends CrudRepository<StockDetails, String> {
    List<BaseEntity> findByCompanyCode(String companyCode);

    @Query("SELECT s FROM StockDetails s WHERE s.companyCode = :companyCode and DATE_FORMAT(s.createdDate, '%Y-%m-%d') BETWEEN DATE_FORMAT(:startDate, '%Y-%m-%d') and DATE_FORMAT(:endDate, '%Y-%m-%d')")
    List<BaseEntity> findByCompanyCodeAndCreatedDateBetween(
            @Param("companyCode") String companyCode,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Transactional
    @Modifying
    @Query("DELETE FROM StockDetails s WHERE s.companyCode = :companyCode")
    void deleteStocksByCompanyCode(@Param("companyCode") String companyCode);
}
