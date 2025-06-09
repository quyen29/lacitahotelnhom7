package vos.hoteldemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vos.hoteldemo.entity.Bill;
import vos.hoteldemo.entity.Customer;
import vos.hoteldemo.entity.Feedback;
import vos.hoteldemo.dao.FeedbackRepository;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findFeedbackByIdDesc();
    }

    @Override
    public void saveFeedback(Feedback feedback) {
        feedbackRepository.save(feedback);
    }

    @Override
    public Feedback findByBillAndCustomer(Bill bill, Customer customer) {
        return feedbackRepository.findByBillAndCustomer(bill, customer);
    }
}
