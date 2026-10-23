# HomeStock

HomeStock is a planned **offline-first native Android household inventory and spending tracker** for the user's sister and household.

**Status:** Planning & Research — implementation has not started.
**Working branch:** planning/research.

## Product at a glance

- First-run name setup and personalized time-based greeting.
- Home: greeting top-left, Settings top-right, clickable This month spending preview, three large category cards.
- Fixed categories: Food & Kitchen; Home Care & Cleaning; Personal Care & Cosmetics.
- Product rows: product name/current stock, plus action, three-dot menu. Tapping a row opens product history.
- Plus opens separate Add and Use actions.
- Add records quantity, compatible unit, actual amount paid and date. Use records quantity used and date.
- Stock and spending update immediately after a successful transaction.
- Date-grouped product history; no time-of-day display in the first version.
- Insights filters: Last 7 days, This month, Last 3 months, Custom date range and All time.

## Source-of-truth planning documents

- [Product requirements](docs/PRODUCT.md) — user flow, all product behaviors and business rules.
- [Architecture](docs/ARCHITECTURE.md) — selected technical stack, package structure, schema, state flow, calculations, privacy and test layers.
- [Acceptance tests](docs/ACCEPTANCE-TESTS.md) — concrete expected outcomes and validation cases.
- [Roadmap](docs/ROADMAP.md) — implementation phases and acceptance gates.
- [Branding direction](docs/BRANDING.md) — locked logo concept and color palette; final app name pending the intended user's decision.
- [Working philosophy](docs/WORKING-PHILOSOPHY.md) — planning-first engineering workflow.

## Key rules

- Native Android, local-first. No account, cloud sync or internet permission.
- Stock starts at zero and is derived from saved Add/Use transactions.
- Mass uses kg/g; volume uses L/ml; count uses whole pieces/units. Never mix dimensions.
- Product creation is separate from a purchase.
- Add requires quantity, compatible unit, actual amount paid and date.
- Use reduces stock only; it does not change money already spent.
- Spending totals derive from Add transactions and update immediately.
- Month filters follow calendar boundaries; custom ranges include both selected dates.
- Delete products only if they have no transaction history, with confirmation. Products with history can be archived only at zero stock; their history still counts toward spending.
- JSON backup/restore is deferred until after the intended user accepts the core app.

## Status honesty

This repository records planning decisions only. No app implementation, build, device run or test result is claimed as complete.
