package com.iiht.stock.command.api.commands;

import com.iiht.cqrs.core.commands.BaseCommand;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteByCompanyCodeStockPriceCommand extends BaseCommand {
    private String companyCode;
}
