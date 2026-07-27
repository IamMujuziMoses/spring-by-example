package com.springbyexample.singletonscope;

/**
 * @author Mujuzi Moses
 */
public class CounterService {

    private final Counter counter;

    public CounterService(Counter counter) {
        this.counter = counter;
    }

    public void incrementCounter() {
        counter.increment();
    }

    public int getCount() {
        return counter.getCount();
    }

}