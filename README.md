# IntiVentory — Home Inventory & Spending Tracker

IntiVentory is an **offline-first native Android household inventory and spending tracker**.

**Status:** Phases 01–07 are recorded complete. On 2026-11-02, Mahesh confirmed that he physically tested the app's functionality on a device and that everything worked as expected. See [Phase 07 verification](docs/phases/phase-07-verification.md) for the user-reported acceptance and remaining automated-evidence notes.
**Current baseline:** `main` (Phases 01–07 recorded complete).
**Baseline:** Planning and branding were merged via PR #1; Phase 01 via PR #2; Phase 02 via PR #3; Phase 03 via PR #5; Phase 04 via PR #6; Phase 05 via PR #7; Phase 06 via PR #8.

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
- Insights: five date filters, total purchase spending, purchase count and average, a spending trend chart, category spending shares, and top five spending products.

## Documentation

- [Product requirements document](docs/PRD.md) — product behavior, user flows, and business rules.
- [Architecture](docs/ARCHITECTURE.md) — technical decisions, data model, state flow, calculations, and test layers.
- [Acceptance tests](docs/ACCEPTANCE-TESTS.md) — expected outcomes and traceability scenarios.
- [Roadmap](docs/ROADMAP.md) — phase scope, delivery history, and acceptance gates.
- [Progress summary](docs/PROGRESS.md) — current baseline, completion status, and verification limits.
- [Branding](docs/BRANDING.md) — approved name, Telugu caption, SVG references, and color palette.
- [Development workflow](docs/DEVELOPMENT-WORKFLOW.md) — planning, evidence, branching, and review process.
- [Phase records](docs/phases/README.md) — phase verification documents and the Phase 06 design reference.
- [Architecture decision records](docs/adr/README.md) — concise history of significant technical decisions.
- [Agent instructions](AGENTS.md) — repository-specific guidance for AI coding agents.

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

Phases 01–07 are recorded complete on the `main` baseline. Phase 05 added product-specific transaction history and correction safeguards (PR #7); Phase 06 added spending filters and Insights analytics (PR #8). Phase 07 records the project owner's physical-device functional acceptance on 2026-11-02.

That manual acceptance is owner-reported evidence. It does not mean every checkbox in `docs/ACCEPTANCE-TESTS.md` was recorded as an individual pass, and it does not imply a new Gradle test or build run. See [Progress](docs/PROGRESS.md) and the [phase records](docs/phases/README.md) for evidence limits.

JSON backup/restore remains deferred and must not be described as an implemented feature.
