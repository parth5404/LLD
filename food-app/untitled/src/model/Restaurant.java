package model;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private static int nextid;
    private int id;
    private String name;
    private String loc;
    private List<MenuItem>menu=new ArrayList<>();


    Restaurant(String name,String loc,MenuItem item){
        this.id=++nextid;
        this.name="";
        this.loc="";
    }
    public void addItem(MenuItem item){
        menu.add(item);
    }
    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getLoc(){
        return loc;
    }
}
