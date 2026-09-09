package com.springbyexample.importbeandefinitionregistrar;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author Mujuzi Moses
 */
public class GreetingRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registry.registerBeanDefinition("greetingService",
                BeanDefinitionBuilder.rootBeanDefinition(GreetingService.class).getBeanDefinition());
    }
}
