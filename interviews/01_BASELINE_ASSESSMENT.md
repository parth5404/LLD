# Baseline Assessment — Parth Lahoti

**Date:** 25 Aug 2026 · *revised the same day, see §0*
**Scope:** `~/Desktop/LLD` (Java) + `~/Desktop/LLD_GO` (Go), all projects
**Method:** full read of every source file; `go build` / `go test -race` on every Go module; `javac -Xlint:all` on the Java projects that have compilable sources. Every bug below was confirmed by compiling or running, not inferred from reading alone.

---

## 0. Recalibration — the AI-assistance disclosure

You told me a lot of this was built with Antigravity's help and that the READMEs would make it obvious. You were right, though not in the way you said: **no README anywhere in either repo contains the words "Antigravity", "AI", "generated", or any attribution line.** I grepped both repos for all of it. What gives it away is the *voice*.

Six substantive docs exist. All six are written **to you, about your code, in second person**:

| Doc | Tell |
|---|---|
| `LLD_GO/architecture_and_plan.md` + `chess_LLD/architecture_and_plan.md` | **Byte-identical files.** "Here is a Mermaid class diagram representing the architecture you have built so far", "Abhi tak aapne board setup... likh liya hai", "**Recommendation on what to tackle first:**" |
| `LLD_GO/chess_LLD/README.md` | "Yahan **aapke** code ka structure hai", "**Aapne** LLD ke sabse important design patterns implement kiye hain", a scripted `Interviewer:` / `Aap:` dialogue, and a "Self-Evaluation & Improvement Path (SDE Level)" section that grades you |
| `LLD_GO/starbrew-pos/README.md` | Switches to "**humne** ... kiya hai" for the concurrency section and titles it "(Staff Engineer Level)" |
| `LLD/ratelimiter/README.md` | "Ye dikhata hai ki **tumhe** concurrency ka practical knowledge hai", signs off *"README written with love in Hinglish"* |
| `LLD/vending_machine/.../README.md` | Third-person and clean, but same Mermaid-plus-emoji house style |

(`LLD/concurrency/README.md` is the stock VS Code Java template. Not relevant.)

So: confirmed, and it matters. Here is what it changes and what it does not.

### What it does NOT change

**Every bug in §4 is still in the code.** Whoever typed the first draft, you committed it, you own the repo, and none of it was caught. B2's inverted CAS, B3's dead castling, B5's `go vet` failure that has kept 218 lines of tests from ever executing — those are facts about the artifact, and the artifact is what a reviewer sees. §4 stands in full.

**Theme 2 gets stronger, not weaker.** "Tests that cannot fail" and "primitive picked right, wired wrong" are *precisely* the defect class that survives AI-assisted development, because both look correct on the page and only fail when executed. Nobody ran this code. That finding is now better supported than when I wrote it.

**And the docs themselves demonstrate it.** `chess_LLD/README.md` diagrams `Castling` as a working `Move` strategy and lists `GetMoveCnt()` in the `Piece` interface — while castling has never once executed and nothing increments that counter. Its "🚨 Kya Galat Hua" section lists three bugs, all of them ones that were *fixed during the session*, and misses both live ones. The doc is an accurate record of a debugging conversation and an inaccurate record of the program. Same shape in `starbrew-pos/README.md`: its claim that every observer takes `event.mu.RLock()` before reading order state is **true** (`observer.go:59-73`), and it says nothing about `Publisher.observers` being appended to and ranged over with no lock at all (`observer.go:121-140`), or about the COD path panicking on a discarded error.

### What it DOES change

**§2's headline design verdict is withdrawn as unverified.** I wrote "design vocabulary: strong, at or above the SDE-1+ bar" by inferring from pattern placement in the source. If the patterns were Antigravity's suggestions, I measured Antigravity. I am not reversing the claim — I have no evidence it is false — I am marking it **unmeasured**, which is a different and more honest state.

**§3's strengths list is provisional.** Items 1, 2, 4, 5 and 7 in particular. The generic `RateLimiter<C, S>` contract, the `EnumMap` registry, the `CountDownLatch` barrier, `TestDoubleSpendPrevention` — all still good work, all now of unknown authorship.

