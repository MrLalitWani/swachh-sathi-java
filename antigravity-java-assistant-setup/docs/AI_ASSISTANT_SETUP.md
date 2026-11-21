# AI assistant setup

This repository uses shared instructions plus Antigravity-specific configuration.

## Source of truth
- `AGENTS.md`: shared engineering rules.
- `GEMINI.md`: Antigravity entry-point context.
- `.agents/rules/`: focused Antigravity rules.
- `.agents/skills/`: reusable task playbooks.
- `.agents/workflows/`: optional repeatable workflows.

## First-run audit
Ask Antigravity to:
1. Inspect the repository, Git status, build files, Java version, modules, architecture, tests, CI, and README.
2. Report the verified stack and exact build/test commands.
3. Check whether the configured rule/skill/workflow paths are recognized by the installed Antigravity version.
4. Identify any project-specific instructions that conflict with these defaults.
5. Propose additions only after the audit; do not modify application code during setup.

## Safety
Start with repository access only. Review terminal commands and file diffs. Require approval for destructive commands, deployments, production data changes, and secret changes. Do not commit credentials.
