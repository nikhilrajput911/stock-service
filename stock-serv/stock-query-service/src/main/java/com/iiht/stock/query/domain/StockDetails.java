package com.iiht.stock.query.domain;

import com.iiht.cqrs.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class StockDetails extends BaseEntity {
    @Id
    private String id;
    private String companyCode;
    private double stockPrice;
    private Date createdDate;
}
