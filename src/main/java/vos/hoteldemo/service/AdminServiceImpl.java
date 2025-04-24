package vos.hoteldemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vos.hoteldemo.dao.AdminRepository;
import vos.hoteldemo.entity.Admin;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}

