# ROUND 0 — REPAIR ROUND
## Fix your own bugs, with tests that prove it

**Level:** SDE-1+ (this round is graded on *discipline*, not cleverness)
**Duration:** 90 minutes (hard stop)
**Total marks:** 100
**Date:** Wed 26 Aug 2026
**Prerequisite:** read [01_BASELINE_ASSESSMENT.md](01_BASELINE_ASSESSMENT.md) first.

---

## Why this round exists

You have 13 real LLD projects and your pattern vocabulary is solid. But the assessment turned up a habit that would sink you in a real code review: **when behaviour is ambiguous or broken, your assertion softens into a log line.**

The clearest example, verified by running it:

```
=== RUN   TestMakeMove_Castling
    game_test.go:216: Castling failed, possibly due to strict castling logic in strategy.
--- PASS: TestMakeMove_Castling
```

Castling has never worked. A test was written for it. The test passes. You wrote the test, saw it fail, and changed `t.Errorf` to `t.Logf`.

That is the single most expensive habit in this assessment, because it makes every other bug invisible. Round 1 cannot fix it — on a greenfield problem you decide what "correct" means, so you can always redefine your way out. Here you cannot: the correct behaviour is chess, or CAS semantics, or car-rental domain logic. Not negotiable by you.

**Rule for this entire round: every fix ships with a test that FAILS on the current code and PASSES after your change. You must demonstrate the red state.** A fix without a demonstrated red state scores zero for that bug, no matter how correct the fix is.

---

## The six bugs

Fix these six. They are ordered by what I think you should do first, not by difficulty.

---

### R0-1 · Parking lot hands out occupied spots · 15 marks
**File:** `parking_lot/untitled/src/model/ParkingFloor.java:24`

```java
if(spots.getAllowed()==type && !spots.tryOccupy()){ return spots; }
```

`tryOccupy()` is `compareAndSet(false,true)` — `true` means "I just claimed it". The `!` inverts the whole thing: a spot is returned only when claiming **failed**, i.e. someone else has it. And every genuinely free spot gets CAS-flipped to occupied and then skipped, so it can never be allocated again.

**Required:** fix the allocation. Then a test proving that (a) a free spot is returned and marked occupied, and (b) **two threads racing for the last remaining spot produce exactly one winner**. Use the `CountDownLatch` barrier from `ratelimiter/src/App.java:51` — do not use `sleep` to line the threads up.

Note what you got *right* here: `AtomicBoolean` + CAS is the correct primitive for this. Only the condition was inverted. Keep the primitive.

---

### R0-2 · Castling is unreachable · 20 marks
**Files:** `LLD_GO/chess_LLD/game/game.go:98`, `LLD_GO/chess_LLD/moves/castling.go:22`

`MakeMove` builds the modifier-piece slice with the rook only:
```go
var pc []*models.Piece
pc = append(pc, &rookPiece)   // len == 1
```
`Castling.CanMove` rejects anything where `len(mod_piece) != 2`. So castling is dead code with a green test over it.

**Required:**
1. Fix it so castling actually executes on the board — king and rook both land on their squares.
2. **Rewrite `TestMakeMove_Castling` to assert, not log.** Assert final king and rook positions. Delete the hedging comment at `game_test.go:212-214`.
3. Add a test that castling is **rejected** when a piece stands between king and rook.

While you are in `castling.go`, decide whether `mod_piece []*models.Piece` is the right signature at all. A `[]*Interface` in Go is almost always a smell — you are pointer-to-interface, and the whole `switch` at `castling.go:34` exists to re-derive information the caller already had. State your call in the writeup; changing it is optional, defending it is not.

---

### R0-3 · Move counts never increment · 15 marks
**File:** `LLD_GO/chess_LLD/models/piece.go:38`

`Piece` exposes `GetMoveCnt()` and nothing anywhere increments it. Consequences, both live right now:
- castling's `GetMoveCnt() != 0` guard can never trigger — you may castle after moving the king
- the pawn double-step guard at `moves/pawn_move.go:41` can never trigger — **a pawn can double-step on every move, forever**

**Required:** add the incrementer, call it on a successful move, and write a test proving a pawn cannot double-step twice. Your Java chess already does this correctly (`chess_lld/src/models/pieces/Piece.java:25`) — the Go port dropped it. Worth asking yourself why you didn't notice.

---

### R0-4 · Your chess tests have never run · 10 marks
**File:** `LLD_GO/chess_LLD/game/game.go:296`

```
game/game.go:296:2: fmt.Println arg list ends with redundant newline
FAIL lld/chess/game [build failed]
```

`go vet` runs as part of `go test`. 218 lines of tests are unreachable because of a trailing `\n` in a `Println`. They pass under `-vet=off`.

