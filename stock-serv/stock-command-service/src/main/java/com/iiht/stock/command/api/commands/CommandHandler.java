package com.iiht.stock.command.api.commands;

public interface CommandHandler {
    void handle(AddStockPriceCommand command);
    void handle(DeleteStockPriceCommand command);
    void handle(DeleteByCompanyCodeStockPriceCommand command);
}
