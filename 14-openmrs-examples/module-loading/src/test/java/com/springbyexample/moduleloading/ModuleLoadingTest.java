package com.springbyexample.moduleloading;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * @author Mujuzi Moses
 */
public class ModuleLoadingTest {

    @Test
    void shouldCreateModuleInUnloadedState() {
        ExampleModule module = new ExampleModule("example-module");

        assertFalse(module.isLoaded());
        assertFalse(module.isStarted());
    }

    @Test
    void shouldLoadModule() {
        ExampleModule module = new ExampleModule("example-module");
        ModuleLoader moduleLoader = new ModuleLoader(new ExampleModuleActivator());

        moduleLoader.load(module);

        assertTrue(module.isLoaded());
        assertFalse(module.isStarted());
    }

    @Test
    void shouldNotStartModuleBeforeLoading() {
        ExampleModule module = new ExampleModule("example-module");
        ModuleLoader moduleLoader = new ModuleLoader(new ExampleModuleActivator());

        assertThrows(IllegalStateException.class, () -> moduleLoader.start(module));
    }

    @Test
    void shouldStartLoadedModule() {
        ExampleModule module = new ExampleModule("example-module");
        ExampleModuleActivator activator = new ExampleModuleActivator();
        ModuleLoader moduleLoader = new ModuleLoader(activator);

        moduleLoader.load(module);
        moduleLoader.start(module);

        assertTrue(module.isLoaded());
        assertTrue(module.isStarted());
    }

    @Test
    void shouldInvokeModuleLifecycleCallbacks() {
        ExampleModule module = new ExampleModule("example-module");
        ExampleModuleActivator activator = new ExampleModuleActivator();
        ModuleLoader moduleLoader = new ModuleLoader(activator);

        moduleLoader.load(module);
        moduleLoader.start(module);

        assertTrue(activator.isWillRefreshContextCalled());
        assertTrue(activator.isContextRefreshedCalled());
        assertTrue(activator.isStartedCalled());
    }

    @Test
    void shouldNotLoadModuleMoreThanOnce() {
        ExampleModule module = new ExampleModule("example-module");
        ModuleLoader moduleLoader = new ModuleLoader(new ExampleModuleActivator());

        moduleLoader.load(module);
        moduleLoader.load(module);

        assertTrue(module.isLoaded());
    }

    @Test
    void shouldNotStartModuleMoreThanOnce() {
        ExampleModule module = new ExampleModule("example-module");
        ExampleModuleActivator activator = new ExampleModuleActivator();
        ModuleLoader moduleLoader = new ModuleLoader(activator);

        moduleLoader.load(module);
        moduleLoader.start(module);
        moduleLoader.start(module);

        assertTrue(module.isStarted());
    }
}