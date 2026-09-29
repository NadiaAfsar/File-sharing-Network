package view;

import model.Status;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel {
    private JLabel usernameLabel;
    private JLabel status;
    private String username;
    public UserPanel(String username) {
        this.username = username;
        setUsernameLabel(username);
        setStatus();
    }

    public String getUsername() {
        return username;
    }

    private void setUsernameLabel(String username) {
        usernameLabel = new JLabel(username);
        usernameLabel.setFont(new Font("Serif", Font.PLAIN,25));
        usernameLabel.setForeground(Color.BLACK);
        add(usernameLabel);
    }
    private void setStatus() {
        status = new JLabel("online");
        status.setFont(new Font("Serif", Font.PLAIN,25));
        status.setForeground(Color.GREEN);
        add(status);
    }
    private void setOnline() {
        status.setText("online");
        status.setForeground(Color.GREEN);
    }
    private void setOffline() {
        status.setText("offline");
        status.setForeground(Color.RED);
    }
    public void update(Status status) {
        if (status.equals(Status.OFFLINE)) {
            setOffline();
        }
        else {
            setOnline();
        }
    }
}
