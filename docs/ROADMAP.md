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

## Phase 02 — First launch, home screen, and category navigation

- [ ] On first launch, ask for the user's name and save it locally.
- [ ] Show a friendly greeting based on the device's local time, using the saved name.
- [ ] Present three fixed categories: Food & Kitchen, Home Care & Cleaning, and Personal Care & Cosmetics.
- [ ] Show touch-friendly category navigation and clear empty states.
- [ ] Keep additional top-level category creation out of the first version.

**Acceptance:** The user's name persists across restarts, the greeting is personalized, and the three categories are easy to navigate.

## Phase 03 — Product management and local storage

- [ ] Provide familiar starter product choices in the appropriate category.
- [ ] Allow custom products to be added and edited within a category.
- [ ] Assign each product a compatible measurement dimension and unit.
- [ ] Support mass (kg/g), volume (L/ml), and count (pieces/units).
- [ ] Persist products and validate required fields.

**Acceptance:** A user can select Rice or Soap from suitable choices, add another product, and see saved products after app restart.

## Phase 04 — Purchase records

- [ ] Record product, quantity, unit, actual price paid, and date.
- [ ] Keep dated purchase history for each product.
- [ ] Update stock using unit-safe calculations.
- [ ] Display money in INR (₹).

**Acceptance:** A purchase increases stock and recorded spending matches the entered amount. Quantities and price belong to the specific purchase record.

## Phase 05 — Usage records and stock calculations

- [ ] Record usage quantity and date.
- [ ] Reduce stock using compatible units only.
- [ ] Validate usage against available stock.
- [ ] Show remaining quantity clearly for weight, liquid, and count items.

**Acceptance:** Example checks pass: 5 kg − 1 kg = 4 kg; 1 L − 200 ml = 800 ml; 10 soap bars − 2 bars = 8 bars. No mass/volume/count mixing.

## Phase 06 — History and spending

- [ ] Show dated purchase and usage history for a product.
- [ ] Show a simple purchase-spending summary.
- [ ] Distinguish money spent from estimated stock value.
- [ ] Use understandable empty and no-history states.

**Acceptance:** Summaries reconcile with recorded purchase entries.

## Phase 07 — JSON backup/restore, usability, and release checks

- [ ] Export a JSON backup through a user-initiated Android file flow.
- [ ] Include the saved name, products, categories, purchase history, usage history, and required metadata.
- [ ] Validate a selected JSON backup before restoring it.
- [ ] Explain overwrite behavior and require confirmation before replacing existing data.
- [ ] Test persistence after force-close/restart and core flows in airplane mode.
- [ ] Test backup and restore with valid, invalid, and older/unsupported files.
- [ ] Test on the target physical phone and review the release build.
- [ ] Update the README with only verified features and evidence.

**Acceptance:** Valid backup/restore recovers data and personalization without network access; invalid backup input does not partially overwrite existing data.

## Branch and phase workflow

1. Create each phase branch from the latest main.
2. Implement only the scoped phase.
3. Build, test, and inspect the result before marking tasks complete.
4. The user reviews and accepts the phase.
5. Merge the accepted branch into main and update project status/docs.
6. Create the next phase branch from the updated main.

A checklist is a plan until verified; never treat planned work as completed work.
