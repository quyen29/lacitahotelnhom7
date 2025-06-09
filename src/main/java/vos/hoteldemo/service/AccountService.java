package vos.hoteldemo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import vos.hoteldemo.entity.Account;

@Service
public interface AccountService extends UserDetailsService {
    boolean existsByAccountID(String accountID);

    void save(Account account);
}
