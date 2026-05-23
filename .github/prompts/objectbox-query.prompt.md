---
mode: agent
description: Write an ObjectBox query for a Finsible repository
---

Write an ObjectBox query for the specified repository. Apply all Finsible query rules:

## Query Requirements

1. Use the generated `Entity_` property DSL (e.g., `TransactionEntity_.transactionDate`).
2. Apply all filters at DB level — never fetch all then filter in Kotlin.
3. Close queries with `.use { query -> query.find() }`.
4. For pagination, use `query.find(offset.toLong(), limit.toLong())`.
5. For aggregates, use `query.property(Entity_.field).sum()` / `.count()` / `.min()` / `.max()`.
6. Multi-step reads: wrap in `box.store.callInReadTx { … }`.
7. Enum-backed fields: apply the corresponding converter (`TransactionTypeConverter`,
   `StatusConverter`, `EntityTypeConverter`) before using the int value in a query condition.

## Filter Application Pattern

Extract each filter into a private `applyXFilter(queryBuilder, params)` function:

```kotlin
private fun applyDateFilter(qb: QueryBuilder<Entity>, start: Long?, end: Long?) {
    when {
        start != null && end != null -> qb.between(Entity_.field, start, end)
        start != null -> qb.greaterOrEqual(Entity_.field, start)
        end != null -> qb.lessOrEqual(Entity_.field, end)
    }
}
```

## OR Conditions

Use the ObjectBox condition DSL — not multiple queries:

```kotlin
val condition = Entity_.field1.contains(query)
    .or(Entity_.field2.equal(value))
queryBuilder.apply(condition)
```

## Sorting

Single `order()`/`orderDesc()` call per query. Multiple sorts: chain them:

```kotlin
queryBuilder.orderDesc(Entity_.date).order(Entity_.amount)
```

## Logging

Log at `Logger.Database.d` level: query params + result count. No sensitive user data.

Generate the query function, all needed helper `applyX` private functions, and the
`Logger.Database.d` call.