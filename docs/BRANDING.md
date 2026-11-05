# IntiVentory Branding

**Status:** The user-facing name, logo direction, caption, and light theme are selected for the MVP. This document is the source of truth for the approved visual identity and future branding changes.

## Approved app identity

- **English name:** IntiVentory
- **Telugu name / pronunciation guide:** ఇంటి వెంటరీ
- **Caption / tagline:** మన ఇంటి సరుకులు, మన ఇంటి లెక్కలు.
- **English meaning:** Our household supplies, our household accounts.
- **Name idea:** Inti (ఇంటి) + Inventory

The repository is named `IntiVentory-Android`. The Android application ID is `com.boyidapu.homestock`; it is a technical identifier and should not be changed merely to match the display name.

## Logo reference assets

- [Full logo lockup — SVG](assets/intiventory-logo.svg)
- [App icon concept — SVG](assets/intiventory-app-icon.svg)

Use these SVGs as the visual source of truth for the logo concept: a rounded deep-teal tile containing a simple white home outline and shelf, with soft-sage household-product accents. The wordmark uses Deep Teal and the Telugu caption appears beneath it.

The SVG assets are reference artwork. During implementation, prepare and verify the actual Android launcher/adaptive icon assets and inspect Telugu glyph rendering on-device. Do not invent a different logo or replace the palette without approval.

## Locked color palette

| Role | Name | Hex |
|---|---|---|
| Primary | Deep Teal | `#2F6B62` |
| Accent | Soft Sage | `#B9D0C0` |
| Main background | Warm Mist | `#F4F6F0` |
| Surfaces / cards | Clean White | `#FFFFFF` |
| Main text | Forest Charcoal | `#273832` |
| Secondary text | Muted Sage-Gray | `#52665C` |

## UI direction

- Calm, friendly, Telugu-household-friendly, and uncluttered.
- Light-first Jetpack Compose + Material 3 design.
- Use Deep Teal for primary actions, selected navigation, and key emphasis.
- Use Soft Sage for quiet category/icon accents.
- Use Warm Mist as the page background, and Clean White for cards and forms.
- Use Forest Charcoal for main text and Muted Sage-Gray for secondary text.
- Favor comfortable spacing, rounded cards, simple icons, and clear ₹ spending totals.
- Avoid neon palettes, purple AI gradients, excessive shadows, busy illustrations, and unnecessary animation.
- Keep the logo mark recognizable at small sizes; do not put tiny text inside the app icon.

## Implementation guidance

- App display name should be **IntiVentory**.
- Keep the Android application ID `com.boyidapu.homestock` unchanged unless a separately approved migration requires a change.
- Do not add accounts, network services, analytics, or any unrelated feature as part of branding work.
- The accepted app implementation uses this branding direction. The SVG files remain the reference artwork; verify any launcher/adaptive icon variants in the Android build and on the physical device.
