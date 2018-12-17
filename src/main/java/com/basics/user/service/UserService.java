package com.basics.user.service;

import com.basics.user.entity.User;

import java.util.List;

/**
 * @author hyk
 * @2018-08-21 12:00:00
 */
public interface UserService {

    /**
     * 所有用户的list
     * @return
     */
    public List<User> findUser();

    /**
     * 根据id找单个用户
     * @param id
     * @return
     */
    public User findUserById(String id);

    /**
     * 保存用户
     * @param user
     */
    public void saveUser(User user);

}
