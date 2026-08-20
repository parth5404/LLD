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

## Current Architecture

This project implements an in-memory rate limiter with two rate limiting algorithms:

- Token Bucket
- Sliding Rolling Window

### High Level Flow

```text
Client / App
    |
    v
RateLimiterSvc
    |
    |-- fetches user config from ConfigRepo
    |-- fetches user state from StateRepo
    |
    v
RateLimiter implementation
    |
    |-- TokenBucket
    |-- SlidingRW
    |
    v
returns allowed / blocked
```

### Components

#### App

`App` is the demo entry point. It creates the config repository, state repository, and `RateLimiterSvc`, then calls `isAllowed(userId)`.

#### RateLimiterSvc

`RateLimiterSvc` is the orchestration layer. For a given `userId`, it:

1. Gets the lock for that user from `UserLockManager`.
2. Reads the user's `State` from `StateRepo`.
3. Reads the user's `Config` from `ConfigRepo`.
4. Finds the matching limiter from the strategy registry.
5. Delegates the decision to the selected rate limiter implementation.

Current dispatch logic:

```text
TOKEN_BUCKET    -> TokenBucket strategy
SLIDING_WINDOW  -> SlidingRW strategy
```

#### ConfigRepo

`ConfigRepo` stores rate limit configuration in memory using a `ConcurrentHashMap`.

Current key:

```text
userId -> Config
```

Example configs:

- `TokenBucketConfig`
- `SlidingRWCfg`

#### StateRepo / InMemoState

`StateRepo` defines methods to get and save user state.

`InMemoState` is the in-memory implementation using a `ConcurrentHashMap`.

Current key:

```text
userId -> State
```

Example states:

- `TokenBucketState`
- `SlidingRWState`

#### UserLockManager

`UserLockManager` provides one lock object per user.

This keeps mutations atomic for the same user's rate-limit state while still allowing different users to be evaluated in parallel.

```text
userA request 1 and userA request 2 -> same lock -> sequential
userA request and userB request     -> different locks -> parallel
```

#### RateLimiter

`RateLimiter` is the common interface for all rate limiting algorithms.

Each implementation takes:

- a `Config`
- a `State`

and returns:

- `true` if the request is allowed
- `false` if the request is blocked

### Token Bucket Architecture

Classes involved:

- `TokenBucketConfig`
- `TokenBucketState`
- `TokenBucket`

`TokenBucketConfig` stores:

- tier name
- maximum tokens
- refill rate

`TokenBucketState` stores:

- currently available tokens
- last refill timestamp

Decision flow:

```text
calculate elapsed time since last refill
    |
    v
add tokens based on refill rate
    |
    v
cap tokens at maxTokens
    |
    v
if tokens >= 1:
    consume 1 token and allow
else:
    block
```

### Sliding Rolling Window Architecture

Classes involved:

- `SlidingRWCfg`
- `SlidingRWState`
- `SlidingRW`

`SlidingRWCfg` stores:

- tier name
- maximum hits allowed
- window length in milliseconds

`SlidingRWState` stores:

- queue of request timestamps

Decision flow:

```text
remove timestamps older than the configured window
    |
    v
check current queue size
    |
    v
if queue size < maxHits:
    add current timestamp and allow
else:
    block
```

### Current Data Model

```text
Config
    |
    |-- TokenBucketConfig
    |-- SlidingRWCfg

State
    |
    |-- TokenBucketState
    |-- SlidingRWState

RateLimiter
    |
    |-- TokenBucket
    |-- SlidingRW
```

### Current Storage

All storage is in memory:

```text
ConfigRepo.configStore: ConcurrentHashMap<String, Config>
InMemoState.stateStore: ConcurrentHashMap<String, State>
UserLockManager.locks: ConcurrentHashMap<String, Object>
```

There is no persistent database or distributed cache in the current architecture.

### Current Limitations

- Config and state must already exist for a user before calling `isAllowed`.
- State initialization is not handled inside `RateLimiterSvc`.
- Mutable state updates are atomic per user inside a single JVM.
- The implementation is single-process only and does not support distributed rate limiting.
