package stratergy;

import Enums.PriceType;
import Enums.Vtype;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class EventBased implements PricingStratergy {
    Map<Vtype,Double> Paise=Map.of(
            Vtype.CAR,10.0
    );
    @Override
    public double calcFees(Vtype vtype, PriceType ptype, LocalDateTime entryTime, LocalDateTime exitType) {
        long duration= Duration.between(entryTime,exitType).getSeconds()/3600;
        return duration*Paise.getOrDefault(vtype,5.0);
    }
}