**§4's ratelimiter design items partly duplicate a doc you already have.** `ratelimiter/README.md` was rewritten yesterday (commit `85f3386`, 24 Aug) and its "❌ KYA GALAT HAI" section already names D1 (`saveState()` never called, its item ❌7), D2 (lock-map leak, ❌8) and D4 (`public final` maps, ❌3), with fixes. I found those independently by reading the source, but I should be straight with you: on that project I was not surfacing blind spots. You had the list. It is one day old — that is not long enough to call it ignored, and I am not calling it that.

### The measurement this forces

There is now exactly one thing I need to know that reading more code cannot tell me: **is the design vocabulary yours?** The only way to find out is to take the editor away and make you defend decisions in the code with reasoning that no document in either repo contains. That is [00A_ORAL_DEFENSE.md](00A_ORAL_DEFENSE.md), and it happens today.

### Resolved, same day — by your own disclosure

Before sitting the oral defense you told me directly: the ratelimiter was **extensively** Antigravity's; your inputs were in it, but **the concurrency and the `getConfigClass()`/`getStateClass()` contract were written by the model** — *"mujhe itna gyan nahi"* — and chess is the same.

That answers Q1 and Q2 without needing to run them, and it is worth recording that **you volunteered it unprompted.** Candidates hide this. Disclosing it is what stopped a week of advice from being calibrated to a person who doesn't exist, and it is the single highest-value thing in this assessment.

**What comes off §3's strengths list:** items 1 (generic contract), 2 (`EnumMap` registry), 3 (`computeIfAbsent` per-key locking), 4 (the `CountDownLatch` barrier) and 7 (chess `moveRules` map). Item 4 in particular was my *reference* for how R1's concurrency test should be written — so that reference is withdrawn and P3 now asks you to build one from scratch.

**What remains genuinely unclaimed:** `starbrew-pos` (item 5, `TestDoubleSpendPrevention`) and the vending machine State pattern (item 6). You have not disclaimed either, and starbrew's README reads like a collaboration rather than a handover. Those two are the current floor of what I can credit you with.

**What this triggers:** R0A's band table lands at the bottom — *"the week restructures: fewer graded exams, more building the primitives from scratch."* That is now [00B_PRIMITIVES_LADDER.md](00B_PRIMITIVES_LADDER.md) on Wed 26 Aug, and R1 moves to Friday. Rationale: R1 carries 48 marks of auto-deduction resting entirely on primitives that are not yours.

**Q3–Q6 remain open and ungraded.** They test reasoning, not knowledge, and nothing you've told me suggests you lack it.

### On using AI at all

Not a mark against you, and I am not going to moralise about it. Using AI to build is normal and it is how you will work. But it changes what is worth practising: the scarce skill stops being *typing the pattern* and becomes **reading code critically and proving it works** — which is the one thing that stays yours regardless of who wrote the draft. That is what this series is now built to train.

---

## 1. Inventory reality check

The repos *look* like 22 projects. Only 11 contain real work.

**Java — real:**

| Project | Lines | Notes |
|---|---|---|
| chess_lld | 656 | Strategy per move type, abstract `Piece` |
| vending_machine | 619 | **State pattern — the best FSM in either repo** |
| parking_lot | 416 | Strategy + Factory, `AtomicBoolean` spots |
| inventory_management_system | 326 | Singleton + Observer + Strategy, Lombok |
| car-rental-lld | 315 | Strategy for payment |
| snake-laddar + snak-lad-prac | 279 | Two attempts at the same problem |
| food-app | 269 | Cart/Order/Restaurant |
| ratelimiter | ~300 | **Latest and strongest Java work** |
| observer_dp | 90 | Pattern drill |

**Java — empty shells:** `Doc_editor`, `Stratergy`, `LSP`, `LSP-Rules`, `DIP`, `ISP`, `OCP` (0 files each), `library-lld` and `car-rental-2` (4-line `Main`).

