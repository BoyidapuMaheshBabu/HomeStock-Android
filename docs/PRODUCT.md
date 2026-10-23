# Product Requirements

## 1. Problem and intended user

HomeStock is for the user's sister and household. The main need is to record household-product purchases and quantities, see what remains, and monitor how much money is spent on home products over time.

The intended user wants a simple Android app rather than a website or complicated inventory system.

## 2. Product outcome

Starting from the first day she uses HomeStock, the user can record each household purchase, track product usage and remaining quantity, and see spending totals for selected time periods. The core experience works offline and updates totals immediately after a purchase is saved.

## 3. First-launch experience

- On first launch, ask for the user's name.
- Save the name locally and reuse it on later launches.
- Show a greeting based on device local time, such as “Good morning, Priya!”
- Do not require an account or internet connection.
- The user begins tracking with purchases recorded from that point onward. No opening-stock entry is required.

## 4. Fixed top-level home categories

The first version has exactly three top-level categories:

1. **Food & Kitchen:** rice, dal, flour, oil, spices, and related household food staples.
2. **Home Care & Cleaning:** toilet/bathroom/floor cleaner, dishwashing liquid, detergent, disinfectant, and related products.
3. **Personal Care & Cosmetics:** soap, shampoo, toothpaste, hair oil, creams, and related products.

Do not add separate top-level categories for snacks, drinks, or other groups in the first version. The intended user does not want those categories. Keep the three main home categories fixed.

## 5. Product customization within fixed categories

- Provide common starter products inside each fixed category.
- Provide **Add product** inside each category for items not included in the starter list.
- Each product has a three-dot menu with **Edit** and **Delete**.
- Edit allows the user to change the product name and compatible unit, subject to data-integrity safeguards.
- Do not ask the user to select or maintain a brand.
- New starter/custom product definitions begin with zero recorded stock. The first recorded purchase increases available stock.

### Product edit/delete safety

- Convert units only within their measurement dimension: kg/g, L/ml, or pieces/units.
- Never reinterpret existing kg history as litres or otherwise change the meaning of saved quantities.
- If a product has purchase or usage history, deletion must not silently erase transactions or alter spending totals. Final behavior (such as archive/hide or blocking deletion) remains an open design decision for planning.
- Require confirmation for destructive actions.

## 6. Unit model

Each product uses one measurement dimension:
- **Mass:** kilograms (kg), grams (g).
- **Volume:** litres (L), millilitres (ml).
- **Count:** pieces/units, such as bars of soap.

Only convert units within the same dimension. Do not convert mass to volume without product-specific density information.

## 7. Purchase, usage, and stock workflow

### A. Record a purchase

For every purchase, the user selects the product and records:
- Quantity and compatible unit
- Actual amount paid in INR (₹)
- Purchase date

Each purchase is a separate dated record. Do not store only one mutable price on the product: the same product may have a different price on a later purchase.

Saving a purchase must immediately update available stock, purchase history, and all applicable dashboard/Insights spending totals. Totals should derive from the saved transaction, not be incremented separately in a way that can count it twice. There should be no manual refresh requirement.

### B. Record usage

The user records the quantity used and date. Usage reduces available stock and updates the displayed remaining quantity immediately. Usage does **not** subtract from historical purchase spending, because the money was already spent when the purchase was made.

The app must prevent or clearly handle usage greater than available stock.

### C. Current stock

The first version starts with zero recorded stock and does not require the user to declare products already in the home. For each product:

**Remaining stock = recorded purchased quantity − recorded used quantity**, after compatible-unit normalization.

Quantity and unit show what remains; they are not a financial estimate of the amount of money currently on the shelf.

## 8. Dashboard and spending Insights

Spending is the **primary dashboard metric**. The app is a practical household expense and inventory tracker: she records purchases and the app calculates the totals.

### Cumulative spending since app start

The app must show total money spent on purchases recorded since the user's first recorded purchase in HomeStock, through today. This total does not decrease when a product is used, because usage does not reverse money already spent.

**Example:**
- Previously recorded household purchases: ₹5,000
- Newly saved shampoo purchase: ₹5
- New cumulative spending total: **₹5,005**

This number means **total recorded purchase spending**, not the estimated market value of current remaining stock. Adding/editing a product definition alone does not change spending. Saving a purchase with its actual amount does.

### Date filters

Provide these spending views:
- **Last 7 days**
- **Last 1 month**
- **Last 3 months**
- **Custom date range**
- **All time / Since app start**

Each filtered total is the sum of actual purchase amounts whose purchase dates fall within the selected range. Use the device's local calendar/date context for filter boundaries and display the range clearly. Exact semantics for “Last 1 month” and “Last 3 months” (rolling intervals versus calendar months) must be finalized during planning before implementation.

### Quantity alongside spending

The dashboard/Insights must also make available remaining product quantities using appropriate units, with access to purchase and usage history. Keep monetary totals distinct from remaining stock quantities. Category-level spending may be included if it helps the user's decisions without cluttering the screen.

### Immediate recalculation rules

After a purchase is successfully saved:
- Recalculate the applicable date-filtered spending total.
- Recalculate the all-time spending total.
- Update purchase history and available stock.
- Ensure the same saved purchase is counted exactly once.

After usage is saved:
- Recalculate remaining quantity and usage history.
- Do not alter historical purchase-spending totals.

## 9. Offline behavior and local persistence

- Core product, purchase, usage, stock, and spending features work without internet.
- Name, product definitions, purchase records, and usage records persist across app restarts.
- No account, cloud database, or multi-device sync is required in the first version.

## 10. Deferred feature: JSON backup and restore

JSON backup/restore is **not part of the initial core-app scope**. Revisit it only after the core app is built and the intended user has reviewed and accepted it.

## 11. First-version exclusions

- Product brand selection or brand management.
- Additional top-level home categories.
- Mandatory opening-stock entry for products already in the home.
- User accounts, cloud sync, or online-only behavior.
- AI recommendations, barcode scanning, receipt OCR, or automatic price lookup.
- JSON backup/restore in the initial core version.

## 12. MVP acceptance criteria

- First launch asks for the user's name once, saves it, and later shows a time-appropriate greeting.
- Home shows exactly the three fixed categories.
- The user can select preset products, add custom products, and edit/delete products without silently corrupting historical records.
- A purchase records product, quantity, compatible unit, actual amount paid, and date.
- A usage entry reduces the correct product's stock.
- Example stock checks pass: 5 kg rice purchased, 1 kg used, 4 kg remains; 1 L cleaner purchased, 200 ml used, 800 ml remains; 10 soap bars purchased, 2 used, 8 remain.
- If the tracked purchase total is ₹5,000 and a ₹5 purchase is saved, the all-time spending total immediately shows ₹5,005.
- The dashboard supports Last 7 days, Last 1 month, Last 3 months, Custom date range, and All time / Since app start.
- Date-filtered totals reconcile exactly with purchase transactions in each range.
- Usage changes stock, not past purchase spending.
- Totals persist and remain correct after app restart; core workflows work in airplane mode.
- JSON backup/restore is deferred until the intended user accepts the core app.
