package vos.hoteldemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vos.hoteldemo.entity.Feedback;
import vos.hoteldemo.entity.Bill;
import vos.hoteldemo.entity.Customer;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    @Query("SELECT f FROM Feedback f WHERE f.bill = :bill AND f.customer = :customer")
    Feedback findByBillAndCustomer(@Param("bill") Bill bill, @Param("customer") Customer customer);

    @Query("SELECT f FROM Feedback f ORDER BY f.id DESC")
    List<Feedback> findFeedbackByIdDesc();
}