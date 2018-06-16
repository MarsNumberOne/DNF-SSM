package com.dnf.service;

import com.dnf.bean.Users;

/**
 * DESCRIPTION:
 * Create on:2018/3/7.
 *
 * @author MACHUNHUI
 */
public interface UsersService {
    Users getUsersByUsername(String username);

    void sendMessage();
}
