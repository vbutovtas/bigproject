package com.project.integration.serv.services;

import com.project.integration.dao.entity.User;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@Service
@ComponentScan("com.project.integration")
public class UserService {
    public UserService() {
    }
    public void create(User user, boolean isCustomer){

    }
}