**Go — real:** `chess_LLD` (~1500 with tests), `starbrew-pos` (~700 with tests), `car_rental_LLD` (~280), `inventory_LLD` (~250).

**Go — not real:**
- `job_queue/main.go` — **does not compile.** `result.add(data*2)` on a `*[]int`. `wg.Done()` and `wg.Wait()` are never called.
- `task_management_system/main.go` — `fmt.Println("SHer")`. That is the whole file.
- `rate_limiter/main.go` — `fmt.Println("HI")`. That is the whole file.

**Zero tests exist anywhere in the Java repo.** All test code in both repos lives in `LLD_GO/chess_LLD` and `LLD_GO/starbrew-pos`.

---

## 2. Where you actually stand

**Design vocabulary: present in the code and correctly placed — authorship unverified.** *(Revised per §0.)* Strategy, Factory, Observer, Decorator, State, Registry, Repository are all there, all applied sensibly, none over-applied. What I cannot tell from the source is whether the judgement behind them is yours or Antigravity's, and that distinction is the whole difference between an SDE-1+ hire and a no-hire. Resolved by [00A_ORAL_DEFENSE.md](00A_ORAL_DEFENSE.md), not by more reading.

**Design correctness under concurrency: below the bar.** This is the headline. You consistently pick the *right primitive* and then wire it *wrong*. That is a more dangerous failure mode than not knowing the primitive, because the code looks defended.

**Verification discipline: below the bar, and it is the root cause of most of §4.** Every confirmed bug in §4 would have been caught by one honest test. Several of them sit directly underneath a test that was written and cannot fail.

If Round 1 ran today against its own rubric, my honest prediction is **50–62** — a working elevator that collects auto-deductions for clock injection, non-asserting tests, and at least one check-then-act race. Not a no-hire. Not yet the hire band either.

---

## 3. Genuine strengths (keep these)

> **Revised 25 Aug.** Items **1, 2, 3, 4 and 7 are withdrawn** — you disclosed that the ratelimiter's concurrency and generic contract, and the chess design, were Antigravity's (see §0). They are still good code in your repo; they are no longer evidence about you. **Items 5 and 6 stand** as the current floor of what I can credit you with.

1. **`ratelimiter` generic strategy contract.** `RateLimiter<C extends Config, S extends State>` with `getConfigClass()` / `getStateClass()` for a checked downcast ([RateLimiterSvc.java:58](../ratelimiter/src/service/RateLimiterSvc.java:58)) is a real solution to the heterogeneous-config problem, not a `Map<String,Object>` dodge. Above SDE-1 baseline.
2. **Self-registering limiter registry.** `EnumMap` keyed off `limiter.getType()` ([RateLimiterSvc.java:54](../ratelimiter/src/service/RateLimiterSvc.java:54)) — adding a limiter touches one line. This is exactly the open/closed shape Round 1's Section G asks for.
3. **Per-key locking done correctly.** `locks.computeIfAbsent(userId, id -> new Object())` ([UserLockManager.java:10](../ratelimiter/src/repo/UserLockManager.java:10)) is the right idiom, correctly used.
4. **The `CountDownLatch` barrier in [App.java:51](../ratelimiter/src/App.java:51).** `ready` / `start` / `done` to make 20 threads collide at one instant is a *proper* concurrency harness. You already know how to test concurrency without `sleep`. You just haven't applied it anywhere else.
5. **`TestDoubleSpendPrevention`** (`LLD_GO/starbrew-pos/order_service_test.go:17`) — 5 goroutines, asserts *exactly one* success. This is the single best artifact in either repo, and `go test -race` passes it.
6. **Vending machine State pattern.** Transitions live inside the state objects, illegal operations are rejected per-state. Correct shape.
7. **Data-driven piece rules in Go chess.** `moveRules map[string][]models.MoveType` injected by the factory (`chess_LLD/pieces/factory.go:16`) avoids duplicating move logic per piece.

---

## 4. Confirmed bugs

All verified by build or run.

### Blocking / correctness

**B1 — `job_queue` does not compile.** `LLD_GO/job_queue/main.go:8`. `go build` output: `result.add undefined (type *[]int has no field or method add)`.

