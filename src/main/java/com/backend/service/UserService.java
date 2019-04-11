package com.backend.service;

public interface UserService {

    boolean login(String username, String password, String companyId);
}
