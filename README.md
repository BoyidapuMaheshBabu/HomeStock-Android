# HomeStock

HomeStock is a planned **offline-first native Android household inventory and spending tracker** for the user's sister and household. It tracks household products, purchases, usage, available stock, and—most importantly—money spent over time.

> **Project status:** Planning & Research — implementation has not started.
> **Current planning branch:** planning/research.
> Requirements are being refined. No app build or feature tests have been verified.

## First-use experience

- Ask for the user's name on first launch and save it locally.
- Show a friendly time-based greeting, such as “Good morning, Priya!”
- Keep the core app usable offline.
- Start tracking with the first purchase recorded in the app; no opening-stock entry is required.

## Home screen: three fixed hero cards

Show three large, rectangular, clickable category cards:

1. **Food & Kitchen:** preset products such as rice, dal, flour, oil, and spices.
2. **Home Care & Cleaning:** toilet/bathroom/floor cleaner, dishwashing liquid, detergent, disinfectant, and similar products.
3. **Personal Care & Cosmetics:** soap, shampoo, toothpaste, hair oil, creams, and similar products.

These are the only top-level home categories in the first version. Users can add custom products inside them. There is no brand field and no extra top-level category creation.

## Product list and actions

Within a category, show products as clear horizontal rows:
- Product name on the left.
- A plus (+) action on the right.
- A three-dot menu on the right with Edit and Delete.
- Tapping the main product row opens its transaction history.

The plus action opens two distinct actions: **Add** (record a purchase/increase stock) and **Use** (record consumption/reduce stock). Creating a new product is a separate workflow, not combined with a purchase.

## Purchases and usage

- **Add:** Enter the quantity, choose a compatible unit for the product, enter the actual amount paid in INR, and select the purchase date.
- **Use:** Show current stock first; enter the quantity used and confirm. Usage is rejected if it exceeds available stock.
- Save each successful action as a separate transaction.
- Recalculate current stock and relevant spending views immediately after a transaction is saved.

Units:
- **Mass:** kg and g.
- **Volume:** litres (L) and millilitres (ml).
- **Count:** pieces/units, for items such as soap bars.

Only convert within the same measurement dimension.

## Product transaction history

Tapping a product row opens a vertically scrollable history screen:
- Show transactions grouped by date, with date labels such as Today, Yesterday, or a calendar date.
- Show the transaction type (Add/Purchase or Use), quantity and unit.
- For purchase entries, show the amount paid.
- Show how the transaction affected the stock; include remaining/current stock clearly.
- Show total recorded spending for that product if useful.
- Date is required; transaction time-of-day is not required in the first version.

## Spending dashboard and Insights

The primary money metric is **cumulative purchase spending since the user started using HomeStock**, not the estimated market value of items remaining on shelves.

Example: existing recorded spending is ₹5,000. The user records a new shampoo purchase for ₹5. The all-time total immediately becomes **₹5,005**. Recording usage changes stock quantity, not past spending. Adding or editing a product definition alone does not count as spending.

Required views:
- **Last 7 days:** today and the previous six calendar dates.
- **This month:** from the first day of the current calendar month through today.
- **Last 3 months:** from the first day of the calendar month two months before the current month through today (the current month plus the two preceding calendar months).
- **Custom date range:** include both the start date and end date selected by the user.
- **All time / Since app start:** all recorded purchases from the start of tracking through today.

Month-based views use calendar-month boundaries, not rolling same-date ranges such as September 23–October 23. For example, on October 23, the This month view covers October 1–23, and Last 3 months covers August 1–October 23. Filtered totals come from purchase records dated within the selected inclusive range. The app should also make current quantities and transaction history easy to inspect.

## Data safety

- Do not allow usage greater than current stock.
- Never mix incompatible units (for example, kg and ml).
- Each purchase retains its own quantity, unit, actual amount paid, and date.
- Delete an accidental product only when it has no transaction history, after a confirmation dialog.
- If a product has history and zero stock, offer Archive / Hide to remove it from the active list while preserving its history and spending records.
- Do not allow Archive / Hide while stock remains. If stock is expired or discarded, record that quantity through Use first; this changes stock but not recorded purchase spending.

## First-version boundaries

- Native Android app. Kotlin, Jetpack Compose, and Room/SQLite are candidates pending technical research.
- Offline local persistence; no account or cloud service.
- No brand field, AI features, barcode scanning, or additional top-level categories.
- JSON backup/restore is deferred until after the core app is built and accepted by the intended user.

## Documentation

- [Product requirements](docs/PRODUCT.md)
- [Roadmap and phase acceptance](docs/ROADMAP.md)
- [Working philosophy](docs/WORKING-PHILOSOPHY.md)

## Planning-first working rule

We are researching and refining the specification, not implementing yet. Finalize the user workflow, data rules, date filters, deletion safety, technical choices, and acceptance criteria before starting implementation. No feature should be described as implemented or tested without evidence.
