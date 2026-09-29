package view;

import Controller.Update;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ServerFrame extends JFrame {
    private JPanel panel;
    private JScrollPane scroller;
    private JLabel clients;
    private JPanel scrollerPanel;
    private ArrayList<UserPanel> users;
    public ServerFrame() {
        setFrame();
        setPanel();
        addScroller();
        addUsers();
        addBackground();
        users = new ArrayList<>();
        new Update().start();
    }
    private void setFrame() {
        setTitle("Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
        setFocusable(true);
        setLayout(null);
    }
    private void setPanel() {
        panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(500,500));
        panel.setFocusable(true);
        setContentPane(panel);
    }
    private void addScroller() {
        scrollerPanel = new JPanel();
        scrollerPanel.setLayout(new BoxLayout(scrollerPanel, BoxLayout.Y_AXIS));
        scroller = new JScrollPane(scrollerPanel);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setBounds(50,100,400,250);
        panel.add(scroller);
    }
    private void addUsers() {
        clients = new JLabel("Clients");
        clients.setFont(new Font("Serif",Font.PLAIN, 40));
        clients.setForeground(Color.BLACK);
        clients.setBounds(200,0,200,100);
        panel.add(clients);
    }
    public void update() {
        revalidate();
        repaint();
    }
    private void addBackground() {
        Background background = new Background("src/main/java/pics/background.jpg");
        background.setLayout(null);
        background.setBounds(0,0,500,500);
        panel.add(background);
    }
    public void showConnection(String string) {
        String[] options = new String[]{"OK"};
        JOptionPane.showOptionDialog(null, string, "New Connection", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
    }
    public void addUser(String username) {
        UserPanel user = new UserPanel(username);
        scrollerPanel.add(user);
        users.add(user);
    }

    public ArrayList<UserPanel> getUsers() {
        return users;
    }
}
