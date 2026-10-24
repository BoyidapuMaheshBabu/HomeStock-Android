# HomeStock Acceptance Tests

**Status:** Active test catalogue; Phases 01–04 are accepted for progression. Individual scenarios are not marked passed unless that specific case has recorded evidence.

**Phase 02:** Mahesh accepted the Welcome/Home implementation after physical-device usability checks. See [Phase 02 verification](PHASE-02-VERIFICATION.md). **Phase 03:** See [Phase 03 verification](PHASE-03-VERIFICATION.md). **Phase 04:** See [Phase 04 verification](PHASE-04-VERIFICATION.md) for the reported 47-unit-test result and evidence limits. Do not infer that every unchecked item in this catalogue passed.

The implementation is accepted only after relevant tests pass and the intended user has reviewed the app.

## A. Onboarding and Home

- [ ] Fresh install without a saved name opens Name Setup.
- [ ] Blank/whitespace-only name cannot be saved.
- [ ] Display name accepts at most 10 characters in Welcome and Settings.
- [ ] Saving a valid name persists locally; later launches skip onboarding.
- [ ] Greeting uses device-local time and the saved name.
- [ ] Home places greeting at top-left and Settings gear at top-right.
- [ ] Home shows a clickable This month card below the top row.
- [ ] Tapping the spending card opens Insights with This month selected.
- [ ] No current-month purchases shows ₹0 and a clear no-purchases message.
- [ ] Home shows exactly three large rectangular cards: Food & Kitchen, Home Care & Cleaning, Personal Care & Cosmetics.
- [ ] There is no top-level Add Category action and no Snacks/Drinks category.
- [ ] Core screens work in airplane mode.

## B. Product catalogue and management

- [ ] Starter products are shown within their intended fixed category.
- [ ] Starter seed runs once; a deleted preset is not recreated on every launch.
- [ ] Creating a product asks for name and exactly one type: Mass, Liquid, or Units; it does not ask for a preferred unit, quantity, or price.
- [ ] Product name accepts at most 40 characters in Create and Edit; blank names are rejected and whitespace is trimmed.
- [ ] New product starts at zero stock.
- [ ] Mass products offer kg/g only; Liquid products offer L/ml only; Units products accept whole pieces only when entering transaction quantities.
- [ ] A product without transaction history can switch repeatedly among Mass, Liquid and Units; all three choices remain available when editing.
- [ ] Once any transaction exists, measurement type is locked even if current stock is zero.
- [ ] Product row shows name/current stock, plus action and three-dot menu.
- [ ] Tapping the product row opens history; tapping plus opens Add/Use choices in Phase 04.
- [ ] Three-dot menu offers Edit, Archive, and Delete permanently with appropriate confirmation.
- [ ] Editing a product's name or an allowed measurement type preserves product identity.
- [ ] Save Changes is disabled when no effective change exists or the form is invalid.
- [ ] Changing a value and restoring its original value disables Save Changes again.
- [ ] An unchanged save causes no repository write or navigation.
- [ ] A successful edit persists once, navigates back once, and the same editor can be reopened reliably without flicker.
- [ ] No brand field or product-level preferred unit is required or stored.

## C. Add / Purchase

- [ ] Add form only shows units compatible with that product's dimension.
- [ ] Add requires positive quantity, compatible unit, actual amount paid and purchase date.
- [ ] Money accepts at most two decimal places and is stored exactly to paise.
- [ ] ₹0.00 is valid for a free purchase; it adds stock but adds zero spending.
- [ ] Purchase date defaults to today's local date; past dates are allowed; future dates are rejected.
- [ ] Save creates one ADD transaction and stock increases by the same normalized quantity.
- [ ] History, stock, This month, selected Insights total and All time update without manual refresh.
- [ ] Rapid double tap on Save cannot create duplicates.
- [ ] Failed write creates no misleading success and does not increment a total.

## D. Use / stock-out

- [ ] Use form shows current stock before the quantity field.
- [ ] Use requires positive quantity, compatible unit and date; it has no money field.
- [ ] Use date defaults to today; past dates are allowed; future dates are rejected.
- [ ] Successful Use creates one USE transaction and reduces stock immediately.
- [ ] Zero stock blocks Use with a clear message.
- [ ] If 1 kg is available and 10 kg is entered, save is rejected, no transaction is created and stock is not negative.
- [ ] Using 500 g from 2 kg leaves 1.5 kg.
- [ ] Expired/thrown-away stock is recorded through Use; no separate disposal action exists.
- [ ] Use never changes purchase spending.
- [ ] Rapid repeated Save cannot create duplicate Use records.

## E. Unit conversion

