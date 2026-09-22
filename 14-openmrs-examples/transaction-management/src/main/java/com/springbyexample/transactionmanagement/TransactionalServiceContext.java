package com.springbyexample.transactionmanagement;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionProxyFactoryBean;

/**
 * @author Mujuzi Moses
 */
public class TransactionalServiceContext {

    private final Map<Class<?>, Object> services = new ConcurrentHashMap<>();

    private final PlatformTransactionManager transactionManager;

    public TransactionalServiceContext(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public <T> void setService(Class<T> serviceType, T service) {
        TransactionProxyFactoryBean proxyFactory = new TransactionProxyFactoryBean();
        proxyFactory.setTarget(service);
        proxyFactory.setTransactionManager(transactionManager);

        Properties transactionAttributes = new Properties();
        transactionAttributes.setProperty("saveGreeting", "PROPAGATION_REQUIRED");
        transactionAttributes.setProperty("fail", "PROPAGATION_REQUIRED");

        proxyFactory.setTransactionAttributes(transactionAttributes);
        proxyFactory.afterPropertiesSet();

        try {
            services.put(serviceType, proxyFactory.getObject());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create transactional service proxy", e);
        }
    }

    public <T> T getService(Class<T> serviceType) {
        Object service = services.get(serviceType);

        if (service == null) {
            throw new IllegalStateException("No service registered for " + serviceType.getName());
        }

        return serviceType.cast(service);
    }
}
