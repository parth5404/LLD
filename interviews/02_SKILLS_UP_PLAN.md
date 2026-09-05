# SKILLS-UP PLAN — the floor-first ladder

**Date:** 29 Aug 2026
**Why this file exists:** the main series (ROUND_0A/0B, ROUND_0_REPAIR, R1-R3) is
calibrated to a level the baseline assessment assumed you were at. You read them and
told me the truth: *"jo plan h woh mere level se bahut upar h"*. That is worth more than
another ten projects, and it changes the method, not the goal.

**The main series is NOT overridden.** It stays in this folder exactly as written, for
the day you climb up to it. This file is a separate, lower ladder that runs first. When
you pass the top rung here, the main series starts making sense — that's the signal.

**Goal:** transfer the only skill that AI cannot reload for you — *producing a correct,
tested design cold, with no editor help*. Not volume. Not cleverness. Correct + tested.

**A note on honesty.** The whole point of this ladder is that you know whether a rung is
done. The rule is simple and non-negotiable:

> **If you needed AI, a book, or your old repo to finish a rung, the rung is not done.**
> Write the date anyway, sleep, and do it again tomorrow. Speed is not the metric;
> finishing cold is. Three tries on one rung beats three rungs half-climbed.

**Languages:** Java first (your LLD interview language). The Go twin of each rung is
optional at first — do it when the Java version passed clean, so the skill transfers
through a second language without double-loading the first climb.

---

## Rung 0 — The test exists. And it can fail. (30 min)

**Why first:** the assessment's headline theme is *"nothing here was ever executed
against an assertion"*. Zero tests in the entire Java repo. Before any design, you need
the thing that makes every later rung honest.

Build it cold, no AI, no editor autocomplete of the test framework:

```
Calculator with add(a,b) and divide(a,b)
```

- One JUnit test class, `@Test` methods, real `assertEquals` / `assertThrows` (divide by
  zero). Maven or Gradle — whatever you already have working.
- **The red test:** first write a deliberately wrong implementation (e.g. `add` returns
  `a-b`), run it, watch it fail, paste the failure line. Then fix, run, watch it pass.
- The test must be able to fail. If it can't fail, it's decoration.

**Passed when:** you can run `mvn test` (or the equivalent) and show one red line and
then one green, from a test you wrote from memory, and you can answer: *"what does
'says the test passes' vs 'the test passed' differ?"*

Rung 0 is the fix for a habit that cost you B3 and D7 — the `t.Logf`-that-passes-everything.

---

## Rung 1 — One interface, cold. (45 min)

The problem is not that you don't know Strategy. You've seen it ~13 times. The question
is whether it's yours. We find out by producing it with zero scaffolding.

```
Payment processor with three strategies: CreditCard, UPI, Wallet
```
- Each strategy: a name, a `pay(int amount)` that returns a result (success or an error
  for "insufficient funds"). No framework, no Lombok, no annotations beyond the minimum.
- A `PaymentService` that takes a strategy and delegates.
- One test: pay via each strategy, assert the correct strategy ran (spy or state flag),
  assert the failure path surfaces.
- **No opening your `car-rental-lld`, `food-app` or `starbrew-pos`.** The point is the
  interface shape lives in YOUR head, not on your disk.

**Cold test:** after writing it, close the file and write the interface signature on
paper. If you wrote `pay(int)` but can't say *why* the caller should depend on the
interface and not the class, the rung isn't done — that *why* is the interview answer.

**Passed when:** code + test run cold, and you can say the *why* out loud:
*"the caller takes the interface so a new payment type needs zero changes at the call
site — that's open/closed, and it's why I logged the chosen strategy instead of a
switch."*

---

## Rung 2 — The thing you did NOT disclaim. (60 min)

You never disclaimed the vending machine and the starbrew double-spend test. That means
either they're truly yours or you just haven't been asked. We ask now — gently, by
rebuilding the small version.

**Part A — State, miniaturised.** A payment FSM with four states and illegal transitions
*that cannot be expressed*:

```
Idle → Selecting → Payment → Dispense → Idle
```
- `Dispense` is the ONLY state from which a purchase completes.
- `Payment → Selecting` must be illegal **and impossible to write** (no check like
  `if(state == ...)` allowed — the state object itself must reject or not expose it).
- One test proving an illegal transition either throws or doesn't compile.

