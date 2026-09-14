package com.springbyexample.methodinjection;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.support.ReplaceOverride;

/**
 * @author Mujuzi Moses
 */
public class MethodInjectionApplication {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerBeanDefinition("commandReplacer",
                BeanDefinitionBuilder.rootBeanDefinition(CommandReplacer.class).getBeanDefinition()
        );

        RootBeanDefinition commandManagerDefinition = new RootBeanDefinition(CommandManager.class);

        commandManagerDefinition.getMethodOverrides().addOverride(
                        new ReplaceOverride("createCommand", "commandReplacer")
                );

        beanFactory.registerBeanDefinition("commandManager", commandManagerDefinition);

        CommandManager commandManager = beanFactory.getBean("commandManager", CommandManager.class);

        Command command = commandManager.process();

        System.out.println(command.message());
    }
}
