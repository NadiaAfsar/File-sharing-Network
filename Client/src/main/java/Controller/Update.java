package Controller;

import view.MyFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Update extends Thread{
    private MyFrame frame;
    private Timer timer;
    private Thread thread;
    public Update(MyFrame frame) {
        this.frame = frame;
    }
    public void run() {
        while (true) {
            frame.update();
            try {
                sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
