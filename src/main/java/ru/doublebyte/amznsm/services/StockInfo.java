package ru.doublebyte.amznsm.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.doublebyte.amznsm.structs.Stock;

/**
 * Get item stock info
 */
public class StockInfo {

    private static final Logger logger = LoggerFactory.getLogger(StockInfo.class);

    public StockInfo() {

    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Stock info by item link
     * @param link Item link
     * @return Item stock info
     */
    public Stock getInfo(String id, String link) {
        try {
            Document document = Jsoup.connect(link).get();

            Stock stock = new Stock(id, link);
            stock.setName(document.select("#productTitle").text());
            stock.setPrice(document.select("#buyNewSection .offer-price").text());
            stock.setStock(document.select("#availability").text());

            return stock;
        } catch (Exception e) {
            logger.error("Stock info error for " + link, e);
            return null;
        }
    }

}
