package com.springbyexample.objectprovider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class AppConfigTest {

    @Test
    void shouldReturnDifferentInstancesForPrototypeBean() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            // Retrieve an ObjectProvider for the Report type. The provider allows retrieving Report instances from the
            // Spring container on demand instead of injecting a single instance.
            var provider = context.getBeanProvider(Report.class);

            // Request the first Report instance from the provider. Since Report is prototype scoped, Spring creates a
            // new instance.
            Report first = provider.getObject();

            // Request another Report instance from the same provider. A prototype bean always returns a new instance
            // for each request.
            Report second = provider.getObject();

            // Verify that Spring created two different Report instances. This confirms that ObjectProvider works
            // correctly with prototype beans.
            assertNotSame(first, second);
        }
    }

    @Test
    void getIfAvailable_shouldReturnBeanWhenAvailable() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            // ObjectProvider allows lazy access to beans from the Spring container. getIfAvailable() returns the bean
            // if it exists instead of throwing an exception.
            var provider = context.getBeanProvider(Report.class);

            Report report = provider.getIfAvailable();

            // Verify that the Report bean was found and returned by the provider.
            assertNotNull(report);

            assertInstanceOf(SalesReport.class, report);
        }
    }


    @Test
    void ifAvailable_shouldExecuteConsumerWhenBeanExists() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var provider = context.getBeanProvider(Report.class);
            var executed = new AtomicBoolean(false);

            // ifAvailable() executes the provided callback only when a matching bean exists. Since Report is registered,
            // the consumer should be executed.
            provider.ifAvailable(report -> {executed.set(true);report.generate();});

            assertTrue(executed.get());
        }
    }


    @Test
    void ifAvailable_shouldNotExecuteConsumerWhenBeanIsMissing() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            // Requesting an ObjectProvider for a type that is not registered allows the application to handle the
            // missing dependency gracefully.
            var provider = context.getBeanProvider(Invoice.class);

            var executed = new AtomicBoolean(false);

            // Because no Invoice bean exists, the consumer should never be executed.
            provider.ifAvailable(invoice -> {executed.set(true);});

            assertFalse(executed.get());
        }
    }


    @Test
    void getIfAvailable_shouldReturnNullWhenBeanIsMissing() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            // ObjectProvider can safely check for optional dependencies. getIfAvailable() returns null when no
            // matching bean exists.
            var provider = context.getBeanProvider(Invoice.class);

            Invoice invoice = provider.getIfAvailable();

            assertNull(invoice);
        }
    }


    @Test
    void stream_shouldReturnAllMatchingBeans() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            // stream() provides access to all beans matching the requested type. This is similar to Collection Injection
            // but resolves beans lazily.
            var provider = context.getBeanProvider(Report.class);

            List<Report> reports = provider.stream().toList();

            // Verify that Spring discovered all Report implementations.
            assertEquals(2, reports.size());

            assertTrue(reports.stream().anyMatch(report -> report instanceof SalesReport));
            assertTrue(reports.stream().anyMatch(report -> report instanceof InventoryReport));
        }
    }


    @Test
    void stream_shouldCreateNewPrototypeInstances() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var provider = context.getBeanProvider(Report.class);

            // Retrieve the available prototype beans for the first time.
            List<Report> first = provider.stream().toList();

            // Retrieve the same prototype beans again. Each request should create new instances.
            List<Report> second = provider.stream().toList();

            // Verify that prototype beans are not reused between requests.
            Report firstSalesReport = first.stream().filter(report -> report instanceof SalesReport).findFirst().orElseThrow();
            Report secondSalesReport = second.stream().filter(report -> report instanceof SalesReport).findFirst().orElseThrow();

            assertNotSame(firstSalesReport, secondSalesReport);

        }
    }

    // Dummy class
    public interface Invoice { }
}
