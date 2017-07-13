package ru.doublebyte.amznsm.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import ru.doublebyte.amznsm.structs.Stock;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Stock monitor itself
 */
public class StockMonitor {

    private static final Logger logger = LoggerFactory.getLogger(StockMonitor.class);

    private ItemStorage itemStorage;
    private StockInfo stockInfo;
    private MailMessageSender mailMessageSender;

    public StockMonitor(ItemStorage itemStorage, StockInfo stockInfo, MailMessageSender mailMessageSender) {
        this.itemStorage = itemStorage;
        this.stockInfo = stockInfo;
        this.mailMessageSender = mailMessageSender;
    }

    ///////////////////////////////////////////////////////////////////////////

    @Scheduled(fixedDelayString = "#{${stock-monitoring.refresh-interval} * 60 * 1000}")
    public void monitor() {
        logger.info("monitoring stocks...");

        List<Stock> stocks = itemStorage.getItems().entrySet().stream()
                .map(it -> stockInfo.getInfo(it.getKey(), it.getValue()))
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList());

        if (stocks.size() > 0) {
            mailMessageSender.sendStocks(stocks);
        } else {
            logger.info("no stocks found");
        }
    }

}
