---
trigger: conditional
description: Core operational skills, context optimization, communication protocols, and ambiguity resolution.
---

# SKILL.md - Core Operational Behaviors

> This file defines how the AI processes requests, optimizes token usage, handles language switching, and resolves ambiguity.

---

## 1. TOKEN OPTIMIZATION & CONTEXT REDUCTION

**Objective:** Minimize context window usage and eliminate redundant token generation.

* **Zero Fluff:** Omit pleasantries, conversational filler, apologies, and moralizing text. Execute the command immediately.
* **Direct Delivery:** Output only the requested artifact (code, plan, analysis). Do not explain the generated code unless explicitly commanded to do so.
* **Diff-Based Updates:** When modifying existing files, output strictly the changed methods or blocks. Use `// ... existing code ...` to represent unchanged sections. Do not output entire files for minor edits.
* **Information Density:** If explanations are required, use terse bullet points. Eliminate transition sentences.

## 2. THE SOCRATIC GATE (AMBIGUITY RESOLUTION)

**Objective:** Prevent hallucination, incorrect assumptions, and rework due to ambiguous user requests.

**Trigger:** The user request lacks technical specificity, omits required configurations, or is open to multiple architectural interpretations.

**Execution:**
1.  **HALT EXECUTION:** Do not generate code, execute `/plan`, or finalize any architectural decision.
2.  **ISOLATE UNKNOWNS:** Identify the specific missing technical parameters (e.g., "Which cache eviction policy?", "Target database version?", "Expected QPS?").
3.  **QUERY THE USER:** Output a concise, numbered list of direct technical questions. Where applicable, provide viable options or trade-offs for the user to select.
4.  **WAIT:** Await explicit user clarification before resuming execution.

## 3. MULTILINGUAL PROTOCOL

**Objective:** Handle non-English inputs while maintaining a globally standardized codebase.

**Execution:**
1.  **Input Parsing:** Detect and accept user input in any language.
2.  **Internal Translation:** Translate the logic and requirements to English internally to leverage optimal LLM comprehension and pattern matching.
3.  **Code Output (Strict English):** All generated code, variable names, class names, database schemas, commit messages, and inline code comments MUST be written in strict **English**.
4.  **Response Output (User Native):** The conversational text, plan details, terminal reports, and Socratic questions MUST match the language of the user's input prompt.

## 4. HIGH-EFFICIENCY CODING PROTOCOL

**Objective:** Maximize output quality while strictly adhering to the existing system context.

* **Pre-Flight Verification:** Automatically locate and parse configuration files (`pom.xml`, `application.yml`, `docker-compose.yml`) before writing implementation logic to ensure dependency alignment.
* **Fail Fast:** If a requested feature structurally conflicts with the existing architecture or configurations, output a `[FATAL EXCEPTION]` block detailing the conflict. Propose an architectural alternative. Do not attempt a forced implementation.
* **Zero Assumptions:** If a requested task requires a library, framework, or tool not present in the ingested context, do not hallucinate its presence. Route back to the Socratic Gate and request authorization to modify dependencies.

## 5. DEFENSIVE PROGRAMMING & SECURITY FIRST

**Objective:** Guarantee that all generated code is immune to common vulnerabilities (OWASP Top 10) by default.

* **Zero Trust:** All user inputs, external API responses, and database reads must be validated and sanitized. Use standard validation annotations (e.g., `jakarta.validation.constraints`).
* **Secret Management:** NEVER hardcode credentials, API keys, or tokens. Force the use of environment variables (`System.getenv()`, `@Value("${...}")`) and secrets managers.
* **Injection Prevention:** Strictly enforce parameterized queries and ORM methods (JPA/Hibernate). Raw SQL generation is prohibited unless explicitly demanded and sanitized.
* **Access Control:** Implement explicit authorization checks (e.g., Spring Security `@PreAuthorize`) at the service layer, not just the controller layer.

## 6. CHUNKED EXECUTION (PROGRESSIVE DELIVERY)

**Objective:** Prevent context window truncation and cognitive overload when dealing with complex implementations or large files.

* **Size Limits:** If an implementation exceeds 150 lines of code, trigger chunked execution.
* **Execution:**
  1. Output the skeleton structure (Interfaces, Class definitions, Method signatures).
  2. Implement the core domain logic first.
  3. Pause and prompt the user: `[AWAITING COMMAND: Continue to Infrastructure implementation?]`
  4. Deliver remaining components sequentially.
* **Context Preservation:** Maintain references to previous chunks using interface contracts, preventing hallucination of method signatures in later chunks.

## 7. OBSERVABILITY & TELEMETRY INJECTION

**Objective:** Ensure all backend components are immediately traceable and monitorable in production environments (ELK, Prometheus, Grafana).

* **Structured Logging:** Force structured JSON logging (e.g., Logback with Logstash encoder). Include context variables (`userId`, `orderId`).
* **Trace Propagation:** Ensure `traceId` and `spanId` are propagated across all thread boundaries (Async, Kafka, external HTTP calls) using Micrometer Tracing or OpenTelemetry.
* **Metric Emitting:** Automatically inject business-critical metrics (counters, timers) using Micrometer `@Timed` or `MeterRegistry` for Prometheus scraping.
* **Error Granularity:** Log exceptions at the `ERROR` level with full stack traces only at the outermost boundary. Use `WARN` for recoverable business exceptions.

## 8. SELF-AUDIT & STATIC ANALYSIS

**Objective:** The agent must mentally compile and review its own code before delivering the output to the user.

* **Pre-Output Compilation:** Perform a simulated syntax check. Ensure all imported classes exist in standard libraries or the provided `pom.xml`.
* **Complexity Control:** Reject generation of methods with high cyclomatic complexity (e.g., deeply nested `if/else` or `switch` statements). Refactor into Strategy Pattern or polymorphism before output.
* **Concurrency Safety:** Explicitly check for thread-safety in singleton beans. Prohibit mutable instance variables in Spring components. Enforce `ThreadLocal` or concurrent collections (`ConcurrentHashMap`) where state is required.

## 9. API CONTRACT & IDEMPOTENCY STRICTNESS

**Objective:** Guarantee that exposed APIs are robust, predictable, and safe for retries.

* **Contract-First:** APIs must adhere to the provided or generated OpenAPI 3.0 specification. Do not alter DTO schemas without explicit user consent.
* **Idempotency Guarantee:** All `POST`, `PUT`, and `PATCH` endpoints must be designed idempotently. Implement idempotency keys (`Idempotency-Key` header) and distributed locks (Redis) for critical state-changing operations (e.g., payments, order creation).
* **Graceful Degradation:** If an external dependency fails, the API must return a standardized error response (RFC 7807 Problem Details) and fallback to a default state or cache if applicable.