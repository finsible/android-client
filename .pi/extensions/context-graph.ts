const fs = require("node:fs/promises");
const path = require("node:path");

const SCHEMA_PATH = ".pi/context-graph.schema.json";
const GRAPH_PATH = ".pi/context-graph.json";

// ─── Session-start summary ────────────────────────────────────────────────────
function buildSessionSummary(graph: any): string {
  const meta = graph._schema ?? {};
  const gradle = graph.gradle ?? {};
  const deps = graph.dependencies ?? {};
  const theme = graph.theme ?? {};
  const tpl = graph.templateLibrary ?? {};
  const data = graph.data ?? {};
  const ui = graph.ui ?? {};

  const components = Object.keys(tpl.components ?? {}).filter(k => !k.startsWith("_"));
  const symbols = Object.keys(graph.symbols ?? {}).filter(k => !k.startsWith("_") && !k.startsWith("FILL"));
  const vms = Object.keys((ui.viewModels ?? {})).filter(k => !k.startsWith("_") && !k.startsWith("FILL"));
  const entities = Object.keys((data.entities ?? {})).filter(k => !k.startsWith("_") && !k.startsWith("FILL"));
  const repos = Object.keys((data.repositories ?? {})).filter(k => !k.startsWith("_") && !k.startsWith("FILL"));
  const diModules = Object.keys((data.di ?? {})).filter(k => !k.startsWith("_") && !k.startsWith("FILL"));
  const iconSets = deps.compose?.icons?.availableSets ?? [];
  const colorTokens = Object.keys(theme.colorTokens ?? {}).filter(k => !k.startsWith("_") && !k.startsWith("FILL"));
  const rules = Object.keys(graph.rules ?? {}).filter(k => !k.startsWith("_"));

  const lines: string[] = [
    `## Finsible Context Graph v${meta.version ?? "?"} (${meta.generated ?? "unknown"})`,
    `## Use graph for all lookups — do NOT search files for anything indexed here`,
    ``,

    `### Project`,
    `  root:    ${meta.rootPackage ?? "?"}`,
    `  sdk:     compileSdk=${gradle.compileSdk ?? "?"} min=${gradle.minSdk ?? "?"} kotlin=${gradle.kotlinVersion ?? "?"}`,
    `  db:      ObjectBox ${deps.db?.objectbox?.version ?? "?"}`,
    `  di:      Hilt ${deps.di?.hilt?.version ?? "?"}`,
    ``,

    `### Theme tokens  (access via FinsibleTheme.*)`,
    `  colors:     ${colorTokens.length ? colorTokens.join(", ") : "run /build-graph to fill"}`,
    `  typography: ${Object.keys(theme.typographyTokens ?? {}).filter(k => !k.startsWith("_") && !k.startsWith("FILL")).join(", ") || "see theme.typographyTokens"}`,
    `  dimes:      ${Object.keys(theme.dimesTokens ?? {}).filter(k => !k.startsWith("_") && !k.startsWith("FILL")).join(", ") || "see theme.dimesTokens"}`,
    `  rule:       ${theme.rule ?? "NEVER extend MaterialTheme"}`,
    ``,

    `### Template components (${components.length})`,
    components.length
      ? components.map(c => `  ${c}: ${(tpl.components[c]?.status ?? "?")} — ${tpl.components[c]?.files?.component ?? "?"}`).join("\n")
      : "  none filled yet — run /build-graph",
    ``,

    `### Icon sets available (com.composables ${deps.compose?.icons?.version ?? "?"})`,
    `  ${iconSets.join(", ") || "see dependencies.compose.icons.availableSets"}`,
    `  import: ${deps.compose?.icons?.importPattern ?? "see graph"}`,
    ``,

    `### Data layer`,
    `  entities:    ${entities.length ? entities.join(", ") : "run /build-graph"}`,
    `  repos:       ${repos.length ? repos.join(", ") : "run /build-graph"}`,
    `  DI modules:  ${diModules.length ? diModules.join(", ") : "run /build-graph"}`,
    `  sync:        ${data.sync?.manager ?? "data/sync/SyncManager.kt"}`,
    ``,

    `### UI layer`,
    `  ViewModels:  ${vms.length ? vms.join(", ") : "run /build-graph"}`,
    `  nav routes:  ${ui.navigation?.routes ?? "ui/navigation/Route.kt"}`,
    ``,

    `### Arch rules enforced`,
    rules.map(r => `  [${r}] ${graph.rules[r]?.rule ?? ""}`).join("\n"),
    ``,

    `### Symbols indexed: ${symbols.length}`,
    `  Resolve any symbol: graph.symbols["ClassName"] → file path`,
    `  Full graph: ${GRAPH_PATH}`,
  ];

  return lines.join("\n");
}

