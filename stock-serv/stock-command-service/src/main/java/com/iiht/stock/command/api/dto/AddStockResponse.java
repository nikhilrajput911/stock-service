package com.iiht.stock.command.api.dto;

import com.iiht.stock.common.dto.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddStockResponse extends BaseResponse {
    private String id;

    public AddStockResponse(String message, String id) {
        super(message);
        this.id = id;
    }
}
