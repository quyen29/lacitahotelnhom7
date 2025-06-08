package vos.hoteldemo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @Column(name = "account_id")
    private String accountID;

    @Column(name = "role_id")
    private int roleID;

    @Column(name = "password")
    private String password;

    public Account() {
    }

    public Account(String accountID, String password, int roleID) {
        this.accountID = accountID;
        this.password = password;
        this.roleID = roleID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
