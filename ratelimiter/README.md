# 🚦 Rate Limiter — LLD Project

## 📁 Directory Structure (Folder ka Map)

```
ratelimiter/
├── src/
│   ├── App.java                        ← Entry point, demo + concurrency test
│   │
│   ├── config/                         ← Saare "data holder" classes yahan hain
│   │   ├── Config.java                 ← Interface: har config ka blueprint
│   │   ├── State.java                  ← Interface: har state ka blueprint
│   │   ├── RateLimiterType.java        ← Enum: TOKEN_BUCKET, SLIDING_WINDOW
│   │   │
│   │   ├── TokenBucketConfig.java      ← Token Bucket ka config (maxTokens, refillRate)
│   │   ├── TokenBucketState.java       ← Token Bucket ka state (currentTokens, lastRefillTime)
│   │   │
│   │   ├── SlidingRWCfg.java           ← Sliding Window ka config (maxHits, windowLen)
│   │   └── SlidingRWState.java         ← Sliding Window ka state (timestamp queue)
│   │
│   ├── rate_limiters/                  ← Actual algorithms yahan hain
│   │   ├── RateLimiter.java            ← Generic interface (Strategy Pattern)
│   │   ├── TokenBucket.java            ← Token Bucket algorithm
│   │   └── SlidingRW.java              ← Sliding Window algorithm
│   │
│   ├── repo/                           ← Storage layer (Repository Pattern)
│   │   ├── StateRepo.java              ← Interface: state save/get karo
│   │   ├── ConfigRepo.java             ← Config store (userId → Config)
│   │   ├── InMemoState.java            ← In-Memory state implementation
│   │   └── UserLockManager.java        ← Per-user lock deta hai (concurrency ke liye)
│   │
│   └── service/
│       └── RateLimiterSvc.java         ← Main service: isAllowed(userId) yahan call hota hai
│
├── lib/                                ← External jars (agar koi dependency ho)
├── bin/                                ← Compiled .class files
└── README.md                           ← Yeh file! 🙂
```

---

## 🧠 System Design — Kya karta hai yeh system?

Ek user ka request aaya → `isAllowed(userId)` call hota hai → system check karta hai ki is user ne allowed limit se zyada requests toh nahi kiye → `true` ya `false` return karta hai.

**Flow:**
```
Request → RateLimiterSvc.isAllowed(userId)
             ↓
    Lock lo us user ke liye (per-user lock)
             ↓
    StateRepo se State nikalo
    ConfigRepo se Config nikalo
             ↓
    Sahi RateLimiter dhundho (EnumMap se)
             ↓
    evaluate(config, state) call karo
             ↓
    true/false return karo
```

---

## ✅ KYA SAHI HAI (Interview mein ye points clearly bolna)

### 1. ✅ Strategy Pattern — Algorithms ko swap karna easy hai
`RateLimiter<C, S>` ek generic interface hai. `TokenBucket` aur `SlidingRW` dono isko implement karte hain.
Naya algorithm (jaise `LeakyBucket`) add karna hai? Sirf nayi class banao aur register karo. Purana code chhona bhi mat.

```
RateLimiter (interface)
    ├── TokenBucket
    └── SlidingRW
         └── (LeakyBucket add karna easy hai)
```

### 2. ✅ Open-Closed Principle (OCP) — Switch-case hataa diya, EnumMap lagaya
`RateLimiterSvc` mein `switch` nahi hai. Ek `Map<RateLimiterType, RateLimiter>` hai.
Naya limiter add karo → `register(new LeakyBucket())` — bas itna kaam.

### 3. ✅ Per-User Locking — Thread Safety hai!
`UserLockManager` mein har user ka apna alag `Object` lock hai.
`synchronized(lock)` se ek user ke liye ek hi thread ek time pe kaam karta hai.
**Bonus:** Alag users ke requests ek dusre ko block nahi karte — bahut smart!

