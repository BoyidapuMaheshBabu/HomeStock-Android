# Roadmap

The roadmap is phase-based. Each phase should stay focused, be implemented on its own branch, and include evidence for acceptance.

## Phase 01 — Foundation and project setup
**Branch:** phase/01-foundation-and-project-setup

- [ ] Create/open the native Android project and confirm application ID, minimum Android version, language, and build tooling.
- [ ] Confirm the actual UI and persistence stack; proposed direction is Kotlin + Jetpack Compose + Room/SQLite.
- [ ] Establish a small, maintainable project structure and app theme.
- [ ] Create a runnable app shell with the HomeStock app name.
- [ ] Document build and physical-device run steps.
- [ ] Build the debug APK successfully and launch on a physical Android device.
- [ ] Record actual build/device evidence; do not claim unverified checks.

**Acceptance:** The project builds, launches on the target phone, has a clear starting screen, and is ready for feature work.

## Phase 02 — Home screen and category navigation

- [ ] Present Food & Kitchen, Home Care & Cleaning, and Personal Care & Cosmetics.
- [ ] Add touch-friendly navigation and clear empty states.
- [ ] Make the primary actions obvious without decorative complexity.

**Acceptance:** A user can understand the three categories and navigate between them.

## Phase 03 — Product management and local storage

- [ ] Add, view, and edit products.
- [ ] Assign a category and compatible stock unit.
- [ ] Persist data locally.
- [ ] Validate required fields and prevent duplicate/ambiguous product entries where appropriate.

**Acceptance:** Products remain after app restart and can be found and edited.

## Phase 04 — Purchase records

- [ ] Record product, quantity, unit, price paid, and date.
- [ ] Keep purchase history.
- [ ] Update stock using unit-safe calculations.
- [ ] Display money in INR (₹).

**Acceptance:** A purchase increases stock and recorded spending matches the entered amount.

## Phase 05 — Usage records and stock calculations

- [ ] Record usage quantity and date.
- [ ] Reduce stock using compatible units only.
- [ ] Validate usage against available stock.
- [ ] Show remaining quantity clearly.

**Acceptance:** Example checks pass: 5 kg − 1 kg = 4 kg; 1 L − 200 ml = 800 ml. No mass/volume/count mixing.

## Phase 06 — History and spending

- [ ] Show dated purchase and usage history for a product.
- [ ] Show a simple purchase-spending summary.
- [ ] Distinguish money spent from estimated stock value.
- [ ] Use understandable empty and no-history states.

**Acceptance:** Summaries reconcile with recorded purchase entries.

## Phase 07 — Usability, backup, and release checks

- [ ] Improve input validation, error messages, and touch usability.
- [ ] Test persistence after force-close/restart and core flows in airplane mode.
- [ ] Consider a user-initiated local backup/export and restore after core data integrity is stable.
- [ ] Test on the target physical phone and review the release build.
- [ ] Update the README with only verified features and evidence.

**Acceptance:** Core flows are reliable, offline behavior is verified, and documentation matches the real app.

## Branch and phase workflow

1. Create each phase branch from the latest main.
2. Implement only the scoped phase.
3. Build, test, and inspect the result before marking tasks complete.
4. The user reviews and accepts the phase.
5. Merge the accepted branch into main and update project status/docs.
6. Create the next phase branch from the updated main.

A checklist is a plan until verified; never treat planned work as completed work.
