package vos.hoteldemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vos.hoteldemo.entity.Customer;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByEmail(String email);

    @Query(value = """
        SELECT 
            c.customer_id,
            c.full_name,
            c.phone_number,
            c.email,
            ct.customer_type_name,
            MAX(b.invoice_time) AS latest_booking_date,
            COUNT(b.bill_id) AS total_bookings,
            SUM(b.total) AS total_spent
        FROM customer c
        LEFT JOIN customer_type ct ON c.customer_type_id = ct.customer_type_id
        LEFT JOIN bill b ON c.customer_id = b.customer_id
        GROUP BY c.customer_id, c.full_name, c.phone_number, c.email, ct.customer_type_name
        ORDER BY c.customer_id
    """, nativeQuery = true)
    List<Object[]> findCustomerSummary();
}
