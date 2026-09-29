package TCP;

import model.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

public class ServerListener {
    private Socket socket;
    private Scanner socketScanner;
    private PrintWriter socketPrintWriter;
    private final Object lock;
    private String receivedMessage;
    private String action;
    private String username;
    private User user;
    private boolean closed;

    public ServerListener(Socket socket) throws IOException {
        this.socket = socket;
        lock = new Object();
        receivedMessage = "null";
        setStreams();
    }
    private void setStreams() throws IOException {
        socketScanner = new Scanner(socket.getInputStream());
        socketPrintWriter = new PrintWriter(socket.getOutputStream());
    }
    public void sendMessage(String message) {
        synchronized (lock) {
//            System.out.println("sent:"+message);
            socketPrintWriter.println(message);
            socketPrintWriter.flush();
        }
    }
    public String getMessage() {
        synchronized (lock) {
//            String s = socketScanner.nextLine();
//            System.out.println(s);
//            return s;
            return socketScanner.nextLine();
        }
    }
    public void setReceivedMessage() {
        try {
            receivedMessage = getMessage();
        }
        catch (Exception e) {

        }
    }

    public String getReceivedMessage() {
        return receivedMessage;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public Socket getSocket() {
        return socket;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
