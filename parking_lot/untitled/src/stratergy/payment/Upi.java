package stratergy.payment;

import model.Ticket;

public class Upi implements PaymentStratergy{
    public boolean processPayment(Ticket ticket, double amount){
        System.out.print("Upi payment of "+ amount);
        return true;
    }
}
