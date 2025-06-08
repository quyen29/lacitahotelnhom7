package vos.hoteldemo.service;

import org.springframework.stereotype.Service;
import vos.hoteldemo.entity.Bill;
import vos.hoteldemo.entity.Customer;
import vos.hoteldemo.entity.Feedback;

import java.util.List;

@Service
public interface FeedbackService {
    List<Feedback> getAllFeedback();

    void saveFeedback(Feedback feedback);

    Feedback findByBillAndCustomer(Bill bill, Customer customer);
}
