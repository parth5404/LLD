package stratergy.payment;

import model.Ticket;

public class Cash implements PaymentStratergy{
    public boolean processPayment(Ticket ticket, double amount){
        System.out.print("Cash payment of "+ amount);
        return true;
    }
}
