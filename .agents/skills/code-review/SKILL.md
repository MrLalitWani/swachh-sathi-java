---
name: code-review
description: Review Java changes for correctness, security, maintainability, performance, and regression risk without editing by default.
---

# Code review

1. Inspect the diff and relevant surrounding code; understand intended behavior.
2. Prioritize actionable findings by severity: blocker, high, medium, low.
3. Check correctness, null/empty handling, boundaries, concurrency, transactions, resource cleanup, and error handling.
4. Check security: authorization, input validation, injection, secrets, sensitive logging, unsafe deserialization, and dependency/configuration risks.
5. Check database/API compatibility, performance, N+1 queries, pagination, and backward compatibility where relevant.
6. Check tests and identify missing cases.
7. Cite exact file paths and line ranges when available. Explain impact and a concrete remedy.
8. Do not report style preferences as bugs. Do not modify files unless explicitly asked.
9. If no findings are identified, say so and state what was and was not inspected.
