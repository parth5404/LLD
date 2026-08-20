import config.SlidingRWCfg;
import config.TokenBucketConfig;
import config.TokenBucketState;
import rate_limiters.SlidingRW;
import rate_limiters.TokenBucket;
import repo.ConfigRepo;
import repo.InMemoState;
import service.RateLimiterSvc;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        ConfigRepo configRepo = new ConfigRepo();
        configRepo.setConfig("u123", new TokenBucketConfig("FREE", 10, 1));
        configRepo.setConfig("PREMIUM", new SlidingRWCfg("PREMIUM", 1000, 60l)); // Sliding window for premium

        InMemoState stateRepo = new InMemoState();
        RateLimiterSvc rateLimiterService = new RateLimiterSvc(stateRepo, configRepo);
        stateRepo.saveState("u123", new TokenBucketState(10.00, System.currentTimeMillis()));
        boolean result = rateLimiterService.isAllowed("u123");
        System.out.println(result);

    }
}