```java
Object lock = lockManager.getLock(userId);
synchronized (lock) {
    // sirf is user ke liye critical section
}
```

### 4. ✅ Null Check aur Graceful Degradation
Agar user ka config ya state nahi mila, toh `false` return karo (allow mat karo).
`userId` blank ya null hai toh `IllegalArgumentException` throw karo.
Ye production-ready behavior hai.

### 5. ✅ Generic Type Safety — ClassCastException nahi aayega
`RateLimiter<C, S>` interface ke saath `getConfigClass()` aur `getStateClass()` methods hain.
`RateLimiterSvc` runtime pe type validate karta hai before casting.
Galat config-state combo doge toh clear error milega, silent bug nahi.

### 6. ✅ Repository Pattern — Storage interchangeable hai
`StateRepo` ek interface hai. `InMemoState` iska in-memory implementation hai.
Kal Redis-backed implementation banana ho toh sirf nayi class banao, baaki code same.

### 7. ✅ Concurrency Test in App.java — Ye bahut achha touch hai!
`CountDownLatch` se 20 threads ek saath fire kiye, check kiya ki exactly 5 hi allowed hue.
Ye dikhata hai ki tumhe concurrency ka practical knowledge hai.

---

## ❌ KYA GALAT HAI / IMPROVEMENT CHAHIYE (Interview ke weak points)

### ❌ 1. `UserLockManager` ki placement galat hai
`UserLockManager` `repo` package mein hai — lekin yeh koi data store nahi hai, yeh ek infrastructure/utility component hai.
**Interview mein puchha jaayega:** "Ye `repo` mein kyun hai?"
**Fix:** Ise `service` package mein rakho, ya alag `util` ya `infra` package banao.

### ❌ 2. `ConfigRepo` ek interface nahi hai — directly implementation hai
`StateRepo` ek interface hai ✅ lekin `ConfigRepo` directly ek concrete class hai ❌.
Agar kal config database se aane lage toh `RateLimiterSvc` ka constructor todna padega.

**Fix:**
```java
// Ye interface banao:
public interface ConfigRepo {
    Config getConfig(String userId);
    void setConfig(String userId, Config config);
}
// Aur rename karo concrete class ko:
public class InMemoryConfigRepo implements ConfigRepo { ... }
```

### ❌ 3. `configStore` aur `stateStore` mein `public final` kyun?
`InMemoState.java` mein:
```java
public final Map<String, State> stateStore = new ConcurrentHashMap<>();
```
Yeh `public` hai — matlab koi bhi bahar se directly map ko manipulate kar sakta hai, bina `getState()` ya `saveState()` use kiye!
**Fix:** `private` karo.

### ❌ 4. `SlidingRWState` mein constructor `Deque` leta hai, lekin field `Queue` hai
```java
// Constructor:
public SlidingRWState(Deque<Long> dq) { this.queue = dq; }

// Field:
private final Queue<Long> queue;
```
Ye thoda confusing hai — agar hum `Deque` de rahe hain toh field bhi `Deque` hona chahiye.
`Deque` zyada powerful hai (`peekFirst`, `pollFirst`) aur sliding window mein actually `Deque` hi chahiye hota hai.

### ❌ 5. State aur Config ek hi package mein hain — aur naam bhi inconsistent hain
`SlidingRWState`, `TokenBucketState` — ye config package mein hain, lekin "state" alag concept hai.
**Interview mein puchha jaayega:** "State aur Config ek hi package mein kyun?"

**Better structure:**
```
config/
    TokenBucketConfig.java
    SlidingWindowConfig.java    ← "Cfg" abbreviation avoid karo
state/
    TokenBucketState.java
    SlidingWindowState.java
```

### ❌ 6. Naming Convention Inconsistency — Ye interview mein notice hoti hai

