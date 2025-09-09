package stratergy;

import Enums.PriceType;
import Enums.Vtype;

import java.time.LocalDateTime;

public interface PricingStratergy {
     double calcFees(Vtype vtype, PriceType ptype, LocalDateTime entryTime,LocalDateTime exitType);
}
