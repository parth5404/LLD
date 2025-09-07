package model;

public class PickUp extends Order{
    private String resadd="";

    public void setResadd(String resadd) {
        this.resadd = resadd;
    }
    public String getType(){
        return "Pickup";
    }

}
