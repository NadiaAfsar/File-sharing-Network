package TCP;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientListener {
    private Socket socket;
    private String username;
    private Scanner socketScanner;
    private PrintWriter socketPrintWriter;
    private final Object lock1;
    private final Object lock2;

    public ClientListener(Socket socket) throws IOException {
        this.socket = socket;
        lock1 = new Object();
        lock2 = new Object();
        setStreams();
    }
    private void setStreams() throws IOException {
        socketScanner = new Scanner(socket.getInputStream());
        socketPrintWriter = new PrintWriter(socket.getOutputStream());
    }
    public void sendMessage(String message) {
        synchronized (lock1) {
            socketPrintWriter.println(message);
            socketPrintWriter.flush();
        }
    }
    public String getMessage() {
        synchronized (lock2) {
            return socketScanner.nextLine();
        }
    }
}
