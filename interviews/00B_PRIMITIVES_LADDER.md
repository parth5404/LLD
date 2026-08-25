# ROUND 0B — PRIMITIVES LADDER
## Build the five things by hand, before you build anything out of them

**Date:** Wed 26 Aug 2026
**Duration:** ~2.5 hours, per-exercise timers (below). Not one continuous timer.
**Total marks:** 100
**No AI. No copying from your own repo.** Both of those matter and the second one is the harder rule.

---

## Why this round exists

You disclosed that the ratelimiter's concurrency and its generic `getConfigClass()` contract were Antigravity's, not yours, and that chess is the same. That was the right call to make and it changed the plan: see [§0 of the assessment](01_BASELINE_ASSESSMENT.md).

The consequence is specific. Round 1 (elevator) auto-deducts **−15 for busy-waiting, −15 for an assignment race, −10 for sleep-as-synchronisation in tests, −8 for one global lock**, and expects an injectable clock. That is 48 marks of exposure sitting on exactly the primitives that turn out not to be yours. Handing you that paper on Thursday would produce a 35 and teach you nothing you couldn't have guessed.

So: five primitives, each small enough to hold in your head, each one load-bearing in R1. Build them standalone, with tests, by hand. Then the elevator is an *integration* exercise instead of a cliff.

**The "no copying from your own repo" rule.** `ratelimiter/src/App.java:51` already contains a working latch barrier. P3 asks you to write one. Do not open that file. The whole point is that the primitive ends up in your hands, and reading a finished one is how you end up with a repo full of things you can't defend.

---

## P1 · The clock seam · 15 marks · 20 min
**Languages:** both

`System.currentTimeMillis()` and `time.Now()` are hardcoded in `TokenBucket.java:28`, `SlidingRW.java:28`, `CreateOrder`, `CancelOrder`, and there's a `time.Sleep(5 * time.Second)` inside `processOrder` (`order.go:121`). That is *why* no time-based behaviour has a test anywhere: you made it untestable, so you didn't test it.

**Build:**
- Java: a `Clock` interface with `long nowMillis()`, a `SystemClock`, and a `FakeClock` with `advance(long millis)`.
- Go: a `Clock` interface with `Now() time.Time`, a `systemClock`, and a `fakeClock` with `Advance(d time.Duration)`.

**Required test, both languages:** a token bucket (write a 30-line throwaway one, do not import yours) that refills 1 token/second, `maxTokens = 5`. Prove with a fake clock that:
1. 5 requests pass, the 6th fails;
2. after `advance(1 second)` exactly **one** more passes;
3. after `advance(1 hour)` the bucket holds **5**, not 3600. *(This is the cap bug the real one has no test for.)*

**Test must contain zero sleeps and run in under 10ms.** If it takes a second, you injected nothing.

**Why it's first:** everything downstream is untestable without it, and it's the cheapest of the five.

---

## P2 · A worker that blocks without spinning, and stops cleanly · 25 marks · 40 min
**Languages:** both

R1 gives each lift its own thread/goroutine. The single most common failure — and the biggest auto-deduction on the paper — is `while (true) { checkQueue(); sleep(10); }`. That burns a core, adds up to 10ms of latency for no reason, and tells me you don't know the blocking primitive.

**Build:** one worker owning a queue of int jobs. It must
- **block** when the queue is empty (consume no CPU),
- wake immediately when a job arrives,
- process strictly in FIFO order,
- shut down on demand, **finishing jobs already queued**,
- and let the caller *wait* for it to be fully stopped.

Java: `LinkedBlockingQueue` + `take()`. Shutdown via poison pill or interrupt — pick one and be able to say why. `ExecutorService.awaitTermination` for the join.
Go: `for { select { case job := <-jobs: ...; case <-done: ... } }` + `sync.WaitGroup`.

**Required tests:**
1. 100 jobs submitted, all 100 processed in order, `shutdown()` returns only after the last one.
2. **No busy-wait proof:** with an empty queue, the worker must be parked. In Go, `runtime.NumGoroutine()` stable and no CPU spin; in Java, assert the thread's state is `WAITING`, not `RUNNABLE`.
3. Submitting after shutdown is rejected, and does not panic or deadlock.
4. Go only: `go test -race` clean.

**The question I'll ask you afterwards:** your `select` has a `done` channel and a `jobs` channel, and both are ready at once. Which fires? What does that mean for "finish queued jobs on shutdown", and how do you actually guarantee drain-then-exit? *(This one catches most people. Think about it while you build.)*

---

## P3 · A concurrency test that can actually fail · 25 marks · 40 min
**Languages:** both. **Do not open `App.java`.**

This is the highest-leverage exercise of the five, because it is the fix for the theme that produced most of the assessment: `TestMakeMove_Castling` passes on broken code, `TestCancelVsPayRace` has no assertions at all.

**Build a harness** that makes N threads/goroutines collide at one instant on a shared resource, with **no sleeps**:
- Java: `CountDownLatch ready` / `start` / `done`. Every thread counts down `ready`, waits on `start`, main awaits `ready` then releases `start` — so all N are parked at the same line before any proceeds.
- Go: a `chan struct{}` closed as a broadcast start signal, plus a `WaitGroup`.

**Then use it three times:**

**(a) The exactly-once test.** A counter with 1 unit of stock and a `tryClaim()` using CAS. 50 threads race. Assert **exactly one** wins. Then deliberately break it — replace CAS with `if (count > 0) { count--; return true; }` — and **prove your test now fails.** Paste both outputs.

> This is the deliverable. A concurrency test you have never seen fail is not a test, it's a decoration.

