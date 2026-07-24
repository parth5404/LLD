package com.vendingmachine.state;

import com.vendingmachine.machine.VendingMachine;

public class DispensingState implements VendingMachineState {

    @Override
    public boolean select() {
        System.out.println("Cannot select item while dispensing.");
        return false;
    }

    @Override
    public boolean pay(VendingMachine vendingMachine) {
        System.out.println("Payment already completed.");
        return false;
    }

    @Override
    public boolean dispense(VendingMachine vendingMachine) {
        System.out.println("Item dispensed successfully!");
        return true;
    }

    @Override
    public void cancelDispense() {
        System.out.println("Cannot cancel while item is dispensing.");
    }
}