**B2 — Parking lot hands out occupied spots and refuses free ones.** `parking_lot/untitled/src/model/ParkingFloor.java:24`:
```java
if(spots.getAllowed()==type && !spots.tryOccupy()){ return spots; }
```
`tryOccupy()` is `compareAndSet(false,true)` — it returns `true` on success. So the condition returns a spot only when occupying **failed**, i.e. the spot is already taken. Worse: every free spot gets CAS-flipped to occupied and then skipped. The `AtomicBoolean` instinct was right; the `!` inverted it.

**B3 — Castling is unreachable, and a passing test hides it.** `LLD_GO/chess_LLD`. `game.go:98` builds the piece list with the rook only:
```go
var pc []*models.Piece
pc = append(pc, &rookPiece)   // len == 1
```
`moves/castling.go:22` requires `len(mod_piece) == 2`. So `CanMove` always returns false. Verified:
```
=== RUN   TestMakeMove_Castling
    game_test.go:216: Castling failed, possibly due to strict castling logic in strategy.
--- PASS: TestMakeMove_Castling
```
The test used `t.Logf` instead of `t.Errorf`, so a broken feature ships green.

**B4 — Go chess move counts never increment.** `models.Piece` (`models/piece.go:38`) exposes `GetMoveCnt()` with no incrementer, and nothing calls one. Consequence: castling's `GetMoveCnt() != 0` guard and the pawn double-step guard (`moves/pawn_move.go:41`) are permanently satisfied — **a pawn can double-step on every move, forever**. Note the Java chess *does* have `incrementMoves()` ([Piece.java:25](../chess_lld/src/models/pieces/Piece.java:25)); the Go port dropped it.

**B5 — Go chess tests cannot run at all.** `go test ./...` fails before executing:
```
game/game.go:296:2: fmt.Println arg list ends with redundant newline
FAIL lld/chess/game [build failed]
```
`go vet` runs as part of `go test`. 218 lines of tests have never executed in this state. They pass with `-vet=off`.

**B6 — `RemoveObserver` removes the wrong element, or panics.** `LLD_GO/inventory_LLD/models/inventory.go:45`:
```go
i.observerList = append(i.observerList[:j-1], i.observerList[j-1+1:]...)
```
Should be `[:j]` and `[j+1:]`. At `j == 0` this is `[:-1]` → runtime panic. At `j > 0` it deletes index `j-1` instead of `j`.

**B7 — Paying for a rental releases the car back to the pool.** `LLD_GO/car_rental_LLD/models/reservation_manager.go:56`:
```go
reservation.status = ReservationStatus(PAID)
reservation.vehicle.SetAvailability(true)   // now bookable by someone else
```
Should stay unavailable until returned.

**B8 — Unknown payment type panics.** `LLD_GO/starbrew-pos/order.go:88`:
```go
start, _ := os.paymentFactory.GetPaymentInterface(payType)
start.pay(order)
```
The error is discarded. `GetPaymentInterface` returns `(nil, err)` for anything outside UPI/WALLET/CREDIT_CARD — including `"COD"`, which `order.go:92` explicitly special-cases. Nil-interface call → panic.

**B9 — Snake & Ladder board is never allocated.** `snake-laddar/src/models/Board.java:11` sets only `n`; `Cell[][] cell` stays null, then `InitializeCell` writes `cell[i][j]` → guaranteed NPE.

**B10 — NPE-ordered null checks in the vending machine.** `SelectionState.java:9` calls `machine.canAddtoCart(row,col,qty)` — which dereferences `shelf[row][col]` — *before* the `!= null` check on the same slot. Same inversion at `CheckoutState.java:12` vs `:26`. Also `row`/`col` are only checked `>= 0`, never against `MAX_ROWS`/`MAX_COLS`.

**B11 — Vending machine keeps the change.** `PaymentState.java:42` prints `"Change to return: $X"` then transfers the *entire* `tempBuffer` into `machineWallet` (`:47`) without deducting the change. The comment says `// Naive change dispensing: simply acknowledging it for now.` The wallet gains money it should have paid out.

