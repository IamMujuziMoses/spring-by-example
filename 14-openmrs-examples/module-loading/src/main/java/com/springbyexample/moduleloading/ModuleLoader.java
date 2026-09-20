package com.springbyexample.moduleloading;

/**
 * @author Mujuzi Moses
 */
public class ModuleLoader {

    private final ExampleModuleActivator activator;

    public ModuleLoader(ExampleModuleActivator activator) {
        this.activator = activator;
    }

    public void load(ExampleModule module) {
        if (module.isLoaded()) {
            return;
        }

        module.markLoaded();
    }

    public void start(ExampleModule module) {
        if (!module.isLoaded()) {
            throw new IllegalStateException("Module must be loaded before it can be started");
        }

        if (module.isStarted()) {
            return;
        }

        activator.willRefreshContext();

        refreshSpringContext();

        activator.contextRefreshed();
        activator.started();

        module.markStarted();
    }

    private void refreshSpringContext() {
        System.out.println("Refreshing Spring context");
    }
}
