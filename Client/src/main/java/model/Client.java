package model;

import TCP.TCPClient;
import UDP.UDPClient;
import view.ClientFrame;

import java.io.IOException;

public class Client {
    private TCPClient tcpClient;
    private UDPClient udpClient;

    public Client() {
        udpClient = new UDPClient();
        try {
            tcpClient = new TCPClient(udpClient,"127.0.0.1", 8090);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void initTCPClient() {
        tcpClient.start();
    }
    public void initUDPClient() {
        udpClient.start();
    }


    public TCPClient getTcpClient() {
        return tcpClient;
    }

    public UDPClient getUdpClient() {
        return udpClient;
    }




}
