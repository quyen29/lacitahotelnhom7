package vos.hoteldemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vos.hoteldemo.entity.Booking;
import vos.hoteldemo.entity.Customer;
import vos.hoteldemo.service.BookingService;
import vos.hoteldemo.service.CustomerService;
import vos.hoteldemo.service.FeedbackService;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/bookingHistory")
public class BookingHistoryController {
    private final CustomerService customerService;
    private final BookingService bookingService;
    private final FeedbackService feedbackService;

    @Autowired
    public BookingHistoryController(CustomerService customerService, BookingService bookingService, FeedbackService feedbackService) {
        this.customerService = customerService;
        this.bookingService = bookingService;
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public String showBookingHistory(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        Customer customer = customerService.findByEmail(email);
        List<Booking> bookings = bookingService.findByCustomer(customer);
        List<Integer> bookingIds = bookings.stream().map(Booking::getBookingID).collect(Collectors.toList());
        Map<Integer, Float> totalMap = bookingService.getTotalByBookingIds(bookingIds);
        Map<Integer, Boolean> hasFeedbackMap = bookings.stream()
                .collect(Collectors.toMap(
                        Booking::getBookingID,
                        booking -> feedbackService.findByBillAndCustomer(booking.getBill(), customer) != null
                ));
        model.addAttribute("bookings", bookings);
        model.addAttribute("totalMap", totalMap);
        model.addAttribute("hasFeedbackMap", hasFeedbackMap);
        return "customer/bookingHistory";
    }
}

