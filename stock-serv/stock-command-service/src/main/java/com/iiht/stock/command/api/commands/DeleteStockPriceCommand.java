package com.iiht.stock.command.api.commands;

import com.iiht.cqrs.core.commands.BaseCommand;

public class DeleteStockPriceCommand extends BaseCommand {
    public DeleteStockPriceCommand(String id) {
        super(id);
    }
}
