package models;

public class Dice {
    private int dice;

    public Dice(int dice){
        this.dice=dice;
    }
    public int roll() {
        int sum = 0;
        for (int i = 0; i < dice; i++) {
            sum += (int)(Math.random() * 6) + 1;
        }
        return sum;
    }
}
