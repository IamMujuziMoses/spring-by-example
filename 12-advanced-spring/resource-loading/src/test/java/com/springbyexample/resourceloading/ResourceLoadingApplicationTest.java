package com.springbyexample.resourceloading;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * @author Mujuzi Moses
 */
public class ResourceLoadingApplicationTest {

    @Test
    void shouldLoadResourceFromClasspath() {
        ResourceLoader resourceLoader = new DefaultResourceLoader();

        Resource resource = resourceLoader.getResource("classpath:greeting.txt");

        assertTrue(resource.exists());
        assertEquals("greeting.txt", resource.getFilename());
    }

    @Test
    void shouldReadResourceContent() throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();

        Resource resource = resourceLoader.getResource("classpath:greeting.txt");

        try (InputStream inputStream = resource.getInputStream()) {
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            assertNotNull(content);
            assertEquals("Hello from a Spring Resource!", content);
        }
    }
}