// ─── Graph lookup logic ─────────────────────────────────────────────────────
function lookupGraph(graph: any, query: string): string {
  const q = query.trim();
  if (!q) return "Usage: lookup_graph { query: \"ClassName\" | \"FinsibleComponent\" | \"rule:ruleName\" | \"vm:ViewModelName\" | \"entity:EntityName\" | \"repo:RepoName\" }";

  // Prefix-routed lookups
  if (q.startsWith("rule:")) {
    const ruleName = q.slice(5);
    const rule = graph.rules?.[ruleName];
    return rule ? `[rule] ${ruleName}: ${rule.rule} (scope: ${rule.scope})` : `Rule "${ruleName}" not found. Available: ${Object.keys(graph.rules ?? {}).filter(k => !k.startsWith("_")).join(", ")}`;
  }

  if (q.startsWith("vm:")) {
    const vmName = q.slice(3);
    const vm = graph.ui?.viewModels?.[vmName];
    if (!vm) return `ViewModel "${vmName}" not found. Available: ${Object.keys(graph.ui?.viewModels ?? {}).filter(k => !k.startsWith("_") && !k.startsWith("FILL")).join(", ")}`;
    return [
      `VM: ${vmName} → ${vm.file}`,
      `  Injected: ${(vm.injected ?? []).join(", ")}`,
      `  StateFlows: ${(vm.stateFlows ?? []).join(", ")}`,
      `  Methods: ${(vm.publicMethods ?? []).join(", ")}`,
    ].join("\n");
  }

  if (q.startsWith("entity:")) {
    const entName = q.slice(7);
    const ent = graph.data?.entities?.[entName];
    if (!ent) return `Entity "${entName}" not found. Available: ${Object.keys(graph.data?.entities ?? {}).filter(k => !k.startsWith("_") && !k.startsWith("FILL")).join(", ")}`;
    return [
      `Entity: ${entName} → ${ent.file}`,
      `  Extends: ${ent.extends ?? "none"}`,
      `  Fields: ${(ent.fields ?? []).join(", ")}`,
      `  Box: ${ent.box ?? "?"}`,
    ].join("\n");
  }

  if (q.startsWith("repo:")) {
    const repoName = q.slice(5);
    // Try repositories first, then localRepositories
    const repo = graph.data?.repositories?.[repoName] ?? graph.data?.localRepositories?.[repoName];
    if (!repo) return `Repository "${repoName}" not found. Public: ${Object.keys(graph.data?.repositories ?? {}).filter(k => !k.startsWith("_") && !k.startsWith("FILL")).join(", ")} | Local: ${Object.keys(graph.data?.localRepositories ?? {}).filter(k => !k.startsWith("_") && !k.startsWith("FILL")).join(", ")}`;
    return [
      `Repository: ${repoName} → ${repo.file}`,
      repo.localRepo ? `  LocalRepo: ${repo.localRepo}` : "",
      repo.apiService ? `  ApiService: ${repo.apiService}` : "",
      `  Methods: ${(repo.keyMethods ?? []).join("\n           ")}`,
    ].filter(Boolean).join("\n");
  }

  // 1. Symbol lookup (fast path — O(1))
  if (graph.symbols?.[q]) {
    const filePath = graph.symbols[q];
    const prefix = graph._schema?.rootPackage
      ? `app/src/main/java/${graph._schema.rootPackage.replace(/\./g, "/")}/${filePath}`
      : filePath;
    return `${q} → ${prefix}`;
  }

  // 2. Template component lookup
  if (graph.templateLibrary?.components?.[q]) {
    const comp = graph.templateLibrary.components[q];
    const lines = [
      `Component: ${q} [${comp.status ?? "?"}]`,
      `  Component: ${comp.files?.component ?? "?"}`,
      `  Defaults:  ${comp.files?.defaults ?? "none"}`,
      `  Models:    ${comp.files?.models ?? "none"}`,
      `  Variant:   ${comp.files?.variant ?? "none"}`,
      `  Preview:   ${comp.files?.preview ?? "none"}`,
    ];
    if (comp.params) {
      lines.push(`  Params: ${Object.entries(comp.params).map(([k, v]) => `${k}: ${v}`).join(", ")}`);
    }
    if (comp.constraints?.length) {
      lines.push(`  Constraints: ${comp.constraints.join(" | ")}`);
    }
    if (comp.tokenUsage) {
      lines.push(`  Token colors: ${(comp.tokenUsage.colors ?? []).join(", ") || "none"}`);
      lines.push(`  Token typography: ${(comp.tokenUsage.typography ?? []).join(", ") || "none"}`);
      lines.push(`  Token spacing: ${(comp.tokenUsage.spacing ?? []).join(", ") || "none"}`);
      lines.push(`  Token sizes: ${(comp.tokenUsage.sizes ?? []).join(", ") || "none"}`);
      lines.push(`  Token stroke: ${(comp.tokenUsage.stroke ?? []).join(", ") || "none"}`);
      lines.push(`  Token elevation: ${(comp.tokenUsage.elevation ?? []).join(", ") || "none"}`);
    }
    if (comp.variants?.length) {
      lines.push(`  Variants: ${comp.variants.join(", ")}`);
    }
    if (comp.modelTypes?.length) {
      lines.push(`  Model types: ${comp.modelTypes.join(", ")}`);
    }
    return lines.join("\n");
  }

  // 3. FinsibleComponent shorthand (strip "Finsible" prefix and try again)
  if (q.startsWith("Finsible")) {
    const baseName = q;
    if (graph.templateLibrary?.components?.[baseName]) {
      return lookupGraph(graph, baseName);
    }
  }

  // 4. Try theme token lookups
  if (graph.theme?.colorTokens?.[q]) {
    return `Color token: ${q} → ${graph.theme.colorTokens[q]} (use FinsibleTheme.semantic.${q})`;
  }
  if (graph.theme?.typographyTokens?.[q]) {
    return `Typography token: ${q} → ${graph.theme.typographyTokens[q]} (use FinsibleTheme.typography.${q})`;
  }
  if (graph.theme?.spacingTokens?.[q]) {
    return `Spacing token: ${q} → ${graph.theme.spacingTokens[q]} (use FinsibleTheme.spacing.${q})`;
  }
  if (graph.theme?.sizeTokens?.[q]) {
    return `Size token: ${q} → ${graph.theme.sizeTokens[q]} (use FinsibleTheme.sizes.${q})`;
  }
  if (graph.theme?.strokeTokens?.[q]) {
    return `Stroke token: ${q} → ${graph.theme.strokeTokens[q]} (use FinsibleTheme.stroke.${q})`;
  }
  if (graph.theme?.elevationTokens?.[q]) {
    return `Elevation token: ${q} → ${graph.theme.elevationTokens[q]} (use FinsibleTheme.elevation.${q})`;
  }

  // 5. Fuzzy suggestions
  const allSymbols = Object.keys(graph.symbols ?? {}).filter(k => !k.startsWith("_") && !k.startsWith("FILL"));
  const allComponents = Object.keys(graph.templateLibrary?.components ?? {}).filter(k => !k.startsWith("_"));
  const suggestions = [...allSymbols, ...allComponents]
    .filter(k => k.toLowerCase().includes(q.toLowerCase()))
    .slice(0, 10);

  if (suggestions.length) {
    return `"${q}" not found. Did you mean: ${suggestions.join(", ")}?`;
  }

  return `"${q}" not found in graph. Try /graph-status to check if the graph is current.`;
}

