package TCP;

import UDP.Progress.ReceiveProgress;
import UDP.Receiver;
import UDP.Sender;
import UDP.UDPClient;
import view.ClientFrame;
import view.MainMenu;

import java.io.IOException;
import java.net.Socket;

public class TCPClient extends Thread{
    private String serverIPAddress;
    private Integer serverPort;
    private Socket clientSocket;
    private ClientListener listener;
    private ClientFrame clientFrame;
    private String message;
    private String messageToShow;
    private String action;
    private UDPClient udpClient;
    public TCPClient(UDPClient udpClient, String serverIPAddress, int serverPort) throws IOException {
        this.serverIPAddress=serverIPAddress;
        this.serverPort=serverPort;
        this.udpClient = udpClient;
        initSocket();
        listener = new ClientListener(clientSocket);
    }
    private void initSocket() throws IOException {
        clientSocket = new Socket(serverIPAddress, serverPort);
    }

    @Override
    public void run() {
        while (true) {
            try {
                message = listener.getMessage();
                checkNameMessage();
                checkPassMessage();
                if (message.equals("Choose a username:") || message.equals("Enter your username:") ||
                        message.equals("Choose a password:") || message.equals("Enter your password:")) {
                    messageToShow = message;
                }
                else if (message.equals("Password accepted")) {
                    clientFrame.getSignUpLogIn().emptyPanel();
                    new MainMenu(clientFrame, this, udpClient);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public boolean sendPassword(String password, String action) {
        if (action.equals("1")) {
            if (password.equals("")) {
                clientFrame.getSignUpLogIn().showError("Please enter a password.");
                return false;
            }
            else if (isNotAvailable(password)) {
                clientFrame.getSignUpLogIn().showError("This password is not available.");
                return false;
            }
        }
        else {
            if (password.equals("")) {
                clientFrame.getSignUpLogIn().showError("Please enter your password.");
                return false;
            }
            else if (isNotAvailable(password)) {
                clientFrame.getSignUpLogIn().showError("Wrong password!");
                return false;
            }
        }
        return true;
    }
    public boolean sendUsername(String username, String action) {
        if (action.equals("1")) {
            if (username.equals("")) {
                clientFrame.getSignUpLogIn().showError("Please enter a username.");
                return false;
            } else if (isNotAvailable(username)) {
                clientFrame.getSignUpLogIn().showError("This username is not available.");
                clientFrame.getSignUpLogIn().getUsername();
                return false;
            }
        }
        else {
            if (username.equals("")) {
                clientFrame.getSignUpLogIn().showError("Please enter your username.");
                return false;
            } else if (isNotAvailable(username)) {
                clientFrame.getSignUpLogIn().showError("This username does not exist.");
                clientFrame.getSignUpLogIn().getUsername();
                return false;
            }
        }
        return true;
    }

    public ClientListener getListener() {
        return listener;
    }
    public String getMessageToShow() {
        return messageToShow;
    }


    public void setClientFrame(ClientFrame clientFrame) {
        this.clientFrame = clientFrame;
    }
    private boolean isNotAvailable(String string) {
        return (string.equals("11") || string.equals("12") || string.equals("21") || string.equals("22")
        || string.equals("New username") || string.equals("Username") || string.equals("New password")
        || string.equals("Password") || string.equals("Offline") || string.equals("Upload")
        || string.equals("Files") || string.equals("Download") || message.equals("Closed")
        || message.equals("Accepted requests") || message.equals("Request") || message.equals("Response to Request")
        || message.equals("Requests"));
    }
    public void signUpLogIn(String action) {
        listener.sendMessage(action);
    }
    public void sendName(String username, String action) {
        if (action.equals("1")) {
            listener.sendMessage("New username");
        }
        else {
            listener.sendMessage("Username");
        }
        listener.sendMessage(username);
        this.action = action;
    }
    private void checkNameMessage() {
        if (message.equals("This username is not available.") || message.equals("This username doesn't exist.")) {
            clientFrame.getSignUpLogIn().showError(message);
            clientFrame.getSignUpLogIn().getUsername();
        }
        else if (message.equals("Name accepted")) {
            listener.sendMessage(action+"2");
            clientFrame.getSignUpLogIn().getPassword();
        }
    }
    public void sendPass(String password, String action) {
        if (action.equals("1")) {
            listener.sendMessage("New password");
        }
        else {
            listener.sendMessage("Password");
        }
        listener.sendMessage(password);

    }
    private void checkPassMessage() {
        if (message.equals("Wrong password!")) {
            clientFrame.getSignUpLogIn().showError(message);
            clientFrame.getSignUpLogIn().getPassword();
        }
    }
    public void setOffline() {
        listener.sendMessage("Offline");
    }
    public void close() {
        setOffline();
        listener.sendMessage("Closed");
    }
    public void download(String name, String path, String username) {
        listener.sendMessage("Download");
        Sender sender = udpClient.getNewSender();
        sender.sendString(username);
        sender.sendString(name);
        sender.sendString(udpClient.getPort()+"");
        Receiver receiver = udpClient.getNewReceiver();
        long size = Long.parseLong(receiver.getString());
        new ReceiveProgress(size, path, name, receiver).run();
    }
    public void sendRequest(String name, String username) {
        listener.sendMessage("Request");
        Sender sender = udpClient.getNewSender();
        sender.sendString(name);
        sender.sendString(username);
        sender.sendString(udpClient.getPort()+"");
        String message = udpClient.getNewReceiver().getString();
        ClientFrame.showError(message);
    }
    public void responseToRequest(String name, String username, String response) {
        listener.sendMessage("Response to request");
        Sender sender = udpClient.getNewSender();
        sender.sendString(name);
        sender.sendString(username);
        sender.sendString(response);
    }


}
