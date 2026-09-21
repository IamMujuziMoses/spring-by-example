package com.springbyexample.servicecontext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import com.springbyexample.servicecontext.impl.GreetingServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */
public class ServiceContextTest {

    private AnnotationConfigApplicationContext applicationContext;

    private ServiceContext serviceContext;

    @BeforeEach
    void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(TestConfig.class);
        serviceContext = ServiceContext.getInstance();
        serviceContext.setApplicationContext(applicationContext);
    }

    @AfterEach
    void tearDown() {
        applicationContext.close();
        ServiceContext.destroyInstance();
    }

    @Test
    void shouldReturnSameServiceContextInstance() {
        ServiceContext first = ServiceContext.getInstance();
        ServiceContext second = ServiceContext.getInstance();

        assertSame(first, second);
    }

    @Test
    void shouldStoreAndRetrieveService() {
        GreetingService service = applicationContext.getBean(GreetingService.class);

        serviceContext.setService(GreetingService.class, service);

        GreetingService retrieved = serviceContext.getService(GreetingService.class);

        assertSame(service, retrieved);
    }

    @Test
    void shouldInvokeRetrievedService() {
        GreetingService service = applicationContext.getBean(GreetingService.class);

        serviceContext.setService(GreetingService.class, service);

        assertEquals("Hello from the OpenMRS ServiceContext!",
                serviceContext.getService(GreetingService.class).greet());
    }

    @Test
    void shouldRetrieveRegisteredComponent() {
        GreetingService service = serviceContext.getRegisteredComponent("greetingService", GreetingService.class);

        assertEquals("Hello from the OpenMRS ServiceContext!", service.greet());
    }

    @Test
    void shouldRetrieveRegisteredComponentsByType() {
        List<GreetingService> services = serviceContext.getRegisteredComponents(GreetingService.class);

        assertEquals(1, services.size());
        assertEquals("Hello from the OpenMRS ServiceContext!", services.get(0).greet());
    }

    @Test
    void shouldFailWhenServiceHasNotBeenRegistered() {
        assertThrows(IllegalStateException.class, () -> serviceContext.getService(GreetingService.class));
    }

    @Configuration
    static class TestConfig {

        @Bean
        public GreetingService greetingService() {
            return new GreetingServiceImpl();
        }
    }
}
