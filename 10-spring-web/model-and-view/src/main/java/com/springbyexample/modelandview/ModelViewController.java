package com.springbyexample.modelandview;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Mujuzi Moses
 */
@Controller
public class ModelViewController {

    @GetMapping("/items")
    public String getItem(Model model) {
        model.addAttribute("name", "Laptop");
        model.addAttribute("price", 999.99);

        return "item";
    }
}
