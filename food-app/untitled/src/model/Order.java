package model;

import java.util.ArrayList;
import java.util.Collection;

import stratergy.payment;

public abstract class Order {
    protected int id;
    protected User user;
    protected Cart cart;
    protected Restaurant res;
    protected Collection<MenuItem>menu=new ArrayList<>();
    protected payment payment;
    protected String type;
    protected String time;

    public abstract String getType();
    public void setUser(User user){
        this.user=user;
    }
    public void setMenu(){
        menu=user.getCartItemsReadOnly();
    }
    public void setRes(Restaurant res){
        this.res=res;
    }
    public void settime(String xyz){
        this.time=xyz;
    }
    public void setPayment(payment type){
        this.payment=type;
    }

}
