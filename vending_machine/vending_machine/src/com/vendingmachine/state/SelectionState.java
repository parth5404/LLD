package com.vendingmachine.state;

import com.vendingmachine.machine.VendingMachine;

public class SelectionState implements VendingMachineState {

    @Override
    public boolean select(VendingMachine machine, int row, int col, int qty) {
        if (row >= 0 && col >= 0 && machine.canAddtoCart(row, col, qty) && machine.getShelf()[row][col] != null) {

            machine.cart.put(machine.getShelf()[row][col].getItem(), qty);
            System.out.println("Added " + qty + " x " + machine.getShelf()[row][col].getItem().getName() + " to cart.");
            return true;
        }
        System.out.println("Cannot add item to cart (insufficient stock or invalid slot).");
        return false;
    }

    @Override
    public boolean checkout(VendingMachine machine) {
        if (machine.cart.isEmpty()) {
            System.out.println("Cart is empty. Cannot checkout.");
            return false;
        }
        System.out.println("Transitioning to Checkout state.");
        machine.setCurrState(StateEnum.CHECKOUT);
        return true;
    }

    @Override
    public boolean completePayment(VendingMachine machine) {
        System.out.println("Cannot pay in Selection state. Please finalize selection first.");
        return false;
    }

    @Override
    public void insertCash(VendingMachine machine, com.vendingmachine.model.Denomination denomination) {
        System.out.println("Cannot insert cash in Selection state. Please checkout first.");
    }

    @Override
    public boolean dispense(VendingMachine vendingMachine) {
        System.out.println("Cannot dispense before payment completion.");
        return false;
    }

    @Override
    public void cancelDispense(VendingMachine machine) {
        System.out.println("No transaction to cancel.");
    }
}
