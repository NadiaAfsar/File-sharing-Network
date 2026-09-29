package UDP;

import model.MyFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class Sender extends Thread{
    private DatagramSocket datagramSocket;
    private InetSocketAddress inetSocketAddress;
    private File file;
    private Object lock;
    private FileHandler fileHandler;

    public Sender(DatagramSocket datagramSocket, InetSocketAddress inetSocketAddress, Object lock) {
        this.datagramSocket = datagramSocket;
        this.inetSocketAddress = inetSocketAddress;
        this.lock = lock;
        fileHandler = new FileHandler();
    }
    public void run() {
        sendFile();
    }
    private void sendData(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, inetSocketAddress);
        try {
            datagramSocket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendString(String string) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);
        printWriter.println(string);
        printWriter.flush();
        byte[] data = byteArrayOutputStream.toByteArray();
        sendData(data);
    }
    private void sendFile() {
        synchronized (lock) {
            ArrayList<byte[]> data = fileHandler.extractData(file);
            sendString(data.get(data.size() - 1).length + "");
            for (int i = 0; i < data.size(); i++) {
                int finalI = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                                sendData(data.get(finalI));
                        }
                    }).start();
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void setFile(File file) {
        this.file = file;
    }
}
