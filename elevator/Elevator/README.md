# Elevator System — LLD

Based on the scoped problem statement (3 elevators, 10 floors, discrete tick-driven simulation).

## Scope

- 3 elevators serving 10 floors (0–9)
- Simulation advances via discrete time steps (`step()` / `tick()`), driven by a single sequential caller — not a real-time, multi-threaded system.

## Functional Requirements (FR)

1. **Multiple elevators** — system manages 3 elevators across 10 floors.
2. **Hall call** — a user requests an elevator from any floor with a direction (UP or DOWN); the system decides which elevator to dispatch.
3. **Destination call** — once inside an elevator, a user can select one or more destination floors (no direction associated).
4. **Discrete-time simulation** — time advances only via explicit `step()`/`tick()` calls; no wall-clock/real-time behavior.
5. **Multiple pending requests** — the system correctly tracks and services many simultaneously outstanding requests across floors (not simultaneous *execution* — the simulation is single-threaded/sequential, so this is a data-structure correctness concern, not a synchronization one).
6. **Invalid request rejection** — a request for a non-existent floor number is rejected (returns `false`).
7. **Same-floor no-op** — a request for the elevator's current floor is treated as already served (a no-op); door mechanics are out of scope so nothing further happens.

## Out of Scope

- Weight capacity / passenger limits
- Door open/close mechanics
- Emergency stop functionality
- Dynamic floor/elevator configuration
- UI/rendering layer

## Scheduling Strategy (Dispatch Algorithm)

For each hall call, the elevator to dispatch is chosen via a cost function over all elevators:

- **Primary cost — ETA:** estimated time for an elevator to reach the calling floor. This folds in distance and direction — a direction-mismatched elevator has a higher ETA because it must finish its current sweep and reverse before it can serve the call (LOOK-style behavior), rather than being excluded outright.
- **Aging term — wait time:** `effective_cost = ETA - α × wait_time`. As a request's wait time grows, its effective cost keeps dropping, guaranteeing it eventually wins selection even if it isn't globally optimal. This satisfies FR#5 without needing a separate starvation mechanism.
- **No capacity/weight filter** — dropped, since weight/passenger limits are out of scope.
- **Deterministic tie-breaking required** — if two elevators land on the exact same `effective_cost`, the selection must resolve deterministically (e.g., lowest elevator ID first), since the simulation must be reproducible given the same sequence of requests and ticks.

The elevator with the lowest `effective_cost` is dispatched.

## Non-Functional Requirements (NFR)

1. **Extensibility** — the scheduling strategy sits behind an interface (Strategy pattern), so a new dispatch algorithm can be added without modifying the `Dispatcher`/`Elevator` classes (Open-Closed Principle). This is independent of the threading model, so it still applies.

**Considered and excluded:**
- *Thread-safety* — the simulation is driven by sequential `step()`/request calls from a single caller, not concurrent threads, so no race conditions are possible. Not a real constraint here.
- *Scalability* — dispatch cost is O(N) over elevators; negligible at any realistic N (3, 50, whatever) with no dedicated design effort needed. Nothing to design for, so nothing to list.
- *Raw dispatch latency* — O(N) computation is microseconds; irrelevant next to simulated travel time. Not a real constraint.
- *Determinism* — automatically guaranteed by using a pure/deterministic cost function with explicit tie-breaking (see Scheduling Strategy above); doesn't need its own NFR line since no extra design effort is required beyond the tie-break rule already specified.

---

## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
