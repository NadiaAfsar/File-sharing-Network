import model.Client;
import view.ClientFrame;

public class ClientApplication implements Runnable{
    @Override
    public void run() {
        try {
            Client client = new Client();
            client.initTCPClient();
            client.initUDPClient();
            new ClientFrame(client.getTcpClient(), client.getUdpClient());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
