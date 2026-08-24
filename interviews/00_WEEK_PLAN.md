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

| Day | Round | Task | Timer |
|---|---|---|---|
| 1 | R1 | Elevator — **Java** | 60 min |
| 1 | R1 | Review + score (me) | — |
| 2 | R1 | Elevator — **Go** | 60 min |
| 2 | R1 | Comparative review | — |
| 3 | R2 | KV Store — **Java** | **45 min** (tight, scope is smaller) |
| 4 | R2 | KV Store — **Go** | 45 min |
| 5 | R3 | Dispatch — **Java** | 90 min |
| 6 | R3 | Dispatch — **Go** | 90 min |
| 7 | Final | **Untimed take-home**: rebuild your weakest round to production grade | no limit |

Papers are issued **one round at a time**. R2 and R3 get calibrated based on how R1 actually goes — if you clear R1 at 85+, R2 and R3 get harder constraints. That is deliberate.

---

## Rules of engagement

1. **Timer is real.** Start it, stop it, and record actual time spent at the top of your submission. Overrunning is not a crime — hiding the overrun is.
2. **Whatever exists when the timer stops is what I grade.** No polishing afterwards. If you want to keep going after the buzzer, commit first, then continue in a second commit marked `post-timer`.
3. **You may ask me clarifying questions during the round.** I will answer like a real interviewer: terse, sometimes deliberately incomplete. Asking good questions earns marks (Part A). Asking me for design hints or code costs marks.
4. **No AI assistance while the timer runs.** That includes me. I am the interviewer this week, not the pair.
5. **Stdlib only.** Java: JDK + JUnit. Go: stdlib + `testing`. No Spring, no Guava, no testify.
6. **Commit per round** with message `interview: R<n> <problem> <lang>` so the diff is reviewable in isolation.

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

- [ ] **R1 Elevator — Java** → paper: [ROUND_1_ELEVATOR.md](ROUND_1_ELEVATOR.md)
- [ ] R1 Elevator — Go
- [ ] R2 KV Store — Java *(paper issued after R1)*
- [ ] R2 KV Store — Go
- [ ] R3 Dispatch — Java *(paper issued after R2)*
- [ ] R3 Dispatch — Go
- [ ] Final untimed rebuild
