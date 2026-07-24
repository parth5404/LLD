package com.vendingmachine.model;

import java.util.HashMap;
import java.util.Map;

public class CashInventory {
    private Map<Denomination, Integer> inventory = new HashMap<>();

    public void add(Denomination d, int count) {
        inventory.put(d, inventory.getOrDefault(d, 0) + count);
    }

    public void remove(Denomination d, int count) {
        int current = inventory.getOrDefault(d, 0);
        if (current >= count) {
            inventory.put(d, current - count);
        } else {
            throw new IllegalArgumentException("Not enough " + d.name() + " bills.");
        }
    }

    public int getQuantity(Denomination d) {
        return inventory.getOrDefault(d, 0);
    }

    public void clear() {
        inventory.clear();
    }

    public double getTotalBalance() {
        double total = 0;
        for (Map.Entry<Denomination, Integer> entry : inventory.entrySet()) {
            total += entry.getKey().getValue() * entry.getValue();
        }
        return total;
    }

    public Map<Denomination, Integer> getInventory() {
        return inventory;
    }
}
