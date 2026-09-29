package view;

import TCP.TCPClient;
import UDP.UDPClient;
import view.download.DownloadFile;
import view.download.DownloadMyFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {
    private JButton upload;
    private JButton download;
    private JButton requests;
    private JButton back;
    private ClientFrame clientFrame;
    private JPanel panel;
    private UDPClient udpClient;
    private TCPClient tcpClient;
    public MainMenu(ClientFrame clientFrame, TCPClient tcpClient, UDPClient udpClient) {
        this.clientFrame = clientFrame;
        this.tcpClient = tcpClient;
        this.udpClient = udpClient;
        panel = clientFrame.getPanel();
        addUpload();
        addDownload();
        addRequests();
        addBack();
        clientFrame.resetBackGround();
    }
    private void addButton(JButton button, int x, int y, int width, int height) {
        button.setFont(new Font("Serif", Font.PLAIN,25));
        button.setForeground(Color.BLACK);
        button.setBackground(Color.white);
        button.setBounds(x, y, width,height);
        panel.add(button);
    }
    private void addUpload() {
        upload = new JButton("Upload New File");
        addButton(upload, 100, 50, 300, 50);
        upload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyPanel();
                new UploadFile(clientFrame, tcpClient, udpClient);
            }
        });
    }
    private void addDownload() {
        download = new JButton("Download Existing File");
        addButton(download, 100, 150, 300, 50);
        download.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyPanel();
                new DownloadFile(clientFrame, tcpClient, udpClient);
            }
        });
    }
    private void addBack() {
        back = new JButton("Back");
        addButton(back, 270, 350, 100, 50);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyPanel();
                tcpClient.setOffline();
                new ChooseAction(clientFrame, tcpClient, udpClient);
            }
        });
    }
    private void addRequests() {
        requests = new JButton("Requests");
        addButton(requests, 100, 250, 300, 50);
        requests.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyPanel();
                new Requests(clientFrame, tcpClient, udpClient);
            }
        });
    }
    private void emptyPanel() {
        panel.remove(upload);
        panel.remove(download);
        panel.remove(requests);
        panel.remove(back);
    }

}
