package vos.hoteldemo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import vos.hoteldemo.entity.Customer;

import java.util.List;
import java.util.Map;

@Service
public interface CustomerService extends UserDetailsService {
    public Customer findByEmail(String email);

    public void save(Customer customer);

    public void update(Customer customer);

    public List<Map<String, Object>> getCustomerSummaryData();

    public Customer findById(int id);
}
