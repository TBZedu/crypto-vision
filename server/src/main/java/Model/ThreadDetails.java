package Model;

public class ThreadDetails {
    private Thread thread;
    private StopMode stopMode;

    public ThreadDetails(Thread thread){
        this.thread = thread;
        this.stopMode = StopMode.running;
    }

    public ThreadDetails(Thread thread, StopMode stopMode){
        this.thread = thread;
        this.stopMode = stopMode;
    }

    public Thread getThread() {
        return thread;
    }

    public StopMode getStopMode() {
        return stopMode;
    }

    public void setStopMode(StopMode stopMode) {
        this.stopMode = stopMode;
    }
}
