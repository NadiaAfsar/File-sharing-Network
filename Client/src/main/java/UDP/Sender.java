package UDP;

import model.MyFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Sender extends Thread{
    private DatagramSocket datagramSocket;
    private InetSocketAddress serverAddress;
    private FileHandler fileHandler;
    private int progress;
    private Object lock;
    private File file;

    public Sender(DatagramSocket datagramSocket, Object lock) {
        this.lock = lock;
        this.datagramSocket = datagramSocket;
        serverAddress = new InetSocketAddress("localHost", 8091);
        fileHandler = new FileHandler();
    }
    public void run() {
        sendFile();
    }
    private void sendData(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress);
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
            progress = 0;
            ArrayList<byte[]> data = fileHandler.extractData(file);
            sendString(data.size() + "");
            sendString(data.get(data.size() - 1).length + "");
            sendString(file.getName());
            for (int i = 0; i < data.size(); i++) {
                int finalI = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                                sendData(data.get(finalI));
                                progress++;
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

    public int getProgress() {
        return progress;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
