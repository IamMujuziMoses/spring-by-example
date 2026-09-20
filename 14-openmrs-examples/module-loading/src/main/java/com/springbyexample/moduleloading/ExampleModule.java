package com.springbyexample.moduleloading;

/**
 * @author Mujuzi Moses
 */
public class ExampleModule {

    private final String moduleId;

    private boolean loaded;

    private boolean started;

    public ExampleModule(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public boolean isStarted() {
        return started;
    }

    void markLoaded() {
        loaded = true;
    }

    void markStarted() {
        started = true;
    }
}