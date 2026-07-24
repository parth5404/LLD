package com.vendingmachine.state;

import com.vendingmachine.machine.VendingMachine;

public class PaymentState implements VendingMachineState {

    @Override
    public boolean select() {
        System.out.println("Cannot change selection during payment processing.");
        return false;
    }

    @Override
    public boolean pay(VendingMachine vendingMachine) {
        System.out.println("Payment processed successfully!");
        return true;
    }

    @Override
    public boolean dispense(VendingMachine vendingMachine) {
        System.out.println("Cannot dispense before payment completion.");
        return false;
    }

    @Override
    public void cancelDispense() {
        System.out.println("Payment cancelled.");
    }
}
