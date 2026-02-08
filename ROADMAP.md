# MyRoutine Android App - ROADMAP

## Overview
Kids daily activity tracker with reward points, printable routine sheets, and Galaxy Tab optimization.

**Location**: `apps/myroutine-android/`
**Package**: `com.halallens.myroutine`
**Epic**: #505

---

## Sprint 1: Foundation

| Issue | Feature | Status |
|-------|---------|--------|
| #506 | Project scaffolding: Gradle, version catalog, build config | DONE |
| #507 | Android resources: Manifest, strings, colors, icons | DONE |
| #508 | App entry: Application class, MainActivity, Hilt setup | DONE |
| #509 | Room database: entities, DAOs, TypeConverters, migrations | DONE |
| #510 | Domain models: ChildProfile, Routine, ActivityLog, TimeSlot, ThemeType | DONE |
| #511 | Repositories + DataStore preferences | DONE |
| #512 | DI modules: AppModule, DatabaseModule, RepositoryModule | DONE |

## Sprint 2: Core UI

| Issue | Feature | Status |
|-------|---------|--------|
| #513 | Theme system: Boy/Girl/Neutral color schemes + kid typography | DONE |
| #514 | Navigation: Routes, NavGraph, BottomNavBar | DONE |
| #515 | Splash + Onboarding: Theme selection + Child setup screens | DONE |
| #516 | Use cases: GetTodayRoutines, ToggleCompletion, SeedDefaults | DONE |
| #517 | Today screen: daily checklist with checkboxes grouped by TimeSlot | DONE |
| #518 | Reusable components: ActivityCheckItem, TimeSlotHeader, PointsBadge | DONE |

## Sprint 3: Features

| Issue | Feature | Status |
|-------|---------|--------|
| #519 | Use cases: CalculateRewardPoints, GetWeeklyProgress | DONE |
| #520 | Weekly screen: 7-day grid with completion indicators | DONE |
| #521 | Rewards screen: points total, milestones, badges, streaks | DONE |
| #522 | Settings screen: edit routines, manage children, theme switch | DONE |
| #523 | Edit Routine + Manage Children sub-screens | DONE |

## Sprint 4: Polish

| Issue | Feature | Status |
|-------|---------|--------|
| #524 | Galaxy Tab optimization: WindowSizeClass adaptive layouts | DONE |
| #525 | Print feature: PrintManager integration + printable sheet | DONE |
| #526 | ProGuard rules for release build | DONE |
| #527 | Unit tests: use cases, DAOs, date utils | DONE |
| #528 | Fastlane metadata + Play Store listing | Created |
| #529 | README + ROADMAP documentation | DONE |

## Sprint 5: Routine Templates & Visual Overhaul

| Feature | Status |
|---------|--------|
| "Sunlit Playroom" visual overhaul: animations, gradients, micro-interactions across all screens | DONE |
| Domain models: TemplateCategory, TemplateRoutine, RoutineTemplate | DONE |
| Built-in template library: 6 templates (3 Islamic + 3 General) | DONE |
| Room entities + migration for custom templates | DONE |
| RoutineTemplateDao, TemplateRepository | DONE |
| ApplyTemplateUseCase with diff preview and replace-with-confirmation | DONE |
| Glassmorphism UI components: GlassCard, GlassPillButton | DONE |
| TemplatePicker screen with category tabs and glassmorphism cards | DONE |
| QuickBuilder screen for custom template creation (checkbox-based) | DONE |
| Navigation integration: onboarding flow now includes template selection | DONE |
| Settings: "Change Routine Template" option | DONE |
| Remove auto-seeding from onboarding and ManageChildren | DONE |

### Built-in Templates

**Islamic:**
- Ramadan Routine (15 routines) — suhoor, prayers, iftar, taraweeh
- Daily Islamic (19 routines) — five daily prayers + standard activities
- Weekend Islamic (12 routines) — prayers, Quran, family time

**General:**
- School Day (18 routines) — standard weekday for school-age kids
- Weekend Fun (10 routines) — relaxed weekend with play and creativity
- Summer Break (12 routines) — balanced summer with learning and family

### Bug Fixes & Enhancements (2026-02-07)

| Feature | Status |
|---------|--------|
| Fix multi-child data loss: createChild no longer deactivates all children | DONE |
| Navigate to template picker after adding new child | DONE |
| Fix theme persistence per-child: sync on switch + save to profile | DONE |
| Inline template editing: toggle routines on/off before applying | DONE |
| Weekly screen visual overhaul: glassmorphism + progress rings | DONE |
| WeekDayCell redesign: glass card with circular progress ring | DONE |

## Sprint 6: Branding & Localization (2026-02-08)

| Feature | Status |
|---------|--------|
| **Branding Change: MyRoutine → Flinkis** | DONE |
| Package rename: `com.halallens.myroutine` → `com.halallens.flinkis` | DONE |
| App name: "Flinkis" (from MyRoutine) | DONE |
| All user-facing strings updated to Flinkis branding | DONE |
| **Content Updates** | DONE |
| Splash subtitle: "Ad-free by halallens.no" with fade-in animation | DONE |
| About section: Rewritten to mention Nafisa's request and sharing with kids worldwide | DONE |
| Contact email: Changed from hasan@halallens.no → mac@halallens.no | DONE |
| **Language Selection Feature** | DONE |
| LocaleHelper utility: SharedPreferences-based locale management with context wrapping | DONE |
| 14 supported locales: en, ar, da, de, fr, in, ja, ko, ms, nb, nl, sv, th, tr | DONE |
| LanguageSelectionScreen: 2-column grid with flag emojis + native names | DONE |
| Onboarding flow updated: Splash → Language → Theme → Child Setup → Template → Today | DONE |
| Settings language picker: ModalBottomSheet with 14 languages | DONE |
| Instant locale switching via activity.recreate() | DONE |
| Locale persistence in MyRoutineApp.attachBaseContext() and MainActivity.attachBaseContext() | DONE |
| Smart splash routing: checks language selection status before theme selection | DONE |
| All 14 locale files updated with language settings strings | DONE |

### Build Artifacts (v1.0.0)

| Artifact | Size | Notes |
|----------|------|-------|
| Debug APK | 16.8 MB | Full debug symbols |
| Release APK | 7.8 MB | Unsigned, minified with R8 |
| Release AAB | 19.0 MB | Ready for Play Store |

---

*Last Updated: 2026-02-08*
