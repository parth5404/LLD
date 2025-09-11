package service;

import Stratergy.Payment.Card;
import Stratergy.Payment.Payment;
import enums.BookingStatus;
import enums.PaymentStatus;
import enums.PaymentType;
import models.Booking;

public class PaymentProcessor {
    private Payment paystrat;
    public PaymentProcessor(){
    }
    public void setPaystrat(Payment paystrat){
        this.paystrat=paystrat;
    }
    public boolean pay(Booking booking){
       PaymentType type= booking.getPaymentType();
       if(type==PaymentType.CARD){
           setPaystrat(new Card());
       }
       if(paystrat.pay(booking)){
           System.out.println("Payment Confirmed");
           booking.setPaystatus(PaymentStatus.DONE);
           return true;
       }
       booking.setPaystatus(PaymentStatus.FAILED);
       System.out.println("Payment Declined");
       return false;
    }

}
