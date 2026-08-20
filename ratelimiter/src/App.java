import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import config.SlidingRWCfg;
import config.SlidingRWState;
import config.TokenBucketConfig;
import config.TokenBucketState;
import repo.ConfigRepo;
import repo.InMemoState;
import repo.UserLockManager;
import service.RateLimiterSvc;

public class App {
    public static void main(String[] args) throws Exception {
        ConfigRepo configRepo = new ConfigRepo();

        InMemoState stateRepo = new InMemoState();
        UserLockManager lockManager = new UserLockManager();
        RateLimiterSvc rateLimiterService = new RateLimiterSvc(stateRepo, configRepo, lockManager);

        String tokenUser = "u-token";
        configRepo.setConfig(tokenUser, new TokenBucketConfig("FREE", 2, 1));
        stateRepo.saveState(tokenUser, new TokenBucketState(2.00, System.currentTimeMillis()));

        System.out.println("Token bucket demo");
        System.out.println("request 1: " + rateLimiterService.isAllowed(tokenUser));
        System.out.println("request 2: " + rateLimiterService.isAllowed(tokenUser));
        System.out.println("request 3: " + rateLimiterService.isAllowed(tokenUser));

        String slidingUser = "u-sliding";
        configRepo.setConfig(slidingUser, new SlidingRWCfg("PREMIUM", 2, 1000L));
        stateRepo.saveState(slidingUser, new SlidingRWState(new ArrayDeque<>()));

        System.out.println("\nSliding window demo");
        System.out.println("request 1: " + rateLimiterService.isAllowed(slidingUser));
        System.out.println("request 2: " + rateLimiterService.isAllowed(slidingUser));
        System.out.println("request 3: " + rateLimiterService.isAllowed(slidingUser));

        System.out.println("\nMissing user demo");
        System.out.println("unknown user: " + rateLimiterService.isAllowed("unknown-user"));

        String concurrentUser = "u-concurrent";
        int maxTokens = 5;
        int threadCount = 20;
        configRepo.setConfig(concurrentUser, new TokenBucketConfig("FREE", maxTokens, 0));
        stateRepo.saveState(concurrentUser, new TokenBucketState(maxTokens, System.currentTimeMillis()));

        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threadCount);
        AtomicInteger allowed = new AtomicInteger();
        AtomicInteger blocked = new AtomicInteger();
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(() -> {
                try {
                    ready.countDown();
                    start.await();
                    if (rateLimiterService.isAllowed(concurrentUser)) {
                        allowed.incrementAndGet();
                    } else {
                        blocked.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
            threads.add(thread);
            thread.start();
        }

        ready.await();
        start.countDown();
        done.await();

        System.out.println("\nConcurrent token bucket demo");
        System.out.println("threads: " + threadCount);
        System.out.println("max tokens: " + maxTokens);
        System.out.println("allowed: " + allowed.get());
        System.out.println("blocked: " + blocked.get());

    }
}
