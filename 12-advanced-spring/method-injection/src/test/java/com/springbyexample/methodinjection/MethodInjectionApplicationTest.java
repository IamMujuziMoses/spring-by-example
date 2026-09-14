package com.springbyexample.methodinjection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.ReplaceOverride;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author Mujuzi Moses
 */
public class MethodInjectionApplicationTest {

    @Test
    void shouldInjectMethodImplementation() {
        CommandManager commandManager = createCommandManager();

        Command command = commandManager.process();

        assertEquals("Hello from Method Injection!", command.message());
    }

    @Test
    void shouldCreateNewCommandForEachInvocation() {
        CommandManager commandManager = createCommandManager();

        Command first = commandManager.process();
        Command second = commandManager.process();

        assertNotSame(first, second);
    }

    private CommandManager createCommandManager() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerBeanDefinition("commandReplacer",
                BeanDefinitionBuilder.rootBeanDefinition(CommandReplacer.class).getBeanDefinition());

        RootBeanDefinition commandManagerDefinition = new RootBeanDefinition(CommandManager.class);

        commandManagerDefinition.getMethodOverrides().addOverride(
                        new ReplaceOverride("createCommand", "commandReplacer")
                );

        beanFactory.registerBeanDefinition("commandManager", commandManagerDefinition);

        return beanFactory.getBean("commandManager", CommandManager.class);
    }
}
