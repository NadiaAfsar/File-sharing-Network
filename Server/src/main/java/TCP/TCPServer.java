package TCP;

import model.Server;
import model.Status;
import model.User;
import view.ServerFrame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

public class TCPServer extends Thread{
    private ServerSocket serverSocket;
    private Integer serverPort;
    private RequestHandler requestHandler;
    public TCPServer (Integer serverPort) throws IOException {
        this.serverPort = serverPort;
        serverSocket = new ServerSocket(serverPort);
        requestHandler = new RequestHandler();
    }
    @Override
    public void run() {
        while(true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                String address = socket.getRemoteSocketAddress().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Server.getInstance().getServerFrame().showConnection("A client connected with Address: " + address);
                    }
                }).start();
                ServerListener listener = new ServerListener(socket);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!(listener.isClosed())) {
                            clientThread(listener);
                        }
                    }
                }).start();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void clientThread(ServerListener listener) {
        listener.setReceivedMessage();
        String message = listener.getReceivedMessage();
        if (message.equals("New username")) {
            requestHandler.receiveNewUsername(listener);
        }
        else if (message.equals("Username")) {
            requestHandler.receiveUsername(listener);
        }
        else if (message.equals("New password")) {
            requestHandler.receiveNewPassword(listener);
        }
        else if (message.equals("Password")) {
            requestHandler.receivePassword(listener);
        }
        else if (message.equals("Offline")) {
            requestHandler.setOffline(listener.getUsername());
        }
        else if (message.equals("Upload")) {
            requestHandler.receiveFile(listener);
        }
        else if (message.equals("Files")) {
            requestHandler.sendFilesName(listener);
        }
        else if (message.equals("Download")) {
            requestHandler.download(listener);
        }
        else if (message.equals("Accepted requests")) {
            requestHandler.sendAcceptedRequests(listener);
        }
        else if (message.equals("Requests")) {
            requestHandler.sendRequests(listener);
        }
        else if (message.equals("Request")) {
            requestHandler.receiveRequest(listener);
        }
        else if (message.equals("Response to request")) {
            requestHandler.receiveResponse(listener);
        }
        else {
            requestHandler.checkClientMessage(listener);
        }
        if (message.equals("Closed")) {
            listener.setClosed(true);
        }
    }

}
