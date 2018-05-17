package ru.doublebyte.amznsm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.doublebyte.amznsm.services.ItemStorage;
import ru.doublebyte.amznsm.services.SessionStorage;
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

    @Value("${access.password}")
    private String password;

    private final ItemStorage itemStorage;
    private final StockInfo stockInfo;
    private final SessionStorage sessionStorage;

    private Map<String, Stock> stocks = new HashMap<>();

    @Autowired
    public IndexController(ItemStorage itemStorage, StockInfo stockInfo, SessionStorage sessionStorage) {
        this.itemStorage = itemStorage;
        this.stockInfo = stockInfo;
        this.sessionStorage = sessionStorage;
    }

    ///////////////////////////////////////////////////////////////////////////

    @GetMapping("/")
    public String index(Model model) {
        if (!sessionStorage.isAuthorized()) {
            return "redirect:/login";
        }

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
        if (!sessionStorage.isAuthorized()) {
            return "redirect:/login";
        }

        link = link.trim();

        if (!link.isEmpty()) {
            itemStorage.addItem(link);
        }

        return "redirect:/";
    }

    @PostMapping("/delete")
    public String removeItem(@RequestParam("id") String id) {
        if (!sessionStorage.isAuthorized()) {
            return "redirect:/login";
        }

        itemStorage.deleteItem(id);

        return "redirect:/";
    }

    ///////////////////////////////////////////////////////////////////////////

    @GetMapping("/login")
    public String getLogin() {
        if (sessionStorage.isAuthorized()) {
            return "redirect:/";
        } else {
            return "login";
        }
    }

    @PostMapping("/login")
    public String postLogin(
            @RequestParam("password") String password,
            Model model
    ) {
        if (this.password.equals(password)) {
            sessionStorage.setAuthorized(true);
            return "redirect:/";
        }

        model.addAttribute("failed", true);

        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        sessionStorage.setAuthorized(false);
        return "redirect:/login";
    }

}
