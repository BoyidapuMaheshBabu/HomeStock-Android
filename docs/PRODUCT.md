# Product Requirements

## 1. Problem and intended user

HomeStock is for the user's sister and household. The intended user wants a simple Android app to understand which household products have been bought, how much remains, how much has been used, and especially how much money is spent on household products.

The user is comfortable with a phone app and does not want to manage a website, account, or complicated inventory system.

## 2. Product outcome

The user can record new household purchases from the day she starts using HomeStock, track usage and remaining quantity, and review actual purchase spending over time—all offline on the phone.

## 3. First-launch experience

- On first launch, ask for the user's name.
- Save the name locally and reuse it on later launches.
- Show a time-appropriate greeting, such as “Good morning, Priya!”, based on the device's local time.
- Do not require an account or internet connection.
- The user is starting tracking from the app's beginning, so the first version does not require entering existing household stock as opening stock.

## 4. Fixed top-level home categories

The first version has exactly three top-level home categories:

1. **Food & Kitchen:** preset examples include rice, dal, flour, oil, and spices.
2. **Home Care & Cleaning:** preset examples include toilet/bathroom/floor cleaner, dishwashing liquid, detergent, and disinfectant.
3. **Personal Care & Cosmetics:** preset examples include soap, shampoo, toothpaste, hair oil, and creams.

Do not add separate top-level categories for snacks, drinks, or other groups in the first version. The category list should use familiar household language.

## 5. Product customization within the fixed categories

- Show a useful starter list of common products inside each of the three fixed categories.
- Provide an **Add product** action within each category, so the user can add a product that is missing from the preset list.
- Every product should have a three-dot overflow menu with **Edit** and **Delete** actions.
- Edit should let the user change the product name and compatible measurement dimension/unit when data-integrity rules allow it.
- Do not add a brand field or make the user choose a brand.
- Preset product entries start with zero recorded stock. Purchases recorded in the app increase stock.
- Do not confuse user-added products with the three fixed top-level home categories.

### Product-edit and delete safety

- Mass units (kg/g) may be converted within the same measurement dimension; volume units (L/ml) may be converted within volume; count remains pieces/units.
- Never let edits silently reinterpret existing quantities—for example, change a product with kg history to litres.
- If a product has purchase/usage history, deleting it must not silently erase that history or distort spending. The final UI behavior for such deletion (for example, prevent deletion or offer an archive/hide option) should be researched before implementation.
- Confirm destructive actions clearly.

## 6. Units and quantity model

Each product has a measurement dimension:

- **Mass:** kilograms (kg) and grams (g).
- **Volume:** litres (L) and millilitres (ml).
- **Count:** pieces/units, such as bars of soap.

Only convert units within the same dimension. Do not convert mass to volume without product-specific density information. The user should not be forced to record soap in kg or liquid cleaner in grams.

## 7. Purchase, usage, and inventory workflow

### A. Record a purchase

The user selects a product and enters quantity, compatible unit, actual price paid in INR (₹), and purchase date. Purchase amount is a core data point because spending insights are a primary reason for the app. Each purchase must preserve its own quantity, price, and date; do not rely only on one editable current price stored on the product.

### B. Record usage

The user selects a product and records the amount used and date. Usage reduces the available stock. The app must prevent or clearly handle usage greater than available stock.

### C. Review inventory

Show remaining quantity using the product's compatible unit. For stock tracking, the first version begins at zero and uses:

**Remaining stock = recorded purchases − recorded usage**, after compatible-unit normalization.

Do not require opening-stock entry in the first version.

### D. Review purchase history

Keep dated purchase and usage records so the user can understand when items were bought, how quantities changed, and how much each purchase cost.

## 8. Dashboard and spending insights

Spending is a **primary feature**, not optional or secondary.

The dashboard/Insights planning must prioritize:
- Actual money spent on household products, in INR.
- Spending over a meaningful period (at minimum, a monthly view should be considered).
- Clear totals based on recorded purchase prices.
- Stock quantities still available, with the correct unit for each product.
- Product purchase/usage history to explain totals and changes.

At the design stage, decide the clearest period filters and category-level breakdown without overcrowding the screen. Never represent unknown/unrecorded amounts as zero, and never confuse amount spent with the estimated value of inventory still remaining.

## 9. Offline behavior and local data

- Core product, purchase, usage, stock, and spending features work without internet.
- User name and records persist across app restarts.
- No login, cloud database, or multi-device sync is required for the first version.

## 10. Deferred feature: JSON backup and restore

JSON backup/restore is **not part of the initial accepted MVP scope**. Revisit it only after the core app is built and the intended user has reviewed and accepted it. When planned later, it should include the saved name and required household records, validate imported data, and protect against accidental replacement.

## 11. First-version exclusions

- Product brand selection or a brand-management field.
- Additional top-level home categories, including separate snacks/drinks categories.
- Mandatory opening-stock entry for items already in the home.
- User accounts, cloud sync, and online-only functionality.
- AI recommendations, barcode scanning, receipt OCR, and automatic price lookup.
- JSON backup/restore in the first accepted core version.

## 12. MVP acceptance criteria

- First launch requests the user's name once; later launches reuse it and show an appropriate greeting.
- Home presents exactly the three fixed top-level categories.
- Starter products are available, and the user can add, edit, and delete products inside those categories without creating a new top-level home category.
- A purchase stores its product, quantity, compatible unit, actual amount paid, and date.
- A usage entry reduces the correct product's stock.
- Example checks pass: 5 kg rice purchased, 1 kg used, 4 kg remains; 1 L cleaner purchased, 200 ml used, 800 ml remains; 10 soap bars purchased, 2 used, 8 remain.
- Dashboard/Insights clearly reports recorded spending and remaining product quantities without misleading totals.
- Data persists after app restart and the core workflows work in airplane mode.
- Product edits/deletions cannot silently corrupt historical quantities or spending.
- JSON backup/restore is excluded until after intended-user acceptance of the core app.
