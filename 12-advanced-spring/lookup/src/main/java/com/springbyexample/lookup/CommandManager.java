package com.springbyexample.lookup;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
public class CommandManager {

    public Command process() {
        return createCommand();
    }

    @Lookup("command")
    protected Command createCommand() {
        throw new UnsupportedOperationException("Spring should override this method");
    }
}
