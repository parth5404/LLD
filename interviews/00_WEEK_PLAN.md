# LLD Machine-Coding Series — Candidate Evaluation Plan

**Candidate:** Parth Lahoti
**Target level:** SDE-1+ (bar: can own a component end-to-end with light review)
**Interviewer stance:** Strict. Working code is the floor, not the ceiling. Design quality, concurrency correctness and extensibility decide the score.
**Languages:** every round is solved twice — Java (`~/Desktop/LLD`) then Go (`~/Desktop/LLD_GO`).

---

## Why these three problems

Your repo already covers: parking lot, snake & ladder, chess, car rental, inventory, vending machine, library, doc editor, food app, rate limiter, job queue, task management, POS. Those are **entity-modelling + strategy/factory** problems. You have repeated that muscle ~13 times.

The three rounds below deliberately attack what your repo does **not** show:

| Round | Problem | Untested muscle it forces |
|---|---|---|
| 1 | Multi-Lift Elevator System | Long-running **state machine** + **scheduling policy** + worker-per-entity concurrency + request redistribution |
| 2 | In-Memory KV Store (TTL + eviction + WAL) | **Data-structure design** under invariants (O(1) LRU), background expiry, read/write lock granularity, durability/replay |
| 3 | Ride-Hailing Dispatch | **Spatial indexing**, distributed-ish race conditions (two riders, one driver), pricing/matching policy layering, idempotency |

Your `ratelimiter` (with `UserLockManager`) is the only concurrency artifact you have. Round 1 extends it, Round 2 stresses it, Round 3 breaks it.

---

## Schedule

| Date | Round | Task | Timer |
|---|---|---|---|
| **Tue 25 Aug** | **R0A** | **Prep day** + [oral defense](00A_ORAL_DEFENSE.md) — 6 questions, no editor, no AI | untimed |
| **Wed 26 Aug** | **R0B** | **[Primitives ladder](00B_PRIMITIVES_LADDER.md)** — clock seam, blocking worker, exactly-once harness, FSM, per-entity locks. Hand-written, no AI, no copying from your repo. | ~2.5 hr |
| **Thu 27 Aug** | **R0** | **Repair round** — fix 6 confirmed bugs in your own code, each with a failing-then-passing test | 90 min |
| Fri 28 Aug | R1 | Elevator — **Java** | 60 min |
| Fri 28 Aug | R1 | Review + score (me), Section H revealed | — |
| Sat 29 Aug | R1 | Elevator — **Go** | 60 min |
| Sat 29 Aug | R1 | Comparative review | — |
| Sun 30 Aug | R2 | KV Store — **Java** | **45 min** (tight, scope is smaller) |
| Mon 31 Aug | R2 | KV Store — **Go** | 45 min |
| Tue 1 Sep | R3 | Dispatch — **Java** | 90 min |
| Wed 2 Sep | R3 | Dispatch — **Go** | 90 min |
| Thu 3 Sep | Final | **Untimed take-home**: rebuild your weakest round to production grade | no limit |

**Why R0 was added:** the [baseline assessment](01_BASELINE_ASSESSMENT.md) found that your single biggest gap is not design — it is that you write tests which cannot fail (`t.Logf` where `t.Errorf` belongs), so real bugs ship green. You cannot practise honest testing on a greenfield problem where you also get to decide what "correct" means. R0 makes you assert against behaviour you did not choose.

**Why R0A was added:** you disclosed that much of the repo was built with Antigravity, and I confirmed it ([§0 of the assessment](01_BASELINE_ASSESSMENT.md)). That means my grade for your *design* vocabulary was read off code whose authorship I can't establish, so it is now marked unmeasured. R0A takes the editor away and makes you defend six decisions already sitting in your code, using reasoning that appears in none of your READMEs.

**Why R0B was added:** on 25 Aug you told me specifically that the ratelimiter's **concurrency and the `getConfigClass()` generic contract were Antigravity's**, not yours — *"mujhe itna gyan nahi"* — and that chess is the same. That resolved R0A's band table downward, and its stated consequence is this: primitives before exams. R1 carries **48 marks of auto-deduction** (busy-wait −15, assignment race −15, sleep-as-sync −10, one global lock −8) sitting on exactly the primitives that turn out not to be yours. Handing you that paper cold would produce a 35 and teach you nothing. R0B makes you build all five by hand — and break one on purpose — so the elevator becomes an integration exercise rather than a cliff.

> **On using AI at all:** it is not being scored and it is not a mark against you. It is how you will work. But it moves where the value sits: when the first draft arrives already looking finished, the scarce skills become **reading code critically** and **proving it works** — and those are precisely the five defect themes the assessment found. Disclosing this yourself was the single highest-value thing you did this week, because grading you on someone else's work would have produced advice calibrated to a person who doesn't exist.

---

## Day 0 (Tue 25 Aug) — what is fair to study

Prep time is legitimate; a real candidate revises before an interview. So here are the **topics** that Round 1 leans on. Topics only — I am not going to walk you through the elevator design, and I will not answer "is this the right class structure?" today. Learn the primitives; the design is the thing being tested.

**Scheduling / algorithms**
- The **LOOK / SCAN** disk-scheduling algorithm and why elevators use the same idea. Understand what "sweep order" means, and why serving stops in *arrival* order is wrong.
- Why a hall call (direction only, no destination) is a fundamentally different object from a car call (destination known).

**State machines**
- Modelling a finite state machine so illegal transitions are *unrepresentable* rather than checked with `if`s everywhere. Look at the difference between a `state` field guarded by validation and a state object that only exposes legal moves.

