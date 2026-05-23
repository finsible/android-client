---
applyTo: "app/src/main/java/**/data/**"
---

# Data Layer Instructions

## Repository Hierarchy

```
BaseLocalRepository<DTO, Entity>
  └── SyncableLocalRepository<DTO, Entity>   ← entities that sync to server
        └── TransactionLocalRepository
            AccountLocalRepository
            CategoryLocalRepository
            AccountGroupLocalRepository
```

`BaseLocalRepository` provides `get`, `add`, `remove`, `addAll`, `getAll`.  
`SyncableLocalRepository` adds `queueCreateEntity`, `queueUpdateEntity`, `queueDeleteEntity` —
always use these for user-initiated mutations so a `PendingOperationEntity` is enqueued
automatically.

## ObjectBox Query Rules

- Build queries with the generated `Entity_` property DSL, not raw strings.
- Prefer `QueryBuilder` conditions over Kotlin `filter {}` on results.
- Use `.use { query -> query.find(…) }` to auto-close queries.
- Aggregate operations (`sum`, `count`, `min`, `max`) via `query.property(…).sum()` etc.
- Multi-entity read transactions: `box.store.callInReadTx { … }`.
- Converters (`TransactionTypeConverter`, `StatusConverter`, `EntityTypeConverter`) must be applied
  when querying enum-backed fields.

## Sync Entity Pattern

Every syncable entity write follows this flow:

1. Call `queueCreateEntity { localId -> Entity(id = localId, …, syncStatus = Status.PENDING) }`.
2. `PendingOperationEntity` is written atomically in the same method.
3. `SyncManager.processQueue()` picks it up on next network availability.

## Remote Models

- `*CreateRequest` / `*UpdateRequest` are pure data classes — no logic.
- `toCreateRequest(entity)` and `toUpdateRequest(entity)` live in the concrete `LocalRepository`.
- `ResponseConverter` / `ResponseHandler` wrap all Retrofit responses — never call `.body()`
  directly in repositories.

## PendingOperationRepository Rules

- `add()` sets `createdAt = System.currentTimeMillis()` automatically.
- Query for pending ops by `Status.PENDING`, ordered by `createdAt` (FIFO).
- `removeCompleted()` is called by `SyncManager` after a full queue pass — not by individual
  handlers.
- `removeByLocalEntityId()` for cleanup when an entity is hard-deleted before sync.

## TypeConverters

All enum↔Int conversions for ObjectBox fields live in `TypeConverters.kt`. When adding a new
enum-backed field, register its converter there and use it consistently in query builders.