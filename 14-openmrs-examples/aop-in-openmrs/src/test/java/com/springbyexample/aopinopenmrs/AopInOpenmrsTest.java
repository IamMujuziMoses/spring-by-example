package com.springbyexample.aopinopenmrs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.springbyexample.aopinopenmrs.impl.GreetingServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.Advised;

/**
 * @author Mujuzi Moses
 */
public class AopInOpenmrsTest {

    @Test
    void shouldCreateAopProxyForService() {
        GreetingService target = new GreetingServiceImpl();
        LoggingAdvice advice = new LoggingAdvice();

        AopServiceContext serviceContext = new AopServiceContext();
        serviceContext.setService(GreetingService.class, target, advice);

        GreetingService service = serviceContext.getService(GreetingService.class);

        assertInstanceOf(Advised.class, service);
        assertSame(advice, serviceContext.getAddedAdvice(GreetingService.class));
    }

    @Test
    void shouldInvokeServiceThroughLoggingAdvice() {
        GreetingService target = new GreetingServiceImpl();
        LoggingAdvice advice = new LoggingAdvice();

        AopServiceContext serviceContext = new AopServiceContext();
        serviceContext.setService(GreetingService.class, target, advice);

        GreetingService service = serviceContext.getService(GreetingService.class);

        assertEquals("Hello from an OpenMRS AOP service!", service.greet());
        assertEquals(1, advice.getInvocationCount());
    }

    @Test
    void shouldAllowAuthorizedServiceInvocation() {
        GreetingService target = new GreetingServiceImpl();
        AuthorizationAdvice advice = new AuthorizationAdvice(true);

        AopServiceContext serviceContext = new AopServiceContext();
        serviceContext.setService(GreetingService.class, target, advice);

        GreetingService service = serviceContext.getService(GreetingService.class);

        assertEquals("Hello from an OpenMRS AOP service!", service.greet());
    }

    @Test
    void shouldPreventUnauthorizedServiceInvocation() {
        GreetingService target = new GreetingServiceImpl();
        AuthorizationAdvice advice = new AuthorizationAdvice(false);

        AopServiceContext serviceContext = new AopServiceContext();
        serviceContext.setService(GreetingService.class, target, advice);

        GreetingService service = serviceContext.getService(GreetingService.class);

        assertThrows(SecurityException.class, service::greet);
    }

    @Test
    void shouldAddAdviceToExistingProxy() {
        GreetingService target = new GreetingServiceImpl();
        LoggingAdvice loggingAdvice = new LoggingAdvice();
        AuthorizationAdvice authorizationAdvice = new AuthorizationAdvice(true);

        AopServiceContext serviceContext = new AopServiceContext();
        serviceContext.setService(GreetingService.class, target, loggingAdvice);
        serviceContext.addAdvice(GreetingService.class, authorizationAdvice);

        GreetingService service = serviceContext.getService(GreetingService.class);

        assertEquals("Hello from an OpenMRS AOP service!", service.greet());
        assertEquals(1, loggingAdvice.getInvocationCount());
    }

    @Test
    void shouldRemoveAddedAdvice() {
        GreetingService target = new GreetingServiceImpl();
        LoggingAdvice loggingAdvice = new LoggingAdvice();

        AopServiceContext serviceContext = new AopServiceContext();

        serviceContext.setService(GreetingService.class, target, loggingAdvice);

        serviceContext.removeAdvice(GreetingService.class);

        GreetingService service = serviceContext.getService(GreetingService.class);

        assertEquals("Hello from an OpenMRS AOP service!", service.greet());
        assertEquals(0, loggingAdvice.getInvocationCount());
    }
}
