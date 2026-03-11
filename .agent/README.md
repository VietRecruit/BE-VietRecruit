# AgentKit

> **The Intelligent Support System for VietRecruit**

AgentKit is a specialized AI agent framework designed to assist the VietRecruit development team. It provides a structured, context-aware environment for planning, coding, debugging, and documenting the VietRecruit platform.

## How It Works

AgentKit uses a **Sequential Agent Flow** to intelligently route your requests to the right specialist with the right knowledge.

**Just ask:**
> "Design a database schema for the user profile module."

**The System Will:**
1.  **Analyze** your request.
2.  **Route** it to the `Database Architect` agent.
3.  **Load** the `database-design` skill.
4.  **Generate** a compliant schema based on VietRecruit's standards.

## Documentation

*   **[Architecture](ARCHITECTURE.md)**: Visualize the decision flow and system components.
*   **[Agents](agents/AGENTS.md)**: Index of available specialist agents.
*   **[Skills](skills/SKILLS.md)**: Index of available technical skills.

## License

Protected under the VietRecruit proprietary license. See [LICENSE](../LICENSE) for details.
