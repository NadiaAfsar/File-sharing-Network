package view;

import TCP.TCPClient;
import UDP.FileHandler;
import UDP.Progress.SendProgress;
import UDP.UDPClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class UploadFile {
    private JButton select;
    private JButton upload;
    private JLabel label;
    private JButton back;
    private File selectedFile;
    private ClientFrame clientFrame;
    private TCPClient tcpClient;
    private UDPClient udpClient;
    private JPanel panel;
    private JButton cancel;
    public UploadFile(ClientFrame clientFrame, TCPClient tcpClient, UDPClient udpClient) {
        this.clientFrame = clientFrame;
        this.tcpClient = tcpClient;
        this.udpClient = udpClient;
        panel = clientFrame.getPanel();
        addSelect();
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
    private void addSelect() {
        select = new JButton("Select File");
        addButton(select, 100, 150, 300, 50);
        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getFile();
            }
        });
    }
    private void addBack() {
        back = new JButton("Back");
        addButton(back, 300, 350, 100, 50);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyPanel();
                new MainMenu(clientFrame, tcpClient, udpClient);
            }
        });
    }
    private void getFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a file to send");
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            prepareUpload();
        }
    }
    private void addLabel() {
        label = new JLabel("Selected file: "+ selectedFile.getName());
        label.setFont(new Font("Serif", Font.PLAIN,25));
        label.setForeground(Color.BLACK);
        label.setBounds(50, 80, 400, 100);
        panel.add(label);
    }
    private void addUpload() {
        upload = new JButton("Upload");
        addButton(upload, 250, 200, 150, 50 );
        upload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFile != null) {
                    tcpClient.getListener().sendMessage("Upload");
                    tcpClient.getListener().sendMessage(FileHandler.getSize(selectedFile)+"");
                    new SendProgress(udpClient.getNewSender(), selectedFile).start();
                    initialPanel();
                }
            }
        });
    }
    private void addCancel() {
        cancel = new JButton("Cancel");
        addButton(cancel, 50, 200, 150, 50);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initialPanel();
            }
        });
    }
    private void initialPanel() {
        panel.remove(label);
        panel.remove(cancel);
        panel.remove(upload);
        panel.add(select);
        selectedFile = null;
        clientFrame.resetBackGround();
    }
    private void prepareUpload() {
        panel.remove(select);
        addLabel();
        addUpload();
        addCancel();
        clientFrame.resetBackGround();
    }
    private void emptyPanel() {
        if (label != null) {
            panel.remove(label);
            panel.remove(upload);
            panel.remove(cancel);
        }
        panel.remove(select);
        panel.remove(back);
    }
}
