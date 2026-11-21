---
name: debugging
description: Reproduce and diagnose bugs in Java applications, identify root cause, make a minimal fix, and verify it.
---

# Debugging

1. Capture the exact symptom, expected behavior, reproduction steps, and relevant logs (redact secrets and personal data).
2. Inspect Git status and recent relevant changes; do not overwrite user work.
3. Reproduce the issue or create a focused failing test when possible.
4. Trace the flow through controller/adapter, service/domain, persistence, and external boundaries as applicable.
5. Separate observed facts from hypotheses. Find the root cause before editing.
6. Apply the smallest safe fix; avoid suppressing exceptions or weakening validation/security.
7. Add a regression test.
8. Run the focused test and relevant checks.
9. Report root cause, fix, evidence, and any remaining uncertainty.
