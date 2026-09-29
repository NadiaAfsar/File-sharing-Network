package UDP;

import model.MyFile;
import model.User;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Scanner;

public class Receiver extends Thread{
    private DatagramSocket datagramSocket;
    private Object lock;
    private User user;
    private FileHandler fileHandler;
    public Receiver(DatagramSocket datagramSocket, Object lock) {
        this.lock = lock;
        this.datagramSocket = datagramSocket;
        fileHandler = new FileHandler();
    }
    public void run() {
        receiveFile();
    }

    private byte[] receiveData(int size) {
            byte[] data = new byte[size];
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
            try {
                datagramSocket.receive(datagramPacket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return datagramPacket.getData();
    }
    public void receiveFile() {
        synchronized (lock) {
            String sizeString = getString();
            int packets = Integer.parseInt(sizeString);
            String lastPacketString = getString();
            int lastPacket = Integer.parseInt(lastPacketString);
            String name = getString();
            MyFile file = new MyFile(name);
            for (int i = 0; i < packets; i++) {
                int finalI = i;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                            byte[] data = null;
                            if (finalI == packets - 1) {
                                data = receiveData(lastPacket);
                            } else {
                                data = receiveData(65507);
                            }
                            file.getData().add(data);
                            if (file.getData().size() == packets) {
                                user.getFiles().add(file.getName());
                                fileHandler.createFile(name, user.getUsername(), file.getData());
                            }
                    }
                }).start();
            }
            try {
                sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getString() {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(receiveData(1024));
            Scanner scanner = new Scanner(byteArrayInputStream);
            return scanner.nextLine();
    }

    public void setUser(User user) {
        this.user = user;
    }
}
