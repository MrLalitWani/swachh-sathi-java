---
trigger: model_decision
---

# Java backend guidance

Apply when editing Java backend code.

- Match the repository's configured Java version and style.
- Follow existing layering and naming conventions.
- Keep HTTP controllers/adapters thin; place business logic in the established service/domain layer.
- Validate external input and use safe, parameterized persistence operations.
- Handle errors consistently with existing application patterns.
- Add tests for normal behavior, validation, failures, and relevant edge cases.
- Avoid unnecessary dependencies, speculative abstractions, and unrelated refactors.
- For Spring Boot, follow the existing Boot version, dependency injection, security, configuration, and transaction patterns.
