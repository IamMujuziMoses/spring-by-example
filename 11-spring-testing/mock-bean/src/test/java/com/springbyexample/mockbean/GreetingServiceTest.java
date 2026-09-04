package com.springbyexample.mockbean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Mujuzi Moses
 */
@SpringBootTest
public class GreetingServiceTest {

    @Autowired
    private GreetingService greetingService;

    @MockitoBean
    private GreetingRepository greetingRepository;

    @Test
    void shouldCreateGreetingUsingMockedRepository() {
        when(greetingRepository.findNameById(1L))
                .thenReturn("Spring");

        String result = greetingService.greet(1L);

        assertEquals("Hello, Spring!", result);

        verify(greetingRepository).findNameById(1L);
    }
}
