package Model;

import Configuration.TradingConfiguration;

public class ThreadDetails {
    private Thread thread;
    private TradingConfiguration configuration;
    private StopMode stopMode;

    public ThreadDetails(Thread thread, TradingConfiguration configuration){
        this.thread = thread;
        this.configuration = configuration;
        this.stopMode = StopMode.running;
    }

    public ThreadDetails(Thread thread, TradingConfiguration configuration, StopMode stopMode){
        this.thread = thread;
        this.configuration = configuration;
        this.stopMode = stopMode;
    }

    public Thread getThread() {
        return thread;
    }

    public TradingConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(TradingConfiguration configuration) {
        this.configuration = configuration;
    }

    public StopMode getStopMode() {
        return stopMode;
    }

    public void setStopMode(StopMode stopMode) {
        this.stopMode = stopMode;
    }
}
