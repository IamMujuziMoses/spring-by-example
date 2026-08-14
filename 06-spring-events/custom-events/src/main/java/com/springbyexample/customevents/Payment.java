package com.springbyexample.customevents;

/**
 * @author Mujuzi Moses
 */
public class Payment {

    private final Long id;
    private final double amount;

    public Payment(Long id, double amount) {
        this.id = id;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }
}
