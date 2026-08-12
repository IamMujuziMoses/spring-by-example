package com.springbyexample.smartlifecycle;

import org.springframework.context.SmartLifecycle;

/**
 * @author Mujuzi Moses
 */
public class ReportSchedulerLifecycle implements SmartLifecycle {

    private final ReportScheduler scheduler;

    public ReportSchedulerLifecycle(ReportScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void start() {
        scheduler.start();
    }

    @Override
    public void stop() {
        scheduler.stop();
    }

    @Override
    public boolean isRunning() {
        return scheduler.isRunning();
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public int getPhase() {
        return 0;
    }
}
