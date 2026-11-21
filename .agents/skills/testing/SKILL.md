---
name: testing
description: Design and run focused tests for Java changes using the repository's actual test framework and build configuration.
---

# Testing

1. Inspect build files and existing test conventions to identify JUnit/TestNG, Mockito, Spring test support, and configured plugins.
2. Identify behavior boundaries and likely regression cases.
3. Prefer deterministic tests without network or production database dependencies.
4. Use existing fixtures/builders and avoid brittle sleeps or order-dependent tests.
5. Cover success, invalid input, failure paths, and edge cases relevant to the change.
6. Run the narrowest test command supported by the repository; discover wrapper usage rather than assuming it.
7. Report exact commands and results. Never state that tests pass if they were not run.
