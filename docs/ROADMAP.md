# Roadmap

**Current status: Phases 01–04 accepted; Phase 05 — Product transaction history is active.**
**Current branch:** phase/05-product-transaction-history.
**Baseline:** planning and branding merged via PR #1; Phase 01 via PR #2; Phase 02 via PR #3; Phase 03 via PR #5.

The architecture and product specification are the working contract. Checkboxes below describe verified completion state and remaining planned work. Phase 01 verification is recorded in [PHASE-01-VERIFICATION.md](PHASE-01-VERIFICATION.md).

## Planning sign-off

- [x] Review and approve product requirements, architecture and acceptance tests for implementation.
- [x] Confirm the intended phone runs Android 15 (API 35), compatible with the planned minimum API 26.
- [x] Agree the three fixed categories, starter product names, and product measurement types: Mass, Liquid, Units.
- [x] Confirm the first MVP uses read-only transaction history.
- [x] Merge the planning baseline into `main` and create the first implementation branch from it.

## Phase 01 — Android foundation

- [x] Native Kotlin Android project, Gradle Kotlin DSL, version catalog, single Activity and single app module.
- [x] Jetpack Compose + Material 3, Navigation 3, screen ViewModels, Kotlin coroutines/Flow, Room/SQLite, Preferences DataStore.
- [x] Manual AppContainer dependency wiring.
- [x] Create package structure from ARCHITECTURE.md.
- [x] Set up app theme, local database, preferences and navigation.
- [x] Build and launch a minimal app on the intended phone.
- [x] Record actual dependency versions and verified build evidence.

**Acceptance:** Accepted by Mahesh after a successful physical-phone launch, icon verification and Telugu-text rendering check. Friday reported successful debug build and 13/13 passing unit tests; see PHASE-01-VERIFICATION.md.

## Phase 02 — Welcome and Home

- [x] First-run name input, validation, and local persistence.
- [x] Time-based greeting top-left and Settings gear top-right.
- [x] Clickable This month spending preview, using calendar month boundaries.
- [x] This month card opens Insights with This month selected.
- [x] Three fixed rectangular category hero cards.

**Acceptance:** Mahesh accepted the current Welcome/Home implementation after Friday's physical-device usability pass. See [PHASE-02-VERIFICATION.md](PHASE-02-VERIFICATION.md) for scope and evidence limits.

## Phase 03 — Product catalogue and management

**Status:** Accepted by Mahesh on October 24, 2026, following physical-device re-testing. See [Phase 03 verification](PHASE-03-VERIFICATION.md).

- [x] Seed the 15 starter products once with zero stock and preserve deliberate deletions.
- [x] Fixed category product lists and separate custom product creation using name + Mass/Liquid/Units only.
- [x] Product row with name/current stock, plus action and three-dot menu.
- [x] Allow repeated measurement-type changes only before the first transaction; lock type after any transaction, even at zero stock.
- [x] Enforce a 40-character product-name limit and a 10-character user display-name limit.
- [x] Permanently delete a product and all its associated transactions atomically after explicit destructive confirmation.
- [x] Allow Archive at any stock/history state; preserve stock-derived balance, history and spending; add Settings > Archived Products and Restore.

## Phase 04 — Add/Use transactions and stock rules

**Status:** Accepted by Mahesh on October 24, 2026, to advance to Phase 05. See [Phase 04 verification](PHASE-04-VERIFICATION.md) for the reported implementation/test evidence and its limits.

- [x] Plus action opens separate Add and Use choices.
- [x] Add form: quantity, compatible unit, required amount paid and date.
- [x] Use form: current stock, quantity, compatible unit and date.
- [x] Base-unit conversions and positive quantity validation.
- [x] Atomic stock check and Use insert; reject over-use.
- [x] Disable Save while write is pending and prevent duplicate submissions.
- [x] Update Room-derived stock, history and spending immediately after successful saves.

## Phase 05 — Product transaction history

**Status:** Active on `phase/05-product-transaction-history`, created from the post-Phase-04 `main` commit `8d64ed09769e4b09ae59a99296f489693d138870`. Scope includes always-available transaction editing and a 30-minute post-recording Delete window.

- [ ] Tapping product row opens scrollable date-grouped transaction history.
- [ ] Add entries display quantity, unit, amount paid and date; Use entries display quantity, unit and stock impact.
- [ ] Show product-specific purchase spending and omit time-of-day from the history list.
- [ ] Every transaction can be edited: Add quantity/unit/amount/date; Use quantity/unit/date.
- [ ] Delete a transaction only during the 30 minutes after it was recorded; enforce the cutoff in domain/repository logic as well as the UI.
- [ ] Preserve transaction IDs/recorded-at timestamps on edit; prevent corrections/deletions from making stock negative.
- [ ] Preserve the permanent product measurement-type lock even if all transaction rows are deleted.

## Phase 06 — Spending Dashboard and Insights

- [ ] Current calendar-month spending preview on Home.
- [ ] Last 7 days, This month, Last 3 months, Custom date range and All time filters.
- [ ] Inclusive date boundaries and visible selected range.
- [ ] Calculate totals from saved Add transactions; Use does not alter spending.
- [ ] Include purchases belonging to archived products in historical totals.
- [ ] Test monthly boundaries, custom range, empty range and immediate recalculation.

## Phase 07 — Verification and intended-user review

- [ ] Run tests listed in ACCEPTANCE-TESTS.md.
- [ ] Test core flow in airplane mode.
- [ ] Verify persistence across restart/force close.
- [ ] Test unit conversions, over-use rejection, double taps, price parsing, date filters and archive/delete.
- [ ] Install on the intended physical phone and let the intended user try the app.
- [ ] Fix feedback that affects agreed MVP requirements.
- [ ] Update README only with verified features and evidence.

## Deferred follow-up — JSON backup/restore

Do not implement JSON backup/restore in the core MVP. Revisit after the intended user reviews and accepts the working app.

## Branch workflow

1. Keep research/design refinements on `planning/research` when needed; do not replace accepted implementation-branch requirements casually.
2. The reviewed planning and branding baseline was merged into `main` via PR #1.
3. Phase 01 was accepted and merged into `main` via PR #2.
4. Phase 02 was accepted and merged into `main` via PR #3.
5. Phase 03 used `phase/03-product-catalogue-and-management`; it was accepted and merged into `main` via PR #5.
6. Phase 04 used `phase/04-add-use-transactions-and-stock-rules` and was merged into `main` via PR #6.
7. Phase 05 uses `phase/05-product-transaction-history`, created from the post-Phase-04 `main` commit.
8. Implement one focused phase at a time and verify builds, tests and device behavior.
9. After the user accepts a completed phase, merge it into `main`, update status/evidence, then create the next phase branch from the latest `main`.
