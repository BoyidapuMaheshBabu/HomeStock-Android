# Roadmap

**Current status: Phase 01 accepted; Phase 02 ready to implement.**
**Current branch:** phase/02-welcome-and-home.
**Planning baseline:** planning and branding merged via PR #1; Phase 01 implementation accepted and merged via PR #2.

The architecture and product specification are the working contract. Checkboxes below describe verified completion state and remaining planned work. Phase 01 verification is recorded in [PHASE-01-VERIFICATION.md](PHASE-01-VERIFICATION.md).

## Planning sign-off

- [x] Review and approve product requirements, architecture and acceptance tests for implementation.
- [x] Confirm the intended phone runs Android 15 (API 35), compatible with the planned minimum API 26.
- [x] Agree the three fixed categories and starter product names; choose sensible compatible preset units during Phase 03 catalog seeding.
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

- [ ] First-run name input, validation, and local persistence.
- [ ] Time-based greeting top-left and Settings gear top-right.
- [ ] Clickable This month spending preview, using calendar month boundaries.
- [ ] This month card opens Insights with This month selected.
- [ ] Three fixed rectangular category hero cards.

## Phase 03 — Product catalogue and management

- [ ] Seed common starter products once with zero stock.
- [ ] Fixed category product lists and separate custom product creation.
- [ ] Product row with name/current stock, plus action and three-dot menu.
- [ ] Edit product name and compatible preferred unit.
- [ ] Delete only products with no transaction history, after confirmation.
- [ ] Archive/hide products with history only when stock is zero.
- [ ] Preserve archived history and spending; add Settings > Archived Products and Restore.

## Phase 04 — Add/Use transactions and stock rules

- [ ] Plus action opens separate Add and Use choices.
- [ ] Add form: quantity, compatible unit, required amount paid and date.
- [ ] Use form: current stock, quantity, compatible unit and date.
- [ ] Base-unit conversions and positive quantity validation.
- [ ] Atomic stock check and Use insert; reject over-use.
- [ ] Disable Save while write is pending and prevent duplicate submissions.
- [ ] Update Room-derived stock, history and spending immediately after successful saves.

## Phase 05 — Product transaction history

- [ ] Tapping product row opens scrollable date-grouped read-only history.
- [ ] Add entries display quantity, unit, amount paid and date.
- [ ] Use entries display quantity, unit and stock impact.
- [ ] Show product-specific purchase spending.
- [ ] No time-of-day display or transaction-row edit/delete in the first MVP.

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
4. Phase 02 uses `phase/02-welcome-and-home`, created from the updated `main`.
5. Implement one focused phase at a time and verify builds, tests and device behavior.
6. After the user accepts a completed phase, merge it into `main`, update status/evidence, then create the next phase branch from the latest `main`.

