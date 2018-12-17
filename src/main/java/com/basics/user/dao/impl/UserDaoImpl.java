package com.basics.user.dao.impl;

import com.basics.user.dao.UserDao;
import com.basics.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author hyk
 */
@Slf4j
@Repository
public class UserDaoImpl implements UserDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findUser() {
        String sql = "select id,userName from t_user";
        Long st = System.currentTimeMillis();
        List<User> userList = jdbcTemplate.query(sql,new BeanPropertyRowMapper(User.class));
        Long et = System.currentTimeMillis();
        log.info(String.valueOf(et -st )+"毫秒");
        return userList;
    }

    @Override
    public User findUserById(String id) {
        String sql = "select * from t_user where id = ?";
        User user = jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(User.class),id);
        return user;
    }

    @Override
    public void saveUser(User user) {

        String sql = "insert into t_user(id,userName) VALUES (?,?)";
        Object[] val = {user.getId(),user.getUserName()};
        Long st = System.currentTimeMillis();
        jdbcTemplate.update(sql,val);
        Long et = System.currentTimeMillis();
        log.info(String.valueOf( et - st )+"毫秒");
    }
}
