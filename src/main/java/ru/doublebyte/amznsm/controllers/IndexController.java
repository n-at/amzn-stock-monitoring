package ru.doublebyte.amznsm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.doublebyte.amznsm.services.ItemStorage;
import ru.doublebyte.amznsm.services.StockInfo;
import ru.doublebyte.amznsm.structs.Stock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class IndexController {

    private static final String DEFAULT_NAME = "Unknown Item";

    private ItemStorage itemStorage;
    private StockInfo stockInfo;

    private Map<String, Stock> stocks = new HashMap<>();

    @Autowired
    public IndexController(ItemStorage itemStorage, StockInfo stockInfo) {
        this.itemStorage = itemStorage;
        this.stockInfo = stockInfo;
    }

    ///////////////////////////////////////////////////////////////////////////

    @GetMapping("/")
    public String index(Model model) {
        Map<String, String> items = itemStorage.getItems();

        List<Stock> stockItems = items.entrySet().stream()
                .map(it -> {
                    String id = it.getKey();
                    String link = it.getValue();

                    if (stocks.containsKey(id)) {
                        return stocks.get(id);
                    }

                    Stock stock = stockInfo.getInfo(id, link);

                    if (stock == null) {
                        stock = new Stock(id, link);
                        stock.setName(DEFAULT_NAME);
                    } else {
                        stock.setPrice(null);
                        stock.setStock(null);
                    }

                    stocks.put(id, stock);

                    return stock;
                })
                .sorted()
                .collect(Collectors.toList());

        model.addAttribute("items", stockItems);

        return "index";
    }

    @PostMapping("/add")
    public String addItem(@RequestParam("link") String link) {
        link = link.trim();

        if (!link.isEmpty()) {
            itemStorage.addItem(link);
        }

        return "redirect:/";
    }

    @PostMapping("/delete")
    public String removeItem(@RequestParam("id") String id) {
        itemStorage.deleteItem(id);
        return "redirect:/";
    }

}
