package com.y3r9.c47.dog.demo.security;

import java.security.Principal;

/**
 * Created by zyq on 2014/8/19.
 */
public class UserPrincipal implements Principal {

    private final String username;

    public UserPrincipal(String username) {
        this.username = username;
    }

    @Override
    public String getName() {
        return username;
    }
}
