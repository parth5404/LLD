package config;

import java.util.Deque;
import java.util.Queue;

public class SlidingRWState implements State {
    private final Queue<Long> queue;

    public SlidingRWState(Deque<Long> dq) {
        this.queue = dq;
    }

    public Queue<Long> getQueue() {
        return queue;
    }

}