# ROUND 0A — ORAL DEFENSE
## Six questions. No editor, no compiler, no AI, no timer.

**Date:** Tue 25 Aug 2026 (today, your prep day)
**Duration:** untimed — but answer each one *before* you look anything up
**Total marks:** 60
**Format:** you type your answers to me in chat, or into `interviews/00A_ANSWERS.md`. Either is fine.

---

## Why this round exists

You told me a lot of the repo was built with Antigravity. I went and confirmed it — see [§0 of the assessment](01_BASELINE_ASSESSMENT.md). The consequence is specific: I graded your design vocabulary as "at or above the SDE-1+ bar" by reading pattern placement in your source, and if the patterns were suggested to you, then I graded the model, not you. So that verdict is now marked **unmeasured**, and everything about how this week runs depends on which way it resolves.

Reading more code cannot resolve it. Only this can.

**Ground rules — these are the entire point:**

1. **No editor, no compiler, no AI, no search.** Not even opening the file. If you cannot answer from memory and reasoning, the honest answer is "I don't know", and *that answer scores more than a correct one you looked up* — because a looked-up answer tells me nothing and costs me the measurement.
2. **Say "I don't know" freely.** I mean it. There is no penalty in this round for not knowing. There is a penalty for a confident answer that turns out to be recited.
3. **Every question is about a decision already in your code**, and none of the six answers appear in any of the six READMEs. I checked. You cannot pattern-match your way through this.
4. **Three-to-six sentences each.** I am not looking for essays, I am looking for whether the reasoning is there.

---

## Q1 · The generic contract · 10 marks
**Code:** `ratelimiter/src/rate_limiters/RateLimiter.java`, `service/RateLimiterSvc.java:58`

Your limiter interface is `RateLimiter<C extends Config, S extends State>`, and it additionally requires every implementation to hand back `getConfigClass()` and `getStateClass()`, which the service then uses for an `isInstance` check before casting.

That looks redundant. You already have generics — generics are supposed to be the type safety.

**Answer:** why are the `Class` tokens necessary *in addition to* the type parameters? Name the specific language mechanism that makes `<C extends Config>` insufficient at the exact point in `isAllowed` where you pull a `Config` out of the repo, and describe the failure you would get without the tokens.

---

## Q2 · What the lock actually protects · 10 marks
**Code:** `ratelimiter/src/repo/UserLockManager.java:10`

```java
private final Map<String, Object> locks = new ConcurrentHashMap<>();
public Object getLock(String userId) { return locks.computeIfAbsent(userId, id -> new Object()); }
```

Two parts.

**(a)** Two threads call `getLock("u1")` at the same instant and neither lock existed yet. Why are they guaranteed to receive **the same** `Object`? Answer at the level of what `computeIfAbsent` promises — "because ConcurrentHashMap is thread-safe" is not an answer, `get`-then-`put` would also be on a thread-safe map and would still be broken.

**(b)** Now suppose I want configs hot-reloadable: an admin thread calls `configRepo.setConfig(userId, newConfig)` while requests for that same user are mid-flight in `isAllowed`. Does your per-user lock protect that? Say exactly what it does and does not cover, and what the observable bad outcome is.

---

## Q3 · Reasoning from the invariant · 10 marks
**Code:** `service/RateLimiterSvc.java`, `repo/InMemoState.java`

Your README already says `isAllowed` never calls `saveState()` and that it works "accidentally, by object reference". Fine — you have that.

So here is the part the README does not have. **Name the single change someone could make inside `InMemoState`, without touching `RateLimiterSvc` at all, that is entirely legal under the `StateRepo` interface contract and instantly turns your rate limiter into a no-op that allows every request forever.** One line. Then say what that tells you about what a Repository interface is actually promising its callers.

---

## Q4 · The guarantee that isn't · 10 marks
**Code:** `vending_machine/.../machine/VendingMachine.java:29-32`, `state/PaymentState.java`

Your State pattern is the best FSM in either repo — transitions live inside the state objects, each state rejects the operations that don't apply to it. That is the correct shape and I said so.

But `cart`, `machineWallet` and `tempBuffer` are `public`.

**Answer:** give me one concrete sequence of calls where a public `cart` breaks a guarantee that the State pattern was specifically supposed to enforce. Walk the actual values through `PaymentState.completePayment`. Then finish the sentence: "an FSM that guards its methods but not its data is really only enforcing ______."

---

## Q5 · Making the bug unrepresentable · 10 marks
**Code:** `LLD_GO/chess_LLD/game/game.go:98`, `moves/castling.go:22`

The caller builds `mod_piece` with one element. The validator opens with `if len(mod_piece) != 2 { return false }`. Castling therefore has never executed, and a green test sat on top of it for months.

**Do not tell me the fix.** Appending the king is obvious and it is not what I am asking.

**Answer:** what change to the *type* would have made this mismatch a **compile error** instead of a silent runtime `false`? Be concrete about the signature you would write. Then state the general principle in one sentence — because it is the same principle Round 1's car state machine is graded on, and I would rather you arrive on Thursday already holding it.

---

## Q6 · When is a concurrency test actually a test · 10 marks
**Code:** `ratelimiter/src/App.java:51`, and `LLD_GO/starbrew-pos/order_service_test.go:65`

`App.java` fires 20 threads through a `ready`/`start`/`done` `CountDownLatch` barrier and asserts exactly 5 are allowed. That is a real harness and it is the best single artifact in either repo.

Now: suppose the limiter had a check-then-act race that let 6 through **roughly one run in fifty**, and your assertion had been "at least 5" rather than "exactly 5".

**Answer, three parts.** (a) Would that harness ever have caught it, and why not? (b) A concurrency test passing once tells you almost nothing — say precisely why, in terms of what a single execution is a sample of. (c) Name two different ways to make such a failure *reproducible* rather than occasional. One of them should not involve running the test more times.

---

## Scoring, and what each band changes

| Band | Reading | What happens to the week |
|---|---|---|
| **45–60** | The design judgement is yours. Antigravity was a typist, not the architect. | §2's verdict is restored and the plan runs exactly as written. R1 Thursday. |
| **30–44** | Partly yours — you can follow the reasoning but did not originate all of it. | Plan runs, but R0 grows a seventh task and I stop giving you credit for patterns you can't defend. |
| **15–29** | The vocabulary is largely borrowed. | The week restructures: fewer graded exams, more building the primitives from scratch — you would write a `Clock` seam, a bounded worker, an FSM by hand before touching the elevator. |
| **< 15** | | Same as above, and we start from the bottom. That is a fine place to start from; it is a bad place to be surprised by in a real interview. |

A high score here plus a mid score in R1 tells me one thing (you know the design, execution needs reps). A low score here plus a mid score in R1 tells me something completely different (you can produce working code without a model of why it works). Those two candidates need opposite advice, which is exactly why I am not guessing.

---

## One thing worth saying plainly

Using AI to build is not cheating and I am not scoring you on it. It is how you will work for the rest of your career and pretending otherwise would be useless preparation.

But it moves where the value sits. When a first draft arrives already looking finished, the scarce skills become **reading code critically** and **proving it works** — and those are exactly the two things the assessment found missing: five defect themes, every one of them invisible on the page and obvious the moment something executes. Nobody ever ran this code. That is the actual finding of the week, and it has nothing to do with who typed it.

**Answer the six. Then we know what Thursday looks like.**
