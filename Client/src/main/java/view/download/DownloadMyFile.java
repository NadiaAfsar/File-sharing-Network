package view.download;

import TCP.TCPClient;
import UDP.Receiver;
import UDP.UDPClient;
import view.ClientFrame;
import view.MainMenu;
import view.download.Download;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DownloadMyFile {
    private JScrollPane scroller;
    private JPanel scrollerPanel;
    private JButton back1;
    private JPanel panel;
    private String selectedFile;
    private ClientFrame clientFrame;
    private TCPClient tcpClient;
    private UDPClient udpClient;
    public DownloadMyFile(ClientFrame clientFrame, TCPClient tcpClient, UDPClient udpClient) {
        this.clientFrame = clientFrame;
        panel = clientFrame.getPanel();
        this.tcpClient = tcpClient;
        this.udpClient = udpClient;
        addScroller();
        addBack1();
        clientFrame.resetBackGround();
    }
    private void addScroller() {
        scrollerPanel = new JPanel();
        scrollerPanel.setLayout(new BoxLayout(scrollerPanel, BoxLayout.Y_AXIS));
        scroller = new JScrollPane(scrollerPanel);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setBounds(50,70,400,200);
        panel.add(scroller);
        addFiles();
    }
    private void addButton(JButton button, int x, int y, int width, int height) {
        button.setFont(new Font("Serif", Font.PLAIN,25));
        button.setForeground(Color.BLACK);
        button.setBackground(Color.white);
        button.setBounds(x, y, width,height);
        panel.add(button);
    }
    private void addFiles() {
        tcpClient.getListener().sendMessage("Files");
        udpClient.getNewSender().sendString(udpClient.getPort()+"");
        Receiver receiver = udpClient.getNewReceiver();
        String filesString = receiver.getString();
        int files = Integer.parseInt(filesString);
        for (int i = 0; i < files; i++) {
            String name = receiver.getString();
            addFile(name);
        }
    }
    private void addFile(String name) {
        JPanel filePanel = new JPanel();
        filePanel.setName(name);
        JLabel fileName = new JLabel(name);
        fileName.setFont(new Font("Serif", Font.PLAIN,25));
        fileName.setForeground(Color.BLACK);
        filePanel.add(fileName);
        filePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JPanel p = (JPanel) e.getSource();
                selectedFile = p.getName();
                panel.remove(scroller);
                panel.remove(back1);
                new Download(clientFrame, tcpClient, udpClient, selectedFile, "client");
            }
        });
        scrollerPanel.add(filePanel);
    }
    private void addBack1() {
        back1 = new JButton("Back");
        addButton(back1, 300, 350, 100, 50);
        back1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.remove(scroller);
                panel.remove(back1);
                new MainMenu(clientFrame, tcpClient, udpClient);
            }
        });
    }
}