**B12 — `CheckoutState.select` returns success after doing nothing.** `CheckoutState.java:15-24`: if `qty > current_qty` and `qty > 0`, neither branch fires, nothing is printed, and it still `return true`.

### Design-level

**D1 — The `ratelimiter` Repository abstraction does not hold.** `RateLimiterSvc.isAllowed` reads state and mutates it through setters, but **never calls `stateRepo.saveState(...)`**. It works only because `InMemoState` hands back the same mutable reference. Swap in Redis or a DB and every token decrement silently vanishes. Read-modify-write with no write-back.

**D2 — `UserLockManager` leaks unboundedly.** One `Object` per userId, never evicted. For a rate limiter this is the one map guaranteed to grow to the size of your user base. No striping, no eviction, no weak refs.

**D3 — No injectable clock anywhere.** `System.currentTimeMillis()` inline in both limiters (`TokenBucket.java:28`, `SlidingRW.java:28`); `time.Now()` inline in `CreateOrder`/`CancelOrder`; a hardcoded `time.Sleep(5 * time.Second)` inside `processOrder` (`order.go:121`). This is *why* there is no test for any time-based behaviour — you made it untestable, so you didn't test it. `currentTimeMillis` is also wall-clock and can move backwards.

**D4 — Public mutable state.** `public Map<Item,Integer> cart`, `public CashInventory machineWallet`, `public CashInventory tempBuffer` (`VendingMachine.java:29-32`); `public final Map` in both `ConfigRepo` and `InMemoState`; `SlidingRWState.getQueue()` hands out the live `Queue` so `SlidingRW` does `getQueue().poll()` on it. Tell-don't-ask violated throughout — the state objects are anaemic bags and the logic reaches in.

**D5 — Unprotected shared mutable state next to correct locking.** `starbrew` locks `Order` properly but leaves `Publisher.observers` unguarded (`observer.go:123`) across `AddObserver`/`NotifyAll`. `inventory_LLD` uses a mutex for singleton creation only — `AddWarehouse`, `RegisterObserver`, `observerList` are all unprotected. `car_rental_LLD.BookReservation` does check-then-act on `v.IsAvailable()` → `SetAvailability(false)` with **no lock at all**, plus a non-atomic package-global `res_id++` → duplicate IDs under concurrency.

**D6 — Broken double-checked locking, ported from a broken Java idiom.** `inventory_LLD/models/inventory.go:18`: the outer `if instance == nil` read is unsynchronised. This is a data race under Go's memory model. `sync.Once` is the answer. The Java twin (`InventoryM.getInstance`) is `synchronized` so it is safe, but it is a **parameterized singleton** — the second caller's `IReplenishment` is silently discarded.

**D7 — Tests that cannot fail.** Beyond B3: `TestCancelVsPayRace` (`order_service_test.go:65`) has no assertions at all — only `t.Logf`. It reports the outcome of a genuine race and calls it a pass either way. Combined with zero tests in the entire Java repo, this is the weakest area in the assessment.

**D8 — State machines as a mutable enum plus a setter.** `Booking.setBookingStatus` (`car-rental-lld`) permits `CREATED → COMPLETED` directly, no guards. `starbrew`'s `Preparing` and `Ready` are declared and never assigned — dead states. `CancelOrder` (`order.go:104`) checks `Completed` but not `PaidP`, so a paid order can be cancelled with no refund path while `processOrder` silently returns. The vending machine is the one place you did this properly.

**D9 — Copy-paste dressed as Strategy.** `UPI`, `Wallet`, `CreditCard` (`payment.go:43-62`) have byte-identical bodies, and the caller discards the return value (`order.go:89`), so payment can never fail. Also two competing factory styles in one 700-line package: a hardcoded switch for payments, a function registry for addons. You learned the better idiom and didn't go back.

**D10 — Singletons force test-reset hacks.** `chess_LLD` uses a package-level `var ins *Game`, so `game_test.go:11` opens with `ins = nil`. When a test has to reach into package globals to get a clean fixture, the design is telling you something.

