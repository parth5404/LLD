package model;

public class DeliveryOrder extends Order {
    private String add;
    public DeliveryOrder(){
        add="";
    }

    public String getType(){
        return "Parcel";
    }
    public void setUserAddress(String addr) {
        add = addr;
    }

    public String getUserAddress() {
        return add;
    }
}
