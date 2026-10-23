# Working Philosophy

## Product first
Build for the intended household user's actual routine. The first version focuses on the three fixed categories, simple product rows, separate Add/Use actions, correct stock, spending and understandable history.

## Planning before implementation
The product flow, data model, unit rules, date boundaries, archive/delete behavior, architecture and acceptance tests have been recorded in the planning baseline merged to `main`. These documents are the shared contract for the engineer and implementation agent. Future refinements should be documented deliberately, not improvised during coding.

## AI-assisted, engineer-owned
AI tools may draft code, designs and tests. The engineer remains responsible for requirements, review, edge cases, test results and understanding the implementation.

## Single source of truth
Room is the source of truth for product and transaction records. Stock and spending derive from saved transactions; do not keep a second editable/cached truth in UI state. User actions save records once, and observed database changes refresh the UI.

## Data integrity before decoration
Never allow negative stock, incompatible-unit arithmetic, duplicate transaction submissions, lost purchase history or misleading spending totals. Validate Add/Use, preserve financial history, and test calculations independently of the UI.

## Offline and privacy
The core app works without internet. Keep structured records local and avoid accounts, cloud services, analytics, ads or unnecessary permissions. JSON backup/restore is a later feature after core-app acceptance.

## Small, evidence-based phases
Implement one scoped phase at a time. A phase is complete only when acceptance tests and physical-device checks have actually passed and the user accepts the result.

## Repository workflow
- GitHub is the source of truth for scope, planning documents, code and evidence.
- The accepted planning baseline is merged into `main`.
- The active implementation branch is `phase/01-foundation-and-project-setup`, created from that baseline.
- After each accepted implementation phase, merge it to main, update status/evidence, and create the next phase branch from latest main.
- Keep README and portfolio claims aligned with verified repository evidence.
