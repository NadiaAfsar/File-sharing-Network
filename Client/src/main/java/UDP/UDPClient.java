package UDP;

import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPClient extends Thread{
    private Integer port;
    private Object lock;
    private DatagramSocket datagramSocket;
    public UDPClient() {
        port = 8092;
        lock = new Object();
    }

    @Override
    public void run() {
        try {
            datagramSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }

    public Receiver getNewReceiver() {
        return new Receiver(datagramSocket, lock);
    }

    public Sender getNewSender() {
        return new Sender(datagramSocket, lock);
    }

    public Integer getPort() {
        return port;
    }
}
