package vos.hoteldemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vos.hoteldemo.entity.Bill;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Integer> {
    Bill findTopByCustomer_CustomerIDOrderByBillIDDesc(int customerID);

    @Query("SELECT SUM(b.total) FROM Bill b WHERE b.invoiceTime BETWEEN :start AND :end")
    Float sumTotalByInvoiceTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT b FROM Bill b WHERE b.booking.bookingID = :bookingID")
    Bill findByBookingID(int bookingID);

    Optional<Bill> findById(int billID);
}
