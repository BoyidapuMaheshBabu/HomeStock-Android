# Roadmap

**Current status: Planning & Research — implementation has not started.**

**Current planning branch:** planning/research.

The roadmap is provisional until the product flow, transaction model, date filters, deletion safety, technical choices, and acceptance criteria are reviewed.

## Phase 01 — Foundation and project setup (planned)

- [ ] Confirm native Android setup, application ID, minimum Android version, language, and build tools.
- [ ] Evaluate UI and local-storage choices; Kotlin + Jetpack Compose + Room/SQLite are candidates pending research.
- [ ] Establish project structure and app theme.
- [ ] Build a runnable HomeStock shell.
- [ ] Verify a debug build and launch on the intended physical device.

## Phase 02 — Welcome and home categories (planned)

- [ ] Ask for the user's name once and save it locally.
- [ ] Show a greeting based on device local time.
- [ ] Present exactly three large, rectangular, clickable hero cards: Food & Kitchen, Home Care & Cleaning, Personal Care & Cosmetics.

## Phase 03 — Product list and customization (planned)

- [ ] Show preset starter products inside each fixed category.
- [ ] Allow custom product creation as a separate workflow from a purchase.
- [ ] New products start with zero recorded stock.
- [ ] Display product rows with name on the left, plus action, and three-dot Edit/Delete menu.
- [ ] Provide Edit for product name and compatible units.
- [ ] Add a Delete confirmation dialog for products with no transactions.
- [ ] Offer Archive / Hide for products with transaction history only when current stock is zero.
- [ ] Preserve all history and spending totals after archive; prevent archiving while stock remains.
- [ ] Support mass (kg/g), volume (L/ml), and count (pieces/units).

## Phase 04 — Add and Use transactions (planned)

- [ ] The product plus action opens separate Add and Use choices.
- [ ] Add form records quantity, compatible unit, amount paid, and purchase date.
- [ ] Use form shows current stock, requests usage quantity, and records date.
- [ ] Add increases stock; Use decreases stock.
- [ ] Reject usage greater than available stock and prevent negative stock.
- [ ] Store each Add/Use as a separate dated transaction.
- [ ] Recalculate stock and relevant spending totals immediately after successful saves.

**Acceptance:** 10 kg purchased and 1 kg used leaves 9 kg. If 1 kg remains, a 10 kg usage entry is rejected. A purchase is counted exactly once in spending totals.

## Phase 05 — Product transaction history (planned)

- [ ] Tapping a product row opens its vertically scrollable transaction history.
- [ ] Group history entries by date with familiar date headings.
- [ ] Show Add/Purchase or Use, quantity/unit, and stock impact.
- [ ] Show amount paid for purchase transactions.
- [ ] Date is required; time-of-day is not required in the first version.

**Acceptance:** The user can inspect how much and when she purchased or used a product, and review its current stock and recorded purchase spending.

## Phase 06 — Dashboard, spending filters, and usability (planned)

- [ ] Make cumulative purchase spending since app start a primary dashboard metric.
- [ ] Provide Last 7 days, Last 1 month, Last 3 months, Custom date range, and All time / Since app start.
- [ ] Finalize rolling-window versus calendar-month semantics.
- [ ] Update all relevant spending totals immediately after a saved purchase.
- [ ] Keep usage from altering past spending totals.
- [ ] Test period boundaries, invalid input, double submission, app restarts, and airplane mode.
- [ ] Verify expired/discarded items can be recorded through Use without changing purchase spending.
- [ ] Verify a product with history cannot be archived while stock remains, and can be archived at zero stock without losing history.

**Acceptance:** Totals reconcile with dated purchase transactions; stock and spending remain correct after Add and Use.

## Deferred follow-up — JSON backup and restore

Plan JSON backup/restore only after the intended user has reviewed and accepted the core app.

## Planning-first workflow

1. Continue research on planning/research.
2. Close product-flow, data-model, date-filter, history and delete-safety questions.
3. Review requirements with the intended user.
4. After planning is accepted, create an implementation branch from the latest accepted main.
5. Implement in focused phases and verify with tests/build/device evidence.
6. After user acceptance of a phase, merge to main, update status, and create the next branch from the latest main.

A roadmap checklist is planned work, not evidence of implementation.
