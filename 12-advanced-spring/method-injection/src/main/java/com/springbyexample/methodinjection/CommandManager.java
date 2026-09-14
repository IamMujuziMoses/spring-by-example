package com.springbyexample.methodinjection;

/**
 * @author Mujuzi Moses
 */
public abstract class CommandManager {

    public Command process() {

        return createCommand();
    }

    protected abstract Command createCommand();
}
