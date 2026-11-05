# Agent Instructions

These instructions apply to AI coding agents and automated contributors working in this repository.

## Project context

IntiVentory is an offline-first native Android household inventory and purchase-spending tracker. The accepted core MVP is implemented through Phase 07. The repository's source-of-truth documents are under `docs/`.

## Read before changing the project

Read the relevant documents before proposing or implementing a change:

1. `README.md` — project scope and entry points.
2. `docs/PRD.md` — product behavior and business rules.
3. `docs/ARCHITECTURE.md` — technical structure and data decisions.
4. `docs/ACCEPTANCE-TESTS.md` — expected behavior and regression scenarios.
5. `docs/ROADMAP.md` and `docs/PROGRESS.md` — accepted status and deferred work.
6. `docs/DEVELOPMENT-WORKFLOW.md` — branching, review, and evidence rules.
7. The relevant file in `docs/phases/` — historical scope and verification limits.

Do not treat an old phase note or remembered branch name as the current repository state. Inspect the actual code, branch, and Git status first.

## Product and architecture constraints

- Keep the app native Android using the existing Kotlin, Jetpack Compose, and Material 3 implementation.
- Preserve offline-first behavior. Do not introduce accounts, cloud sync, backend services, analytics, ads, network calls, or new permissions without explicit approval.
- Room is the source of truth for product and transaction records. Derive stock and purchase spending from saved transactions; do not maintain separately editable totals.
- Keep quantity conversion dimension-safe: Mass, Liquid, and Units must never be mixed.
- Preserve atomic stock validation, no-negative-stock behavior, transaction identity and correction rules, archive/restore history, and permanent-delete safeguards.
- Preferences DataStore is for user preferences, not inventory or transaction records.
- JSON backup/restore is deferred. Do not implement or claim it as complete unless a separate scope is approved.
- Avoid new dependencies and unrelated features unless a concrete requirement justifies them.

## Engineering workflow

- Inspect `git status`, the current branch, project structure, and relevant implementation before editing. Preserve unrelated or uncommitted work.
- Start product work from the latest accepted `main` baseline on a focused `phase/NN-short-description` branch. Use `chore/short-description` for repository maintenance.
- Do not commit product work directly to `main`.
- Work only within the approved objective. Update the PRD, architecture, roadmap, acceptance tests, progress snapshot, or phase records when the change makes them inaccurate.
- Do not merge a product phase until the project owner has tested the result on the intended device and explicitly accepted it.

## Validation and evidence

- Run the applicable Gradle tests and build tasks after code changes. Report the exact commands and actual results.
- Never claim tests, builds, CI checks, or physical-device checks were completed unless their results are available.
- Distinguish implementation-agent reports, CI results, and project-owner device observations.
- Do not mark acceptance-test checkboxes as passed without recorded evidence for those scenarios.
- Call out tests or checks that could not be run and explain the limitation.

## Repository hygiene

- Never commit API keys, passwords, signing credentials, private user data, `local.properties`, generated build output, or local IDE state.
- Keep source changes focused and avoid unrelated formatting churn.
- Preserve existing documentation and asset references when moving or renaming files.

## Completion report

Summarize the objective, changed files, branch, validation commands and results, manual checks still required, and remaining limitations. Provide a clear commit or pull-request summary.
