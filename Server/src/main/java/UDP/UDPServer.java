package UDP;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class UDPServer implements Runnable{
    private Receiver receiver;
    private Sender sender;
    private Integer port;
    private DatagramSocket datagramSocket;
    private Object lock;
    public UDPServer() {
        port = 8091;
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

    public Sender getNewSender(InetSocketAddress inetSocketAddress) {
        return new Sender(datagramSocket, inetSocketAddress, lock);
    }
}
