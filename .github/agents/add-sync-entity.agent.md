---
mode: agent
description: Add a new syncable entity type end-to-end (entity → repository → sync handler → Hilt wiring)
tools:
  - codebase
  - editFiles
  - runCommands
---

You are adding a fully wired syncable entity to Finsible. Complete all steps; do not skip any.

## Step 1 — Enum Registration

In `common/Enums.kt`, add the new value to `EntityType`:

```kotlin
enum class EntityType { ACCOUNT, ACCOUNT_GROUP, CATEGORY, TRANSACTION, NEW_TYPE }
```

## Step 2 — Entity

Create `data/local/entity/NewTypeEntity.kt`:

- `@Entity` annotation.
- Fields matching the domain model.
- Enum fields annotated with `@Convert(converter = XConverter::class, dbType = Int::class)`.
- Extends `BaseEntity` (provides `id: Long`), implements `SyncableEntity` (provides `syncStatus`,
  `serverId`, `localId`).

## Step 3 — Domain Model

Create `data/model/NewType.kt`:

- Plain data class, no Android imports.
- `fun NewType.toEntity(): NewTypeEntity` extension at the bottom.

## Step 4 — Remote Models

Create `data/remote/model/NewTypeCreateRequest.kt` and `NewTypeUpdateRequest.kt` — data classes
only.

## Step 5 — Local Repository

Create `data/local/repository/NewTypeLocalRepository.kt`:

- Extends `SyncableLocalRepository<NewType, NewTypeEntity>`.
- Implement `entityType = EntityType.NEW_TYPE`.
- Implement `idProperty()`, `syncStatusProperty()`, `toCreateRequest()`, `toUpdateRequest()`.
- Add feature-specific query methods following ObjectBox query rules (DB-level filters, `.use { }`,
  `Logger.Database.d`).

## Step 6 — Sync Handler

Create `data/sync/NewTypeSyncHandler.kt`:

- Implements `EntitySyncHandler`.
- `processCreate`: call remote API, on success update `entity.serverId` and
  `entity.syncStatus = Status.COMPLETED`.
- `processUpdate` / `processDelete`: analogous.
- Throw `SyncException(msg, isRetryable)` on failure.
- Inject `NewTypeLocalRepository` and the relevant `*ApiService`.

## Step 7 — Hilt Wiring

In `data/di/NewTypeModule.kt`:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NewTypeModule {
    @Provides
    @Singleton
    fun provideNewTypeBox(store: BoxStore): Box<NewTypeEntity> = store.boxFor()

    @Provides
    @Singleton
    fun provideNewTypeLocalRepository(…): NewTypeLocalRepository = …

    @Provides
    @IntoMap
    @EntityTypeKey(EntityType.NEW_TYPE)
    fun provideNewTypeSyncHandler(handler: NewTypeSyncHandler): EntitySyncHandler = handler
}
```

## Step 8 — ObjectBox Model Registration

Add `NewTypeEntity` to the ObjectBox model (the plugin handles codegen on next build, but ensure the
class is annotated and in the correct package).

## Step 9 — Verification Checklist

- [ ] `EntityType.NEW_TYPE` added.
- [ ] `NewTypeEntity` compiles with no missing converter errors.
- [ ] `NewTypeLocalRepository` satisfies all abstract members of `SyncableLocalRepository`.
- [ ] `NewTypeSyncHandler` registered via `@IntoMap` in the Hilt module.
- [ ] No direct `box.all` usage in the repository.
- [ ] `Logger.Database.d` call in every public repository method.