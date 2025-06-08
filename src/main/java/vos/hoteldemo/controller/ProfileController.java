package vos.hoteldemo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vos.hoteldemo.entity.Customer;
import vos.hoteldemo.service.CustomerService;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final CustomerService customerService;

    public ProfileController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    public String showProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Customer customer = customerService.findByEmail(email);
        model.addAttribute("customer", customer);
        return "customer/profile";
    }

    @GetMapping("/customize")
    public String customize(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Customer customer = customerService.findByEmail(email);
        model.addAttribute("customer", customer);
        return "customer/customize";
    }

    @PostMapping("/customize/save")
    public String save(@ModelAttribute("customer") Customer customer, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Customer existingCustomer = customerService.findByEmail(email);
        existingCustomer.setFullName(customer.getFullName());
        existingCustomer.setPhoneNumber(customer.getPhoneNumber());
        existingCustomer.setSsn(customer.getSsn());
        existingCustomer.setDOB(customer.getDOB());
        existingCustomer.setGender(customer.getGender());
        customerService.update(existingCustomer);
        model.addAttribute("customer", existingCustomer);
        return "redirect:/profile";
    }
}
