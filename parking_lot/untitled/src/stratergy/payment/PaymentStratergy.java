package stratergy.payment;

import model.Ticket;

public interface PaymentStratergy {
    boolean processPayment(Ticket ticket,double amount);
}
