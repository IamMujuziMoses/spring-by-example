package com.springbyexample.aopinopenmrs;

import java.util.HashMap;
import java.util.Map;

import org.aopalliance.aop.Advice;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;

/**
 * @author Mujuzi Moses
 */
public class AopServiceContext {

    private final Map<Class<?>, Object> services = new HashMap<>();

    private final Map<Class<?>, Advice> addedAdvice = new HashMap<>();

    /**
     * Registers a service and wraps it with a Spring AOP proxy.
     *
     * @param serviceType service interface
     * @param service service implementation
     * @param advice advice to apply to the service
     * @param <T> service type
     */
    public <T> void setService(Class<T> serviceType, T service, Advice advice) {
        ProxyFactory proxyFactory = new ProxyFactory(service);
        proxyFactory.addAdvice(advice);

        T proxy = serviceType.cast(proxyFactory.getProxy());

        services.put(serviceType, proxy);
        addedAdvice.put(serviceType, advice);
    }

    /**
     * Returns the proxied service.
     *
     * @param serviceType service interface
     * @param <T> service type
     * @return proxied service
     */
    public <T> T getService(Class<T> serviceType) {
        Object service = services.get(serviceType);

        if (service == null) {
            throw new IllegalStateException("No service registered for " + serviceType.getName());
        }

        return serviceType.cast(service);
    }

    /**
     * Returns the advice registered for a service.
     *
     * @param serviceType service interface
     * @return registered advice
     */
    public Advice getAddedAdvice(Class<?> serviceType) {
        return addedAdvice.get(serviceType);
    }

    /**
     * Adds additional advice to an existing service proxy.
     *
     * @param serviceType service interface
     * @param advice advice to add
     */
    public void addAdvice(Class<?> serviceType, Advice advice) {
        Object service = services.get(serviceType);

        if (!(service instanceof Advised advisedService)) {
            throw new IllegalStateException("Service is not an AOP proxy: " + serviceType.getName());
        }

        advisedService.addAdvice(advice);
        addedAdvice.put(serviceType, advice);
    }

    /**
     * Removes advice from an existing service proxy.
     *
     * @param serviceType service interface
     */
    public void removeAdvice(Class<?> serviceType) {
        Object service = services.get(serviceType);

        if (!(service instanceof Advised advisedService)) {
            return;
        }

        Advice advice = addedAdvice.remove(serviceType);

        if (advice != null) {
            advisedService.removeAdvice(advice);
        }
    }
}
