# Roadmap

**Current project status: Planning & Research — implementation has not started.**

**Current planning branch:** `planning/research`.

The implementation roadmap below is provisional until product requirements, data rules, technical choices, and acceptance criteria have been reviewed. Do not start implementation simply because phases are listed.

## Phase 01 — Foundation and project setup (planned)

- [ ] Confirm native Android project setup, package/application ID, minimum Android version, language, and build tooling.
- [ ] Evaluate and choose the UI and local-storage stack; Kotlin + Jetpack Compose + Room/SQLite is the current proposal, not yet a verified implementation choice.
- [ ] Establish a maintainable project structure and app theme.
- [ ] Build a runnable HomeStock shell.
- [ ] Document build and physical-device run steps.
- [ ] Verify a debug build and launch on the intended physical device.

**Acceptance:** The native app builds, launches, and is ready for core feature implementation.

## Phase 02 — Welcome, home, and fixed categories (planned)

- [ ] Ask for the user's name on first launch and save it locally.
- [ ] Show a friendly greeting based on local time and the saved name.
- [ ] Show exactly three top-level categories: Food & Kitchen, Home Care & Cleaning, and Personal Care & Cosmetics.
- [ ] Keep the home navigation simple, readable, and touch-friendly.
- [ ] Do not include additional top-level categories.

**Acceptance:** The saved name persists across restarts and the three-category home screen is clear.

## Phase 03 — Preset products and customization (planned)

- [ ] Provide common starter products under each fixed top-level category.
- [ ] Offer Add product within each category.
- [ ] Provide three-dot Edit and Delete actions for each product.
- [ ] Edit the name and compatible unit/type safely.
- [ ] Support mass (kg/g), volume (L/ml), and count (pieces/units).
- [ ] Seed products with zero recorded stock; do not require opening inventory.

**Acceptance:** The user can add a custom product, edit a product, and remove a product safely without losing or corrupting history.

## Phase 04 — Purchase records (planned)

- [ ] Record product, quantity, compatible unit, actual price paid, and purchase date.
- [ ] Preserve each purchase as a separate history entry.
- [ ] Increase stock using unit-safe calculations.
- [ ] Display money in INR (₹).

**Acceptance:** Purchase quantities update stock correctly and spending totals reconcile with recorded purchase amounts.

## Phase 05 — Usage records and stock calculations (planned)

- [ ] Record usage quantity and date.
- [ ] Reduce stock only using compatible units.
- [ ] Validate usage against available stock.
- [ ] Show current remaining quantities clearly for weight, liquid, and count products.

**Acceptance:** Example checks pass: 5 kg − 1 kg = 4 kg; 1 L − 200 ml = 800 ml; 10 soap bars − 2 bars = 8 bars. No mass/volume/count mixing.

## Phase 06 — Dashboard, spending insights, history, and usability (planned)

- [ ] Prioritize total recorded spending in the dashboard/Insights experience.
- [ ] Design a clear spending period view (at minimum, consider monthly spending).
- [ ] Consider category-level spending breakdown without cluttering the UI.
- [ ] Show remaining quantities and compatible units alongside spending information.
- [ ] Show dated purchase and usage history.
- [ ] Test persistence after app restart and core flows in airplane mode.
- [ ] Test representative data, empty states, invalid inputs, and product edit/delete cases.
- [ ] Verify behavior on the intended physical device and review the release build.
- [ ] Update README with only verified features and evidence.

**Acceptance:** Spending sums match the stored purchase entries; remaining quantities are correct; core workflows are usable and offline.

## Deferred follow-up — JSON backup and restore (after core app acceptance)

Do not build this as part of the initial accepted core app. After the intended user has reviewed and accepted the core app, plan and implement JSON backup/restore as a separate follow-up, including data validation and protection from accidental overwrites.

## Planning and branch workflow

1. Continue product research and specification refinement on `planning/research`.
2. Close open product and data-model questions before implementation.
3. Review the final product flow, technical choices, and acceptance criteria with the intended user.
4. Only after planning is accepted, prepare the first implementation phase from the latest accepted `main`.
5. Implement in small phases, then build, test, and inspect the result before calling a phase complete.
6. After the user accepts a phase, merge it into `main`, update project status/evidence, and create the next implementation branch from the latest `main`.

A roadmap checklist is a plan until verified; never treat planned work as completed work.
