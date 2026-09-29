package TCP;

import Controller.Hashing;
import UDP.FileHandler;
import UDP.Receiver;
import UDP.Sender;
import model.Server;
import model.Status;
import model.User;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler {
    private Map<String, User> users;
    private Map<String, Socket> onlineClients;
    private Map<String, User> creatingUsers;
    public RequestHandler() {
        users = Server.getInstance().getUsersMap();
        onlineClients = new HashMap<>();
        creatingUsers = new HashMap<>();
    }
    protected void getUsername(ServerListener listener) {
        String action = listener.getAction();
        if (action.equals("11")) {
            listener.sendMessage("Choose a username:");
        } else {
            listener.sendMessage("Enter your username:");
        }

    }
    protected void receiveNewUsername(ServerListener listener) {
        String username = listener.getMessage();
        if (users.get(username) != null || creatingUsers.get(username) != null) {
            listener.sendMessage("This username is not available.");
            getUsername(listener);
        } else {
            User user = new User(username);
            creatingUsers.put(username, user);
            listener.setUsername(username);
            listener.sendMessage("Name accepted");
        }
    }
    protected void receiveUsername(ServerListener listener) {
        String username = listener.getMessage();
        if (users.get(username) == null) {
            listener.sendMessage("This username doesn't exist.");
            getUsername(listener);
        } else {
            listener.setUsername(username);
            listener.sendMessage("Name accepted");
        }
    }
    protected void getPassword(ServerListener listener) {
        String action = listener.getAction();
        if (action.equals("12")) {
            listener.sendMessage("Choose a password:");
        } else {
            listener.sendMessage("Enter your password:");
        }
    }
    protected void receiveNewPassword(ServerListener listener) {
        String message = listener.getMessage();
        String password = Hashing.getMd5(message);
        listener.sendMessage("Password accepted");
        createNewUser(listener.getUsername(), password);
        setOnline(listener.getUsername(), listener.getSocket());
        listener.setUser(users.get(listener.getUsername()));
    }
    private void createNewUser(String username, String password) {
        User user = new User(username,password);
        users.put(username, user);
        creatingUsers.put(username, null);
        Server.getInstance().addUser(user);
        Server.getInstance().getServerFrame().addUser(username);
    }
    private void setOnline(String username, Socket socket) {
        onlineClients.put(username, socket);
    }
    public void setOffline(String username) {
        onlineClients.put(username, null);
        if (users.get(username) != null) {
            users.get(username).setStatus(Status.OFFLINE);
        }
    }
    protected void receivePassword(ServerListener listener) {
        String message = listener.getMessage();
        String password = Hashing.getMd5(message);
        if (!password.equals(users.get(listener.getUsername()).getPassword())) {
            listener.sendMessage("Wrong password!");
            getPassword(listener);
        }
        else {
            listener.sendMessage("Password accepted");
            users.get(listener.getUsername()).setStatus(Status.ONLINE);
            listener.setUser(users.get(listener.getUsername()));
        }
    }
    protected void checkClientMessage(ServerListener listener) {
        String message = listener.getReceivedMessage();
        if (message.equals("11") || message.equals("21")) {
            listener.setAction(message);
            getUsername(listener);

        }
        else if (message.equals("12") || message.equals("22")) {
            listener.setAction(message);
            getPassword(listener);

        }
    }
    protected void receiveFile(ServerListener listener) {
        Receiver receiver = Server.getInstance().getUdpServer().getNewReceiver();
        receiver.setUser(listener.getUser());
        receiver.start();
    }
    public void sendFilesName(ServerListener listener) {
        Integer port = Integer.valueOf(Server.getInstance().getUdpServer().getNewReceiver().getString());
        Sender sender = Server.getInstance().getUdpServer().getNewSender(new InetSocketAddress("localHost",port));
        int files = listener.getUser().getFiles().size();
        sender.sendString(files+"");
        for (int i = 0; i < files; i++) {
            sender.sendString(listener.getUser().getFiles().get(i));
        }
    }
    public void download(ServerListener serverListener) {
        Receiver receiver = Server.getInstance().getUdpServer().getNewReceiver();
        String username = receiver.getString();
        String name = receiver.getString();
        File file = null;
        if (username.equals("client")) {
            file = new File("src/main/java/SavedFiles/" + serverListener.getUsername() + "/" + name);
        }
        else {
            file = new File("src/main/java/SavedFiles/" + username + "/" + name);
        }
        Integer port = Integer.valueOf(receiver.getString());
        Sender sender = Server.getInstance().getUdpServer().getNewSender(new InetSocketAddress("localHost",port));
        sender.sendString(FileHandler.getSize(file)+"");
        sender.setFile(file);
        sender.start();
    }
    public void sendAcceptedRequests(ServerListener listener) {
            Receiver receiver = Server.getInstance().getUdpServer().getNewReceiver();
            Integer port = Integer.valueOf(receiver.getString());
            Sender sender = Server.getInstance().getUdpServer().getNewSender(new InetSocketAddress("localHost", port));
            ArrayList<String[]> acceptedRequests = users.get(listener.getUsername()).getAcceptedRequests();
            int requests = acceptedRequests.size();
            sender.sendString(requests+"");
            for (int i = 0; i < requests; i++) {
                sender.sendString(acceptedRequests.get(i)[0]);
                sender.sendString(acceptedRequests.get(i)[1]);
            }
    }
    public void sendRequests(ServerListener listener) {
        Receiver receiver = Server.getInstance().getUdpServer().getNewReceiver();
        Integer port = Integer.valueOf(receiver.getString());
        Sender sender = Server.getInstance().getUdpServer().getNewSender(new InetSocketAddress("localHost", port));
        ArrayList<String[]> requests = users.get(listener.getUsername()).getRequests();
        int r = requests.size();
        sender.sendString(r+"");
        for (int i = 0; i < r; i++) {
            sender.sendString(requests.get(i)[0]);
            sender.sendString(requests.get(i)[1]);
        }
    }
    public void receiveRequest(ServerListener listener) {
        Receiver receiver = Server.getInstance().getUdpServer().getNewReceiver();
        String name = receiver.getString();
        String username = receiver.getString();
        Integer port = Integer.valueOf(receiver.getString());
        Sender sender = Server.getInstance().getUdpServer().getNewSender(new InetSocketAddress("localHost",port));
        if (users.get(username) != null) {
            if (users.get(username).fileExists(name)) {
                users.get(username).getRequests().add(new String[]{name, listener.getUsername()});
                sender.sendString("Request sent.");
            }
            else {
                sender.sendString("File doesn't exist.");
            }
        }
        else {
            sender.sendString("File doesn't exist.");
        }
    }
    public void receiveResponse(ServerListener listener) {
        Receiver receiver = Server.getInstance().getUdpServer().getNewReceiver();
        String name = receiver.getString();
        String username = receiver.getString();
        String response = receiver.getString();
        users.get(listener.getUsername()).getRequests().remove(users.get(listener.getUsername()).getRequest(name, username));
        if (response.equals("Accept")) {
            users.get(username).getAcceptedRequests().add(new String[]{name, listener.getUsername()});
        }
    }
}
