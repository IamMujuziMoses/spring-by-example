package com.springbyexample.exceptionhandling;

/**
 * @author Mujuzi Moses
 */
public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(Long id) {
        super("Item not found: " + id);
    }
}
