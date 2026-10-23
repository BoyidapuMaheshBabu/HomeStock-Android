# Product Requirements

## 1. Purpose and user

IntiVentory is for the user's sister and household. The repository remains named HomeStock-Android. It is a simple offline native Android app to track household products, purchases, usage, quantities remaining and money spent on household products.

## 2. First launch

- Display the approved app name **IntiVentory** and use the logo assets documented in [Branding](BRANDING.md).
- Brand caption: **మన ఇంటి సరుకులు, మన ఇంటి లెక్కలు.**
- Ask for the user's name on first launch.
- Trim whitespace and reject a blank name.
- Save the name locally and reuse it on later launches.
- Show a friendly time-based greeting using device-local time.
- No account or internet connection is required.
- Start tracking purchases from app use; no opening-stock entry is required.

## 3. Home screen

The screen order is fixed:
1. Top row: greeting top-left; Settings gear top-right.
2. Compact clickable This month spending card showing actual recorded purchase spending from the first day of the current local calendar month through today, including the date range.
3. A short heading and exactly three large rectangular clickable hero cards: Food & Kitchen, Home Care & Cleaning, Personal Care & Cosmetics.

The card opens Insights with This month selected. If no purchases exist this month, show ₹0 and a no-purchases message. No separate Snacks or Drinks category and no top-level category creation. Keep detailed filters off the Home screen.

## 4. Fixed categories and starter products

- **Food & Kitchen:** Rice, Dal, Flour, Oil, Spices.
- **Home Care & Cleaning:** Toilet Cleaner, Floor Cleaner, Dishwashing Liquid, Detergent, Disinfectant.
- **Personal Care & Cosmetics:** Soap, Shampoo, Toothpaste, Hair Oil, Cream.

These are preset products inside exactly three top-level categories. Users may add custom products inside any of the three. No brand field.

Seed the preset product list once and begin all products with zero recorded stock. A product deliberately deleted before it has history must not be silently recreated on every launch.

## 5. Product list and product creation

Product rows show the name and current quantity on the left, plus action and three-dot menu on the right. Tapping the main row opens product history.

Creating a product is separate from purchasing it. Product creation asks for the name, fixed category context, and measurement type (Mass, Liquid, or Units); it does not ask for price, starting quantity, or a preferred unit. New products begin at zero stock.

A product uses one measurement type:
- Mass: kg/g.
- Liquid: L/ml.
- Units: pieces/items.

Starter products have sensible measurement type defaults. A product's measurement type may be changed before any transaction exists. Once transactions exist, the measurement type cannot be changed.

## 6. Product row actions and safety

### Plus action
Opens Add and Use choices:
- **Add:** purchase/stock-in.
- **Use:** consumption/stock-out.

### Three-dot menu
- **Edit:** product name and compatible preferred unit.
- **Delete:** only when the product has no transaction history; require confirmation.
- **Archive / Hide:** only when transaction history exists and stock is zero. Preserve all history and spending.
- If stock remains, block archive. For consumed, expired or discarded stock, record the quantity through Use; no separate disposal feature.
- Archived products disappear from active category lists and appear under Settings > Archived Products. Restore makes an archived item active again.
- A product with transaction history must never be permanently deleted through the UI.

## 7. Add transaction

From a product's plus action, select Add. The form shows the product, quantity, compatible unit, actual total amount paid in INR, and purchase date.

- Quantity must be positive and compatible with the product dimension.
- Mass uses kg/g; volume uses L/ml; count uses whole pieces/units.
- kg/L input may use up to three decimal places; g/ml and count use whole numbers.
- Amount paid is required, non-negative, and at most two decimal places. ₹0.00 is allowed for a free purchase.
- Date defaults to today; past dates can be selected; future dates are disallowed.
- Save one dated Add transaction, increase stock, and update all relevant spending totals immediately.
- The transaction must be counted exactly once.

## 8. Use transaction

From the plus action, select Use. Show current stock before the quantity input.

- Quantity must be positive and in a compatible unit.
- Date defaults to today; past dates can be selected; future dates are disallowed.
- If current stock is zero, block Use with a clear message.
- If requested quantity exceeds current stock, reject it, show available stock, and create no transaction.
- On success, save one dated Use transaction and reduce stock immediately.
- Use never changes historic purchase spending.
- Use covers expired or thrown-away quantities too; no separate disposal workflow is needed.
- Check stock and save Use atomically; disable Save while a write is in progress.

## 9. Stock calculation

Stored base units are grams for mass, millilitres for volume, and pieces for count.

**Remaining stock = total Add quantities − total Use quantities**, after converting units to the base unit.

Examples:
- 10 kg rice purchased; 1 kg used; 9 kg remains.
- 2 kg rice purchased; 500 g used; 1.5 kg remains.
- 1 L cleaner purchased; 200 ml used; 800 ml remains.
- 5 soap bars purchased; 1 used; 4 remain.

Never mix mass, volume or count. Never allow negative stock.

## 10. Product history

Tapping a product row opens a vertically scrollable, read-only transaction history:
- Group entries by local calendar date using Today, Yesterday or a readable date.
- Newer dates first; same-day entries newest-inserted first.
- Show Add/Purchase or Use, quantity, compatible unit and stock impact.
- Add/Purchase entries show actual amount paid; Use entries do not show a money amount.
- Show product-specific total recorded purchase spending.
- Do not display time-of-day in the first MVP.
- Transaction-row edit/delete controls are excluded from the first MVP.

## 11. Dashboard and spending Insights

The key money metric is actual recorded purchase spending—not an estimated market value of remaining stock.

If recorded purchase spending is ₹5,000 and a new ₹5 shampoo purchase is saved, the all-time total immediately becomes ₹5,005. Use changes stock only. Creating or editing a product alone does not change spending.

Filters use device-local calendar dates and inclusive boundaries:
- **Last 7 days:** today and previous six calendar dates.
- **This month:** first day of current calendar month through today.
- **Last 3 months:** first day of the calendar month two months before the current month through today.
- **Custom date range:** both selected start and end dates included; start must be on or before end; future dates not allowed.
- **All time / Since app start:** all saved Add transactions.

Example for October 23, 2026: Last 7 days is October 17–23; This month is October 1–23; Last 3 months is August 1–October 23. Last 3 months is a calendar period, not a rolling same-date range.

For each range, sum actual paid amounts from Add transactions in that range. Include archived products. Use never changes spending. Display the actual selected range. Empty range returns ₹0 and a clear empty state. Home's This month value must match Insights' This month value.

## 12. Offline storage, privacy and deferred features

Products, name, transactions, stock and spending are local. Room is the source of truth for product/transaction data; totals are derived from transactions. No account, cloud sync, internet permission, analytics or ad SDK is needed. JSON backup/restore comes after core-app acceptance.

## 13. Related specifications

- [Architecture](ARCHITECTURE.md)
- [Acceptance tests](ACCEPTANCE-TESTS.md)
- [Roadmap](ROADMAP.md)

