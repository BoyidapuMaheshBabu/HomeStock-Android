# ADR 0001: Local Persistence Strategy

- **Status:** Accepted
- **Recorded:** 2026-11-05
- **Decision area:** Data persistence

## Context

IntiVentory is an offline-first, single-user Android application. Its core records are household products and Add/Use transactions. Current stock and purchase spending are derived from those saved transactions, so persistence must support structured records, relationships, consistent writes, and observable updates while the app is offline.

The accepted architecture already selects Room backed by SQLite for structured local records and Preferences DataStore for the user's display name and onboarding state.

## Decision

- Use **Room backed by SQLite** as the source of truth for products and inventory transactions.
- Use **Preferences DataStore** for lightweight user preferences, including the saved display name and onboarding state.
- Derive stock and spending from saved transaction records; do not store separately editable totals.
- Do not introduce a remote database, account, or cloud synchronization into the accepted core MVP.

## Rationale

- Products and transactions are structured records with relationships and integrity constraints.
- Transaction operations and derived stock require consistent local data handling.
- Room provides the application's SQLite-backed persistence layer and integrates with observable data flows used by the repository and UI.
- The saved name and onboarding flag are simple preferences rather than inventory records, so they remain in DataStore.

## Consequences

- The application remains usable without an internet connection or backend service.
- Schema changes require explicit Room migrations that preserve existing user data.
- Repository and domain logic must treat Room data as the source of truth.
- Stock and spending must be recalculated from the saved transaction history.
- DataStore must not become a second storage location for products, transactions, stock, or spending.
- A future cloud-sync or multi-device requirement would require a separate architecture decision and approved scope; it is not implied by this ADR.

## Alternatives and scope

This ADR records the persistence strategy already selected in the accepted architecture. It is not a claim that a formal benchmark or exhaustive comparison of every database product was performed. Cloud persistence is outside the current offline-first scope; using preferences storage for the structured inventory model would not satisfy the selected data responsibilities.

## Source

See [Architecture — Architecture decisions](../ARCHITECTURE.md#1-architecture-decisions), [Storage model](../ARCHITECTURE.md#4-storage-model), and [Offline storage, privacy and deferred features](../ARCHITECTURE.md#12-offline-storage-privacy-and-deferred-features).
