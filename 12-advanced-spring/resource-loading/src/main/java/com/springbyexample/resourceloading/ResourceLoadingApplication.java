package com.springbyexample.resourceloading;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * @author Mujuzi Moses
 */
public class ResourceLoadingApplication {

    public static void main(String[] args) throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:greeting.txt");

        System.out.println("Resource exists: " + resource.exists());
        System.out.println("Resource filename: " + resource.getFilename());

        try (InputStream inputStream = resource.getInputStream()) {
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            System.out.println("Resource content: " + content);
        }
    }
}