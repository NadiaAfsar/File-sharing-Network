package UDP.Progress;

import view.ProgressFrame;

public abstract class Progress extends Thread{
    protected int totalProgress;
    protected ProgressFrame progressFrame;

    public abstract int getPercentage();
}
