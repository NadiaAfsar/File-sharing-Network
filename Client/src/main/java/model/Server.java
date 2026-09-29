package model;

import java.util.ArrayList;

public class Server {
    private ArrayList<User> users;
    public Server() {
        users = new ArrayList<>();
    }
    public void addUser(User user) {
        users.add(user);
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
