package vos.hoteldemo.service;

<<<<<<< HEAD
import org.springframework.stereotype.Service;
=======
>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
import vos.hoteldemo.entity.Bill;
import vos.hoteldemo.entity.Customer;
import vos.hoteldemo.entity.Feedback;

import java.util.List;

<<<<<<< HEAD
@Service
=======
>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
public interface FeedbackService {
    List<Feedback> getAllFeedback();

    void saveFeedback(Feedback feedback);

    Feedback findByBillAndCustomer(Bill bill, Customer customer);
}
