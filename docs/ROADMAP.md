# Roadmap

**Current status: Planning & Research — implementation has not started.**

**Current planning branch:** \`planning/research\`.

This roadmap remains provisional until product requirements, date-filter definitions, data rules, technical decisions, and acceptance criteria have been reviewed.

## Phase 01 — Foundation and project setup (planned)

- [ ] Confirm native Android project setup, application ID, minimum Android version, language, and build tools.
- [ ] Evaluate UI and local-storage options; Kotlin + Jetpack Compose + Room/SQLite is a proposal, not yet a verified decision.
- [ ] Establish project structure and app theme.
- [ ] Build a runnable HomeStock shell.
- [ ] Document build and physical-device run steps.
- [ ] Verify debug build and launch on the intended physical device.

## Phase 02 — Welcome, home, and fixed categories (planned)

- [ ] Ask for the user's name once and save it locally.
- [ ] Show a greeting based on the device's local time.
- [ ] Show exactly three top-level categories: Food & Kitchen, Home Care & Cleaning, and Personal Care & Cosmetics.
- [ ] Keep navigation simple and touch-friendly.

## Phase 03 — Preset products and customization (planned)

- [ ] Provide starter products inside each fixed category.
- [ ] Provide Add product inside each category.
- [ ] Provide three-dot Edit and Delete actions.
- [ ] Allow safe changes to product name and compatible unit.
- [ ] Support mass (kg/g), volume (L/ml), and count (pieces/units).
- [ ] Start products at zero recorded stock; do not require opening inventory.
- [ ] Resolve delete/archive behavior for products that have history before implementation.

## Phase 04 — Purchase records and immediate recalculation (planned)

- [ ] Record product, quantity, compatible unit, actual amount paid, and purchase date.
- [ ] Preserve each purchase as its own transaction.
- [ ] Recalculate stock, history, and all applicable spending totals as soon as a purchase is saved.
- [ ] Ensure the same transaction is not counted twice.
- [ ] Display money in INR (₹).

**Acceptance:** A purchase adds to stock and recorded spending exactly once. Example: existing all-time purchases total ₹5,000; a new ₹5 shampoo purchase immediately changes the all-time total to ₹5,005.

## Phase 05 — Usage records and stock calculation (planned)

- [ ] Record usage quantity and date.
- [ ] Reduce available stock only using compatible units.
- [ ] Validate usage against available stock.
- [ ] Update remaining quantity immediately after a usage record is saved.
- [ ] Do not subtract usage from historical spending.

**Acceptance:** 5 kg − 1 kg = 4 kg; 1 L − 200 ml = 800 ml; 10 soap bars − 2 bars = 8 bars.

## Phase 06 — Dashboard, spending filters, history, and usability (planned)

- [ ] Make cumulative recorded purchase spending since app start a primary dashboard metric.
- [ ] Provide Last 7 days, Last 1 month, Last 3 months, Custom date range, and All time / Since app start views.
- [ ] Finalize rolling-window versus calendar-month semantics for the 1-month and 3-month filters.
- [ ] Calculate each period total from dated purchase records and display the selected date range.
- [ ] Show remaining quantities and compatible units without confusing them with money spent.
- [ ] Provide purchase/usage history.
- [ ] Test totals against known transaction examples, period boundaries, empty ranges, app restarts, and airplane mode.
- [ ] Update README only with verified outcomes.

**Acceptance:** Date-filtered and all-time totals reconcile with the saved purchase records; stock remains correct; spending and usage are not conflated.

## Deferred follow-up — JSON backup and restore

Do not include JSON backup/restore in the initial core-app implementation. Plan it after the intended user has reviewed and accepted the core app.

## Planning-first workflow

1. Continue research and specification refinement on \`planning/research\`.
2. Close remaining product, data model, date-filter, delete-history, and technical questions.
3. Review the product flow and acceptance criteria with the intended user.
4. Once planning is accepted, prepare an implementation-phase branch from the latest accepted \`main\`.
5. Implement in small phases and verify with builds/tests/physical-device evidence.
6. After a phase is accepted, merge to \`main\`, update status/evidence, and create the next phase branch from the latest \`main\`.

A roadmap checklist represents planned work until verification evidence exists.
