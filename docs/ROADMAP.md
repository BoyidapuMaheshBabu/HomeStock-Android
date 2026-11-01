# Roadmap

**Current status: Phases 01–07 recorded complete. On 2026-11-02, Mahesh confirmed physical-device testing of the app's functionality and reported that everything worked as expected. Automated build/test evidence remains separately qualified.**
**Current branch:** phase/07-verification-and-intended-user-review.
**Baseline:** planning and branding merged via PR #1; Phase 01 via PR #2; Phase 02 via PR #3; Phase 03 via PR #5; Phase 04 via PR #6; Phase 05 via PR #7; Phase 06 via PR #8.

The architecture and product specification are the working contract. Checkboxes below describe verified completion state and remaining planned work. Phase 01 verification is recorded in [PHASE-01-VERIFICATION.md](PHASE-01-VERIFICATION.md).

## Planning sign-off

- [x] Review and approve product requirements, architecture and acceptance tests for implementation.
- [x] Confirm the intended phone runs Android 15 (API 35), compatible with the planned minimum API 26.
- [x] Agree the three fixed categories, starter product names, and product measurement types: Mass, Liquid, Units.
- [x] Confirm product transaction history with auditable corrections and time-limited deletion.
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

**Status:** Accepted by Mahesh on October 24, 2026, to advance to Phase 05. See [Phase 04 verification](PHASE-04-VERIFICATION.md).

- [x] Plus action opens separate Add and Use choices.
- [x] Add form: quantity, compatible unit, required amount paid and date.
- [x] Use form: current stock, quantity, compatible unit and date.
- [x] Base-unit conversions and positive quantity validation.
- [x] Atomic stock check and Use insert; reject over-use.
- [x] Disable Save while write is pending and prevent duplicate submissions.
- [x] Update Room-derived stock, history and spending immediately after successful saves.

## Phase 05 — Product transaction history

**Status:** Accepted by Mahesh and merged into `main` via [PR #7](https://github.com/BoyidapuMaheshBabu/HomeStock-Android/pull/7). See [Phase 05 verification](PHASE-05-VERIFICATION.md) for scope and evidence limits; the branch README reported 54 passing unit tests.

- [x] Tapping product row opens scrollable date-grouped transaction history.
- [x] Add entries display quantity, unit, amount paid and date; Use entries display quantity, unit and stock impact.
- [x] Show product-specific purchase spending and omit time-of-day from the history list.
- [x] Every transaction can be edited: Add quantity/unit/amount/date; Use quantity/unit/date.
- [x] Delete a transaction only during the 30 minutes after it was recorded; enforce the cutoff in domain/repository logic as well as the UI.
- [x] Preserve transaction IDs/recorded-at timestamps on edit; prevent corrections/deletions from making stock negative.
- [x] Preserve the permanent product measurement-type lock even if all transaction rows are deleted.

## Phase 06 — Spending Dashboard and Insights

**Status:** Accepted by Mahesh and merged into `main` via [PR #8](https://github.com/BoyidapuMaheshBabu/HomeStock-Android/pull/8). See [Phase 06 verification](PHASE-06-VERIFICATION.md) and [the screen design](PHASE-06-INSIGHTS-DESIGN.md). The verification record documents the test/build evidence limits.

- [x] Home This month card shows current calendar-month purchase spending and the local date range; opens Insights with This month selected.
- [x] Five global filters: Last 7 days, This month, Last 3 months, Custom date range, and All time; every screen section follows the same selected period.
- [x] Inclusive local-calendar boundaries and future-date restrictions for custom ranges.
- [x] Summary card shows total spending, number of ADD purchase records, and average amount per purchase with safe empty/zero handling.
- [x] Spending trend bar chart uses period-specific daily/weekly/monthly buckets and readable values.
- [x] Category breakdown shows spending amount, share percentage, and proportional bar for each of the three fixed categories.
- [x] Top spending products lists up to five products in descending spending order with category and amount; archived status is shown and archived purchases remain included.
- [x] All analytics derive from saved ADD transactions. USE never affects spending. Free purchases count as records but contribute zero money.
- [x] Transaction edits/deletes refresh affected sections through observable data; permanently deleted product history is excluded.
- [x] Screen follows IntiVentory branding and Material 3, is vertically scrollable, and remains offline.
- [x] Regression tests for Insights ViewModel behavior and related repository test fakes are included.
- [ ] Gradle tests and debug build need recorded execution evidence; see Phase 06 verification and complete full acceptance testing in Phase 07.

## Phase 07 — Verification and intended-user review

**Status:** Completed and physically accepted by the project owner on 2026-11-02. Mahesh reports that he tested the app's functionality on a physical device and that everything worked as expected. See [Phase 07 verification](PHASE-07-VERIFICATION.md).

**Evidence note:** This is user-reported physical-device functional acceptance. The detailed acceptance catalogue remains available for traceability; its individual checkboxes were not all transcribed one-by-one during the physical test. No new Gradle test/debug-build execution output is recorded by this status update.

## Deferred follow-up — JSON backup/restore

Do not implement JSON backup/restore in the core MVP. Revisit after the intended user reviews and accepts the working app.

## Branch workflow

1. Keep research/design refinements on `planning/research` when needed; do not replace accepted implementation-branch requirements casually.
2. The reviewed planning and branding baseline was merged into `main` via PR #1.
3. Phase 01 was accepted and merged into `main` via PR #2.
4. Phase 02 was accepted and merged into `main` via PR #3.
5. Phase 03 used `phase/03-product-catalogue-and-management`; it was accepted and merged into `main` via PR #5.
6. Phase 04 used `phase/04-add-use-transactions-and-stock-rules` and was merged into `main` via PR #6.
7. Phase 05 used `phase/05-product-transaction-history`; after Mahesh accepted it, PR #7 was squash-merged into `main`.
8. Phase 06 used `phase/06-spending-dashboard-and-insights`; after Mahesh accepted it, PR #8 was squash-merged into `main`.
9. Phase 07 uses `phase/07-verification-and-intended-user-review`, created from the latest `main` after Phase 06 merge and status/evidence updates.
10. Implement one focused phase at a time and verify builds, tests and device behavior.
11. After Mahesh accepts a completed phase, merge it into `main`, update status/evidence, then create the next phase branch from the latest `main`.
