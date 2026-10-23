# Product Requirements

## 1. Problem and intended user

HomeStock is for the user's sister and household. It is a simple offline Android app for tracking household-product purchases, usage, available quantities, and—especially—how much money the household spends over time.

## 2. First launch

- Ask for the user's name on first launch and save it locally.
- Show a friendly time-based greeting, such as “Good morning, Priya!”
- Do not require an account or internet connection.
- Tracking begins with purchases the user records after starting to use the app. Do not require opening-stock entry for products already in the house.

## 3. Home screen and fixed categories

The home screen shows exactly three large, rectangular, clickable hero cards:

1. **Food & Kitchen:** rice, dal, flour, oil, spices, and related staples.
2. **Home Care & Cleaning:** toilet/bathroom/floor cleaner, dishwashing liquid, detergent, disinfectant, and related products.
3. **Personal Care & Cosmetics:** soap, shampoo, toothpaste, hair oil, creams, and related products.

Do not add separate top-level categories for snacks, drinks, or other groups in the first version. Do not provide a feature to add top-level categories.

## 4. Product management within each category

Each category contains a list of preset starter products and an option to add a custom product.

Each product row should have:
- Product name on the left.
- Plus (+) action on the right.
- Three-dot overflow menu with Edit and Delete.
- A tappable main row area that opens product transaction history.

Tapping the plus action opens two separate transaction actions: **Add** and **Use**. These are not the same as creating a product.

### New product setup

Creating a product is a separate workflow. It asks for the product name, category, and measurement dimension/unit. It does not automatically open a purchase form or require a price. The new product starts with zero recorded stock until the user records a purchase.

### Product editing

- Allow editing of the product name and its compatible measurement unit.
- Do not ask for or store a brand.
- Never let a unit change silently reinterpret old transaction quantities.
- If transaction history already exists and a dimension change is requested (such as mass to volume), block it or require a safe migration path rather than changing old meaning.

### Delete, archive, and history safety

- **Delete:** If a product has no purchase or usage transactions, allow permanent deletion only after a confirmation dialog. This is for products created accidentally or never used.
- **Archive / Hide:** If a product has transaction history and its current stock is zero, offer Archive / Hide. Remove it from the active product list, but preserve its transaction history and all spending totals.
- **Stock remains:** Do not allow Archive / Hide while current stock is greater than zero. The user can record the remaining quantity through Use if it was consumed, expired, or thrown away; when stock reaches zero, the product can be archived.
- An archived product's historic transactions remain available from its history and remain included in the date-appropriate spending totals.
- Editing or deleting must never silently reinterpret old quantities or corrupt spending.

## 5. Unit model

Each product uses one measurement dimension:
- **Mass:** kilograms (kg), grams (g).
- **Volume:** litres (L), millilitres (ml).
- **Count:** pieces/units, such as bars of soap.

Only convert within the same dimension. Do not convert mass to volume without product-specific density information. Show appropriate unit choices for the product's dimension; never allow kg/ml/count to be mixed within one product's stock arithmetic.

## 6. Product list and plus-action workflow

### A. Add transaction (purchase / stock-in)

The user taps the product's plus action and chooses **Add**. The app shows an Add form for that product, including:
- Product name for context.
- Quantity.
- A unit selector restricted to that product's measurement dimension (for example, kg/g for rice; L/ml for oil or cleaner; pieces/units for soap).
- Actual amount paid in INR (₹).
- Purchase date.

On confirmation, save a separate purchase transaction. Increase stock by the entered quantity and include the paid amount in spending totals for the purchase date. Update relevant UI immediately. The same transaction must not count twice.

### B. Use transaction (consumption / stock-out)

The user taps the product's plus action and chooses **Use**. The form shows the current available stock and its unit before requesting the quantity.

Use represents stock leaving inventory. It covers normal consumption and, when needed, stock that expired or was thrown away. The user records the amount through the same Use flow; no separate disposal feature is needed in the first version.

On confirmation, save a dated Use transaction and deduct the quantity immediately. Use does not reduce recorded purchase spending because the money was already spent when the purchase occurred.

### C. Stock validation

- Reject zero or negative usage quantities.
- Do not allow a usage quantity greater than current stock.
- If current stock is 1 kg and the user enters 10 kg, reject the transaction and explain the available stock.
- Normalize compatible units before checking and subtracting (for example, 500 g used from 2 kg stock leaves 1.5 kg).
- Never allow negative stock due to invalid input, double taps, or stale displayed values.
- Only update stock after the transaction is successfully saved.

