package com.vendingmachine.model;

public class Slot {
    private int row;
    private int col;
    private int max_qty;
    private Item item;
    private int current_qty;
    
    public Slot(int row, int col, int max_qty, Item item, int current_qty) {
        this.row = row;
        this.col = col;
        this.max_qty = max_qty;
        this.item = item;
        this.current_qty = current_qty;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getMax_qty() {
        return max_qty;
    }

    public void setMax_qty(int max_qty) {
        this.max_qty = max_qty;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getCurrent_qty() {
        return current_qty;
    }

    public void setCurrent_qty(int current_qty) {
        this.current_qty = current_qty;
    }

}
