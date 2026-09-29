package view;

import TCP.TCPClient;
import UDP.UDPClient;
import model.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseAction {
    private JPanel panel;
    private JButton signUp;
    private JButton logIn;
    private TCPClient tcpClient;
    private ClientFrame clientFrame;
    private UDPClient udpClient;
    public ChooseAction(ClientFrame clientFrame, TCPClient tcpClient, UDPClient udpClient) {
        this.clientFrame = clientFrame;
        this.tcpClient = tcpClient;
        this.panel = clientFrame.getPanel();
        addSignUp();
        addLogIn();
        clientFrame.resetBackGround();
    }
    private void addSignUp() {
        signUp = new JButton("Sign Up");
        signUp.setFont(new Font("Serif", Font.PLAIN, 30));
        signUp.setBackground(Color.white);
        signUp.setForeground(Color.BLUE);
        signUp.setBounds(150, 100, 200, 100);
        signUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tcpClient.signUpLogIn("11");
                emptyPanel();
                clientFrame.signUpLogIn("1");
            }
        });
        panel.add(signUp);
    }
    private void addLogIn() {
        logIn = new JButton("Log In");
        logIn.setFont(new Font("Serif", Font.PLAIN, 30));
        logIn.setBackground(Color.white);
        logIn.setForeground(Color.BLUE);
        logIn.setBounds(150, 300, 200, 100);
        logIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tcpClient.getListener().sendMessage("21");
                emptyPanel();
                clientFrame.signUpLogIn("2");
            }
        });
        panel.add(logIn);
    }
    private void emptyPanel() {
        panel.remove(signUp);
        panel.remove(logIn);
    }
}
