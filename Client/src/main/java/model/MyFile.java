package model;

import java.util.ArrayList;

public class MyFile {
    private String name;
    private ArrayList<byte[]> data;

    public MyFile(String name) {
        this.name = name;
        data = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<byte[]> getData() {
        return data;
    }
}
