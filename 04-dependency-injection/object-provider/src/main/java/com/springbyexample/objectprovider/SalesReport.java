package com.springbyexample.objectprovider;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
@Primary
@Scope("prototype") // remember we already covered @Scopes in bean-scopes
public class SalesReport implements Report {

    public SalesReport() {
        System.out.println("Creating SalesReport instance");
    }

    @Override
    public void generate() {
        System.out.println("Generating sales report");
    }
}