// ─── Build prompt ─────────────────────────────────────────────────────────────
const BUILD_PROMPT = `\
You are filling the Finsible context graph. Follow these steps exactly:

1. Read .pi/context-graph.schema.json — this is the authoritative schema.
2. For EVERY key marked "FILL:" or "FILL_*", scan the actual source files and replace the placeholder with the real value.
3. Remove ALL keys that start with "_example" — they are schema hints, not output.
4. Keep all keys that start with "_purpose", "_note", "_schemaNote" — they help future agents.
5. Keep empty arrays [] for sections with no entries — never omit keys.
6. Write the completed graph to .pi/context-graph.json.

Scanning order (do not skip sections):
  a. gradle        → read app/build.gradle.kts + gradle/libs.versions.toml
  b. dependencies  → read libs.versions.toml for all versions; verify icon sets in build.gradle.kts
  c. theme         → read ui/theme/*.kt — fill ALL color/typography/spacing/sizes/stroke/elevation tokens
  d. templateLibrary.components → for each Finsible* in ui/component/templates/:
       read component, defaults, models, variant, preview files
       fill: status, files, params (every param), variants, modelTypes, tokenUsage, constraints
  e. data.entities    → read data/local/entity/*.kt
  f. data.localRepositories → read data/local/repository/*.kt
  g. data.remoteModels     → read data/remote/model/*.kt
  h. data.apiServices      → read data/remote/api/*.kt
  i. data.repositories     → read data/repository/*.kt
  j. data.di               → read data/di/*.kt (main + debug + release)
  k. ui.viewModels         → read ui/viewmodel/*.kt — fill injected deps, all StateFlows, public methods
  l. ui.screens            → read ui/screen/*.kt
  m. ui.uiModels           → read ui/model/uimodel/*.kt
  n. ui.uiState            → read ui/model/state/*.kt
  o. ui.uiEvents           → read ui/model/event/*.kt
  p. ui.mappers            → read ui/mapper/*.kt
  q. symbols               → add EVERY public class, object, companion object, top-level fun
                             found in source — format: "ClassName": "relative/path.kt"
                             relative to app/src/main/java/com/itsjeel01/finsiblefrontend/

Quality checks before writing:
  - Zero "FILL:" strings remaining in output
  - Zero "FILL_" keys remaining in output
  - symbols map has at least one entry per source file
  - Every templateLibrary.component entry has non-empty tokenUsage.colors and layout tokens

Write to .pi/context-graph.json when complete. Do NOT write until all sections are done.`;

