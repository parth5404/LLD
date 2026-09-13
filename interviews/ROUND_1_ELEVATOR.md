# ROUND 1 — MACHINE CODING / LLD
## Multi-Lift Elevator Control System

**Level:** SDE-1+
**Duration:** 60 minutes per language (hard stop)
**Total marks:** 100
**Allowed:** JDK + JUnit (Java) · Go stdlib + `testing` (Go). Nothing else.
**Deliverable location:** `~/Desktop/LLD/elevator_system/` (Java) · `~/Desktop/LLD_GO/elevator_system/` (Go)

> Read the whole paper before writing a single line. Sections A–G are visible now — you are expected to design against **all** of them, including G (declared extensions). Section H is **sealed** and will be revealed the moment you submit; if your design is genuinely open/closed, H should cost you almost nothing.

---

## 1. Problem statement

Build the control software for the elevator bank of a commercial office building.

The building has **floors 0 to 15** (0 = ground) and **3 elevator cars**. People request service in two ways:

- **Hall call (external):** a person standing on floor `F` presses the `UP` or `DOWN` button on the wall. They have not chosen a destination yet — they only declared an intended direction. The system must pick **exactly one** car to serve this call.
- **Car call (internal):** a person already inside car `C` presses a destination floor button.

Cars move one floor at a time. When a car reaches a floor it has committed to stop at, the doors open, stay open for a dwell period, then close, and the car continues.

Your job is the **control system**: the model, the dispatcher, the per-car state machine, and the concurrency around them. There is no UI, no database, no network — a single process with a driver `main` that prints state transitions.

---

## 2. Functional requirements

**FR1 — Hall calls.** `requestFromFloor(floor, direction)` accepts a hall call and returns a request id. The same (floor, direction) pair requested twice while still pending is **one** logical request, not two. A hall call for `UP` at floor 15 and `DOWN` at floor 0 are invalid and must be rejected.

**FR2 — Car calls.** `requestFromCar(carId, destinationFloor)` adds a destination to that specific car. It is **not** re-dispatched to another car — the passenger is already inside.

**FR3 — Car state machine.** Every car has, at minimum: current floor, motion direction (`UP` / `DOWN` / `IDLE`), door state (`OPEN` / `CLOSED`), an ordered set of committed stops, and an operational status (`IN_SERVICE` / `MAINTENANCE` / `EMERGENCY`). Illegal transitions must be impossible or rejected — a car must never move with its doors open, and must never open its doors between floors.

**FR4 — Dispatch policy is pluggable.** The rule for choosing which car serves a hall call must sit behind an interface with **at least two** working implementations:
- `NearestCarPolicy` — baseline. Pick the idle-or-closest car by absolute floor distance.
- `DirectionAwareLookPolicy` — a car already travelling `UP` and currently *below* the requested floor is a better candidate than a closer car travelling `DOWN`. Cars must serve committed stops in sweep order (the classic LOOK / elevator algorithm), not in arrival order.

Swapping the policy must not require editing `Car`, `ElevatorSystem`, or any concrete car type.

**FR5 — Maintenance mode.** `setMaintenance(carId, true)` takes a car out of service. Any **hall calls** already assigned to it must be redistributed to the remaining in-service cars. Its **car calls** cannot be redistributed (passengers are physically inside) — define and document your behaviour for that case; a defensible answer scores, silence does not.

**FR6 — Emergency.** `triggerEmergency()` puts every car into `EMERGENCY`: each car stops accepting requests, travels to floor 0 by the shortest path, opens its doors, and stays there. All pending hall calls are dropped. `clearEmergency()` restores normal service.

**FR7 — Observability.** `snapshot()` returns the current state of every car (id, floor, direction, door state, status, pending stops) as an immutable value. Floor displays and car displays must be **notified** of state changes rather than polling — at least one observer implementation must exist and print to console.

**FR8 — Capacity.** Each car has a passenger capacity. A car at capacity must not be selected for new **hall** calls (it can still serve its existing committed stops). You may model boarding/exiting simply — an explicit `board(carId, n)` / `exit(carId, n)` is acceptable.

---

## 3. Non-functional requirements

**NFR1 — Real concurrency, not simulated.** Each car runs its own worker (a `Thread` in Java, a goroutine in Go) driving its state machine. Requests arrive from arbitrary caller threads at any time.

**NFR2 — No busy-waiting.** An idle car must block, not spin. A `while (true) { if (nothing) continue; }` loop is an automatic 15-mark deduction. Use `wait/notify`, `Condition`, `BlockingQueue`, channels, or `sync.Cond`.

**NFR3 — No lost or duplicated assignment.** Under concurrent hall calls, every accepted call is assigned to exactly one car — never zero, never two. Say explicitly, in `DESIGN.md`, which lock or channel protects the assignment decision and why the window is closed.

