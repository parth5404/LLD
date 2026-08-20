package config;

public class SlidingRWCfg implements Config {
    private final String tier;
    private final int maxHits;
    private final Long windowLenInMillis;

    public Long getWindowLenInMillis() {
        return windowLenInMillis;
    }

    public SlidingRWCfg(String tier, int maxHits, Long windowLenInMillis) {
        this.tier = tier;
        this.maxHits = maxHits;
        this.windowLenInMillis = windowLenInMillis;
    }

    @Override
    public String getTierName() {
        return tier;
    }

    public int getMaxHits() {
        return maxHits;
    }

    @Override
    public RateLimiterType getType() {
        return RateLimiterType.SLIDING_WINDOW;
    }

}
