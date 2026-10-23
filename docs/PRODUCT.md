# Product Requirements

## 1. Problem statement

A household needs a simple way to track supplies purchased, purchase quantities and prices, consumption, remaining stock, and spending over time. Paper notes and memory make it difficult to answer everyday questions such as “How much rice is left?”, “When did we last buy floor cleaner?”, and “How much did we spend on household supplies this month?”

## 2. Primary user

A household member who is comfortable using a phone app but does not want to manage a website, account, or complicated inventory system.

## 3. Product outcome

The user can personalize the app, record a product purchase, record usage, and see a credible remaining quantity and relevant history—all on the device and without internet access. The user can also export a JSON backup and restore saved data later.

## 4. First-launch and home experience

- On first launch, request the user's name with a simple welcome screen.
- Save the name locally and reuse it on later launches; do not ask every time.
- Show a friendly greeting based on the device's local time, such as “Good morning, Priya!”, with appropriate afternoon/evening wording.
- Let the user reach the main household categories quickly.
- Include the saved name in the app's JSON backup so restoring a backup can restore personalization too.
- Avoid requiring an account, network access, or cloud service.

## 5. Product categories

The initial home screen has three fixed top-level categories:

1. **Food & Kitchen:** rice, dal, flour, oil, spices, sugar, and related items.
2. **Home Care & Cleaning:** bathroom/toilet/floor cleaners, dishwashing liquid, detergent, disinfectant, and related items.
3. **Personal Care & Cosmetics:** soap, shampoo, toothpaste, hair oil, creams, and related items.

Use familiar starter product choices within each category to make common entries fast. Users must also be able to add their own products within a category. Adding new top-level home categories is out of scope for the first version.

## 6. Core user journeys

### A. Add a product
Choose a category, select a familiar starter product or enter a custom product name, select the appropriate unit family, and save. Products should be easy to find and edit.

### B. Record a purchase
Select a product and enter the quantity, unit, actual amount paid in rupees, and purchase date. This creates a purchase-history entry and increases stock. A purchase record must retain its own price and quantity rather than relying only on a single current price on the product.

### C. Record usage
Select a product and enter the quantity used and date. This creates a usage-history entry and reduces stock. The app must prevent or clearly handle usage greater than available stock.

### D. Review inventory
See the current remaining quantity in the product's chosen unit. The UI must not mix incompatible measurement dimensions.

### E. Review history and spending
See dated purchase and usage entries. Spending summaries must use actual recorded purchase amounts, not an estimate of the value of remaining stock.

### F. Back up and restore
Allow the user to create a JSON backup through the Android file picker/share flow and restore from a selected JSON file. Include the saved name, products, purchase records, usage records, and required category/unit metadata. Validate the file before modifying current data, clearly explain whether restore replaces existing data, and require confirmation to avoid accidental overwrites. The core app must not need the internet for backup or restore.

## 7. MVP requirements

- Native Android application; not a website wrapper.
- Works offline for the core workflow.
- Local persistence across app restarts.
- First-launch name setup and time-appropriate personalized greeting.
- Three fixed top-level categories with familiar starter product choices.
- Create, view, and edit custom products within a category.
- Product-appropriate units: mass (kg/g), volume (L/ml), and count (pieces/units).
- Purchase entries: quantity, compatible unit, actual price paid, and date.
- Usage entries: quantity, compatible unit, and date.
- Calculated remaining stock.
- Basic per-product history and total/period purchase spending.
- User-initiated JSON backup and restore, including the saved name and household records.
- Clear empty states, input validation, and understandable success/error feedback.
- Usable on a physical Android phone with touch-friendly controls.

## 8. Calculation and data-integrity rules

- Give each product a measurement dimension: mass, volume, or count. Quantities may convert only inside the same dimension.
- Mass: kg and g can convert to each other.
- Volume: litres (L) and millilitres (ml) can convert to each other.
- Count: pieces/units, such as soap bars, stay separate unless a future requirement explicitly defines package-size conversions.
- Never convert mass to volume without product-specific density information.
- Stock = opening stock + valid purchased quantities − valid usage quantities, after unit normalization.
- Do not silently mix quantities recorded using incompatible dimensions for the same product.
- Opening stock, if offered, must be explicitly entered and must not silently count as current-period spending.
- Purchase spend = sum of recorded purchase amounts for the selected period.
- Validate positive quantities, non-negative prices, dates, and stock rules.
- Define the currency as INR (₹) for this MVP.
- Restore must validate schema/version and required fields before applying imported data. Failed validation must not partially overwrite the current inventory.

## 9. Out of scope for the first MVP

- User accounts, cloud backend, and multi-device sync.
- AI recommendations or forecasting.
- Barcode scanning, receipt OCR, and automatic price lookup.
- Creating new top-level home categories.
- Complex supplier/warehouse management.
- Mandatory notifications or background services.
- Online-only functionality.

## 10. UX principles

- Build for a household member, not an inventory specialist.
- Prioritize the common tasks: check stock, add a purchase, record usage.
- Prefer plain labels, readable quantities, and large touch targets.
- Use clear category cards and avoid cluttering the first screen.
- Ask only for information needed for the chosen action.
- Make destructive actions explicit and avoid silently losing records.
- Use starter product choices for convenience, without trapping users in a fixed list.

## 11. MVP acceptance criteria

A user can enter their name once, close and reopen the app, and receive a time-appropriate greeting using that saved name. The user can add rice, record a purchase of 5 kg for ₹350, record 1 kg used, and see 4 kg remaining. A liquid example must also pass: purchase 1 L of cleaner, record 200 ml used, and see 800 ml remaining. A count example must support soap bars as pieces/units without being forced into kg or litres. Purchase spending must match the amounts entered. All core data must survive app restart and work in airplane mode. A JSON backup must contain the saved name and household records, and a valid restore must recover them.
