# HomeStock

HomeStock is an **offline-first native Android household inventory and spending tracker**. It helps a household understand what was purchased, what has been used, what remains in stock, and how spending changes over time.

> **Project status:** Planning & Research — implementation has not started.
> **Current planning branch:** `planning/research`.
> This branch contains the current product planning documents; implementation phases remain planned, and no app build or feature tests have been verified.

## First-use experience

- On first launch, ask for the household user's name and save it locally.
- Display a welcoming time-based greeting, such as “Good morning, Priya!”.
- Keep the saved name and household records available offline.
- Provide user-initiated JSON backup and restore for the saved name and app data.

## Product scope

The home screen starts with three categories:

- **Food & Kitchen:** rice, dal, flour, oil, spices, sugar, and similar supplies.
- **Home Care & Cleaning:** toilet/bathroom/floor cleaners, dishwashing liquid, detergent, and disinfectants.
- **Personal Care & Cosmetics:** soap, shampoo, toothpaste, hair oil, and similar products.

Show familiar starter product choices within categories and let users add their own products. Creating additional top-level home categories is not in the first version.

## MVP priorities

1. Personalize the app with a locally saved name and greeting.
2. Add and edit products with compatible stock units.
3. Record purchases with quantity, actual price paid, and date.
4. Record usage and calculate remaining stock.
5. Review product history and actual purchase spending.
6. Back up and restore household records through a user-selected JSON file.
7. Persist records locally and verify they survive app restarts and work without internet.

No login, cloud sync, AI features, or barcode scanning are required for the first MVP.

## Unit and money rules

- **Mass:** kg and g can convert to each other.
- **Volume:** litres (L) and millilitres (ml) can convert to each other.
- **Count:** pieces/units are used for items such as soap bars.
- Never mix incompatible units, such as kg and ml, in a stock calculation.
- Record actual purchase spending separately from any estimated value of remaining stock. A purchase entry stores its quantity, unit, price paid, and date.

## Engineering direction

The target is a native Android app, with Kotlin, Jetpack Compose, and Room/SQLite as the proposed implementation direction. Confirm the actual stack and minimum Android version during Phase 01 project setup.

## Documentation

- [Product requirements](docs/PRODUCT.md)
- [Roadmap and phase acceptance](docs/ROADMAP.md)
- [Working philosophy](docs/WORKING-PHILOSOPHY.md)

## Working rule

Work in small, reviewable phases. Do not mark a phase complete until its acceptance criteria have been checked and the relevant build/device evidence is recorded. After a phase is accepted, merge it into main, update project status, and create the next phase branch from the latest main.
