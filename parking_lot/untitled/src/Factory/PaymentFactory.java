package Factory;
import Enums.PaymentType;
import stratergy.payment.Cash;
import stratergy.payment.PaymentStratergy;
import stratergy.payment.Upi;

public class PaymentFactory {
    public static PaymentStratergy getPaymentStrategy(PaymentType type) {
        switch (type) {
            case UPI: return new Upi();
            case CASH: return new Cash();
            default: throw new IllegalArgumentException("Unknown payment type: " + type);
        }
    }
}

