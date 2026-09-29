package UDP;

import model.MyFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Scanner;

public class Receiver extends Thread{
    private DatagramSocket datagramSocket;
    private FileHandler fileHandler;
    private Object lock;
    private String name;
    private String path;
    private int packets;
    private int progress;
    public Receiver(DatagramSocket datagramSocket, Object lock) {
        this.datagramSocket = datagramSocket;
        fileHandler = new FileHandler();
        this.lock = lock;
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
    private void receiveFile() {
        synchronized (lock) {
            progress = 0;
            String lastPacketString = getString();
            int lastPacket = Integer.parseInt(lastPacketString);
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
                            progress++;
                            file.getData().add(data);
                            if (file.getData().size() == packets) {
                                fileHandler.createFile(name, path, file.getData());
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

    public void setFileName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPackets(int packets) {
        this.packets = packets;
    }

    public int getProgress() {
        return progress;
    }
}