- [ ] 10 kg rice purchased; 1 kg used; 9 kg remains.
- [ ] 2 kg rice purchased; 500 g used; 1.5 kg remains.
- [ ] 1 L cleaner purchased; 200 ml used; 800 ml remains.
- [ ] 5 soap bars purchased; 1 used; 4 remain.
- [ ] Mass, Liquid, and Units cannot be mixed; only compatible quantity-entry units are offered.
- [ ] Count is whole units in the first MVP.
- [ ] kg/L input allows up to three decimals; g/ml and count use whole numbers.

## F. Spending math

- [ ] Existing recorded spending ₹5,000 plus newly saved ₹5 purchase shows ₹5,005 immediately.
- [ ] The total represents purchase spending, not estimated stock value.
- [ ] Product creation/editing alone does not affect spending.
- [ ] Use changes stock only; it does not subtract past spending.
- [ ] Free purchases increase stock but contribute ₹0 to spending.
- [ ] Purchases for archived products remain included in historical spending.
- [ ] Currency math uses paise integers and avoids floating-point drift.

## G. Spending period boundaries

All periods use device-local calendar dates and inclusive boundaries. Test date: October 23, 2026.

- [ ] Last 7 days covers October 17–23.
- [ ] This month covers October 1–23.
- [ ] Last 3 months covers August 1–October 23 (current month-to-date plus two preceding calendar months).
- [ ] Custom date range includes both start and end dates.
- [ ] Custom start after end is rejected.
- [ ] Future dates cannot be chosen in custom ranges.
- [ ] All time includes all saved Add transactions, including purchases of archived products.
- [ ] Period totals sum only ADD amounts whose selected purchase dates fall within the range.
- [ ] Empty range shows ₹0 and a clear empty state.
- [ ] Changing filters updates total and displayed range correctly.
- [ ] Home's This month card always agrees with Insights' This month total.

## H. Product history

- [ ] Tapping product row opens a vertically scrollable history for that product.
- [ ] Entries are grouped by local transaction date, with Today/Yesterday/date labels.
- [ ] Newer dates first; same-date transactions ordered newest inserted first.
- [ ] Time-of-day is not shown.
- [ ] Add/Purchase entries show quantity, unit and actual amount paid.
- [ ] Use entries show quantity, unit and stock impact.
- [ ] Product-specific spending matches the sum of its Add transactions.
- [ ] History rows are read-only in the first MVP; no transaction-row edit/delete controls.

## I. Delete, Archive and Restore

- [ ] A product with or without transaction history can be permanently deleted only after explicit confirmation that the product and all its history will be lost.
- [ ] Canceling Delete leaves the product and all transactions unchanged.
- [ ] Deleting a history-bearing product removes only that product's transaction rows and product record atomically; no orphaned records remain.
- [ ] Spending totals recalculate without the deleted product's Add transactions.
- [ ] Deleting a starter product does not cause it to be silently recreated by a later seed/relaunch.
- [ ] Archive is allowed regardless of current stock or transaction history.
- [ ] Archive preserves the product's current stock as derived from history, transactions, and spending totals.
- [ ] Archived products disappear from active category lists and cannot receive new transactions.
- [ ] Settings > Archived Products shows archived items, including items with stock/history.
- [ ] Restore returns the same product to its active category list with stock and history unchanged.
- [ ] Archive and Restore never duplicate or reseed the product.

## J. Persistence and privacy

- [ ] Name, products, archive state and transactions survive restart/force close.
- [ ] Stock and spending after restart equal values before restart.
- [ ] Core features function in airplane mode.
- [ ] First version has no Internet permission, account or cloud dependency.
- [ ] Future database migrations preserve existing data; destructive migration fallback is not used after real user records exist.
- [ ] JSON backup/restore is deferred and is not represented as a completed core feature.

## K. Demonstration scenario

1. Fresh launch: save the name Priya.
2. Home: verify greeting, Settings, This month ₹0 and the three categories.
3. Open Food & Kitchen and choose Rice.
4. Add 10 kg rice for ₹500 with today's date.
5. Verify 10 kg stock and spending increase of ₹500.
6. Use 1 kg rice.
7. Verify 9 kg remains and spending stays ₹500.
8. Try to Use 10 kg when only 9 kg remains; verify it is blocked with no transaction.
9. Open Rice history and verify dated Add and Use entries.
10. Record shampoo purchase for ₹5.
11. Verify all-time total increases by ₹5. If previous total was ₹5,000, verify ₹5,005.
12. Test Last 7 days, This month, Last 3 months, Custom and All time against purchase dates.
13. Archive a product with transaction history and non-zero stock; verify it disappears from active lists but restores with the same stock/history and unchanged spending.
14. Permanently delete a separate product with transaction history; verify only its associated history is removed and spending totals update accordingly.
15. Restart the app and verify deleted presets are not silently recreated, then repeat key checks with internet disabled.
