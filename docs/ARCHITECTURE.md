# IntiVentory Architecture

**Status:** Selected architecture for the planned MVP; not implemented or build-verified.
**Working branch:** planning/research.

This is the implementation blueprint for one small native Android app. It is not a web app and does not require a cloud service.

**Product identity:** IntiVentory. Use the exact name, logo SVGs, caption, and colors specified in [Branding](BRANDING.md). The repository and proposed application ID remain technical identifiers, not the user-facing brand.

## 1. Architecture decisions

| Concern | Decision |
|---|---|
| Application type | Native Android |
| Language | Kotlin |
| UI toolkit | Jetpack Compose + Material 3 |
| Navigation | Jetpack Navigation 3 for Compose destinations |
| App shape | Single Activity, single Gradle app module, separated packages |
| UI state | Screen-level ViewModels, immutable UI state, Kotlin StateFlow, lifecycle-aware Compose collection |
| Business logic | Small plain-Kotlin functions/use cases for unit conversion, stock validation, date filters and spending calculations |
| Structured local data | Room database backed by SQLite |
| Preferences | Preferences DataStore for the saved user's name and onboarding state |
| Async operations | Kotlin coroutines and Flow |
| Dependency wiring | Small manual AppContainer; no Hilt needed for this single-module MVP |
| Network/backend | None |
| JSON backup/restore | Deferred until the intended user accepts the core app |

Use the latest stable versions that are mutually compatible when the implementation branch is created. Record actual resolved versions in the project version catalog and README at that time. Do not copy arbitrary dependency versions.

**Platform decision:** Application ID: com.boyidapu.homestock. Plan for minimum Android API 26 (Android 8.0+), which supports the chosen date APIs without extra compatibility code. Check Priya akka's target phone is API 26 or newer before scaffolding; change the minimum only if her actual device requires it and the selected dependencies permit it. Use a current stable compile/target SDK supported by the Android Gradle Plugin used at implementation time.

## 2. Why this architecture

HomeStock has one user and no server. Its important complexity is local data integrity: purchases, usage, quantity and spending must agree after every action. A small layered design keeps these rules out of UI components and makes them testable.

Data flow:
**Compose screen → screen ViewModel → domain rule/use case → repository → Room database**

Room emits updated query results through Flow. Repository and ViewModel expose updated UI state, and Compose redraws the relevant values. DataStore supplies the saved name and onboarding state. There is no network layer.

## 3. Package structure

Keep one Gradle app module. Organize packages by responsibility:

- **app:** application class, MainActivity, AppContainer, app setup.
- **navigation:** Navigation 3 back stack and destination definitions.
- **core/money:** INR formatting and rupees-to-paise parsing.
- **core/quantity:** unit conversion, base-unit normalization and stock calculation.
- **core/dates:** local-calendar date handling and spending-period boundaries.
- **data/local:** Room database, entities, DAOs and catalog-seed metadata.
- **data/preferences:** Preferences DataStore implementation.
- **data/repository:** repository implementations.
- **domain/model:** fixed category IDs, measurement dimensions, units, transaction types and spending-period types.
- **domain/repository:** interfaces consumed by domain/UI code.
- **domain/usecase:** create/edit/archive products, Add, Use, obtain stock, obtain history, obtain spending summary.
- **ui/onboarding:** first-run name screen and ViewModel.
- **ui/home:** personalized greeting, Settings, this-month card and category hero cards.
- **ui/category:** category product list and product-row actions.
- **ui/product:** create/edit product form.
- **ui/transaction:** Add/Use chooser and transaction forms.
- **ui/history:** product transaction history.
- **ui/insights:** spending period controls and totals.
- **ui/settings:** rename user and archived products.
- **ui/components:** reusable cards, buttons, unit inputs, amount inputs and confirmation dialogs.

Do not split into multiple Gradle modules in the first version. Do not add libraries unless they solve a real requirement.

## 4. Storage model

### 4.1 User preferences — Preferences DataStore

Store the trimmed display name and whether onboarding has been completed. Do not store product quantities or purchase/spending records in DataStore.

### 4.2 Fixed categories

Top-level categories are fixed code/resource definitions, not user-created database categories:

1. FOOD_KITCHEN — Food & Kitchen
2. HOME_CARE_CLEANING — Home Care & Cleaning
3. PERSONAL_CARE_COSMETICS — Personal Care & Cosmetics

There is no Add Category feature and no separate Snacks or Drinks category.

### 4.3 Product table

