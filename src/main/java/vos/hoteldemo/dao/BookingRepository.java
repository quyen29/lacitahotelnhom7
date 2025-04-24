package vos.hoteldemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vos.hoteldemo.entity.Booking;
import vos.hoteldemo.entity.Customer;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("SELECT b FROM Booking b JOIN Bill bill ON bill.booking = b WHERE bill.customer = :customer")
    List<Booking> findAllByCustomerViaBill(Customer customer);
}