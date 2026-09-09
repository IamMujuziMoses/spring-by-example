package com.springbyexample.importselector;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author Mujuzi Moses
 */
public class GreetingImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(@NonNull AnnotationMetadata importingClassMetadata) {

        return new String[] {GreetingConfiguration.class.getName()};
    }
}
