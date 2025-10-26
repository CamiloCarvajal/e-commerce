package com.camilo.ecommerce.domain.model;

import java.util.List;

public class Role {

    private int id;
    private String name;
    private List<User> users;

    public Role() {
    }

    public Role(int id, List<User> user, String name) {
        this.id = id;
        this.users = user;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUser() {
        return users;
    }

    public void setUser(List<User> users) {
        this.users = users;
    }
}