**NFR4 — Lock granularity.** A single global mutex around the whole system for every read and write is a junior answer and costs marks. `snapshot()` in particular must not block all three cars from moving. State the granularity you chose and the trade-off.

**NFR5 — Time must be injectable.** Door dwell and floor-travel duration must come from an injected abstraction (`Clock` / `Ticker` / step function), not hardcoded `Thread.sleep` / `time.Sleep` scattered through the model. **Tests must not sleep to synchronise.** A test that passes only because of a `sleep(500)` is treated as a failing test.

**NFR6 — Extensibility.** Adding a new car *type* (see Section G) must not modify existing car or dispatcher code. Adding a new dispatch policy must not modify the model.

**NFR7 — Clean shutdown.** `shutdown()` terminates all workers and returns only once they have actually stopped. No leaked threads, no goroutine leaks. In Go, a `go test -race` run must be clean.

---

## 4. Given constraints — do not ask me about these

- Single process, all in-memory. No persistence, no HTTP, no framework, no external library.
- Floors are `0..15` inclusive, hardcodable as config. 3 cars, also config.
- One elevator shaft per car; cars never collide, never share a shaft. Ignore physics — travelling one floor is one unit of simulated time.
- Ignore authentication, floor access control, and billing.
- Console output is the only interface. A `main` that runs the scenario in Section 6 is required.
- Weight is out of scope; capacity is a passenger count.

---

## 5. Parts and mark allocation

### Part A — Requirements & assumptions · 15 marks · ~7 min
Write `DESIGN.md` starting with:
1. **Five clarifying questions** you would have asked a real interviewer, each with the **assumption you proceeded with**. Vague questions ("what are the requirements?") score zero. I am looking for questions that expose a genuine ambiguity in Section 2 — there are at least four planted.
2. Your explicit answer to the FR5 car-call problem.
3. What you consciously chose **not** to build, and why. Scoping down deliberately scores; running out of time silently does not.

### Part B — Class design · 20 marks · ~10 min
In the same `DESIGN.md`:
1. Entities, enums, interfaces and their relationships (ASCII diagram or an indented list — no tool required).
2. For each abstraction, one line: **what would break if it did not exist.**
3. Design patterns used, with the *reason*. **Pattern-stuffing is penalised**: naming a pattern you did not need, or wrapping a single implementation in a Strategy interface "for flexibility" that Section G does not require, costs marks. I would rather see three justified patterns than seven decorative ones.
4. One paragraph on your concurrency model: what runs on which thread, what is shared, what is immutable.

### Part C — Implementation · 40 marks · ~30 min
Implement the system. The public surface must be conceptually equivalent to:

```
RequestId  requestFromFloor(int floor, Direction dir)      // FR1, may reject
void       requestFromCar(int carId, int destinationFloor) // FR2
void       setMaintenance(int carId, boolean on)           // FR5
void       triggerEmergency() / clearEmergency()           // FR6
List<CarStatus> snapshot()                                 // FR7, immutable
void       board(int carId, int n) / exit(int carId, int n) // FR8
void       shutdown()                                      // NFR7
```

Idiomatic naming per language is expected — do not transliterate Java into Go. Marks are on the model, the state machine, the dispatcher, and the concurrency, in that order of weight.

### Part D — Concurrency writeup + demo · 15 marks · ~8 min
1. A `CONCURRENCY.md` (or a section in `DESIGN.md`) answering: which lock/channel guards which invariant · why NFR3's race window is closed · what happens if two hall calls for the same floor+direction arrive on two threads in the same microsecond · your deadlock argument (lock ordering, or "there is only one lock held at a time").
2. A `main` that runs **exactly** the scenario in Section 6 and prints a readable transition log.

### Part E — Tests · 10 marks · ~5 min
At minimum these four, named clearly:
1. `dispatch_prefers_car_already_moving_toward_the_call` — direction-aware policy beats naive distance.
2. `duplicate_hall_call_is_deduplicated` — FR1.
3. `maintenance_redistributes_pending_hall_calls` — FR5.
4. `concurrent_hall_calls_are_each_assigned_exactly_once` — NFR3, real threads/goroutines, no sleep-based sync.

Go: `go test -race ./...` must pass. Java: tests must be deterministic across 20 consecutive runs.

---

## 6. Mandatory demo scenario

Your `main` must run this, in order, and print state after each step:

1. Boot the system: 16 floors, 3 cars, all `IDLE` at floor 0, policy = `DirectionAwareLookPolicy`. Print the initial snapshot.
2. From **three separate threads/goroutines simultaneously**, fire: hall call `(5, UP)`, hall call `(12, DOWN)`, hall call `(2, UP)`. Print the assignment table — which car took which call. All three must be assigned, to distinct cars.
3. Fire hall call `(5, UP)` again while it is still pending. Print proof that it was deduplicated, not re-dispatched.
4. Let the simulation advance until the car serving floor 5 arrives. Board 2 passengers, then `requestFromCar(thatCar, 11)`.
5. While that car is en route to 11, fire hall call `(8, UP)`. It should be served **on the way** by that same car (this is the LOOK behaviour — if a different car takes it, your policy is wrong and Part C loses marks).
6. Put the car serving `(12, DOWN)` into `MAINTENANCE` while it still has that stop pending. Print proof the hall call reappeared on a different in-service car.
7. `triggerEmergency()`. Print the convergence — every car ends at floor 0, doors `OPEN`, status `EMERGENCY`.
8. `shutdown()`. Print confirmation that all workers exited.

