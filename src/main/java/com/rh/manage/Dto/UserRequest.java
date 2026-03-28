package com.rh.manage.Dto;

import com.rh.manage.Model.User;

public class UserRequest {
    private User user;
    private String typeUser;

    public UserRequest() {}
    public User getUser() {return user;}
    public String getTypeUser() {return typeUser;}
    public void setUser(User user) {this.user = user;}
    public void setTypeUser(String typeUser) {this.typeUser = typeUser;}
}
