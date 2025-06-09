package vos.hoteldemo.service;

import vos.hoteldemo.entity.Admin;

public interface AdminService {
    Admin findByEmail(String email);
}
