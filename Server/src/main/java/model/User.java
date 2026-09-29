package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String username;
    private String password;
    private Status status;
    private ArrayList<String> files;
    private ArrayList<String[]> requests;
    private ArrayList<String[]> acceptedRequests;
    public User(String username) {
        this.username = username;
    }


    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.status = Status.ONLINE;
        requests = new ArrayList<>();
        acceptedRequests = new ArrayList<>();
        files = new ArrayList<>();
    }

    public ArrayList<String[]> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<String[]> requests) {
        this.requests = requests;
    }

    public ArrayList<String[]> getAcceptedRequests() {
        return acceptedRequests;
    }

    public void setAcceptedRequests(ArrayList<String[]> acceptedRequests) {
        this.acceptedRequests = acceptedRequests;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<String> getFiles() {
        return files;
    }
    public boolean fileExists(String name) {
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).equals(name)) {
                return true;
            }
        }
        return false;
    }
    public String[] getRequest(String name, String username) {
        for (int i = 0; i < requests.size(); i++) {
            if (requests.get(i)[0].equals(name) && requests.get(i)[1].equals(username)) {
                return requests.get(i);
            }
        }
        return null;
    }
}
