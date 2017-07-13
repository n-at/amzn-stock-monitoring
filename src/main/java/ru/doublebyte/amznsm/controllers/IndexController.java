package ru.doublebyte.amznsm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.doublebyte.amznsm.services.ItemStorage;

@Controller
@RequestMapping("/")
public class IndexController {

    private ItemStorage itemStorage;

    @Autowired
    public IndexController(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    ///////////////////////////////////////////////////////////////////////////

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("items", itemStorage.getItems());
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
