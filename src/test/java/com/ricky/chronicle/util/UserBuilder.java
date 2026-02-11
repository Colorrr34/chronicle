package com.ricky.chronicle.util;

import com.ricky.chronicle.entity.User;

public class UserBuilder {
    private String username = "default_test_user";
    private String hashedPassword = "hashed_password123";

    public UserBuilder withUsername(String username){
        this.username = username;
        return this;
    }

    public UserBuilder withUsernameAndHashedPassword(String username, String hashedPassword){
        this.username = username;
        this.hashedPassword = hashedPassword;
        return this;
    }

    public User build(){
        User user = new User();
        user.setUsername(this.username);
        user.setHashedPassword(this.hashedPassword);
        return user;
    }
}