// ─── Update prompt (single component patch) ───────────────────────────────────
function buildUpdatePrompt(componentName: string): string {
  return `\
Patch .pi/context-graph.json — update ONLY the entry for: ${componentName}

Steps:
1. Read the existing .pi/context-graph.json
2. Read ALL source files for ${componentName}:
     component/Finsible${componentName}.kt
     default/Finsible${componentName}Defaults.kt
     model/Finsible${componentName}Models.kt
     model/variant/Finsible${componentName}Variant.kt  (if exists)
     preview/Finsible${componentName}Preview.kt        (if exists)
     util/Finsible${componentName}*.kt                 (if any)
3. Update templateLibrary.components.Finsible${componentName} with:
     - status (complete|in-progress|planned|deprecated)
     - files (all actual paths)
     - params (every public parameter with type, default, constraints)
     - variants (enum values if any)
     - modelTypes (data class names from models file)
     - tokenUsage.colors / typography / spacing / sizes / stroke / elevation (actual tokens used)
     - constraints (all require() guards)
4. Add any new symbols from these files to graph.symbols
5. Write the patched graph back to .pi/context-graph.json

Do NOT modify any other section of the graph.`;
}

export default function (pi: any) {

  // 1. Session start — show graph status in UI
  pi.on("session_start", async (_event: any, ctx: any) => {
    try {
      const raw = await fs.readFile(GRAPH_PATH, "utf-8");
      const graph = JSON.parse(raw);

      const tpl = graph.templateLibrary ?? {};
      const ui = graph.ui ?? {};
      const data = graph.data ?? {};

      const components = Object.keys(tpl.components ?? {}).filter(k => !k.startsWith("_"));
      const symbols = Object.keys(graph.symbols ?? {}).filter(k => !k.startsWith("_") && !k.startsWith("FILL"));
      const vms = Object.keys((ui.viewModels ?? {})).filter(k => !k.startsWith("_") && !k.startsWith("FILL"));
      const entities = Object.keys((data.entities ?? {})).filter(k => !k.startsWith("_") && !k.startsWith("FILL"));

      ctx.ui.notify(
        `✅ Graph v${graph._schema?.version ?? "?"} — ${components.length} components · ${symbols.length} symbols · ${vms.length} VMs · ${entities.length} entities`,
        "success"
      );
    } catch {
      ctx.ui.notify(
        "⚠️ No context-graph.json — run /build-graph to generate.",
        "warning"
      );
    }
  });

  // 2. Inject compact summary into system prompt before every agent turn
  pi.on("before_agent_start", async (event: any, ctx: any) => {
    try {
      const raw = await fs.readFile(GRAPH_PATH, "utf-8");
      const graph = JSON.parse(raw);
      const summary = buildSessionSummary(graph);

      return {
        systemPrompt: event.systemPrompt + "\n\n" + summary
      };
    } catch {
      return {};
    }
  });

  // 3. Register the lookup_graph tool — MODEL uses this for O(1) context lookups
  pi.registerTool({
    name: "lookup_graph",
    label: "Lookup Context Graph",
    description:
      "Query the Finsible project context graph to find file paths, component params, theme tokens, " +
      "ViewModel deps, entity schemas, repository methods, and architectural rules. " +
      "MUST be used before any file search tool for anything indexed in the graph. " +
      "Prefix queries: 'rule:name', 'vm:Name', 'entity:Name', 'repo:Name'. " +
      "Bare name queries resolve symbols → template components → theme tokens with fuzzy fallback.",
    parameters: {
      type: "object",
      properties: {
        query: {
          type: "string",
          description:
            "Class name, component name, theme token, or prefixed query. " +
            "Examples: 'FinsibleButton', 'FinsibleTheme', 'rule:no-material-theme-extension', " +
            "'vm:DashboardViewModel', 'entity:AccountEntity', 'repo:AccountRepository', 'primaryBackground'"
        }
      },
      required: ["query"]
    },
    async execute(_id: string, params: any) {
      try {
        const raw = await fs.readFile(GRAPH_PATH, "utf-8");
        const graph = JSON.parse(raw);
        const result = lookupGraph(graph, params.query);
        return { content: [{ type: "text", text: result }] };
      } catch (e: any) {
        return { content: [{ type: "text", text: `Graph read error: ${e.message}. Run /build-graph to regenerate.` }] };
      }
    }
  });

  // Intercept user input and append a tactical reminder (beats LLM recency bias)
  pi.on("input", async (event: any, ctx: any) => {
    // Do not transform slash commands or empty inputs
    if (!event.text || event.text.startsWith("/")) {
      return { action: "continue" };
    }

    const reminder = "\n\n[Use `lookup_graph` before any file search or code write.]";

    return {
      action: "transform",
      text: event.text + reminder
    };
  });

  // 4. /graph-status — quick health check
  pi.registerCommand("graph-status", {
    description: "Check if context-graph.json is current and report stats",
    handler: async (_args: any, ctx: any) => {
      try {
        const raw = await fs.readFile(GRAPH_PATH, "utf-8");
        const graph = JSON.parse(raw);

        const tpl = graph.templateLibrary ?? {};
        const ui = graph.ui ?? {};
        const data = graph.data ?? {};
        const meta = graph._schema ?? {};

        const components = Object.keys(tpl.components ?? {}).filter(k => !k.startsWith("_"));
        const symbols = Object.keys(graph.symbols ?? {}).filter(k => !k.startsWith("_") && !k.startsWith("FILL"));
        const vms = Object.keys((ui.viewModels ?? {})).filter(k => !k.startsWith("_") && !k.startsWith("FILL"));
        const entities = Object.keys((data.entities ?? {})).filter(k => !k.startsWith("_") && !k.startsWith("FILL"));
        const repos = Object.keys((data.repositories ?? {})).filter(k => !k.startsWith("_") && !k.startsWith("FILL"));
        const diModules = Object.keys((data.di ?? {})).filter(k => !k.startsWith("_") && !k.startsWith("FILL"));
        const colorTokens = Object.keys(graph.theme?.colorTokens ?? {}).filter(k => !k.startsWith("_") && !k.startsWith("FILL"));
        const fillCount = (raw.match(/"FILL/g) || []).length;

        const lines = [
          `📊 Graph v${meta.version ?? "?"} — generated ${meta.generated ?? "unknown"}`,
          `   root: ${meta.rootPackage ?? "?"}`,
          ``,
          `   Components:  ${components.length}  (${components.join(", ") || "none"})`,
          `   Symbols:     ${symbols.length}`,
          `   ViewModels:  ${vms.length}`,
          `   Entities:    ${entities.length}`,
          `   Repos:       ${repos.length}`,
          `   DI modules:  ${diModules.length}`,
          `   Color tokens: ${colorTokens.length}`,
          ``,
          fillCount > 0
            ? `⚠️ ${fillCount} FILL: placeholders remaining — graph is incomplete. Run /build-graph.`
            : `✅ No FILL: placeholders — graph is complete.`
        ];

        ctx.ui.notify(lines.join("\n"), fillCount > 0 ? "warning" : "success");
      } catch {
        ctx.ui.notify("❌ No context-graph.json found. Run /build-graph to generate.", "error");
      }
    }
  });

  // 5. /build-graph — full scan and regenerate
  pi.registerCommand("build-graph", {
    description: "Scan entire project and generate .pi/context-graph.json from schema",
    handler: async (_args: any, ctx: any) => {
      try {
        await fs.access(SCHEMA_PATH);
      } catch {
        ctx.ui.notify(`⚠️ Schema not found at ${SCHEMA_PATH} — add it first.`, "warning");
        return;
      }

      ctx.ui.notify("Build prompt loaded. Review and press Enter to execute.", "info");
      ctx.ui.setEditorText(BUILD_PROMPT);
    }
  });

  // 6. /update-graph — patch a single component entry
  pi.registerCommand("update-graph", {
    description: "Update a single template component entry in context-graph.json (/update-graph Button)",
    handler: async (args: string, ctx: any) => {
      if (!args || !args.trim()) {
        ctx.ui.notify("Usage: /update-graph <ComponentName>  (e.g. /update-graph Button)", "warning");
        return;
      }

      const componentName = args.trim();
      try {
        await fs.access(GRAPH_PATH);
      } catch {
        ctx.ui.notify("No context-graph.json found. Run /build-graph first.", "warning");
        return;
      }

      const prompt = buildUpdatePrompt(componentName);
      ctx.ui.notify(`Update prompt for ${componentName} loaded. Review and press Enter.`, "info");
      ctx.ui.setEditorText(prompt);
    }
  });
}