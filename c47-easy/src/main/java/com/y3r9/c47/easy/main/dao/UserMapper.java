package com.y3r9.c47.easy.main.dao;

import com.y3r9.c47.easy.main.domain.User;

import java.util.List;

/**
 * Created by zyq on 2014/8/7.
 */
public interface UserMapper {

    public List<User> selectUser();
    public void insertUser(User user);
    public void updateUser(User user);
    public void deleteUser(int userId);
}
