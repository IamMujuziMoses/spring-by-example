package com.springbyexample.moduleloading;

/**
 * @author Mujuzi Moses
 */
public class ModuleLoadingApplication {

    public static void main(String[] args) {
        ExampleModule module = new ExampleModule("example-module");
        ExampleModuleActivator activator = new ExampleModuleActivator();
        ModuleLoader moduleLoader = new ModuleLoader(activator);

        moduleLoader.load(module);

        System.out.println("Module loaded: " + module.isLoaded());

        moduleLoader.start(module);

        System.out.println("Module started: " + module.isStarted());
    }
}
