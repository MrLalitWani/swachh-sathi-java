# Antigravity Java Assistant Setup

This is a workspace-level starter configuration for an existing Java repository.

## Install

1. Download and extract this ZIP.
2. Open your existing Java repository in Antigravity.
3. Copy the contents of `antigravity-java-assistant-setup/` into the repository root. Merge folders; do not overwrite existing files without reviewing them.
4. If `AGENTS.md`, `GEMINI.md`, or `.agents/` already exists, merge the instructions carefully rather than replacing your current configuration.
5. Confirm the workspace root is the repository root. Antigravity discovers `AGENTS.md`, `GEMINI.md`, `.agents/rules/*.md`, and `.agents/skills/<name>/SKILL.md`.
6. Restart/reload the workspace or start a new agent conversation.
7. Ask Antigravity to perform the first-run audit described in `docs/AI_ASSISTANT_SETUP.md`.

## Use the skills

The reusable skills are:
- `feature-development`
- `debugging`
- `code-review`
- `testing`

Antigravity discovers skills from `.agents/skills/<skill-name>/SKILL.md`. Ask the agent to use the relevant skill, or invoke it using the skill command exposed by your installed Antigravity version.

## Important

- This is a generic Java starter, not a repository-specific configuration. The actual Java version, Maven/Gradle commands, Spring Boot usage, package layout, and test framework must be verified from your project.
- The package does not add dependencies, configure a database, or install MCP servers.
- Do not copy the optional `.gitignore-ai-setup` file over your `.gitignore` blindly. Merge its entries only if appropriate.
- Back up or commit your existing work before applying configuration changes.
- Review all changes in Git before using the assistant for code edits.

## Official Antigravity documentation

- Rules: https://antigravity.google/docs/rules/
- Skills and learning: https://antigravity.google/docs/commands/learn
- Customization discovery: https://antigravity.google/docs/customization-discovery
- MCP: https://antigravity.google/docs/mcp
