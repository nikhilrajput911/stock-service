package com.iiht.stock.query.api.dto;

import com.iiht.stock.common.dto.BaseResponse;

import com.iiht.stock.query.domain.StockDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class StockLookupResponse extends BaseResponse {
    private List<StockDetails> stocks;

    public StockLookupResponse(String message) {
        super(message);
    }
}
