# Development Workflow

## Requirements before implementation

Treat the documents in `docs/` as the shared product and engineering contract. Before changing behavior, review the relevant requirements, architecture decisions, acceptance scenarios, roadmap status, and prior phase evidence. Record approved scope changes in the appropriate source-of-truth document before implementation.

## Product first

Build for the intended household routine. Preserve the agreed product scope: three fixed categories, clear product rows, separate Add and Use actions, reliable stock calculations, purchase-spending accuracy, and understandable transaction history.

## AI-assisted, engineer-owned

AI tools may help research, design, implement, and test. The engineer remains responsible for understanding the change, reviewing edge cases, validating the implementation, and accurately reporting what was and was not tested.

## Single source of truth

Room is the source of truth for product and transaction records. Stock and spending must be derived from saved transactions. Do not introduce a second independently editable total in UI state or preferences. Persist each user action once and observe database changes to refresh the UI.

## Data integrity before decoration

Never allow negative stock, incompatible-unit arithmetic, duplicate transaction submissions, lost purchase history, or misleading spending totals. Keep stock checks and writes atomic where required. Test domain calculations independently of the UI.

## Offline operation and privacy

The accepted core app is local-first and works without internet. Do not add accounts, cloud services, analytics, ads, remote APIs, or unnecessary permissions without an explicitly approved scope change. JSON backup/restore remains deferred and is not an implemented feature in the accepted MVP.

## Small, evidence-based delivery

Work on one focused objective at a time. A phase is complete only after its agreed scope is implemented, the relevant automated checks are run and reported accurately, and the project owner accepts the user-visible behavior. A test result does not substitute for physical-device acceptance; physical-device acceptance does not imply an automated test was run.

## Branch and review workflow

1. Start from the latest accepted `main` baseline and inspect the current repository state before editing.
2. Use a focused `phase/NN-short-description` branch for product work or `chore/short-description` for repository maintenance.
3. Keep each branch scoped to one objective. Update the README and relevant documentation when the change affects product behavior, project status, or developer workflow.
4. Run the applicable Gradle tests and build tasks. Record exact commands and actual outcomes; do not copy reported results from an earlier phase as proof of a new run.
5. Provide a reviewable pull request. Do not merge a product phase until the project owner has tested the result on the intended device and explicitly accepted it.
6. After acceptance and merge, create the next phase branch from the updated `main` baseline.

## Evidence and reporting

Keep implementation-agent reports, CI results, and project-owner physical-device observations distinguishable. The acceptance-test catalogue is a requirements and traceability checklist; unchecked items must not be described as individually passed. Keep portfolio and README claims aligned with evidence recorded in the repository.
