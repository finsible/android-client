const fs = require("node:fs/promises");
const path = require("node:path");

const staged = new Map();
let gateOpen = false;

// Helper to resolve an index (e.g., "1") or a raw path back to the actual file path
function resolveTarget(input: string): string | null {
  const cleanInput = input.trim();
  if (!cleanInput) return null;

  const keys = Array.from(staged.keys());
  const index = parseInt(cleanInput, 10);

  // If user passed a valid number like "1" or "2"
  if (!isNaN(index) && index > 0 && index <= keys.length) {
    return keys[index - 1];
  }

  // Otherwise assume they typed the literal path
  return cleanInput;
}

export default function (pi: any) {
  // 1. APPROVE
  pi.registerCommand("approve", {
    description: "Approve all or specific staged file (/approve <number>)",
    handler: async (args: string, ctx: any) => {
      if (staged.size === 0) {
        ctx.ui.notify("Nothing staged.", "info");
        return;
      }

      const targetPath = resolveTarget(args);

      // PARTIAL APPROVE
      if (targetPath) {
        if (!staged.has(targetPath)) {
          ctx.ui.notify(`${targetPath} is not staged.`, "warning");
          return;
        }
        gateOpen = true;
        await fs.writeFile(targetPath, staged.get(targetPath), "utf-8");
        staged.delete(targetPath);
        gateOpen = false;
        ctx.ui.notify(`✅ Applied: ${targetPath}`, "success");
        return;
      }

      // FULL APPROVE
      gateOpen = true;
      const applied = [];
      for (const [filePath, content] of staged) {
        await fs.writeFile(filePath, content, "utf-8");
        applied.push(filePath);
      }
      staged.clear();
      gateOpen = false;
      ctx.ui.notify(`✅ Applied ALL:\n${applied.map((p: string) => "  • " + p).join("\n")}`, "success");
    },
  });

  // 2. REVERT
  pi.registerCommand("revert", {
    description: "Revert all or specific staged file (/revert <number>)",
    handler: async (args: string, ctx: any) => {
      if (staged.size === 0) {
        ctx.ui.notify("Nothing staged.", "info");
        return;
      }

      const targetPath = resolveTarget(args);

      // PARTIAL REVERT
      if (targetPath) {
        if (!staged.has(targetPath)) {
          ctx.ui.notify(`${targetPath} is not staged.`, "warning");
          return;
        }
        staged.delete(targetPath);
        ctx.ui.notify(`↩️ Reverted: ${targetPath}`, "info");
        return;
      }

      // FULL REVERT
      const count = staged.size;
      staged.clear();
      ctx.ui.notify(`↩️ Reverted ALL ${count} staged change(s).`, "info");
    },
  });

  // 3. PENDING
  pi.registerCommand("pending", {
    description: "List staged files with their index numbers",
    handler: async (_args: any, ctx: any) => {
      if (staged.size === 0) {
        ctx.ui.notify("Nothing staged.", "info");
        return;
      }
      const keys = Array.from(staged.keys());
      const list = keys.map((p, i) => `  [${i + 1}] ${p}`).join("\n");
      ctx.ui.notify(`Staged files:\n${list}\n\nTip: Use /approve 1 or /diff 1`, "info");
    },
  });

  // 4. DIFF
  pi.registerCommand("diff", {
    description: "Show staged vs disk for a file (/diff <number>)",
    handler: async (args: string, ctx: any) => {
      const filePath = resolveTarget(args);
      if (!filePath) {
        ctx.ui.notify("Usage: /diff <number> or /diff <path>", "warning");
        return;
      }
      if (!staged.has(filePath)) {
        ctx.ui.notify(`${filePath} is not staged.`, "warning");
        return;
      }

      let current = "(new file)";
      try {
        current = await fs.readFile(filePath, "utf-8");
      } catch {}

      const stagedContent = staged.get(filePath);
      const diffOutput = `=== CURRENT (${filePath}) ===\n\n${current}\n\n=== STAGED (${filePath}) ===\n\n${stagedContent}`;

      const diffPath = path.join(process.cwd(), ".pi", "staged-diff.txt");
      await fs.writeFile(diffPath, diffOutput, "utf-8");

      ctx.ui.notify(`Diff written! Open .pi/staged-diff.txt to review.`, "success");
    },
  });

  // 5. Intercept the LLM's writes
  pi.on("tool_call", async (event: any, ctx: any) => {
    if (!gateOpen && (event.toolName === "write" || event.toolName === "edit")) {
      const filePath = event.input.path ?? "unknown";

      let content: string;
      if (event.toolName === "write") {
        // write tool: content is provided directly
        content = event.input.content ?? "";
      } else {
        // edit tool: compute result by applying edits to current file
        // Chain on already-staged content so sequential edits on same file accumulate
        let baseContent: string;
        if (staged.has(filePath)) {
          baseContent = staged.get(filePath)!;
        } else {
          try {
            baseContent = await fs.readFile(filePath, "utf-8");
          } catch {
            baseContent = "";
          }
        }
        content = baseContent;
        for (const e of event.input.edits ?? []) {
          content = content.replace(e.oldText, e.newText);
        }
      }

      staged.set(filePath, content);
      const index = Array.from(staged.keys()).indexOf(filePath) + 1;

      ctx.ui.notify(`📋 STAGED: [${index}] ${filePath}`, "warning");

      return { block: true, reason: "Staged for user approval." };
    }
  });
}