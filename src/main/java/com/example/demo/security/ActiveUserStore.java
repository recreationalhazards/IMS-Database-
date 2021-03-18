package com.example.demo.security;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

public class ActiveUserStore {

    public List<String> users;

    public ActiveUserStore() { users = new ArrayList<>();
    }

    public List<String> getUsers() { return users; }

    public void setUsers(List<String> users) { this.users = users; }
}
