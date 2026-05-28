package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Layer 1 — Primitive color palette.
 *
 * These are the raw building blocks. No component ever references these directly.
 * Semantic tokens (Layer 2) alias these; component tokens (Layer 3) alias semantics.
 *
 * Ramp steps run 50→950. Values match the light-mode palette;
 * dark-mode overrides are resolved at the semantic layer.
 */
object FinsiblePrimitives {

    // ── Green (brand base) ──────────────────────────────────────────────
    object Green {
        val g50  = Color(0xFFEBF6F1)
        val g100 = Color(0xFFD0ECE0)
        val g200 = Color(0xFF8AD1AB)
        val g400 = Color(0xFF4CAF78)
        val g600 = Color(0xFF2E8B57)
        val g700 = Color(0xFF267A4C)
        val g800 = Color(0xFF1D6B40)
        val g900 = Color(0xFF174E30)
        val g950 = Color(0xFF0A2E1A)
    }

    // ── Neutral ─────────────────────────────────────────────────────────
    object Neutral {
        val n0   = Color(0xFFFFFFFF)
        val n50  = Color(0xFFFAFAEB)
        val n100 = Color(0xFFF5F5F7)
        val n200 = Color(0xFFEFEFF2)
        val n300 = Color(0xFFE8E8EC)
        val n400 = Color(0xFF9E9EA8)
        val n500 = Color(0xFF6E6E78)
        val n600 = Color(0xFF4E4E58)
        val n700 = Color(0xFF1A1A20)
        val n800 = Color(0xFF101013)
    }

    // ── Red (error) ─────────────────────────────────────────────────────
    object Red {
        val r50  = Color(0xFFF5E0E0)
        val r600 = Color(0xFFCC4444)
    }

    // ── Amber (warning) ─────────────────────────────────────────────────
    object Amber {
        val a50  = Color(0xFFF5EDD8)
        val a600 = Color(0xFFC27A1A)
    }

    // ── Teal (success) ──────────────────────────────────────────────────
    object Teal {
        val t50  = Color(0xFFDAE7E2)
        val t600 = Color(0xFF28855E)
    }

    // ── Blue (info) ─────────────────────────────────────────────────────
    object Blue {
        val b50  = Color(0xFFE4EEF8)
        val b600 = Color(0xFF3574C4)
    }
}
