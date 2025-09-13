package models;

public class Players {
    String  name;
    int position;
    public Players(String name){
        this.name=name;
    }
    public void setPosition(int pos){
        this.position=pos;
    }
    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }
}
