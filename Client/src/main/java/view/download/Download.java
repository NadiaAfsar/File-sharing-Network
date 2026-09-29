package view.download;

import TCP.TCPClient;
import UDP.UDPClient;
import view.ClientFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Download {
    private ClientFrame clientFrame;
    private TCPClient tcpClient;
    private UDPClient udpClient;
    private JButton selectLocation;
    private String path;
    private String selectedFile;
    private JPanel panel;
    private JButton back;
    private JButton cancel;
    private JButton download;
    private JLabel fileToDownload;
    private JLabel fileName;
    private String username;
    public Download(ClientFrame clientFrame, TCPClient tcpClient, UDPClient udpClient, String selectedFile, String username) {
        this.clientFrame = clientFrame;
        panel = clientFrame.getPanel();
        this.tcpClient = tcpClient;
        this.udpClient = udpClient;
        this.selectedFile = selectedFile;
        this.username = username;
        addLabels();
        addSelectLocation();
        addBack2();
        clientFrame.resetBackGround();
        clientFrame.resetBackGround();
    }
    private void addButton(JButton button, int x, int y, int width, int height) {
        button.setFont(new Font("Serif", Font.PLAIN,25));
        button.setForeground(Color.BLACK);
        button.setBackground(Color.white);
        button.setBounds(x, y, width,height);
        panel.add(button);
    }
    private void addSelectLocation() {
        selectLocation = new JButton("Select location");
        addButton(selectLocation, 100, 180, 300, 50);
        selectLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("Select a location to download the file");
                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jFileChooser.setAcceptAllFileFilterUsed(false);
                if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    path = jFileChooser.getSelectedFile().getAbsolutePath();
                    prepareDownload();
                }

            }
        });
    }
    private void addBack2() {
        back = new JButton("Back");
        addButton(back, 300, 350, 100, 50);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.remove(fileToDownload);
                panel.remove(fileName);
                panel.remove(selectLocation);
                panel.remove(back);
                if (cancel != null) {
                    panel.remove(cancel);
                    panel.remove(download);
                }
                new DownloadMyFile(clientFrame, tcpClient, udpClient);
            }
        });
    }
    private void addLabels() {
        fileToDownload = new JLabel("File to download:");
        fileToDownload.setFont(new Font("Serif", Font.PLAIN,25));
        fileToDownload.setForeground(Color.BLACK);
        fileToDownload.setBounds(100, 30, 300,50);
        fileName = new JLabel(selectedFile);
        fileName.setFont(new Font("Serif", Font.PLAIN,25));
        fileName.setForeground(Color.BLACK);
        fileName.setBounds(100, 100, 400,50);
        panel.add(fileName);
        panel.add(fileToDownload);

    }
    private void addCancel() {
        cancel = new JButton("Cancel");
        addButton(cancel, 50, 180, 150, 50);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.remove(cancel);
                panel.remove(download);
                panel.add(selectLocation);
                clientFrame.resetBackGround();
            }
        });
    }
    private void prepareDownload() {
        panel.remove(selectLocation);
        addCancel();
        addDownload();
        clientFrame.resetBackGround();
    }
    private void addDownload() {
        download = new JButton("Download");
        addButton(download, 250, 180, 150, 50);
        download.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tcpClient.download(selectedFile, path, username);
            }
        });
    }
}
