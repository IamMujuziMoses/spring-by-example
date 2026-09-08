package com.springbyexample.factorybean;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author Mujuzi Moses
 */
public class GreetingServiceFactoryBean implements FactoryBean<GreetingService> {

    @Override
    public GreetingService getObject() {
        return new GreetingService("Hello from FactoryBean!");
    }

    @Override
    public Class<?> getObjectType() {
        return GreetingService.class;
    }
}