## 7. Product transaction history screen

Tapping the product row (not its plus action or overflow menu) opens that product's transaction history.

Requirements:
- Vertically scrollable transaction list.
- Group entries by date, using familiar date labels such as Today, Yesterday, or a calendar date.
- Date is required; time-of-day is not required in the first version.
- Label each entry clearly as Add/Purchase or Use.
- Show quantity and compatible unit.
- For Add/Purchase entries, show the actual amount paid.
- Show stock impact or resulting balance so users can understand how stock changed.
- Show total recorded spending for this product if it can be presented clearly.
- The list must preserve transaction history rather than deriving it from only the product's current values.

## 8. Dashboard and spending Insights

Spending is the primary money metric.

### Cumulative spending

Show total money spent on purchases recorded since the user started using HomeStock, through today. It does not decrease when products are used.

Example:
- Previously recorded purchase spending: ₹5,000.
- Newly saved shampoo purchase: ₹5.
- Immediately displayed all-time total: **₹5,005**.

This amount means total recorded purchase spending, not the current market value of products remaining in the home. Adding/editing a product alone does not affect spending.

### Date filters

Provide:
- **Last 7 days:** today and the previous six calendar dates.
- **This month:** the first day of the current calendar month through today.
- **Last 3 months:** the first day of the calendar month two months before the current month through today. This includes the current month-to-date plus the two preceding calendar months.
- **Custom date range:** the user chooses a start date and end date; both dates are included.
- **All time / Since app start:** all recorded purchases from the start of tracking through today.

Month-based filters must follow calendar-month boundaries, not rolling same-date intervals. For example, on October 23, 2026, This month covers October 1–23, while Last 3 months covers August 1–October 23. Each filtered total is the sum of purchase amounts whose purchase dates fall within the selected inclusive range. Show the selected range and use the device's local calendar/date context. The rolling-versus-calendar-month decision is settled: use calendar-month boundaries.

### Immediate updates

After an Add/Purchase transaction is saved:
- Update current stock and the product's transaction history.
- Update applicable date-filtered spending.
- Update all-time spending.
- Count the transaction exactly once.

After a Use transaction is saved:
- Update current stock and usage history.
- Do not change purchase-spending totals.

## 9. Offline behavior and persistence

Product definitions, saved user name, purchase transactions, usage transactions, stock, and spending summaries must persist locally and work without internet access. No account or cloud sync is required.

## 10. Deferred backup

JSON backup/restore is deferred until after the core app is built and accepted by the intended user.

## 11. First-version exclusions

- Product brands.
- Additional top-level categories.
- Mandatory opening-stock entry.
- Account/cloud sync.
- AI recommendations, barcode scanning, receipt OCR, automatic price lookup.
- JSON backup/restore in the core MVP.

## 12. MVP acceptance criteria

- Home has exactly three large clickable category cards.
- A category shows product rows with the name on the left, plus action, and three-dot menu.
- Creating a product is separate from recording a purchase; a new product starts at zero stock.
- Plus opens separate Add and Use actions.
- Add records quantity, compatible unit, actual amount paid, and date; stock and spending update immediately.
- Use displays current stock, records quantity used and date, and subtracts only if valid.
- Example: 10 kg rice purchased, 1 kg used, 9 kg remains.
- If 1 kg remains, trying to use 10 kg is rejected and creates no invalid transaction.
- Product tap opens a scrollable transaction history grouped by date, without requiring time-of-day.
- Add/Purchase history shows amount paid; Use history shows quantity consumed and stock impact.
- Product Edit/Delete/Archive behavior is safe: Delete is for products with no transactions and requires confirmation; products with history can be archived only when stock is zero, preserving history and spending.
- The all-time total changes immediately from ₹5,000 to ₹5,005 after saving a ₹5 purchase.
- Dashboard spending filters support Last 7 days, This month (from the 1st through today), Last 3 months (from the 1st of the month two months earlier through today), Custom date range (inclusive), and All time / Since app start.
- Use can represent consumption or expired/discarded stock. Use changes stock, not past purchase spending.
- A product with no transactions can be deleted after confirmation. A product with history can be archived only when stock is zero, and its history/spending remain preserved.
- Core records persist across restart and work offline.
- JSON backup/restore remains deferred until after user acceptance.
