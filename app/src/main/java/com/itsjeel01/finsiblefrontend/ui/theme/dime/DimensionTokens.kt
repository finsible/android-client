package com.itsjeel01.finsiblefrontend.ui.theme.dime

enum class Size {
    ZERO, S1, S2, S4, S6, S8, S10, S12, S14, S16,
    S18, S20, S22, S24, S26, S28, S30, S32,
    S34, S36, S38, S40, S42, S44, S46, S48,
    S50, S52, S54, S56, S58, S60, S64, S68,
    S72, S76, S80, S88, S96, S104, S112, S120,
    S128, S136, S144, S152, S160, S168, S176, S184,
    S192, S200, FULL
}

enum class IconSize {
    XS,     // 12dp - Small indicators, badges
    SM,     // 16dp - Standard icons (Material 3 baseline)
    MD,     // 20dp - Prominent icons
    LG,     // 28dp - Large feature icons
    XL      // 40dp - Hero icons, avatars
}

enum class Radius {
    ZERO,   // 0dp   - No rounding
    XS,     // 2dp   - Subtle rounding
    SM,     // 4dp   - Small rounding
    MD,     // 8dp   - Standard rounding (Material 3 baseline)
    LG,     // 16dp  - Large rounding
    XL,     // 24dp  - Extra large rounding
    PILL,    // 50%   - Pill shape (computed at runtime)
    FULL    // 100%  - Full rounding (circle)
}

enum class BorderStroke {
    THIN,   // 0.5dp - Subtle borders, dividers
    MEDIUM, // 1dp   - Standard borders
    THICK   // 2dp   - Prominent borders, focus indicators
}

enum class Layout {
    SCREEN_PADDING,     // Main screen edge padding
    CONTAINER_PADDING,  // Container internal padding
    CARD_PADDING,       // Card internal padding
    SECTION_SPACING,    // Spacing between major sections
    ELEMENT_SPACING     // Spacing between related elements
}