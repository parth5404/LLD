package com.vendingmachine.machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vendingmachine.model.Item;
import com.vendingmachine.model.Slot;
import com.vendingmachine.state.CheckoutState;
import com.vendingmachine.state.DispensingState;
import com.vendingmachine.state.PaymentState;
import com.vendingmachine.state.SelectionState;
import com.vendingmachine.state.StateEnum;
import com.vendingmachine.state.VendingMachineState;
import com.vendingmachine.utils.Util;

public class VendingMachine {

    private static final int MAX_ROWS = 10;
    private static final int MAX_COLS = 10;

    private StateEnum currState;
    private Map<StateEnum, VendingMachineState> stateMap = new HashMap<>();

    private Slot[][] shelf = new Slot[MAX_ROWS][MAX_COLS];
    public Map<Item, Integer> cart = new HashMap<>();
    public com.vendingmachine.model.CashInventory machineWallet = new com.vendingmachine.model.CashInventory();
    public com.vendingmachine.model.CashInventory tempBuffer = new com.vendingmachine.model.CashInventory();
    public com.vendingmachine.utils.Util util = new Util();

    public VendingMachine() {
        stateMap.put(StateEnum.SELECTION, new SelectionState());
        stateMap.put(StateEnum.CHECKOUT, new CheckoutState());
        stateMap.put(StateEnum.PAYMENT, new PaymentState());
        stateMap.put(StateEnum.DISPENSE, new DispensingState());

        this.currState = StateEnum.SELECTION;
    }

    public StateEnum getCurrState() {
        return currState;
    }

    public void setCurrState(StateEnum state) {
        this.currState = state;
    }

    public VendingMachineState getState(StateEnum key) {
        return stateMap.get(key);
    }

    public Slot[][] getShelf() {
        return shelf;
    }

    public void setShelf(Slot[][] slots) {
        this.shelf = slots;
    }

    public boolean canAddtoCart(int row, int col, int qty) {
        boolean ok = false;
        if (shelf[row][col].getCurrent_qty() >= qty) {
            ok = true;
        }
        return ok;
    }

    public double getCartTotal() {
        double total = 0;
        for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }
}
