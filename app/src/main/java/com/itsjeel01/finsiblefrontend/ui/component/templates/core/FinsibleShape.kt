package com.itsjeel01.finsiblefrontend.ui.component.templates.core

/** Shape variants for Finsible components.
 * Circle is for icon-only square buttons and Pill for labeled fully-rounded buttons.
 * */
enum class FinsibleShape {
    /** Fully circular intent, typically for square icon-only surfaces and badges. */
    Circle,

    /** Fully rounded labeled-surface intent (currently same runtime shape as Circle by design). */
    Pill,

    /** Standard rounded corners (often variable by component size). */
    Rounded,

    /** No rounding, sharp corners (often used for full-width views or tiles). */
    Sharp
}

