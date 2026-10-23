# Phase 03 Verification — Product Catalogue and Management

**Status:** Accepted by Mahesh on October 24, 2026, after physical-device re-testing.
**Implementation branch:** `phase/03-product-catalogue-and-management`
**Next phase:** Phase 04 — Add/Use transactions and stock rules.

## Scope accepted

- Fixed category catalogue and 15 starter products, seeded without initial stock.
- Custom product creation and editing, including a 40-character product-name limit.
- Measurement dimensions labelled Mass, Liquid, and Units, without a product-level preferred unit.
- Measurement type can be changed repeatedly while no transaction has ever existed; it locks after transaction history exists, even if stock later reaches zero.
- Product archive and restore preserve product identity, stock derived from transactions, history, and spending.
- Permanent deletion is a separate destructive action for the selected product and associated transaction history.
- Product editor no-op save behavior and reliable re-entry were refined.

## Product editor regression: root cause and correction

The persistent measurement-type reversal defect was traced to stale editor baseline state in a retained ViewModel. After saving a change such as Mass → Liquid, `originalDimension` could remain Mass. On a later editor session, switching Liquid → Mass was incorrectly classified as unchanged and the Save button remained disabled.

The reported correction:
- Refresh the editor from the latest saved product when loading/re-entering.
- Update the original name and measurement-type baseline after a successful save.
- Trigger a product reload when entering the edit route.
- Add regression coverage for reversing a saved type change across sessions, reverting a selection before saving, repeated transitions, and locking when transactions exist.

## Verification evidence

Friday reported that these commands completed successfully:

- `gradlew.bat assembleDebug`
- `gradlew.bat testDebugUnitTest`

Reported unit-test result: **40/40 passed**, including four EditProductViewModel regression cases.

Mahesh confirmed that the measurement-type reversal problem was resolved after re-testing on his phone and accepted the Phase 03 scope.

## Evidence limits

This record distinguishes the implementation agent's reported automated results from Mahesh's physical-device confirmation. The unchecked cases in `ACCEPTANCE-TESTS.md` remain a detailed catalogue of individual scenarios; they should not be treated as individually passed unless a pass is recorded. Phase 04 transaction-entry workflows are not part of Phase 03 and remain upcoming.
