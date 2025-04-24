package vos.hoteldemo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vos.hoteldemo.dao.CustomerRepository;
import vos.hoteldemo.dao.RoleRepository;
import vos.hoteldemo.dao.AccountRepository;
import vos.hoteldemo.entity.Account;
import vos.hoteldemo.entity.Customer;
import vos.hoteldemo.entity.Role;

import java.util.Collections;
import java.util.Collection;

@Service
public class CustomerServiceImpl implements CustomerService, org.springframework.security.core.userdetails.UserDetailsService {
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, RoleRepository roleRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void update(Customer customer) {
        customerRepository.saveAndFlush(customer);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            throw new UsernameNotFoundException("Không tìm thấy khách hàng với email: " + email);
        }
        Role role = roleRepository.findByRoleID(customer.getCustomerID());
        if (role == null) {
            throw new UsernameNotFoundException("Không tìm thấy vai trò của khách hàng!");
        }
        Account account = accountRepository.findByAccountID(email);
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role.getRoleName()));
        return new User(account.getAccountID(), account.getPassword(), authorities);
    }
}
