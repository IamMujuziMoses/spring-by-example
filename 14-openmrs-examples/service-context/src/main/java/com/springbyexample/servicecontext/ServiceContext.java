package com.springbyexample.servicecontext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Simplified OpenMRS-style ServiceContext.
 *
 * <p>
 * The real OpenMRS ServiceContext acts as the service layer between
 * the application and Spring-managed services. This example focuses
 * on the core concepts rather than reproducing the complete OpenMRS
 * implementation.
 * </p>
 *
 * @author Mujuzi Moses
 */
public class ServiceContext implements ApplicationContextAware {

    private static ServiceContext instance;

    private final Map<Class<?>, Object> services = new HashMap<>();

    private ApplicationContext applicationContext;

    private ServiceContext() {}

    /**
     * Returns the singleton ServiceContext instance.
     *
     * @return the current ServiceContext
     */
    public static synchronized ServiceContext getInstance() {
        if (instance == null) {
            instance = new ServiceContext();
        }

        return instance;
    }

    /**
     * Destroys the current ServiceContext instance.
     *
     * <p>
     * This is useful when shutting down or resetting the example.
     * </p>
     */
    public static synchronized void destroyInstance() {
        if (instance != null) {
            instance.services.clear();
            instance.applicationContext = null;
            instance = null;
        }
    }

    /**
     * Registers a service with the service layer.
     *
     * @param serviceType service interface
     * @param service service implementation
     */
    public void setService(Class<?> serviceType, Object service) {
        if (serviceType == null || service == null) {
            throw new IllegalArgumentException("Service type and service must not be null");
        }

        services.put(serviceType, service);
    }

    /**
     * Retrieves a service by its interface.
     *
     * @param serviceType service interface
     * @param <T> service type
     * @return registered service
     */
    public <T> T getService(Class<T> serviceType) {
        Object service = services.get(serviceType);

        if (service == null) {
            throw new IllegalStateException("No service registered for " + serviceType.getName());
        }

        return serviceType.cast(service);
    }

    /**
     * Retrieves all Spring beans matching the supplied type.
     *
     * @param type component type
     * @param <T> component type
     * @return registered Spring components
     */
    public <T> List<T> getRegisteredComponents(Class<T> type) {
        if (applicationContext == null) {
            throw new IllegalStateException("ApplicationContext has not been configured");
        }

        Map<String, T> beans = applicationContext.getBeansOfType(type);

        return new ArrayList<>(beans.values());
    }

    /**
     * Retrieves a Spring bean by name and type.
     *
     * @param beanName bean name
     * @param type bean type
     * @param <T> bean type
     * @return registered Spring bean
     */
    public <T> T getRegisteredComponent(String beanName, Class<T> type) {

        if (applicationContext == null) {
            throw new IllegalStateException("ApplicationContext has not been configured");
        }

        try {
            return applicationContext.getBean(beanName, type);
        } catch (BeansException exception) {
            throw new IllegalStateException("Unable to retrieve registered component: " + beanName, exception);
        }
    }

    /**
     * Receives the Spring ApplicationContext.
     *
     * @param applicationContext Spring application context
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {

        this.applicationContext = applicationContext;
    }

    /**
     * Returns the Spring ApplicationContext.
     *
     * @return application context
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
