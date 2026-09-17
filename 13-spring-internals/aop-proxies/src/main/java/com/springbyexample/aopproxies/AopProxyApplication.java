package com.springbyexample.aopproxies;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

/**
 * @author Mujuzi Moses
 */
public class AopProxyApplication {

    public static void main(String[] args) {
        GreetingService target = new GreetingServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new LoggingInterceptor());

        GreetingService proxy = (GreetingService) proxyFactory.getProxy();

        System.out.println("Target type: " + target.getClass().getName());
        System.out.println("Proxy type: " + proxy.getClass().getName());
        System.out.println("Is AOP proxy: " + AopUtils.isAopProxy(proxy));
        System.out.println(proxy.greet());
    }
}
