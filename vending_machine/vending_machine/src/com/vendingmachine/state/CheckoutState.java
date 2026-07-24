package com.vendingmachine.state;

import com.vendingmachine.machine.VendingMachine;
import com.vendingmachine.model.Item;
import com.vendingmachine.model.Slot;

public class CheckoutState implements VendingMachineState {

    @Override
    public boolean select(VendingMachine machine, int row, int col, int qty) {
        if (row >= 0 && col >= 0) {
            Item item = machine.getShelf()[row][col].getItem();
            Slot st = machine.getShelf()[row][col];
            if (item != null && machine.cart.containsKey(item)) {
                if (qty <= st.getCurrent_qty()) {
                    machine.cart.put(item, qty);
                    System.out.println(
                            "Updated " + qty + " x " + machine.getShelf()[row][col].getItem().getName() + " to cart.");
                } else if (qty <= 0) {
                    machine.cart.remove(item);
                    System.out.println(
                            "Removed " + qty + " x " + machine.getShelf()[row][col].getItem().getName() + " to cart.");
                }
                return true;
            }
            if (machine.canAddtoCart(row, col, qty) && st != null) {
                machine.cart.put(item, qty);
                System.out.println(
                        "Added " + qty + " x " + machine.getShelf()[row][col].getItem().getName() + " to cart.");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkout(VendingMachine machine) {
        System.out.println("Already in checkout state.");
        return true;
    }

    @Override
    public boolean completePayment(VendingMachine machine) {
        if (machine.cart.isEmpty()) {
            System.out.println("Cart is empty. Cannot proceed to payment.");
            machine.setCurrState(StateEnum.SELECTION);
            return false;
        }
        System.out.println("Proceeding to payment.");
        machine.setCurrState(StateEnum.PAYMENT);
        return machine.getState(StateEnum.PAYMENT).completePayment(machine);
    }

    @Override
    public void insertCash(VendingMachine machine, com.vendingmachine.model.Denomination denomination) {
        if (machine.cart.isEmpty()) {
            System.out.println("Cart is empty. Please select items first.");
            return;
        }
        System.out.println("Proceeding to payment state to accept cash.");
        machine.setCurrState(StateEnum.PAYMENT);
        machine.getState(StateEnum.PAYMENT).insertCash(machine, denomination);
    }

    @Override
    public boolean dispense(VendingMachine machine) {
        System.out.println("Cannot dispense before payment completion.");
        return false;
    }

    @Override
    public void cancelDispense(VendingMachine machine) {
        System.out.println("Checkout cancelled.");
    }
}
