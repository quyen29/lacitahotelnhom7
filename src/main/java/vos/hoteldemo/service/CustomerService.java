package vos.hoteldemo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import vos.hoteldemo.entity.Customer;

<<<<<<< HEAD
import java.util.List;
import java.util.Map;

=======
>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
@Service
public interface CustomerService extends UserDetailsService {
    public Customer findByEmail(String email);

    public void save(Customer customer);

    public void update(Customer customer);
<<<<<<< HEAD

    public List<Map<String, Object>> getCustomerSummaryData();

    public Customer findById(int id);
=======
>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
}
