package model;

import Controller.Configs;
import Controller.ServerConfigs;
import TCP.TCPServer;
import UDP.UDPServer;
import view.ServerFrame;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private ServerFrame serverFrame;
    private TCPServer tcpServer;
    private UDPServer udpServer;
    private static Server instance;
    private Integer port;
    private ArrayList<User> users;
    private Map<String, User> usersMap;
    private static boolean isMade;
    private Server() {
        port = 8090;
        usersMap = new HashMap<>();
        users = new ArrayList<>();
        serverFrame = new ServerFrame();
        Configs.getInstance().loadData(this);
        isMade = true;
    }
    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public void initTcpServer() throws IOException {
        tcpServer = new TCPServer(port);
        tcpServer.start();
    }
    public void initUdpServer() {
        udpServer = new UDPServer();
        udpServer.run();
    }

    public ServerFrame getServerFrame() {
        return serverFrame;
    }

    public Map<String, User> getUsersMap() {
        return usersMap;
    }
    public void addUser(User user) {
        usersMap.put(user.getUsername(), user);
        users.add(user);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setUsersMap(Map<String, User> usersMap) {
        this.usersMap = usersMap;
    }

    public UDPServer getUdpServer() {
        return udpServer;
    }

    public static boolean isIsMade() {
        return isMade;
    }
}