# HomeStock

HomeStock is a planned **offline-first native Android household inventory and spending tracker** for the user's sister and household. It helps the household track products, quantities remaining, purchase history, usage, and—most importantly—money spent on household products.

> **Project status:** Planning & Research — implementation has not started.
> **Current planning branch:** \`planning/research\`.
> Requirements are being refined. No app build or feature tests have been verified.

## First-use experience

- Ask for the user's name on first launch and save it locally.
- Show a friendly time-based greeting, such as “Good morning, Priya!”
- Keep the core app usable offline.
- Begin tracking from the first purchase recorded in the app; no opening-stock entry is required.

## Three fixed home categories

1. **Food & Kitchen:** starter products such as rice, dal, flour, oil, and spices.
2. **Home Care & Cleaning:** toilet/bathroom/floor cleaner, dishwashing liquid, detergent, disinfectant, and similar products.
3. **Personal Care & Cosmetics:** soap, shampoo, toothpaste, hair oil, creams, and similar products.

These three top-level categories stay fixed in the first version. Users can add, edit, and delete products inside them. Product brands and additional top-level categories are not needed.

## Core workflow

1. Choose a category and select a starter product or add a custom product.
2. Edit a product's name or compatible unit, or use its three-dot menu to edit/delete.
3. Record each purchase with quantity, unit, amount actually paid, and date.
4. Record usage to reduce available stock.
5. Review current stock, purchase/usage history, and spending insights.

## Spending is the primary dashboard metric

Every saved purchase immediately updates the dashboard and Insights. The tracked money total is **cumulative purchase spending since the user started using HomeStock**, not the estimated current value of items still on the shelf.

Example: spending recorded since app start is ₹5,000. The user records a new shampoo purchase for ₹5. The cumulative total updates to **₹5,005** immediately. Recording usage changes stock quantity, not money already spent.

Spending views should include:
- Last 7 days
- Last 1 month
- Last 3 months
- Custom date range
- All time, from the first recorded purchase through today

Period totals must be calculated from purchase records dated inside the selected range. Adding a product alone does not count as spending; recording a purchase with its actual amount does.

## Units and inventory rules

- **Mass:** kg and g.
- **Volume:** litres (L) and millilitres (ml).
- **Count:** pieces/units, for items such as soap bars.
- Convert only within the same measurement dimension; never mix kg and ml.
- Starter products begin with zero recorded stock. Purchases increase stock; usage reduces stock.
- A saved purchase retains its own quantity, unit, price paid, and date.
- Editing/deleting a product must not silently destroy history or corrupt spending totals.

## First-version boundaries

- Native Android app. Kotlin, Jetpack Compose, and Room/SQLite are proposed options, pending technical research.
- Offline local persistence; no account or cloud service.
- No brand field, barcode scanning, AI features, or additional top-level categories.
- JSON backup/restore is deferred until after the core app is built and accepted by the intended user.

## Documentation

- [Product requirements](docs/PRODUCT.md)
- [Roadmap and phase acceptance](docs/ROADMAP.md)
- [Working philosophy](docs/WORKING-PHILOSOPHY.md)

## Planning-first working rule

We are planning and researching, not implementing. Close the product workflow, date-filter definitions, data rules, technical decisions, and acceptance criteria before implementation starts. No feature should be described as implemented or tested without evidence.
