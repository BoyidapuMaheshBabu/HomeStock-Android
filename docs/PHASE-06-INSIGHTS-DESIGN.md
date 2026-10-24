# Phase 06 — Spending Insights Screen Design

**Branch:** `phase/06-spending-dashboard-and-insights`
**Status:** Implemented scope reference for Phase 06, accepted and merged into `main` via [PR #8](https://github.com/BoyidapuMaheshBabu/HomeStock-Android/pull/8). This document records the agreed screen design and acceptance contract.
**Platform:** Native Android, Kotlin, Jetpack Compose + Material 3, offline-first.

## Goal

Turn Insights from a single total with date filters into a useful household-spending view that answers:

1. How much was spent in the selected period?
2. When did purchases happen?
3. Which fixed category accounted for the most spending?
4. Which products accounted for the most purchase spending?

All figures must come from saved local ADD transactions. Use transactions never count as spending. Do not use sample values in the live app.

## Screen layout and order

Use one vertically scrollable screen. Keep existing app navigation, typography, brand palette, and Material 3 patterns. Reuse the existing `InsightsScreen` and `InsightsViewModel` where applicable; inspect the current working tree before editing so completed work is preserved.

### 1. Top app bar

- Back navigation on the left.
- Title: **Spending Insights**.
- Follow the existing app bar and theme.

### 2. Global date filters

Show five single-selection filters:

- Last 7 days
- This month
- Last 3 months
- Custom
- All time

Use compact Material 3 filter chips or a segmented treatment consistent with the existing app. On narrow screens, allow horizontal scrolling rather than compressing text or overflowing.

The selection applies to **every** section below: summary, trend, category breakdown, and product ranking. Default to This month. Opening Insights from Home's This month card must select This month.

### 3. Spending summary card

Show, in this order:

- Label: **Total purchase spending**
- Large formatted INR amount.
- Human-readable selected date range.
- Two secondary metrics side by side when the available width permits:
  - **Purchases** — number of saved ADD transaction records in the selected range.
  - **Average per purchase** — selected-range spending total divided by the number of ADD transaction records in that range, rounded to the nearest paise for display.

A free purchase of ₹0 still counts as a purchase record, but contributes ₹0 to spending. If the purchase count is zero, show an em dash for average and a helpful no-purchases hint; do not divide by zero. No hard-coded metrics or demo numbers in production UI.

### 4. Spending trend card

Title: **Spending trend**. Draw a simple, readable bar chart of purchase spending over the selected period.

The chart must:
- Aggregate only ADD amounts by the transaction's editable local calendar date.
- Include zero-spend buckets inside the displayed date range when appropriate, so the timeline is not misleading.
- Label each bucket clearly and display exact bucket amount on tap or another accessible interaction.
- Handle long ranges without making labels overlap or rendering an unusably dense chart.
- Display a clear empty state when there is no positive spending to plot.
- Use accessible descriptions/semantics so the chart is not visual-only.

Use existing chart functionality if already available. Otherwise prefer a small Compose-native chart or other lightweight implementation using existing dependencies. Do not add a new charting dependency unless there is a documented, concrete need.

#### Deterministic bucket rules

- **Last 7 days:** one bucket per local calendar day (7 buckets).
- **This month:** one bucket per local calendar day from the first of the month through today; horizontally scroll if necessary on narrow screens.
- **Last 3 months:** one bucket per calendar month (current month is month-to-date).
- **Custom range:** daily buckets for ranges of 31 calendar days or fewer; weekly buckets for 32–180 days; monthly buckets for ranges longer than 180 days. Weekly buckets use Monday–Sunday and are clipped to the selected start/end dates.
- **All time:** one bucket per calendar month from the month of the first saved ADD transaction through the current local month.

Buckets must use local calendar dates, not fixed-millisecond day arithmetic. Range ends are inclusive. For a custom range, the first and last buckets may be partial. If there are no ADD transactions at all, show an empty state rather than an invented timeline.

### 5. Spending by category card

Title: **Spending by category**. Show the three fixed categories:

- Food & Kitchen
- Home Care & Cleaning
- Personal Care & Cosmetics

For each category, show:
- Category name.
- Total purchase spending in the selected range.
- Percentage share of the overall selected-range spending.
- A proportionate horizontal progress bar.

Category totals must be derived from ADD transactions joined to their product's fixed category. Keep purchases for archived products included. Calculate shares from the selected-range spending total; when that total is zero, do not divide by zero or show misleading percentages. Show a useful no-spending state when the range contains no positive spending.

### 6. Top spending products card

Title: **Top spending products**. Show up to five products ranked by their total ADD amount in the selected period, highest spending first.

Each row displays:
- Product name.
- Category name.
- Total amount paid for that product during the selected period.
- An unobtrusive **Archived** label for archived products, if applicable.

Include archived products in the ranking because archiving preserves history and spending. Exclude permanently deleted products naturally because their product and transaction records have been deleted. Include only products with positive total spending in this ranking; free purchases still count in summary purchase count but are not a top-spending item. Break equal totals deterministically, such as by product name. If none qualify, show a clear empty state.

If all five are displayed, no extra "See all" screen is required for Phase 06.

## Shared business rules

- All totals use saved ADD transactions only.
- All sections use exactly the same selected period and inclusive date boundaries.
- All spending amounts use integer paise in the domain/data layer and existing INR formatting utilities.
- Do not keep an independently updated spending counter or hard-code example totals.
- Purchases for archived products remain in total spending, trend, category breakdown, product ranking, purchase count, and average.
- Editing an ADD amount/date updates every affected section through observable database/repository state.
- Editing or adding a USE transaction changes stock/history only and never changes spending analytics.
- Deleting a product removes its associated transactions, so those transactions no longer contribute to analytics.
- A selected period with no ADD records shows ₹0, zero purchases, average as an em dash, and helpful empty states where appropriate. A period containing only free purchases shows a purchase count but no positive-spending chart/category/product results.
- Do not invent budget, savings, forecasting, overspending, or AI insights as part of this phase.
- Keep the app fully offline. No account, network access, analytics SDK, or remote service is required.

## Data and architecture expectations

Inspect existing Room queries and repository APIs before adding new ones. Add focused, parameterized queries or testable domain calculations as needed for:

- Spending total and ADD transaction count over the same selected range.
- Spending trend buckets.
- Spending grouped by product category.
- Spending grouped by product, with product name, category, and archive state.
- The average per purchase, derived safely from total paise and purchase count.

Do not create a parallel data source. Keep data calculations testable and feed the screen through the existing ViewModel and observable repository flow. Prefer a single consistent period-boundary calculation that is reused by all sections.

## Acceptance checklist

- The screen visually follows this section order: app bar, five filters, summary card, spending trend, category breakdown, top products.
- Selecting any filter updates every section and the selected range together.
- Home's This month amount agrees with Insights.
- Summary count and average are derived from saved ADD records and handle zero purchases safely.
- Trend buckets follow the deterministic aggregation rules above and remain readable on a phone.
- The three category rows show correct amounts, shares, and progress.
- The top five products are sorted by spending and include archived products.
- Empty, zero-spending, and long-range states are readable.
- Screen works offline and reacts to transaction add/edit/delete changes without manual refresh.
- Automated tests cover calculations, boundaries, grouping, archived products, free purchases, and empty states.
