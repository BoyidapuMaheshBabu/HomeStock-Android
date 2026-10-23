# Product Requirements

## 1. Problem statement

A household needs a simple way to track supplies purchased, purchase quantities and prices, consumption, remaining stock, and spending over time. Paper notes and memory make it difficult to answer everyday questions such as “How much rice is left?”, “When did we last buy floor cleaner?”, and “How much did we spend on household supplies this month?”

## 2. Primary user

A household member who is comfortable using a phone app but does not want to manage a website, account, or complicated inventory system.

## 3. Product outcome

The user can create a product, record a purchase, record usage, and see a credible remaining quantity and relevant history—all on the device and without internet access.

## 4. Product categories

1. **Food & Kitchen:** rice, dal, flour, oil, spices, sugar, and related items.
2. **Home Care & Cleaning:** bathroom/toilet/floor cleaners, dishwashing liquid, detergent, disinfectant, and related items.
3. **Personal Care & Cosmetics:** soap, shampoo, toothpaste, hair oil, creams, and related items.

Categories should be easy to scan and select. Category naming must use familiar household language.

## 5. Core user journeys

### A. Add a product
Choose a category, enter product name, choose a stock unit, and save. The product should be easy to find again and should be editable.

### B. Record a purchase
Select a product and enter the quantity purchased, amount paid in rupees, and purchase date. This creates a purchase-history entry and increases stock.

### C. Record usage
Select a product and enter the quantity used. This creates a usage-history entry and reduces stock. The app must prevent or clearly handle usage greater than available stock.

### D. Review inventory
See the current remaining quantity in the product's chosen unit. The UI must not mix incompatible measurement dimensions.

### E. Review history and spending
See dated purchase and usage entries. Spending summaries must use actual recorded purchase amounts, not an estimate of the value of remaining stock.

## 6. MVP requirements

- Native Android application; not a website wrapper.
- Works offline for the core workflow.
- Local persistence across app restarts.
- Category browsing.
- Create, view, and edit products.
- Purchase entries: quantity, unit, price paid, and date.
- Usage entries: quantity and date.
- Calculated remaining stock.
- Basic per-product history and total/period purchase spending where feasible.
- Clear empty states, input validation, and understandable success/error feedback.
- Usable on a physical Android phone with touch-friendly controls.

## 7. Calculation and data-integrity rules

- Store a consistent base quantity per measurement dimension and convert before arithmetic; alternatively, restrict each product to a single selected unit family and normalize its quantities.
- Mass: kg and g can convert to each other.
- Volume: L and ml can convert to each other.
- Count: pieces/units stay separate unless a later requirement defines package-size conversions.
- Never convert mass to volume without product-specific density information.
- Stock = opening stock + valid purchased quantities − valid usage quantities, after unit normalization.
- Opening stock, if offered, must be explicitly entered and must not silently count as current-period spending.
- Purchase spend = sum of recorded purchase amounts for the selected period.
- Validate positive quantities, non-negative prices, dates, and stock rules.
- Define the currency as INR (₹) for this MVP.

## 8. Out of scope for the first MVP

- User accounts, cloud backend, and multi-device sync.
- AI recommendations or forecasting.
- Barcode scanning, receipt OCR, and automatic price lookup.
- Complex supplier/warehouse management.
- Mandatory notifications or background services.
- Online-only functionality.

## 9. UX principles

- Build for a household member, not an inventory specialist.
- Prioritize the common tasks: check stock, add a purchase, record usage.
- Prefer plain labels, readable quantities, and large touch targets.
- Use clear category cards and avoid cluttering the first screen.
- Ask only for information needed for the chosen action.
- Make destructive actions explicit and avoid silently losing records.

## 10. MVP acceptance criteria

A user can create a product, record a purchase, record usage, and see the correct remaining stock. For example: 5 kg rice purchased, 1 kg used, 4 kg remains. For volume: 1 L cleaner purchased, 200 ml used, 800 ml remains. Records survive app restart and the core workflow works in airplane mode. The shown spending total matches recorded purchase amounts.
