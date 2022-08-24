package com.iiht.stock.command.api.commands;

import com.iiht.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class AddStockPriceCommand extends BaseCommand {
    private String companyCode;
    private double stockPrice;
}
