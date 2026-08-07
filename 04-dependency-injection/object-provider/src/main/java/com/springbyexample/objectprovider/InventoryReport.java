package com.springbyexample.objectprovider;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
@Scope("prototype") // remember we already covered @Scopes in bean-scopes
public class InventoryReport implements Report {

    @Override
    public void generate() {
        System.out.println("Generating inventory report");
    }
}
