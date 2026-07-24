package com.vendingmachine.state;

import com.vendingmachine.machine.VendingMachine;

public class PaymentState implements VendingMachineState {

    @Override
    public boolean select(VendingMachine machine, int row, int col, int qty) {
        System.out.println("Cannot change selection during payment processing.");
        return false;
    }

    @Override
    public boolean checkout(VendingMachine machine) {
        System.out.println("Already checked out. Ready for payment.");
        return true;
    }

    @Override
    public void insertCash(VendingMachine machine, com.vendingmachine.model.Denomination denomination) {
        machine.tempBuffer.add(denomination, 1);
        System.out.println("Inserted $" + denomination.getValue() + ". Total inserted: $" + machine.tempBuffer.getTotalBalance());
    }

    @Override
    public boolean completePayment(VendingMachine machine) {
        double inserted = machine.tempBuffer.getTotalBalance();
        double required = machine.getCartTotal();

        if (inserted < required) {
            System.out.println("Insufficient funds. Inserted: $" + inserted + ", Required: $" + required);
            return false;
        }

        double change = inserted - required;
        if (change > 0) {
            if (machine.machineWallet.getTotalBalance() < change) {
                System.out.println("Machine does not have enough change. Refunding inserted cash.");
                cancelDispense(machine);
                return false;
            }
            System.out.println("Change to return: $" + change);
            // Naive change dispensing: simply acknowledging it for now.
        }

        // Transfer tempBuffer to machineWallet
        for (java.util.Map.Entry<com.vendingmachine.model.Denomination, Integer> entry : machine.tempBuffer.getInventory().entrySet()) {
            machine.machineWallet.add(entry.getKey(), entry.getValue());
        }
        machine.tempBuffer.clear();

        System.out.println("Payment processed successfully!");
        machine.setCurrState(StateEnum.DISPENSE);
        return true;
    }

    @Override
    public boolean dispense(VendingMachine vendingMachine) {
        System.out.println("Cannot dispense before payment completion.");
        return false;
    }

    @Override
    public void cancelDispense(VendingMachine machine) {
        System.out.println("Payment cancelled. Refunding $" + machine.tempBuffer.getTotalBalance());
        machine.tempBuffer.clear();
        machine.setCurrState(StateEnum.SELECTION);
    }
}