Each product stores:
- stable product ID;
- fixed category ID;
- product name;
- measurement dimension: MASS, VOLUME or COUNT;
- preferred/default entry unit;
- whether it is a preset product;
- whether it is archived;
- creation metadata for stable ordering.

A product does not store current stock or total spending. Both are derived from transaction history to prevent balances drifting from records.

### 4.4 Inventory transaction table

Each successful Add or Use becomes one immutable historical record:
- auto-generated transaction ID;
- product ID as a foreign key;
- type: ADD or USE;
- positive quantity normalized to base units;
- unit selected for that entry, so history shows what the user entered;
- purchase amount in paise for ADD; null for USE;
- local calendar transaction date stored as an epoch-day integer.

The database must restrict deleting products with transaction references. App rules decide whether Delete or Archive is allowed.

Transactions are not running-balance rows. In the first MVP, product history is read-only; transaction-row edit/delete controls are not included. This keeps the first version similar to a simple payment-history view. If the intended user later asks for corrections, design that as a deliberate follow-up rather than silently rewriting history.

### 4.5 Starter-catalog seed metadata

Seed starter products once. Keep a Room metadata marker for the starter-catalog version so a preset product deliberately deleted before acquiring history is not silently recreated on every launch. Catalog updates must not overwrite user edits or resurrect deleted products.

## 5. Unit and quantity model

Each product has one measurement dimension, chosen during product creation.

| Dimension | Base unit stored for calculation | Valid entry/display units |
|---|---|---|
| Mass | whole grams | kg, g |
| Volume | whole millilitres | L, ml |
| Count | whole pieces | units/pieces |

Rules:
- Convert only within one dimension; never convert kg to ml or count.
- kg and L input may use up to three decimal places, converted exactly to grams or millilitres.
- g and ml input are whole numbers in the first MVP.
- Count quantities must be whole positive integers.
- All Add/Use quantities must be greater than zero.
- Parse quantity using decimal-safe arithmetic (such as BigDecimal), not binary floating-point.
- A product's dimension cannot change after it has transaction history. Preferred unit may change within the same dimension because quantities are normalized and each transaction retains its original entry unit.
- Current stock uses the product's preferred unit. Each historical transaction displays its originally entered unit.

## 6. Money model

- Currency is INR (₹).
- Add requires the actual total amount paid for that purchase, not an estimated product value or a unit price.
- Accept at most two decimal places.
- Parse rupees into integer paise and store as a Long; do not use Float or Double for stored currency.
- Amount paid is required and non-negative. ₹0.00 is allowed for a free purchase and contributes zero to spending.
- Use has no money field and never changes historic spending.
- Totals are derived from saved Add transactions, including transactions belonging to archived products.

## 7. Stock and transaction consistency

Base-unit stock is:

**Stock = sum of ADD base quantities − sum of USE base quantities.**

Do not store an editable stock balance or use UI state as a second source of truth.

### Add transaction

1. Product must exist and be active.
2. User taps plus and chooses Add.
3. Show product name, quantity, compatible unit, actual amount paid and date.
4. Quantity must be positive and compatible with the product's dimension.
5. Amount paid is required, non-negative and has at most two decimal places.
6. Date defaults to today in device-local time. Past dates are allowed for missed entries; future transaction dates are not allowed.
7. Save one transaction. Stock, product history and applicable spending views update from the database.
8. No manual refresh and no separate increment to a cached spending total.

### Use transaction

1. Product must exist and be active; show current stock prominently.
2. User taps plus and chooses Use.
3. Request quantity, compatible unit and date. Date defaults to today; past dates are allowed; future dates are not.
4. Quantity must be positive and cannot exceed stock after unit conversion.
5. If stock is zero, disable/block Use with a clear message.
6. If requested quantity exceeds stock, reject save, show available stock and create no transaction.
7. Check current stock and insert the Use transaction atomically in one database transaction.
8. Disable Save while a write is in progress to prevent rapid double submissions.
9. On success, stock and history update immediately; spending totals do not change.

## 8. Product edit, delete, archive and restore

- Product creation is separate from purchase and does not ask for price.
- Preset and custom products start with zero recorded stock.
- Edit can change name and preferred unit.
- Dimension change is allowed only before the first transaction.
- Delete is available only when there is no transaction history and always requires confirmation.
- A product with transaction history and zero stock can be Archived/Hidden, not permanently deleted.
- A product with positive stock cannot be archived. If stock was consumed, expired or thrown away, record it through Use first; no separate disposal feature is needed.
- Archived products disappear from active category lists. History and purchase records remain intact and included in spending totals.
- Settings includes Archived Products, where the user can review and restore an archived product to the active list. Archived products have zero stock by rule.
- A history-bearing product must never be hard-deleted by the UI.

