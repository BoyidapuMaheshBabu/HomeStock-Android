# HomeStock

HomeStock is an **offline-first native Android household inventory and spending tracker**. It helps a household understand what was purchased, what has been used, what remains in stock, and how spending changes over time.

> **Project status:** Phase 01 — foundation and project setup. This repository currently contains planning documentation; app implementation and tests are not yet verified.

## Problem

Household supplies are purchased repeatedly, but it can be difficult to remember quantities, compare prices, estimate what remains, and understand spending across food, cleaning, and personal-care products.

## Product scope

- **Food & Kitchen:** rice, dal, flour, oil, spices, and similar supplies.
- **Home Care & Cleaning:** toilet/bathroom/floor cleaners, dishwashing liquid, detergent, and disinfectants.
- **Personal Care & Cosmetics:** soap, shampoo, toothpaste, hair oil, and similar products.
- Record products, purchases, and quantities used.
- Calculate remaining stock and show purchase/usage history and spending.
- Keep household records on the device, available without an internet connection.

## MVP priorities

1. Add and edit products with clear units.
2. Record purchases with quantity, price, and date.
3. Record usage and calculate remaining stock.
4. Review history and actual purchase spending.
5. Persist records locally and verify they survive app restarts.

No login, cloud sync, AI features, or barcode scanning are required for the first MVP.

## Engineering direction

The target is a native Android app, with Kotlin, Jetpack Compose, and Room/SQLite as the proposed implementation direction. Confirm the actual stack and minimum Android version during Phase 01 project setup.

## Important data rules

- Mass units convert only to mass (kg ↔ g); volume units only to volume (L ↔ ml); count units remain separate.
- Do not combine incompatible units, such as kg and ml, in stock calculations.
- Keep actual money spent on purchases distinct from any estimated value of stock remaining.
- If opening stock is supported, record it explicitly rather than treating it as a purchase made in the app.

## Documentation

- [Product requirements](docs/PRODUCT.md)
- [Roadmap and phase acceptance](docs/ROADMAP.md)
- [Working philosophy](docs/WORKING-PHILOSOPHY.md)

## Working rule

Work in small, reviewable phases. Do not mark a phase complete until its acceptance criteria have been checked and the relevant build/device evidence is recorded. After a phase is accepted, merge it into main, update project status, and create the next phase branch from the latest main.
