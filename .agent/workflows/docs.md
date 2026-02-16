---
description: Parse codebase and generate professional, audience-targeted software documentation based on industry standards.
---

# /docs - Technical Documentation Mode

$ARGUMENTS

---

## 🔴 CRITICAL RULES

1. **Diátaxis Framework Mandatory** - Every generated document MUST strictly adhere to one of the four Diátaxis quadrants:
    * **Tutorials:** Learning-oriented. Step-by-step practical lessons for beginners.
    * **How-To Guides:** Goal-oriented. Sequential steps to solve a specific problem for practitioners.
    * **Reference:** Information-oriented. Factual, highly structured API/system data for quick lookup.
    * **Explanation:** Understanding-oriented. Architectural concepts and the "why" behind the system.
2. **Professional Style Standard** - Enforce the following structural rules based on Google and Microsoft Developer Style Guides:
    * **Active Voice & Second Person:** Use "you" instead of "we", "I", or "the user". Write in the active voice.
    * **Terse & Direct:** Put key information first. Eliminate transition sentences and marketing fluff.
    * **Conditional Precedence:** Put conditions *before* instructions (e.g., "If using Docker, run `x`", NOT "Run `x` if using Docker").
    * **Visual Hierarchy:** Use sentence-case for headings. Bold for UI elements, `inline-code` for variables, parameters, and file paths.
3. **Context-Driven Accuracy** - Never hallucinate features. Ingest `CODEBASE.md` or specific source files before drafting. Code examples must be runnable and directly map to the existing architecture.

---

## Task

Execute the documentation protocol with the following context:

```
CONTEXT:
- Target Subject: $ARGUMENTS
- Mode: DOCUMENTATION GENERATION
- Output: docs/{diataxis-type}-{subject-slug}.md

EXECUTION STEPS:
1. Context Ingestion: Read the relevant source code, configuration, or `/plan` file to extract factual behavior.
2. Audience & Type Selection: Identify the target audience (e.g., API Consumer, DevOps, Core Contributor) and assign the strict Diátaxis type.
3. Structural Drafting: 
    - Create a mandatory "Overview" and "Prerequisites" section.
    - Chunk information into small, manageable units.
    - Include minimal, practical code snippets showing realistic use cases.
4. Formatting Verification: Apply technical writing syntax rules (serial commas, parallel list structures, active voice).
5. Generation: Output the complete markdown document.
```

---

## Expected Output

| Deliverable | Location |
|-------------|----------|
| Technical Document | `docs/{diataxis-type}-{subject-slug}.md` |
| Diátaxis Classification | Terminal |

---

## After Documenting

```
[OK] Documentation generated: docs/{diataxis-type}-{subject-slug}.md

Next steps:
- Review the drafted document.
- Execute Markdown linter to verify formatting compliance.
```

---

## Usage

```
/docs Generate an API reference for the PaymentGatewayController
/docs Create a how-to guide for migrating the database using Flyway
/docs Write an architectural explanation of the Kafka event streaming flow
```