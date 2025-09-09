package Factory;

import Enums.PriceType;
import stratergy.EventBased;
import stratergy.PricingStratergy;
import stratergy.TimeBased;

public class  PricingFactory {
    public static PricingStratergy create(PriceType p){
        switch (p){
            case TIME -> {
                return new TimeBased();
            }
            case EVENT -> {
                return new EventBased();
            }
        }
        return null;
    }
}