**D11 — `Piece.move()` in Java chess is private, unused, and returns `null` from an `Optional` method** ([Piece.java:34](../chess_lld/src/models/pieces/Piece.java:34)). Dead code that would NPE the moment it were wired up.

---

## 5. The pattern behind the bugs

Four themes account for nearly everything above.

1. **You know the primitive, then mis-wire it.** CAS inverted (B2), DCL without synchronisation (D6), mutex scoped to the wrong thing (D5), `RWMutex` used correctly on `Order` and forgotten on `Publisher`. This is not a knowledge gap — it is a verification gap.
2. **You write tests that cannot fail.** When behaviour is ambiguous or broken, the assertion softens to `t.Logf` (B3, D7). A test that passes on both branches is worse than no test, because it buys false confidence.
3. **You leave data public and let logic reach in.** (D4) Every state class in the vending machine mutates `machine.cart` directly. Every rate-limiter strategy mutates the state object's internals. Encapsulation is the cheapest defence against 1, and you are not using it.
4. **You hardcode time, which makes correctness untestable.** (D3) The one thing that would have caught the token-bucket and window-expiry edge cases is an injectable clock, and there isn't one anywhere.
5. **Nothing here was ever executed against an assertion.** *(Added per §0.)* Zero tests in the Java repo. In Go, the two suites that exist are one module that cannot build under `go vet` (B5) and one with a test that has no assertions (D7). The READMEs describe a design; the compiler and the race detector were never asked to agree. This is the theme that all four above are downstream of, and it is the only one that gets *worse* when a first draft arrives already looking finished.

Note what is *not* on this list: pattern selection, layering, naming, package structure. Those are fine **in the artifact** — whether they are fine in your head is what the oral defense measures. The gap is narrow and specific, and narrow gaps close fast.

---

## 6. What this changes about the plan

**Today (Tue 25 Aug) gains a round: the oral defense.** No editor, no timer, no AI. Six questions, each about a decision already sitting in your code, each requiring reasoning that appears in none of the six READMEs. See [00A_ORAL_DEFENSE.md](00A_ORAL_DEFENSE.md). **Q1 and Q2 were resolved the same day by your own disclosure** — the ratelimiter's concurrency and generic contract were Antigravity's. Q3–Q6 stay open, ungraded.

**Wed 26 Aug becomes a primitives day: [00B_PRIMITIVES_LADDER.md](00B_PRIMITIVES_LADDER.md).** Five things built by hand with no AI and no copying from your own repo — clock seam, non-spinning worker with clean shutdown, a barrier-based exactly-once test you must *demonstrate failing* on broken code, an FSM whose illegal transition doesn't compile, and per-key locking with the leak fixed. Rationale: R1 carries **48 marks of auto-deduction** (busy-wait −15, assignment race −15, sleep-as-sync −10, one global lock −8) resting on precisely the primitives that are not yours. Building them standalone converts that exposure into five things you have already broken once.

**Round 0 (repair) stays, and moves to Thu 27 Aug.** Fixing bugs in code you own but did not write from scratch *is* the job. "I didn't type this line" is not a defence available to anyone in a real code review, and R0 is the round where you stop being able to use it.

**Round 1 (elevator) is unchanged as a paper, and moves to Fri 28 / Sat 29.** It targets themes 1, 2, 4 and 5 head-on, greenfield, under a timer, with no AI — the direct measurement of what is actually in your hands.

**One new standing rule for every remaining round:** after the buzzer, before I grade, you do a **self-review pass with no AI** and hand me a list of your own bugs. I score your list against mine. That delta — not the code — is the number I most want to move this week, because it is the skill that transfers to every line you ship whether you wrote it or a model did.

**Two corrections to the Day 0 study list I gave you:**
- I pointed you at `LLD_GO/job_queue/main.go` as a worker-pool reference. It is a 22-line file that does not compile (B1). Ignore it.
- The better reference in your own code is `ratelimiter/src/App.java:51` — the `CountDownLatch` barrier. That is the harness shape Round 1's Part E needs.

**Revised schedule:** see [00_WEEK_PLAN.md](00_WEEK_PLAN.md).
