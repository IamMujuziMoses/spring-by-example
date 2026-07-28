package com.springbyexample.bean;

/**
 * @author Mujuzi Moses
 */
import java.time.LocalTime;

public class TimeService {

    public LocalTime currentTime() {
        return LocalTime.now();
    }

}
