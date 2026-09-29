package view;

import TCP.TCPClient;
import UDP.UDPClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpLogIn {
    private JPanel panel;
    private TCPClient tcpClient;
    private JTextField textField;
    private String message;
    private JLabel messageJLabel;
    private JButton next1;
    private JButton next2;
    private JButton back1;
    private JButton back2;
    private ClientFrame clientFrame;
    private UDPClient udpClient;
    private String action;
    public SignUpLogIn(String action, ClientFrame clientFrame, TCPClient tcpClient, UDPClient udpClient) {
        this.tcpClient = tcpClient;
        this.clientFrame = clientFrame;
        this.udpClient = udpClient;
        this.panel = clientFrame.getPanel();
        this.action = action;
        message = tcpClient.getMessageToShow();
        addMessageJLabel();
        getUsername();

    }
    private void setComponent(JComponent component, int x, int y, int width, int height) {
        component.setFont(new Font("Serif", Font.PLAIN,25));
        component.setForeground(Color.BLACK);
        component.setBounds(x, y, width,height);
    }
    private void addMessageJLabel() {
        messageJLabel = new JLabel(message);
        setComponent(messageJLabel, 50, 50, 400,100);
        panel.add(messageJLabel);
    }
    private void addTextField() {
        textField = new JTextField();
        setComponent(textField, 50, 150, 300,50);
        panel.add(textField);
    }
    private void addNext1() {
        next1 = new JButton("Next");
        setComponent(next1, 250, 230, 100,40);
        next1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tcpClient.sendUsername(textField.getText(), action)) {
                    tcpClient.sendName(textField.getText(), action);
                }
            }
        });
        panel.add(next1);
    }
    private void addNext2() {
        next2 = new JButton("Next");
        setComponent(next2, 250, 230, 100,40);
        next2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tcpClient.sendPassword(textField.getText(), action)) {
                    tcpClient.sendPass(textField.getText(), action);
                }
            }
        });
        panel.add(next2);
    }
    private void addBack1() {
        back1 = new JButton("Back");
        setComponent(back1, 50, 230, 100, 40);
        back1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.remove(messageJLabel);
                panel.remove(textField);
                panel.remove(next1);
                panel.remove(back1);
                new ChooseAction(clientFrame, tcpClient, udpClient);
            }
        });
        panel.add(back1);
    }
    private void addBack2() {
        back2 = new JButton("Back");
        setComponent(back2, 50, 230, 100, 40);
        back2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tcpClient.getListener().sendMessage(action+"1");
                getUsername();
            }
        });
        panel.add(back2);
    }

    public void update() {
        message = tcpClient.getMessageToShow();
        messageJLabel.setText(message);
    }
    public void getUsername() {
        removeButtons();
        if (textField != null) {
            textField.setText("");
        }
        else {
            addTextField();
        }
        addNext1();
        addBack1();
        clientFrame.resetBackGround();
    }
    public void getPassword() {
        removeButtons();
        textField.setText("");
        addNext2();
        addBack2();
        clientFrame.resetBackGround();
    }
    public void showError(String message) {
        String[] options = new String[]{"OK"};
        JOptionPane.showOptionDialog(null, message, null, JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
    }
    private void removeButtons() {
        if (next1 != null) {
            panel.remove(next1);
        }
        if (next2 != null) {
            panel.remove(next2);
        }
        if (back1 != null) {
            panel.remove(back1);
        }
        if (back2 != null) {
            panel.remove(back2);
        }
    }
    public void emptyPanel() {
        panel.remove(messageJLabel);
        panel.remove(textField);
        panel.remove(next2);
        panel.remove(back2);
    }

}
