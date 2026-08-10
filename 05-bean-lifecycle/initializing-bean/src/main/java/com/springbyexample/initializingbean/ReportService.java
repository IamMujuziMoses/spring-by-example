package com.springbyexample.initializingbean;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author Mujuzi Moses
 */
public class ReportService implements InitializingBean {

    private boolean initialized;

    @Override
    public void afterPropertiesSet() {
        initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