If a step is not implemented, print the step number and `NOT IMPLEMENTED` rather than skipping it silently. Honest gaps cost fewer marks than invisible ones.

---

## 7. Section G — Declared extensions (design for these, do **not** implement)

Your design must absorb each of these **without modifying existing classes**. In `DESIGN.md`, one or two lines each: which file(s) you would add, and which existing file you would touch (ideally: only wiring/config).

- **G1. Express car** — serves only floors 0 and 10–15. Skips everything else even if a hall call exists there.
- **G2. Freight car** — capacity in kg not passengers, longer door dwell, only serves calls placed with a service badge.
- **G3. Zoned dispatch** — the building splits into low zone (0–7) and high zone (8–15); a third policy prefers cars whose home zone matches the call.
- **G4. Peak-hour policy** — at 09:00, idle cars park at floor 0 instead of where they last stopped. At 18:00 they park at the middle floor.
- **G5. Priority requests** — a VIP/firefighter call jumps the queue on the assigned car, but must not violate FR3 (no direction reversal mid-door-cycle).

A design that needs `Car` edited for G1, or the dispatcher edited for G3, is not open/closed and Part B loses marks.

---

## 8. Section H — SEALED

Three additional constraints are sealed and will be revealed **at submission**. They test the same claim Section G tests — that your abstractions are real and not decorative. You will be asked to *state* how you would absorb them (and possibly implement one, timed, in 10 minutes). Do not try to guess them; just don't paint yourself into a corner.

---

## 9. Rubric

| Part | Marks | What full marks looks like |
|---|---|---|
| A — Requirements | 15 | Finds the planted ambiguities. Assumptions are decisions, not shrugs. Scope cuts are explicit. |
| B — Design | 20 | Every abstraction justified by a real requirement. Patterns earned, not sprinkled. Concurrency model stated before code. |
| C — Implementation | 40 | Correct LOOK sweep · legal-only state transitions · pluggable policy actually pluggable · dedup works · redistribution works · idiomatic in both languages |
| D — Concurrency | 15 | Race window named and closed. Deadlock argument holds. Demo scenario runs and is readable. |
| E — Tests | 10 | Four tests present, meaningful, deterministic, race-clean. |

### Auto-deductions (stack, non-negotiable)

| Deduction | Trigger |
|---|---|
| **−15** | Busy-wait / spin loop for an idle car (NFR2) |
| **−15** | Assignment race: a hall call can reach two cars, or be silently dropped (NFR3) |
| **−12** | God class: `ElevatorSystem` or `Car` holds dispatch policy logic, state machine, and observers all inline |
| **−10** | Dispatch ignores direction — LOOK not implemented, only nearest-distance (FR4) |
| **−10** | Tests use `sleep` as a synchronisation primitive (NFR5) |
| **−10** | Car can move with doors open, or open doors between floors (FR3) |
| **−8** | One global lock held for every read including `snapshot()` (NFR4) |
| **−8** | Adding an express car (G1) would require editing existing car/dispatcher code (NFR6) |
| **−6** | Leaked threads/goroutines after `shutdown()`, or `go test -race` reports a race (NFR7) |
| **−5** | Enums replaced by magic strings/ints (`"up"`, `status == 2`) |
| **−5** | Duplicate hall call creates two assignments (FR1) |
| **−5** | Decorative patterns: interfaces with exactly one implementation and no Section G justification |

### Instant no-hire, regardless of score
- Core flow does not run at all (nothing observable happens).
- Concurrency is faked — a single loop stepping all cars sequentially, presented as concurrent. (A deterministic `step()` **in addition to** real workers is fine and encouraged for tests. Replacing workers with it is not.)
- Copy-paste of a car type per variant instead of abstraction.

---

## 10. Submission format

At the top of `DESIGN.md`:

```
Language:        Java | Go
Timer started:   HH:MM
Timer stopped:   HH:MM
Actual minutes:  __
Completed:       FR1 ✅ FR2 ✅ FR3 ⚠️(partial: ___) FR4 ✅ FR5 ❌ FR6 ✅ FR7 ✅ FR8 ❌
Known bugs:      ...
Cut deliberately: ...
```

Then commit:

```bash
git add -A && git commit -m "interview: R1 elevator java"
```

Tell me when you're done and I'll grade it against this rubric — part by part, deductions itemised — and then open Section H.

---

**Start the timer when you're ready. Good luck.**
