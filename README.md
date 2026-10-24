# IntiVentory — Home Inventory & Spending Tracker

IntiVentory is an **offline-first native Android household inventory and spending tracker** being built for the user's sister and household.

**Status:** Phases 01–04 accepted. Phase 05 — Product transaction history is implemented on branch `phase/05-product-transaction-history` with 54 passing unit tests, ready for Mahesh's physical-device review.
**Working branch:** phase/05-product-transaction-history.
**Baseline:** Planning and branding were merged via PR #1; Phase 01 via PR #2; Phase 02 via PR #3; Phase 03 via PR #5; Phase 04 via PR #6.

## Product at a glance

- First-run name setup and personalized time-based greeting.
- Home: greeting top-left, Settings top-right, clickable This month spending preview, three large category cards.
- Fixed categories: Food & Kitchen; Home Care & Cleaning; Personal Care & Cosmetics.
- Product rows: product name/current stock, plus action, three-dot menu. Tapping a row opens product history.
- Plus opens separate Add and Use actions.
- Add records quantity, compatible unit, actual amount paid and date. Use records quantity used and date.
- Stock and spending update immediately after a successful transaction.
- Date-grouped product history; no time-of-day display in the history list.
- Every transaction can be edited; individual transaction deletion is available only for 30 minutes after recording.
- Insights filters: Last 7 days, This month, Last 3 months, Custom date range and All time.

## Source-of-truth planning documents

- [Product requirements](docs/PRODUCT.md) — user flow, all product behaviors and business rules.
- [Architecture](docs/ARCHITECTURE.md) — selected technical stack, package structure, schema, state flow, calculations, privacy and test layers.
- [Acceptance tests](docs/ACCEPTANCE-TESTS.md) — concrete expected outcomes and validation cases.
- [Roadmap](docs/ROADMAP.md) — implementation phases and acceptance gates.
- [Phase 01 verification record](docs/PHASE-01-VERIFICATION.md) — reported build/test results and Mahesh's physical-device acceptance.
- [Phase 02 verification record](docs/PHASE-02-VERIFICATION.md) — accepted Welcome/Home behavior and evidence limits.
- [Phase 03 verification record](docs/PHASE-03-VERIFICATION.md) — accepted catalogue scope, editor regression root cause and reported verification evidence.
- [Phase 04 verification record](docs/PHASE-04-VERIFICATION.md) — accepted Add/Use transaction scope and reported automated test evidence.
- [Branding direction](docs/BRANDING.md) — selected name IntiVentory, Telugu caption, SVG logo references, and locked color palette.
- [Working philosophy](docs/WORKING-PHILOSOPHY.md) — planning-first engineering workflow.

## Key rules

- Native Android, local-first. No account, cloud sync or internet permission.
- Stock starts at zero and is derived from saved Add/Use transactions.
- Each product has one measurement type: Mass (kg/g), Liquid (L/ml), or Units (whole pieces). Compatible quantities convert exactly into integer base units.
- Product creation asks for a name and measurement type, not a preferred unit, starting quantity, or price.
- Add requires quantity, compatible unit, actual amount paid and date.
- Use reduces stock only; it does not change money already spent.
- Spending totals derive from Add transactions and update immediately.
- Month filters follow calendar boundaries; custom ranges include both selected dates.
- Measurement type can change only before a product's first transaction. After any transaction, it is locked even when stock returns to zero.
- Archive hides a product while preserving stock-derived quantity, history and spending, regardless of current stock. Restore returns it to active use.
- Delete permanently removes the product and all its associated transaction history after explicit destructive confirmation; the operation must be atomic and spending totals recalculate.
- Transaction correction rules preserve stock integrity: Delete is time-limited to 30 minutes after recording, while Edit remains available.
- JSON backup/restore is deferred until after the intended user accepts the core app.

## Implementation status

Phases 01–04 are accepted: the native Android foundation, Welcome/name setup, Home dashboard, product catalogue/management, and Add/Use transaction workflows with stock rules are implemented. Phase 05 — Product transaction history with always-available editing and 30-minute Delete window is implemented on branch `phase/05-product-transaction-history` with 54 passing unit tests, ready for physical-device review. Full Insights behavior remains assigned to Phase 06.
