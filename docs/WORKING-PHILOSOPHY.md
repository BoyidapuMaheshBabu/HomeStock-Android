# Working Philosophy

## Product first
Start with the real household problem and the user's everyday workflow. Prefer understandable, useful behavior over feature count or visual effects.

## AI-assisted, engineer-owned
AI tools may draft code, designs, and tests. The engineer remains responsible for deciding whether the work meets requirements, reviewing the changes, checking edge cases, running tests, and being able to explain the implementation.

## Small, evidence-based phases
Keep each phase small and reviewable. Define acceptance criteria before implementation. Report honestly what passed, failed, or remains unverified. Never infer that an app works merely because code was generated.

## Data integrity before decoration
Stock is a calculation based on recorded opening stock, purchases, and usage. Enforce compatible units and clear money semantics. Protect users from silent data loss and misleading totals.

## Privacy and offline usability
The first MVP should store its operational data locally and support the core task without internet access. Avoid adding accounts, cloud services, or permissions unless a demonstrated requirement justifies them.

## User-centered design
Use plain household language, touch-friendly layouts, readable amounts, and short workflows. Test with the intended user rather than assuming the developer's preferences match theirs.

## Definition of done
A phase is done only after its acceptance criteria have been reviewed and the relevant build/device checks have actually been performed. Commit code and documentation together where practical.

## Repository workflow
- GitHub is the source of truth for scope, changes, and phase status.
- Work on the named phase branch, not directly on main.
- After the user accepts a phase, merge it to main.
- Update status and verified evidence, then create the next branch from the latest main.
- Keep claims in README and portfolio descriptions aligned with the repository's real, tested state.
