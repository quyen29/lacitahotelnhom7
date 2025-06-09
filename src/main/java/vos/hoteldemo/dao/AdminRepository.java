package vos.hoteldemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import vos.hoteldemo.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByEmail(String email);
}

