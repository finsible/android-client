---
description: "Master the art of atomic commits and intent-based sequencing to create a clean, logical, and review-friendly Git history."
---

## 1. The Atomic Commit Philosophy

* **One Logical Change:** A single commit must represent exactly one logical unit of work. If a
  commit can be described with the word "and", it must be split.
* **Non-Breaking States:** Every individual commit in the history must compile. Never leave the tree
  in a broken state midway through a patch series.
* **Separation of Concerns:**
    * UI tweaks belong in UI commits.
    * Logic changes belong in Logic commits.
    * Dependency updates belong in Chore commits.

## 2. Intent-Based Sequencing (The Order of Operations)

Commits must be sequenced logically so the build never breaks and the review process tells a
chronological story. Enforce this strict order of intent:

1. **Cleanup & Refactoring:** Formatting or restructuring existing code must *always* precede the
   feature patch that relies on it.
2. **Foundations (Data Layer):** Schema changes, ObjectBox/Spring Boot entity updates, or
   migrations.
3. **Business Logic (Domain Layer):** Repository updates, API integrations, and Use Cases that
   consume the new foundations.
4. **Presentation (UI Layer):** ViewModels, Compose screens, and `FinsibleTheme` updates that expose
   the logic to the user.

## 3. Commit Message Anatomy

* **Subject Line (The "What"):**
    * Maximum 50 characters.
    * Must be a Title Case imperative verb. Valid starts: "Add", "Remove", "Implement", "Update", "
      Fix", "Refactor", "Extract", "Rename", "Delete", "Wire".
    * **ILLEGAL:** Past tense, gerunds, ending periods, or type prefixes (`feat:`, `fix:`).
* **Blank Line:** Must strictly separate the subject from the body.
* **Body (The "Why" and "How"):**
    * Wrap text at 72 characters. Focus on *why* the change was made, not just a literal translation
      of the diff.

## 4. Git CLI Command Reference

* **Staging:**
    * `git add <file>` — Stage whole files.
    * `git add -p <file>` — Stage specific hunks (for splitting files across logical bounds).
* **Execution & Stashing:**
    * `git commit -m "..." -m "..."` — Execute the commit with subject and body.
    * `git stash push --keep-index -m "WIP: unstaged remnants"` — Stash unstaged changes to ensure
      the commit builds cleanly.