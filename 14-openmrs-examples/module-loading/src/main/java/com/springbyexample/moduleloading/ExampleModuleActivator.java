package com.springbyexample.moduleloading;

/**
 * @author Mujuzi Moses
 */
public class ExampleModuleActivator {

    private boolean willRefreshContextCalled;

    private boolean contextRefreshedCalled;

    private boolean startedCalled;

    public void willRefreshContext() {
        willRefreshContextCalled = true;
        System.out.println("Module will refresh Spring context");
    }

    public void contextRefreshed() {
        contextRefreshedCalled = true;
        System.out.println("Module Spring context refreshed");
    }

    public void started() {
        startedCalled = true;
        System.out.println("Module started");
    }

    public boolean isWillRefreshContextCalled() {
        return willRefreshContextCalled;
    }

    public boolean isContextRefreshedCalled() {
        return contextRefreshedCalled;
    }

    public boolean isStartedCalled() {
        return startedCalled;
    }
}
