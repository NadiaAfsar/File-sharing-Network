package Controller;

import model.Server;
import model.User;
import view.UserPanel;

import java.util.ArrayList;
import java.util.Map;

public class Update extends Thread{
    public Update() {

    }
    public void run() {
        while (true) {
            if (Server.isIsMade()) {
            Map<String, User> users = Server.getInstance().getUsersMap();
            ArrayList<UserPanel> userPanels = Server.getInstance().getServerFrame().getUsers();
            for (UserPanel userPanel : userPanels) {
                userPanel.update(users.get(userPanel.getUsername()).getStatus());
            }
                Server.getInstance().getServerFrame().update();
                Configs.getInstance().saveData(Server.getInstance());
            }
            try {
                sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
