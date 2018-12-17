package com.basics.user.dao;

import com.basics.user.entity.User;

import java.util.List;

/**
 * 用户的逻辑层接口
 * @author hyk
 */
public interface UserDao {

    /**
     *  所有用户的list
     * @return
     */
    public List<User> findUser();

    /**
     * 查找单个用户
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
