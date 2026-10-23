# HomeStock

HomeStock is a planned **offline-first native Android household inventory and spending app** for the user's sister and household. It is designed to track purchases, product quantities, usage, remaining stock, and—especially—how much the household spends on home products.

> **Project status:** Planning & Research — implementation has not started.
> **Current planning branch:** \`planning/research\`.
> The requirements are being refined. No app build or feature tests have been verified.

## Intended first-use experience

- Ask for the user's name on first launch and save it locally.
- Show a friendly, local-time greeting, such as “Good morning, Priya!”
- Keep the core app usable without an internet connection.
- Begin stock tracking from the user's first recorded purchases. Existing household stock does not need to be entered as opening stock.

## Three fixed home categories

1. **Food & Kitchen:** starter products such as rice, dal, flour, oil, and spices.
2. **Home Care & Cleaning:** starter products such as toilet/bathroom/floor cleaner, dishwashing liquid, detergent, and disinfectant.
3. **Personal Care & Cosmetics:** starter products such as soap, shampoo, toothpaste, hair oil, and creams.

These three top-level home categories stay fixed in the first version. Users can add, edit, and delete products/items inside each category; they cannot create additional top-level home categories in the first version. Product brands are not needed.

## Core product workflow

1. Choose one of the three home categories.
2. Select a preset product or add a custom product.
3. Edit a product's name and compatible unit when needed, or delete it using its three-dot menu.
4. Record a purchase with quantity, unit, price paid, and date.
5. Record usage to reduce stock.
6. Review the remaining quantity, purchase/usage history, and spending.

## Spending and inventory are core

Money is a primary concern for the intended user, not an optional add-on. The dashboard/Insights experience should make spending easy to understand while also showing remaining quantities. Spending must be calculated from recorded purchase amounts, kept separate from any estimated value of stock remaining.

## Unit and data rules

- **Mass:** kg and g can convert to each other.
- **Volume:** litres (L) and millilitres (ml) can convert to each other.
- **Count:** pieces/units suit items such as soap bars.
- Never combine incompatible units, such as kg and ml, in a stock calculation.
- The predefined product list starts with zero stock; quantities increase through recorded purchases and decrease through recorded usage.
- Protect purchase and usage history from being silently lost or misrepresented when a product is edited or deleted.

## First-version boundaries

- Native Android app; proposed direction is Kotlin, Jetpack Compose, and Room/SQLite, pending technical research.
- Offline local persistence; no account or cloud service.
- No brand field, barcode scanning, AI features, or extra top-level home categories.
- JSON backup/restore is **deferred** until after the core app is built and accepted by the intended user.

## Documentation

- [Product requirements](docs/PRODUCT.md)
- [Roadmap and phase acceptance](docs/ROADMAP.md)
- [Working philosophy](docs/WORKING-PHILOSOPHY.md)

## Planning-first working rule

We are planning and researching now, not implementing. Clarify the product workflow, data rules, technical choices, and acceptance criteria before asking an implementation agent to build. Work in small, reviewable phases only after planning is accepted. Do not claim any feature is implemented or tested without evidence.
