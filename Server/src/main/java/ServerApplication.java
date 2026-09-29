import model.Server;

import java.io.IOException;

public class ServerApplication implements Runnable{

    @Override
    public void run() {
        try {
            Server.getInstance().initTcpServer();
            Server.getInstance().initUdpServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
