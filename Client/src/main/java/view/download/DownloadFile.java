package view.download;

import TCP.TCPClient;
import UDP.Receiver;
import UDP.UDPClient;
import view.ClientFrame;
import view.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DownloadFile {
    private ClientFrame clientFrame;
    private JPanel panel;
    private TCPClient tcpClient;
    private UDPClient udpClient;
    private JButton myFiles;
    private JButton allFiles;
    private JButton acceptedRequest;
    private JButton newRequest;
    private JScrollPane scroller;
    private JPanel scrollerPanel;
    private JButton back;
    private ActionListener actionListener1;
    private ActionListener actionListener2;
    private ActionListener actionListener3;
    private ActionListener actionListener4;
    private JLabel file;
    private JTextField fileField;
    private JLabel username;
    private JTextField userField;
    private JButton send;
    public DownloadFile(ClientFrame clientFrame, TCPClient tcpClient, UDPClient udpClient) {
        this.clientFrame = clientFrame;
        panel = clientFrame.getPanel();
        this.tcpClient = tcpClient;
        this.udpClient = udpClient;
        addMyFiles();
        addAllFiles();
        setAction1();
        setAction2();
        setAction3();
        setAction4();
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
        addData();
    }
    private void addData() {
        tcpClient.getListener().sendMessage("Accepted requests");
        udpClient.getNewSender().sendString(udpClient.getPort()+"");
        Receiver receiver = udpClient.getNewReceiver();
        int requests = Integer.parseInt(receiver.getString());
        for (int i = 0; i < requests; i++) {
            String name = receiver.getString();
            String username = receiver.getString();
            addRequestPanel(name, username);
        }
    }
    private void addRequestPanel(String name, String username) {
        JPanel requestPanel = new JPanel();
        requestPanel.setName(name);
        JLabel label = new JLabel(name+" from "+username);
        label.setFont(new Font("Serif", Font.PLAIN,25));
        label.setForeground(Color.BLACK);
        requestPanel.add(label);
        requestPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JPanel p = (JPanel) e.getSource();
                panel.remove(scroller);
                panel.remove(back);
                new Download(clientFrame, tcpClient, udpClient, name, username);
            }
        });
        scrollerPanel.add(requestPanel);
    }
    private void addButton(JButton button, int x, int y, int width, int height) {
        button.setFont(new Font("Serif", Font.PLAIN,25));
        button.setForeground(Color.BLACK);
        button.setBackground(Color.white);
        button.setBounds(x, y, width,height);
        panel.add(button);
    }
    private void addMyFiles() {
        myFiles = new JButton("My Files");
        addButton(myFiles, 100, 100, 300, 50);
        myFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyPanel();
                new DownloadMyFile(clientFrame, tcpClient, udpClient);
            }
        });
    }
    private void setAction1() {
        actionListener1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyPanel();
                panel.remove(back);
                new MainMenu(clientFrame, tcpClient, udpClient);
            }
        };
    }
    private void addAllFiles() {
        allFiles = new JButton("All Files");
        addButton(allFiles, 100, 200, 300, 50);
        allFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyPanel();
                addAccepted();
                addNewRequest();
                back.removeActionListener(actionListener1);
                back.addActionListener(actionListener2);
                clientFrame.resetBackGround();
            }
        });
    }
    private void setAction2() {
        actionListener2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyPanel();
                panel.add(myFiles);
                panel.add(allFiles);
                back.removeActionListener(actionListener2);
                back.addActionListener(actionListener1);
                clientFrame.resetBackGround();
            }
        };
    }
    private void addAccepted() {
        acceptedRequest = new JButton("Accepted Requests");
        addButton(acceptedRequest, 100, 100, 300, 50);
        acceptedRequest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyPanel();
                addScroller();
                back.removeActionListener(actionListener2);
                back.addActionListener(actionListener3);
                clientFrame.resetBackGround();
            }
        });
    }
    private void setAction3() {
        actionListener3 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.remove(scroller);
                panel.add(acceptedRequest);
                panel.add(newRequest);
                back.removeActionListener(actionListener3);
                back.addActionListener(actionListener2);
                clientFrame.resetBackGround();
            }
        };
    }
    private void addNewRequest() {
        newRequest = new JButton("New Request");
        addButton(newRequest, 100, 200, 300, 50);
        newRequest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyPanel();
                newRequest();
                addSend();
                back.removeActionListener(actionListener2);
                back.addActionListener(actionListener4);
                clientFrame.resetBackGround();
            }
        });
    }
    private void setAction4() {
        actionListener4 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.remove(file);
                panel.remove(fileField);
                panel.remove(username);
                panel.remove(userField);
                panel.remove(send);
                panel.add(acceptedRequest);
                panel.add(newRequest);
                back.removeActionListener(actionListener4);
                back.addActionListener(actionListener2);
                clientFrame.resetBackGround();
            }
        };
    }
    private void addSend() {
        send = new JButton("Send");
        addButton(send, 50, 350, 100, 50);
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String file = fileField.getText();
                String username = userField.getText();
                if (file != null && username != null) {
                    tcpClient.sendRequest(file, username);
                }
            }
        });
    }
    private void newRequest() {
        file = new JLabel("File Name:");
        file.setFont(new Font("Serif", Font.PLAIN,25));
        file.setForeground(Color.BLACK);
        file.setBounds(50, 50, 200, 50);
        fileField = new JTextField();
        fileField.setFont(new Font("Serif", Font.PLAIN,25));
        fileField.setForeground(Color.BLACK);
        fileField.setBounds(50, 100, 300, 50);
        username = new JLabel("Request to:");
        username.setFont(new Font("Serif", Font.PLAIN,25));
        username.setForeground(Color.BLACK);
        username.setBounds(50, 160, 200, 50);
        userField = new JTextField();
        userField.setFont(new Font("Serif", Font.PLAIN,25));
        userField.setForeground(Color.BLACK);
        userField.setBounds(50, 260, 300, 50);
        panel.add(file);
        panel.add(fileField);
        panel.add(username);
        panel.add(userField);
    }

    private void addBack() {
        back = new JButton("Back");
        addButton(back, 300, 350, 100, 50);
        back.addActionListener(actionListener1);
    }
    private void emptyPanel() {
        panel.remove(myFiles);
        panel.remove(allFiles);
        if (acceptedRequest != null) {
            panel.remove(acceptedRequest);
        }
        if (newRequest != null) {
            panel.remove(newRequest);
        }
        if (scroller != null) {
            panel.remove(scroller);
        }
    }
}