**Required:** fix the vet failure so `go test -race ./...` runs clean on the whole module with no flags. Then answer in the writeup: **how long has this been broken, and what does it tell you about your workflow?** I want the honest answer, not a nice one.

---

### R0-5 · `RemoveObserver` deletes the wrong element or panics · 15 marks
**File:** `LLD_GO/inventory_LLD/models/inventory.go:45`

```go
i.observerList = append(i.observerList[:j-1], i.observerList[j-1+1:]...)
```

Should be `[:j]` and `[j+1:]`. At `j == 0` this is `observerList[:-1]` → runtime panic. At `j > 0` it removes index `j-1`.

**Required:** fix it, plus tests for removing the first element, the last element, a middle element, and an observer that was never registered. Also fix the loop itself — mutating the slice you are indexing while iterating forward is wrong even once the arithmetic is right.

Then, in the same file: `GetInstance` at `inventory.go:18` reads `instance` outside the mutex. Under Go's memory model that is a data race, not a "probably fine". Replace it with `sync.Once` and say in the writeup why the double-checked-locking idiom you ported does not hold here.

---

### R0-6 · Paying for a rental releases the car · 15 marks
**File:** `LLD_GO/car_rental_LLD/models/reservation_manager.go:56`

```go
reservation.status = ReservationStatus(PAID)
reservation.vehicle.SetAvailability(true)   // now bookable by someone else
```

Paying makes the vehicle available again. It should stay unavailable until returned.

**Required:** fix it, add the missing return/complete step, and add a test proving a paid vehicle cannot be double-booked.

Then handle the race sitting next to it: `BookReservation` (`:29`) does check-then-act on `v.IsAvailable()` → `SetAvailability(false)` **with no lock at all**, and `res_id++` is a non-atomic package-level global. Two goroutines can book the same car and get the same reservation id. Fix both, and write the concurrent test that proves it. This is the same class of bug as R0-1 — notice that you defended it correctly in one project and not at all in the other.

---

## Deliverable

A `ROUND_0_WRITEUP.md` at the repo root of whichever repo you're in (both repos get one, since the bugs span both):

```
Timer started:  HH:MM
Timer stopped:  HH:MM
Actual minutes: __

Per bug:
  R0-n
    Root cause (one sentence — the actual cause, not the symptom):
    Fix:
    Test that proves it:
    Red state evidence: <paste the failing test output BEFORE the fix>
    Green state evidence: <paste the passing output AFTER>
```

The red-state paste is not optional. It is the whole point of the round.

Plus short answers to the three thinking questions:
1. **R0-2:** is `mod_piece []*models.Piece` the right signature? Defend or replace.
2. **R0-4:** how long has your chess test suite been unrunnable, and what does that say about your workflow?
3. **R0-5:** why is the double-checked-locking idiom you ported from Java broken in Go?

And one more, added after your Antigravity disclosure — folded into the writeup-quality marks, not scored separately:

4. `chess_LLD/README.md` diagrams `Castling` as a working `Move` strategy and lists `GetMoveCnt()` in the `Piece` interface. Both are dead. Its "Kya Galat Hua" section lists three bugs and every one of them had already been fixed. **What would have had to be different about how that document was produced for it to have caught R0-2 and R0-3?** One paragraph. I am not fishing for "I should run my tests" — I want the specific thing a design doc cannot know about a program.

---

## Rubric

| Item | Marks |
|---|---|
| R0-1 parking lot + race test | 15 |
| R0-2 castling + honest assertions | 20 |
| R0-3 move count + pawn test | 15 |
| R0-4 vet fix + honest workflow answer | 10 |
| R0-5 observer removal + `sync.Once` | 15 |
| R0-6 rental release + booking race | 15 |
| Writeup quality: root causes are causes, not restatements | 10 |

### Auto-deductions

| Deduction | Trigger |
|---|---|
| **−100% of that bug** | Fix shipped without demonstrated red-state output |
| **−15** | Any new or surviving `t.Logf` / soft assertion where the behaviour is actually asserted-able |
| **−10** | `sleep` used to synchronise a concurrent test |
| **−10** | `go test -race ./...` not clean on both Go modules at the end |
| **−10** | Root cause in the writeup restates the symptom ("castling returned false") instead of naming the cause ("caller passed 1 modifier piece, validator required 2") |
| **−5** | A bug "fixed" by deleting the feature or the test |

### Instant fail
- Any of the six tests passes both before and after the fix.

---

## Two things I am not asking for

- **Do not refactor.** You will be tempted to redesign `mod_piece`, split the vending machine's public fields, add a `Clock`. Not today. Today is six surgical fixes with six honest tests. Round 1 is where you get to design.
- **Do not fix the other bugs.** The assessment lists 12 confirmed bugs and 11 design issues. Six are in scope. Leaving the rest alone is part of the exercise — scope discipline is graded in every round of this series.

---

**Start the timer when you're ready.**
