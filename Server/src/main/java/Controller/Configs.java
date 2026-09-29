package Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Server;
import model.Status;
import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Configs {
    private Gson gson;
    private static Configs instance;
    private Configs() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
    }
    private <T> T loadConfigs(Class<T> tClass, String address) {
        File file = new File(address);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        InputStreamReader iSReader = new InputStreamReader(fileInputStream);
        JsonReader reader = new JsonReader(iSReader);

        return gson.fromJson(reader,tClass);
    }
    private <T> void saveConfigs(String address, T configs) {
        File file = new File(address);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            gson.toJson(configs, configs.getClass(), bufferedWriter);
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void saveData(Server server) {
        saveConfigs("src/main/resources/ServerConfigs.json", saveUsers(server.getUsers()));
    }
    private ServerConfigs saveUsers(ArrayList<User> users) {
        String[] usersArray = new String[users.size()];
        for (int i = 0; i < usersArray.length; i++) {
            usersArray[i] = users.get(i).getUsername();
            saveConfigs("src/main/resources/"+users.get(i).getUsername()+".json", saveUser(users.get(i)));
        }
        ServerConfigs serverConfigs = new ServerConfigs();
        serverConfigs.setUsers(usersArray);
        return serverConfigs;
    }
    private UserConfigs saveUser(User user) {
        UserConfigs userConfigs = new UserConfigs();
        userConfigs.setUsername(user.getUsername());
        userConfigs.setPassword(user.getPassword());
        userConfigs.setRequests(setRequests(user.getRequests()));
        userConfigs.setAcceptedRequests(setRequests(user.getAcceptedRequests()));
        String[] files = new String[user.getFiles().size()];
        for (int i = 0; i < files.length; i++) {
            files[i] = user.getFiles().get(i);
        }
        userConfigs.setFiles(files);
        return userConfigs;
    }
    public void loadData(Server server) {
        ArrayList<User> users = new ArrayList<>();
        Map<String, User> usersMap = new HashMap<>();
        String[] usersName = getUsers();
        for (int i = 0; i < usersName.length; i++) {
            User user = loadUser(usersName[i]);
            users.add(user);
            usersMap.put(user.getUsername(), user);
            server.getServerFrame().addUser(user.getUsername());
        }
        server.setUsers(users);
        server.setUsersMap(usersMap);
    }
    private String[] getUsers() {
        ServerConfigs serverConfigs = loadConfigs(ServerConfigs.class, "src/main/resources/ServerConfigs.json");
        return serverConfigs.getUsers();
    }
    private User loadUser(String name) {
        UserConfigs userConfigs = loadConfigs(UserConfigs.class, "src/main/resources/"+name+".json");
        User user = new User(userConfigs.getUsername(), userConfigs.getPassword());
        user.setFiles(getFiles(userConfigs.getFiles()));
        user.setRequests(getRequests(userConfigs.getRequests()));
        user.setAcceptedRequests(getRequests(userConfigs.getAcceptedRequests()));
        user.setStatus(Status.OFFLINE);
        return user;
    }
    private ArrayList<String> getFiles(String[] filesArray) {
        ArrayList<String> files = new ArrayList<>();
        for (int i = 0; i < filesArray.length; i++) {
            files.add(filesArray[i]);
        }
        return files;
    }
    private ArrayList<String[]> getRequests(String[][] requests) {
        ArrayList<String[]> requestsList = new ArrayList<>();
        for (int i = 0; i < requests.length; i++) {
            String[] request = new String[2];
            request[0] = requests[i][0];
            request[1] = requests[i][1];
            requestsList.add(request);
        }
        return requestsList;
    }
    private String[][] setRequests(ArrayList<String[]> requests) {
        String[][] requestsArray = new String[requests.size()][2];
        for (int i = 0; i < requests.size(); i++) {
            requestsArray[i][0] = requests.get(i)[0];
            requestsArray[i][1] = requests.get(i)[1];
        }
        return requestsArray;
    }

    public static Configs getInstance() {
        if (instance == null) {
            instance = new Configs();
        }
        return instance;
    }
}
