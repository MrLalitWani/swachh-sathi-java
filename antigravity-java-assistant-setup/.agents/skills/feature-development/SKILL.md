---
name: feature-development
description: Plan and implement a focused feature in an existing Java repository, following its architecture and verifying behavior.
---

# Feature development

1. Clarify the requested behavior and acceptance criteria. Ask only for essential missing information.
2. Inspect Git status, project instructions, relevant modules, existing implementations, tests, and build configuration.
3. Trace the current request/data flow end-to-end before changing code.
4. Present a short plan with files likely to change, compatibility concerns, and tests.
5. Implement the smallest complete change using existing patterns.
6. Add/update unit, integration, or API tests as appropriate.
7. Run the narrowest relevant test command discovered from repository configuration; broaden checks if practical.
8. Review the diff for accidental changes, secrets, regressions, and missing tests.
9. Report changed files and actual verification results. Do not claim unrun checks.
