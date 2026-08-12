package com.springbyexample.smartlifecycle;

/**
 * @author Mujuzi Moses
 */
public class ReportScheduler {

    private boolean running;

    public void start() {
        running = true;
        System.out.println("Report scheduler started");
    }

    public void stop() {
        running = false;
        System.out.println("Report scheduler stopped");
    }

    public boolean isRunning() {
        return running;
    }
}
