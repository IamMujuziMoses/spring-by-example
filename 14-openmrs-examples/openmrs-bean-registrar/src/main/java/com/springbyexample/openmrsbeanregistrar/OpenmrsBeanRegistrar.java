package com.springbyexample.openmrsbeanregistrar;

import com.springbyexample.openmrsbeanregistrar.impl.GreetingServiceImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author Mujuzi Moses
 */
public class OpenmrsBeanRegistrar {

    public void registerBeans(BeanDefinitionRegistry registry) {
        BeanDefinition beanDefinition = new RootBeanDefinition(GreetingServiceImpl.class);

        registry.registerBeanDefinition("greetingService", beanDefinition);
    }
}
