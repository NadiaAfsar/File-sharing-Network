package UDP.Progress;

import UDP.FileHandler;
import UDP.Progress.Progress;
import UDP.Sender;
import model.Client;
import view.ProgressFrame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SendProgress extends Progress {
    private File file;
    private Sender sender;
    private Object lock;
    public SendProgress(Sender sender, File file) {
        lock = new Object();
        this.sender = sender;
        this.file = file;
        long size = 0;
        try {
            size = Files.size(Paths.get(file.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        totalProgress = FileHandler.getPacketsNumber(size);
        progressFrame = new ProgressFrame("Uploading "+file.getName(), this);
    }
    @Override
    public void run() {
        sender.setFile(file);
        sender.start();
    }

    @Override
    public int getPercentage() {
        synchronized (lock) {
            return (int) (100.0 * sender.getProgress() / totalProgress);
        }
    }
}
