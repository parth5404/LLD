package config;

public class TokenBucketConfig implements Config {
    private final String tier;
    private final int maxTokens;
    private final int refillRate;

    public TokenBucketConfig(String tier, int maxTokens, int refillRate) {
        this.tier = tier;
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
    }

    @Override
    public String getTierName() {
        return tier;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public int getRefillRate() {
        return refillRate;
    }

}
