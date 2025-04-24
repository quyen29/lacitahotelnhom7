package vos.hoteldemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vos.hoteldemo.dao.AccountRepository;
import vos.hoteldemo.dao.RoleRepository;
import vos.hoteldemo.entity.Account;
import vos.hoteldemo.entity.Role;

import java.util.Collections;
import java.util.Collection;

@Primary
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean existsByAccountID(String accountID) {
        return accountRepository.existsByAccountID(accountID);
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String accountID) throws UsernameNotFoundException {
        Account account = accountRepository.findByAccountID(accountID);
        if (account == null) {
            throw new UsernameNotFoundException("Tài khoản hoặc mật khẩu không hợp lệ!");
        }
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) roleToAuthorities(account.getRoleID());
        return new User(account.getAccountID(), account.getPassword(), authorities);
    }

    private Collection<? extends GrantedAuthority> roleToAuthorities(int roleID) {
        Role role = roleRepository.findByRoleID(roleID);
        if (role == null) {
            throw new UsernameNotFoundException("Không tìm thấy vai trò của tài khoản!");
        }
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName());
        return Collections.singletonList(authority);
    }
}
