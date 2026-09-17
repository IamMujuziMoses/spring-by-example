package com.springbyexample.aopproxies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

/**
 * @author Mujuzi Moses
 */
public class AopProxyTest {

    @Test
    void shouldCreateAopProxy() {
        GreetingService target = new GreetingServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new LoggingInterceptor());

        GreetingService proxy = (GreetingService) proxyFactory.getProxy();

        assertTrue(AopUtils.isAopProxy(proxy));
        assertNotSame(target, proxy);
        assertTrue(proxy instanceof GreetingService);
    }

    @Test
    void shouldInvokeTargetThroughProxy() {
        GreetingService target = new GreetingServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new LoggingInterceptor());

        GreetingService proxy = (GreetingService) proxyFactory.getProxy();

        assertEquals("Hello from GreetingService!", proxy.greet());
    }
}