| Class | Problem |
|-------|---------|
| `SlidingRWCfg` | "Cfg" abbreviation hai, baaki sab full naam use karte hain |
| `SlidingRW` | "RW" ka matlab "Rolling Window"? Clear nahi hai |
| `InMemoState` | "InMemory" full likhna chahiye |

**Better names:**
- `SlidingRWCfg` → `SlidingWindowConfig`
- `SlidingRW` → `SlidingWindowLimiter`
- `InMemoState` → `InMemoryStateRepo`

### ❌ 7. `stateRepo.saveState()` kabhi call nahi hota service mein
`RateLimiterSvc` state read karta hai, algorithm run karta hai (jo state object ko mutate karta hai), lekin `saveState()` kabhi call nahi karta.

In-memory mein yeh **accidentally** kaam karta hai (object reference ke wajah se) — lekin yeh **wrong pattern hai**.
Agar kal Redis ya DB backend use karo toh state silently save nahi hogi!

**Fix:**
```java
boolean result = evaluate(limiter, config, state);
stateRepo.saveState(userId, state);  // ← yeh line zaroor chahiye
return result;
```

### ❌ 8. `UserLockManager` mein memory leak ka risk hai
`locks.computeIfAbsent(userId, ...)` — ek baar lock bana, woh kabhi remove nahi hota.
Agar millions of unique users aaye toh memory full ho jaayegi!

**Fix:** TTL-based eviction use karo (`Caffeine` cache), ya periodic cleanup karo.

---

## 📊 Summary Table — Ek Nazar Mein

| Area | Status | Note |
|------|--------|------|
| Strategy Pattern | ✅ Excellent | Generic interface, easily extensible |
| OCP (Open-Closed) | ✅ Excellent | EnumMap + register() pattern |
| Thread Safety | ✅ Good | Per-user locking — smart approach |
| Null Handling | ✅ Good | Graceful degradation |
| Type Safety | ✅ Good | Runtime type checking before cast |
| Repository Pattern | ⚠️ Partial | ConfigRepo interface nahi hai |
| State Persistence | ❌ Bug | saveState() call missing in service |
| Package Structure | ⚠️ Messy | State + Config same package, UserLockManager misplaced |
| Naming Convention | ⚠️ Inconsistent | Cfg vs Config, RW vs Window, InMemo vs InMemory |
| Encapsulation | ❌ Weak | public fields in repo implementations |
| Memory Management | ⚠️ Risk | UserLockManager locks never evicted |

---

## 🎯 Interview Mein Yeh Questions Puchhe Jaate Hain

1. **"Naya algorithm kaise add karoge?"**
   → `RateLimiter<C,S>` implement karo, `register()` se add karo — OCP explain karo.

2. **"Distributed system mein kaise kaam karega?"**
   → `UserLockManager` ko distributed lock (Redis Redlock) se replace karein, `StateRepo` ko Redis-backed banao.

3. **"Ek user ke 1000 concurrent requests aaye toh?"**
   → Per-user lock hai, sirf ek thread at a time process hogi, baaki queue mein rahenge.

4. **"Memory leak kab hoga?"**
   → `UserLockManager` mein locks kabhi remove nahi hote. Inactive users ke locks memory mein rahenge. Fix: `WeakReference` ya TTL-based eviction (Caffeine cache).

5. **"saveState() kyun nahi call kiya?"**
   → In-memory mein accidentally kaam karta hai, lekin DB/Redis backend mein break ho jaata. Ye ek bug hai.

6. **"Token Bucket aur Sliding Window mein difference kya hai?"**
   → Token Bucket burst traffic allow karta hai (refill rate se). Sliding Window har timestamp track karta hai, zyada accurate hai lekin memory zyada use karta hai.

---

## 🚀 Quick Run

```bash
# Compile karo
javac -d bin src/**/*.java src/*.java

# Run karo
java -cp bin App
```

---

*README written with love in Hinglish — for maximum clarity and minimum confusion!*
