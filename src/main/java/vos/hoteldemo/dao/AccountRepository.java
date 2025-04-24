package vos.hoteldemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vos.hoteldemo.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByAccountID(String accountID);

    Account findByAccountID(String accountID);
}
