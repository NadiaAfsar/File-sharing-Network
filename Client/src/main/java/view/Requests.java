package view;

import TCP.TCPClient;
import UDP.Receiver;
import UDP.UDPClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Requests {
    private ClientFrame clientFrame;
    private JPanel panel;
    private UDPClient udpClient;
    private TCPClient tcpClient;
    private JScrollPane scroller;
    private JPanel scrollerPanel;
    private JButton back;
    public Requests(ClientFrame clientFrame, TCPClient tcpClient, UDPClient udpClient) {
        this.clientFrame = clientFrame;
        this.tcpClient = tcpClient;
        this.udpClient = udpClient;
        panel = clientFrame.getPanel();
        addScroller();
        addBack();
        clientFrame.resetBackGround();
    }
    private void addScroller() {
        scrollerPanel = new JPanel();
        scrollerPanel.setLayout(new BoxLayout(scrollerPanel, BoxLayout.Y_AXIS));
        scroller = new JScrollPane(scrollerPanel);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setBounds(50,70,400,200);
        panel.add(scroller);
        addRequests();
    }
    private void addRequests() {
        tcpClient.getListener().sendMessage("Requests");
        udpClient.getNewSender().sendString(udpClient.getPort()+"");
        Receiver receiver = udpClient.getNewReceiver();
        int requests = Integer.parseInt(receiver.getString());
        for (int i = 0; i < requests; i++) {
            String name = receiver.getString();
            String username = receiver.getString();
            addRequest(name, username);
        }
    }
    private void addRequest(String name, String username) {
        JPanel requestPanel = new JPanel();
        requestPanel.setName(name);
        JLabel r = new JLabel("Access to "+name+" from "+username);
        r.setFont(new Font("Serif", Font.PLAIN,25));
        r.setForeground(Color.BLACK);
        requestPanel.add(r);
        requestPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JPanel p = (JPanel) e.getSource();
                tcpClient.responseToRequest(p.getName(), username, getResponse());
                scrollerPanel.remove(requestPanel);
            }
        });
        scrollerPanel.add(requestPanel);
    }
    private String getResponse() {
        String[] options = new String[]{"Accept","Decline"};
        int response = JOptionPane.showOptionDialog(null, "Do you accept this request?", null, JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        return options[response];
    }
    private void addBack() {
        back = new JButton("Back");
        back.setFont(new Font("Serif", Font.PLAIN,25));
        back.setForeground(Color.BLACK);
        back.setBackground(Color.white);
        back.setBounds(300, 350, 100, 50);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.remove(scroller);
                panel.remove(back);
                new MainMenu(clientFrame, tcpClient, udpClient);
            }
        });
    }
}
