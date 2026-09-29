package UDP.Progress;

import UDP.FileHandler;
import UDP.Receiver;
import model.Client;
import view.ProgressFrame;

public class ReceiveProgress extends Progress{
    private long size;
    private String path;
    private String name;
    private Receiver receiver;
    private Object lock;
    public ReceiveProgress(long size, String path, String name, Receiver receiver) {
        this.size = size;
        this.path = path;
        this.name = name;
        this.receiver = receiver;
        lock = new Object();
        totalProgress = FileHandler.getPacketsNumber(size);
        progressFrame = new ProgressFrame("Downloading "+name, this);
    }
    @Override
    public void run() {
        receiver.setPath(path);
        receiver.setPackets(totalProgress);
        receiver.setFileName(name);
        receiver.start();
    }

    @Override
    public int getPercentage() {
        synchronized (lock) {
            return (int) (100.0 * receiver.getProgress() / totalProgress);
        }
    }
}
