package view;

import Controller.Update;
import UDP.Progress.Progress;

import javax.swing.*;
import java.awt.*;

public class ProgressFrame extends MyFrame{
    private JProgressBar progressBar;
    private JPanel panel;
    private Progress progress;
    private Object lock;
    public ProgressFrame(String name, Progress progress) {
        lock = new Object();
        this.progress = progress;
        setFrame(name);
        setPanel();
        addProgressBar();
        new Update(this).start();
    }
    private void setFrame(String name) {
        setTitle(name);
        setSize(300,200);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
        setFocusable(true);
        setLayout(null);
    }
    private void setPanel() {
        panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(300,200));
        panel.setFocusable(true);
        setContentPane(panel);
    }
    private void addProgressBar() {
        progressBar = new JProgressBar(0,100);
        progressBar.setBounds(50, 50, 200, 50);
        progressBar.setString("0%");
        progressBar.setValue(0);
        panel.add(progressBar);
    }
    @Override
    public void update() {
        synchronized (lock) {
            int percent = progress.getPercentage();
            if (percent >= 100) {
                dispose();
            }
            progressBar.setValue(percent);
            revalidate();
            repaint();
        }
    }
}