**(b) The inverted-condition test.** Reproduce B2 in miniature: write `tryOccupy()` as `compareAndSet(false, true)`, then write the *caller* with the `!` in front, exactly as `ParkingFloor.java:24` has it. Write the test that catches it. Notice how simple the test is, and that nothing in the repo has one.

**(c) The one-in-fifty problem.** Take (a)'s broken version. Some interleavings pass by luck. Run your test 1000 times in a loop — how often does it actually fail? Record the number. Then answer: **a concurrency test that passes once has told you what, exactly?** (This is Q6 from the oral defense. Now you'll have measured it instead of reasoning about it.)

**Auto-deduction: −15** if any test in this exercise uses `sleep` to line threads up.

---

## P4 · An FSM where the illegal move doesn't compile · 20 marks · 30 min
**Languages:** one, your choice. Do the other on Thursday if there's time.

Your vending machine is the one FSM in either repo with the right shape — transitions live inside the state objects. But `Booking.setBookingStatus` (car-rental) permits `CREATED → COMPLETED` directly with no guard, and starbrew's `Preparing`/`Ready` are declared and never assigned. The difference between those is the whole exercise.

**Build** a 4-state order machine: `CREATED → PAID → PREPARING → DELIVERED`, plus `CANCELLED` reachable from `CREATED` and `PAID` only.

**The constraint that makes this worth doing:** there must be **no `setStatus(Status)` method, and no `switch (currentStatus)` in the context.** A caller must not be able to *express* `CREATED → DELIVERED`. Each state exposes only the operations legal from it; every other transition is either absent from the type or returns a rejection you can assert on.

**Required tests:**
1. The happy path walks all four states.
2. `CREATED → DELIVERED` — show me *how* it's prevented. Best answer: it doesn't compile, and you paste the compiler error as your evidence. Acceptable answer: a runtime rejection with a test asserting it.
3. Cancel from `PAID` works; cancel from `DELIVERED` is rejected.
4. **No dead states.** Every state is reachable in some test. *(starbrew has two that never are.)*

**Then answer in the writeup:** which of your two chess/vending designs was closer to this, and what specifically did the other one get wrong? This is Q5 from the oral defense arriving from a different direction — making the bad state unrepresentable rather than validated.

---

## P5 · Per-entity locking, with the leak fixed · 15 marks · 25 min
**Language:** Java. **Do not open `UserLockManager.java`.**

You now know from today's session why `computeIfAbsent` is required and why `get`-then-`put` silently gives two threads two different monitors. Build it yourself.

**Build:** a `LockManager` handing out one lock per key, such that
- operations on **different** keys proceed **concurrently** (this is the entire reason it exists),
- operations on the **same** key serialise,
- and locks for idle keys **do not accumulate forever** (D2 — the one map in a rate limiter guaranteed to grow to the size of your user base).

For the leak, pick one and defend it: reference-counted release, striping onto a fixed lock array, or `WeakReference` values. Striping is the pragmatic answer and there's a real trade-off you should be able to state.

**Required tests:**
1. Two threads on **different** keys are in their critical sections *simultaneously* — prove it with your P3 barrier, not with timing.
2. Two threads on the **same** key never overlap.
3. Whatever bound you chose actually holds: 10,000 distinct keys touched once each, then assert the map is bounded.

---

## Deliverable

`interviews/00B_PRIMITIVES/` with one subfolder per exercise, plus a `NOTES.md`:

```
Per exercise:
  Actual minutes:
  What I got wrong on the first attempt:      <-- the most valuable line in this file
  Test output (paste):
  For P3(a): the BROKEN run's failure output, and the FIXED run's pass
```

Plus: the P2 `select` question, the P4 comparison question, and P3(c)'s measured failure rate.

**"What I got wrong on the first attempt" is not optional, and an empty one costs 10 marks.** These five primitives all have a standard first-attempt mistake. Knowing which one you made is the difference between having built it and having typed it.

---

## Rubric

| Item | Marks |
|---|---|
| P1 clock seam + 3 fake-clock tests, no sleeps | 15 |
| P2 blocking worker + non-spin proof + clean drain-and-stop | 25 |
| P3 barrier + exactly-once + **demonstrated failure on broken code** | 25 |
| P4 FSM with the illegal transition unrepresentable | 20 |
| P5 per-key locks + concurrency proof + bounded growth | 15 |

### Auto-deductions

| Deduction | Trigger |
|---|---|
| **−25** | P3's exactly-once test never demonstrated failing against the broken implementation |
| **−15** | `sleep` used to synchronise anywhere in this round |
| **−15** | Any exercise copied from your existing repos |
| **−10** | Empty "what I got wrong" lines |
| **−10** | `go test -race` not clean |
| **−10** | P4 has a `setStatus` setter or a `switch` on state in the context |
| **−5** | A test that takes over a second (means real time leaked in) |

### Instant fail
- Any of it produced with AI assistance. This round's entire value is that these five things end up in your hands. There is no other reason to do it.

---

## What this buys you on Friday

| Primitive | R1 clause it defuses |
|---|---|
| P1 | injectable time; "tests must not sleep" (NFR5) |
| P2 | thread-per-car; **−15 busy-wait**; clean shutdown, `-race` clean (NFR2, NFR7) |
| P3 | Part E test 4, `concurrent_hall_calls_are_each_assigned_exactly_once`; **−15 assignment race** |
| P4 | FR3 car state machine; **−10 doors-open movement** |
| P5 | per-car lock granularity; **−8 one global lock** |

That's 48 marks of auto-deduction, converted into five things you'll have already built and broken once.

**Start with P1. It's 20 minutes and everything else leans on it.**
