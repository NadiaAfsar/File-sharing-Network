package view;

import Controller.Update;
import TCP.TCPClient;
import UDP.UDPClient;
import model.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientFrame extends MyFrame {
    private JPanel panel;
    private Background background;
    private SignUpLogIn signUpLogIn;
    private TCPClient tcpClient;
    private UDPClient udpClient;
    public ClientFrame(TCPClient tcpClient, UDPClient udpClient) {
        this.tcpClient = tcpClient;
        this.udpClient = udpClient;
        setFrame();
        setPanel();
        this.tcpClient.setClientFrame(this);
        new ChooseAction(this, tcpClient, udpClient);
        new Update(this).start();
    }
    private void setFrame() {
        setTitle("Client");
        setSize(500,500);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
        setFocusable(true);
        setLayout(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                tcpClient.close();
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
    private void setPanel() {
        panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(500,500));
        panel.setFocusable(true);
        setContentPane(panel);
    }
    @Override
    public void update() {
        if (signUpLogIn != null) {
            signUpLogIn.update();
        }
        revalidate();
        repaint();
    }
    public void signUpLogIn(String action) {
        signUpLogIn = new SignUpLogIn(action, this, tcpClient, udpClient);
        resetBackGround();
        update();
    }
    private void addBackground() {
        background = new Background("src/main/java/pics/background.jpg");
        background.setLayout(null);
        background.setBounds(0,0,500,500);
        panel.add(background);
    }


    public SignUpLogIn getSignUpLogIn() {
        return signUpLogIn;
    }
    public void resetBackGround() {
        if (background != null) {
            panel.remove(background);
            panel.add(background);
        }
        else {
            addBackground();
        }
    }
    public static void showError(String message) {
        String[] options = new String[]{"OK"};
        JOptionPane.showOptionDialog(null, message, null, JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
    }

    public JPanel getPanel() {
        return panel;
    }

    public UDPClient getUdpClient() {
        return udpClient;
    }
}
