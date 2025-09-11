package models;

public class Player {
    private final String name;
    private int pos=0;

    public Player(String name) {
        this.name = name;
    }


    public int getPosition(){
        return pos;
    }
    public void setpos(int position){
        pos=position;
    }
    public String getName(){
        return this.name;
    }
}
