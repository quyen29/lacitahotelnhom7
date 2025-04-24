package vos.hoteldemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class HomeController {
    @GetMapping()
    public String showHomePage(Model model) {
        return "customer/home";
    }

    @GetMapping("/contact")
    public String showContactPage() {
        return "customer/contact";
    }

    @GetMapping("/showPage403")
    public String showPage403() {
        return "error/403";
    }

    @GetMapping("/showPage404")
    public String showPage404() {
        return "error/404";
    }
}
