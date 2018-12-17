package com.basics.user.service.impl;

import com.basics.user.dao.UserDao;
import com.basics.user.entity.User;
import com.basics.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author  hyk
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Override
    public List<User> findUser() {
        return userDao.findUser();
    }

    @Override
    public User findUserById(String id) {
        return userDao.findUserById(id);
    }

    @Override
    public void saveUser(User user) {
        userDao.saveUser(user);
    }
}
