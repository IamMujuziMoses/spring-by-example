package com.springbyexample.deferredimportselector;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author Mujuzi Moses
 */
public class GreetingDeferredImportSelector implements DeferredImportSelector {

    @Override
    public String[] selectImports(@NonNull AnnotationMetadata importingClassMetadata) {
        return new String[] {
                GreetingConfiguration.class.getName()
        };
    }
}
