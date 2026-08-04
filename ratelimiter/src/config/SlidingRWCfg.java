package config;

public class SlidingRWCfg implements Config {
    private final String tier;
    private final int maxHits;
    private final Long windowLenInSec ;

    public SlidingRWCfg(String tier, int maxHits, Long windowLenInSec) {
        this.tier = tier;
        this.maxHits = maxHits;
        this.windowLenInSec = windowLenInSec;
    }

    @Override
    public String getTierName() {
        return tier;
    }

    public int getMaxHits() {
        return maxHits;
    }

    public Long getWindowLenInSec() {
        return windowLenInSec;
    }

}
