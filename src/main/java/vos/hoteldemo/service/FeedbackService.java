package vos.hoteldemo.service;

import vos.hoteldemo.entity.Bill;
import vos.hoteldemo.entity.Customer;
import vos.hoteldemo.entity.Feedback;

import java.util.List;

public interface FeedbackService {
    List<Feedback> getAllFeedback();

    void saveFeedback(Feedback feedback);

    Feedback findByBillAndCustomer(Bill bill, Customer customer);
}
