package com.dnf.dao;

import com.dnf.bean.Users;

import java.util.List;

/**
 * DESCRIPTION:
 * Create on:2018/3/7.
 *
 * @author MACHUNHUI
 */
public interface  UsersDao {
    Users getUsersByUsername(String username);

    List<Users> selectAllUsers();

    List<Users> getUsersByUsername2();
}
