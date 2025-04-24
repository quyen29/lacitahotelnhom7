package vos.hoteldemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vos.hoteldemo.entity.Bill;
import vos.hoteldemo.entity.Customer;
import vos.hoteldemo.entity.Feedback;
import vos.hoteldemo.service.CustomerService;
import vos.hoteldemo.service.FeedbackService;
import vos.hoteldemo.service.BookingService;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {
    private final CustomerService customerService;
    private final FeedbackService feedbackService;
    private final BookingService bookingService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService, BookingService bookingService, CustomerService customerService) {
        this.feedbackService = feedbackService;
        this.bookingService = bookingService;
        this.customerService = customerService;
    }

    @GetMapping
    public String showFeedbackForm(@RequestParam("billID") int billID, Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String accountID = userDetails.getUsername();
        Customer customer = customerService.findByEmail(accountID);
        if (customer == null) {
            return "redirect:/showPage404";
        }
        Bill bill = bookingService.getBillByBillId(billID);
        if (bill == null) {
            return "redirect:/showPage404";
        }
        Feedback existingFeedback = feedbackService.findByBillAndCustomer(bill, customer);
        if (existingFeedback != null) {
            model.addAttribute("feedback", existingFeedback);
            model.addAttribute("billId", bill.getBillID());
            model.addAttribute("hasFeedback", true);
            return "customer/viewFeedback";
        }
        model.addAttribute("customerId", customer.getCustomerID());
        model.addAttribute("billId", bill.getBillID());
        model.addAttribute("hasFeedback", false);
        return "customer/feedback";
    }

    @PostMapping
    public String sendFeedback(@RequestParam("content") String content,
                               @RequestParam("bill_id") int billID,
                               @RequestParam("title") String title) {
        Bill bill = bookingService.getBillByBillId(billID);
        if (bill == null) {
            return "redirect:/showPage404";
        }
        int customerID = bill.getCustomer().getCustomerID();
        Feedback feedback = new Feedback();
        feedback.setCustomer(bill.getCustomer());
        feedback.setBill(bill);
        feedback.setFeedbackDate(LocalDateTime.now());
        feedback.setTitle(title);
        feedback.setContent(content);
        feedbackService.saveFeedback(feedback);
        return "redirect:/bookingHistory";
    }

    @GetMapping("/view")
    public String viewFeedback(@RequestParam("billID") int billID, Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String accountID = userDetails.getUsername();
        Customer customer = customerService.findByEmail(accountID);
        if (customer == null) {
            return "redirect:/showPage404";
        }
        Bill bill = bookingService.getBillByBillId(billID);
        Feedback feedback = feedbackService.findByBillAndCustomer(bill, customer);
        if (feedback != null) {
            model.addAttribute("feedback", feedback);
            return "customer/viewFeedback";
        }
        return "redirect:/showPage404";
    }
}