## 9. Dates and spending filters

Use the device's local calendar date for transaction dates and range boundaries. Store a date rather than a displayed timestamp to avoid timezone shifts. Time-of-day is not shown in transaction history.

| Filter | Inclusive start | Inclusive end |
|---|---|---|
| Last 7 days | Today minus 6 calendar days | Today |
| This month | First day of current calendar month | Today |
| Last 3 months | First day of the calendar month two months before the current month | Today |
| Custom date range | User-selected start | User-selected end |
| All time / Since app start | All saved purchase transactions | All saved purchase transactions |

Custom range rules:
- Both start and end are included.
- Start must be on or before end.
- Future dates are not selectable.
- Show the actual selected date range.

Example for October 23, 2026:
- Last 7 days: October 17–23.
- This month: October 1–23.
- Last 3 months: August 1–October 23.

Last 3 months means the current month-to-date plus the two preceding calendar months; it is not a rolling same-date interval.

### Spending calculation

For a selected range, sum amountPaidPaise for all ADD transactions dated within the range. Ignore USE transactions for money totals. Include purchases associated with archived products.

Example:
- Existing recorded purchase spending: ₹5,000.
- New shampoo purchase saved for ₹5.
- All-time spending immediately becomes ₹5,005.
- Recording Use changes stock only; spending remains ₹5,005.

Home's This month card uses the current calendar-month range and opens Insights with This month selected. With no purchases this month, show ₹0 and a clear no-purchases message.

## 10. Screen map

1. **Welcome / Name setup:** shown only if no valid name is saved.
2. **Home:** greeting top-left; Settings gear top-right; clickable This month card; three category hero cards.
3. **Category Products:** product rows show name/current stock; plus and three-dot actions; tapping the main row opens history; Add Product is separate.
4. **Create/Edit Product:** name, category fixed by the current category context, measurement dimension and preferred unit.
5. **Add/Use chooser:** opened by plus action, with two clear choices.
6. **Add transaction form:** quantity, compatible unit, amount paid and purchase date.
7. **Use transaction form:** current stock, quantity to use, compatible unit and date.
8. **Product History:** scrollable date-grouped read-only Add/Use history.
9. **Insights:** Last 7 days, This month, Last 3 months, Custom date range, All time; show total and boundaries.
10. **Settings:** edit saved name and open Archived Products.
11. **Archived Products:** list archived items and provide Restore.

Do not add a bottom navigation bar unless usability testing demonstrates a need. The intended entry points are Home's spending card for Insights and the gear for Settings; normal back navigation returns to the previous screen.

## 11. Immediate UI updates

Room is the single source of truth. Observable DAO queries and repository Flows derive stock, product history, product spending, This month spending and Insights totals. Screen ViewModels expose StateFlow; Compose collects UI state lifecycle-aware.

After Add is successfully saved:
- stock increases;
- product history shows the purchase;
- This month total updates if purchase date is in this month;
- selected Insights total updates if the date falls within the selected range;
- All time spending updates;
- the transaction is counted exactly once.

After Use is successfully saved:
- stock decreases;
- history shows usage;
- no spending total changes.

Never keep a separate editable/cached total in UI state.

## 12. Privacy and exclusions

- Core operation needs no internet. Do not request the INTERNET permission.
- Store app data locally. Disable Android cloud auto-backup for the first version so records are not silently uploaded through platform backup.
- No login, cloud DB, multi-device sync, analytics SDK, ads, AI, barcode scanner, receipt OCR or background worker is needed.
- JSON backup/restore is deferred until the intended user has reviewed and accepted the core app.

## 13. Test layers

- **Pure unit tests:** unit conversion, stock math, validation, date boundaries and spending calculations.
- **Room tests:** foreign-key safety, queries, atomic transaction writes, seed idempotency, archive preservation and observable updates.
- **ViewModel tests:** form validation, saving states, errors and state refresh.
- **Compose UI tests:** first launch, home layout, category/product actions, Add/Use validation, history navigation and Insights filters.
- **Physical-device checks:** offline use, persistence after restart, local dates and touch usability.

## 14. Official Android references

These are implementation references, not evidence that HomeStock has been built:
- Architecture recommendations: https://developer.android.com/topic/architecture/recommendations
- Offline-first data layer: https://developer.android.com/topic/architecture/data-layer/offline-first
- Room database: https://developer.android.com/training/data-storage/room
- Preferences DataStore: https://developer.android.com/topic/libraries/architecture/datastore
- Navigation 3: https://developer.android.com/guide/navigation/navigation-3