**Java concurrency** (you have used `ReentrantLock` in `ratelimiter/src/repo/UserLockManager.java` — build outward from that)
- `BlockingQueue` / `LinkedBlockingQueue`, `Condition.await()` / `signal()`, and why either beats a spin loop.
- `ReentrantReadWriteLock` — when a read lock actually buys you something.
- Graceful thread shutdown: interruption, poison pills, `ExecutorService.awaitTermination`.
- Immutable snapshot objects (`List.copyOf`, records) for handing state out of a locked region.

**Go concurrency**
- Worker goroutine + `select` over a request channel and a `done`/`context` channel.
- `sync.Cond`, and when a channel is the better answer than a mutex.
- `context.Context` cancellation for shutdown; `sync.WaitGroup` to actually wait for exit.
- `go test -race` — run it on something you already wrote (`rate_limiter`, `job_queue`) and see what it says.

**Design**
- Open/closed in practice: re-read Section G of the Round 1 paper and ask "which file would I add" for each of G1–G5. You don't have to answer it today, but knowing *that* the question is coming should shape your design tomorrow.
- Injectable clocks: why `Thread.sleep` inside a model makes it untestable, and what a `Clock` seam looks like.

**Worth reading in your own repo** — *revised 25 Aug, and two entries are now off-limits*

- ~~`ratelimiter/src/repo/UserLockManager.java`~~ — **do not open.** [P5](00B_PRIMITIVES_LADDER.md) asks you to build per-key locking from scratch. Reading a finished one is how you end up with a repo full of things you can't defend.
- ~~`ratelimiter/src/App.java:51`~~ — **do not open.** Same reason: [P3](00B_PRIMITIVES_LADDER.md) is that barrier. I originally told you to reuse it; you've since told me it isn't yours, so reusing it would carry the same problem forward into R1.
- `vending_machine/.../state/` — **still worth reading**, and it's the one design you have not disclaimed. Correct shape: transitions live inside the states. But read it against [P4](00B_PRIMITIVES_LADDER.md)'s harder constraint — no `setStatus`, no `switch` on state in the context — and note where it falls short of that.

> **Correction to an earlier note:** I originally pointed you at `LLD_GO/job_queue/main.go` as a worker-pool reference. It is a 22-line file that does not compile (`result.add undefined`). Ignore it — there is no worker-pool example in your repos.

**Do not** write elevator code today. If you show up Wednesday with a half-built solution the round is void — the timer measures cold-start design ability, and that is the whole point.

Papers are issued **one round at a time**. R2 and R3 get calibrated based on how R1 actually goes — if you clear R1 at 85+, R2 and R3 get harder constraints. That is deliberate.

---

## Rules of engagement

1. **Timer is real.** Start it, stop it, and record actual time spent at the top of your submission. Overrunning is not a crime — hiding the overrun is.
2. **Whatever exists when the timer stops is what I grade.** No polishing afterwards. If you want to keep going after the buzzer, commit first, then continue in a second commit marked `post-timer`.
3. **You may ask me clarifying questions during the round.** I will answer like a real interviewer: terse, sometimes deliberately incomplete. Asking good questions earns marks (Part A). Asking me for design hints or code costs marks.
4. **No AI assistance while the timer runs.** That includes me. I am the interviewer this week, not the pair. Outside the timer, use whatever you like — the point of the timer is to measure what is in your hands, not to have an opinion about your tooling.
5. **Self-review before I review.** After the buzzer and before I grade, you do one pass over your own code with **no AI** and hand me a list of your own bugs. I score your list against mine. That delta is the number I most want to move this week; see §6 of the assessment for why.
6. **Stdlib only.** Java: JDK + JUnit. Go: stdlib + `testing`. No Spring, no Guava, no testify.
7. **Commit per round** with message `interview: R<n> <problem> <lang>` so the diff is reviewable in isolation.

---

## Grading scale (applied identically to both languages)

| Score | Verdict |
|---|---|
| 85–100 | Strong hire, leaning SDE-2 |
| 70–84 | **Hire — SDE-1+** (the target) |
| 55–69 | Borderline. Code works, design is junior. |
| 40–54 | No hire. Requirements met, engineering absent. |
| < 40 | No hire. Incomplete or incorrect core flow. |

Every round is scored out of 100 across five parts, with **auto-deductions** listed in the paper. Auto-deductions are non-negotiable and stack — a working program can still land at 45.

---

## Round status

- [x] **Baseline assessment** → [01_BASELINE_ASSESSMENT.md](01_BASELINE_ASSESSMENT.md) *(revised 25 Aug — see §0, AI-assistance recalibration)*
- [x] **R0A Oral defense** → paper: [00A_ORAL_DEFENSE.md](00A_ORAL_DEFENSE.md) — Q1/Q2 resolved by your own disclosure; **Q3–Q6 still open, ungraded**
- [ ] **R0B Primitives ladder** → paper: [00B_PRIMITIVES_LADDER.md](00B_PRIMITIVES_LADDER.md) — **start here, tomorrow, P1 first**
- [ ] **R0 Repair round** → paper: [ROUND_0_REPAIR.md](ROUND_0_REPAIR.md)
- [ ] **R1 Elevator — Java** → paper: [ROUND_1_ELEVATOR.md](ROUND_1_ELEVATOR.md)
- [ ] R1 Elevator — Go
- [ ] R2 KV Store — Java *(paper issued after R1)*
- [ ] R2 KV Store — Go
- [ ] R3 Dispatch — Java *(paper issued after R2)*
- [ ] R3 Dispatch — Go
- [ ] Final untimed rebuild