**Part B — the double-spend test, from memory.** Reproduce the shape of
`TestDoubleSpendPrevention` cold — 5 goroutines, exactly one success — for a *simple*
`order.Pay()` you write now (not your starbrew). If you can produce this from memory,
your floor is real. If you reach for the old file, that's the answer too — and it means
Part B climbs again next week.

**Passed when:** both parts run, each with a test, and you can narrate *"the state
object holds its own transitions, so an illegal move is refused by the state, not
checked by the caller"* — because that is what separates State from Strategy, and it's
the one design fact you should never have to look up again.

---

## Rung 3 — One concurrency primitive, and prove it. (45 min)

Not the whole ladder. Not channels and goroutine pools. One primitive, honest:

```
A counter that 10 goroutines increment 1000 times each. Final value must be exactly 10,000.
```
- First version with **no lock** — and a `-race` test that FAILS. Paste the race output.
- Then with one `sync.Mutex` — race clears, value exact.
- Then (optional) with `atomic.AddInt64`.

**Why this rung:** the baseline found your pattern — *pick the right primitive, wire it
wrong* (B2 inverted CAS, D6 broken DCL, D5 lock on the wrong thing). The fix is not more
theory; it's watching a real race die once, by hand.

**Passed when:** you have pasted output showing (a) a failing `-race`, then (b) a
passing one — and you can explain *why* the +10,000 invariant proves the mutex is
actually protecting, not just present.

---

## Rung 4 — Read code you don't remember writing, and find a lie. (60 min)

This rung uses your **own repo** — but for the one thing the sequence so far can't train:
critical reading. Pick any ONE confirmed bug from the baseline list (B1-B11, D1-D11).
Don't fix it. Find it.

- Read the file fresh, as if a stranger wrote it. Write down: *what this code claims to
  do → what it actually does → where they diverge.*
- Then write a test that FAILS on the current behaviour. Paste the red.
- Only then fix it. Paste the green.

**Recommended start — pick the smallest lie first:** B6 (`RemoveObserver` deletes
`j-1` instead of `j`, panics at 0).

**Passed when:** one bug from the list has a demonstrated red→green in *your* repo, and
you can show the one line that lied.

---

## Rung 5 — A design-without-code: say the skeleton. (Day-long, low-stakes)

Choose any of the 13 projects you've already "built": parking lot, snake & ladder,
vending machine, restaurant. Without opening any file:

1. Write the noun list (entities) on paper.
2. Write the verb list (behaviours).
3. Draw which nouns own which verbs (one line each — this is where SRP hides).
4. Say — out loud, to a person or a voice memo — *"the one thing I'd change if
   requirements X arrives tomorrow"* (this is OCP).

You are not writing code. You are checking whether the *map* you carry matches the
*tiles* you've built. If it doesn't, the tiles were never yours.

**Passed when:** you can do all four for one project without opening the folder, and the
"change X" answer doesn't start with "I'd add an if."

---

## Climbing rules

1. **One rung per day, evening block (21:00-22:45).** Never two. Sleep is where it
   sticks, and sleeping on a half-learned rung is how it un-learns.
2. **No AI, no book, no old repo — until the rung is out.** After you've written it cold
   and the test is green, THEN you may diff against the old implementation as a free
   review. Learning from a diff you already produced is free; learning from a diff you
   only read is what you're breaking the habit of.
3. **Red before green, always.** No red → the rung doesn't count. This is the one rule
   you cannot skip, because it's the rule that was missing everywhere before.
4. **Fail honestly, climb again.** A rung that took AI to finish = not done. This is a
   ladder, not a to-do list; leaving a rung half-climbed means the next one is built on
   air.
5. **Java rungs pass → re-run the same rung in Go** (same design, small test). Same
   week, a different evening. Two passes of one rung > one pass of two.

---

## When you can open the main series again

Finish Rung 5. Then read [00A_ORAL_DEFENSE.md](00A_ORAL_DEFENSE.md) again. If the six
questions now read like questions *you could answer* — not lecture notes — you're ready
for [00B_PRIMITIVES_LADDER.md](00B_PRIMITIVES_LADDER.md). If they still read like a
foreign language, repeat Rungs 2-4 once more and meet me back here.

You are not behind. You are at a different rung than the repo suggests, and this ladder
is the honest path from where you are to where the main series assumes you are — in
days, not months, because the gap is narrow.
