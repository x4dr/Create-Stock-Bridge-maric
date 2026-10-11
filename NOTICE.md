# Attribution

## Create Stock Bridge — fork notes

This repository is a fork of **Create Stock Bridge** by **tom5454**.

- Upstream project: https://github.com/tom5454/Create-Stock-Bridge
- Upstream CurseForge: https://www.curseforge.com/minecraft/mc-mods/create-stock-bridge

### Licensing of this fork

The upstream mod is licensed **MIT** (Copyright (c) 2022 tom5454). The full upstream
license text is preserved in `LICENSE-UPSTREAM-MIT`.

**This fork as a whole is distributed under the GNU Lesser General Public License v3.0
or later** (`LICENSE`, verbatim). `LICENSE-GPL-3.0.txt` holds the GNU General Public
License v3.0 text that LGPL-3.0 incorporates.

Why the whole fork is LGPL-3.0 and not MIT: the fork ships one texture,
`createstockbridge:block/provider_interface`, which is a recoloured derivative of
`ae2:block/interface` from **Applied Energistics 2**. AE2 is licensed LGPL-3.0, so that
derived asset cannot be redistributed under MIT terms. Relicensing the whole fork to
LGPL-3.0 is the conservative and compatible way to ship it. See **Assets** below for the
full asset breakdown.

`LICENSE` (LGPL-3.0) is compatible with the upstream MIT terms — MIT code may be
combined into an LGPL work provided the MIT notice is retained, which it is, in
`LICENSE-UPSTREAM-MIT` and in this file.

### Assets

- `assets/createstockbridge/textures/block/provider_interface.png` — **added by this
  fork.** A recoloured derivative of `ae2:block/interface` from **Applied Energistics 2**
  (LGPL-3.0): every pixel is an exact per-channel function of the corresponding AE2 pixel.
  Copyright (c) the Applied Energistics team. **This asset is the sole reason the fork is
  LGPL-3.0 rather than MIT.**
- `assets/ae2/textures/guis/csb_bridge.png` — authored by upstream at `d09c6fa`, carried
  through unchanged and verified byte-identical to the copy in the upstream release
  (sha1 `817963aa…`). It lives under the `ae2` namespace so it resolves at runtime.
  Its status is unchanged from upstream; this fork simply covers it with the same terms as
  the rest of the jar.

No Create texture file is copied or redistributed. The `ae_provider` block models
reference `create:block/*` textures **by name only**; Create is MIT for code and
All Rights Reserved for assets, so referencing by name without copying is the correct
approach.

All other upstream assets remain Copyright (c) tom5454, MIT.

## Changes from upstream

Based on upstream `d09c6fa` (v0.2.0), branch `main`.

### Added

- **AE Stock Provider** (`createstockbridge:ae_provider`) — a unidirectional
  **AE -> Create** block. Create can request any item from the ME network through
  an adjacent Packager. It exposes nothing back to AE2 and does not participate in
  AE2 virtual-pattern crafting. Create -> AE continues to work through the normal
  frogport-to-ME-interface package path.
  - Rationale: one Stock Bridge is limited by a single Packager, which emits one
    package per 20-tick cycle (`PackagerBlockEntity.CYCLE = 20`), capping AE -> Create
    dispatch at roughly one package per second regardless of how many Factory Gauges you
    run. Placing additional providers alongside additional packagers parallelises it.
  - The provider and the original bridge share a block-entity type. Crafting a bridge
    yields a provider and vice-versa.

### Changed

- **Reporter election** in `fetchSummaryFromPackager()`: among AE-facing links on a
  Create frequency that have an adjacent Packager, only the one at the lowest `BlockPos`
  reports the ME inventory; the rest report `EMPTY`. This prevents the ME stock being
  counted more than once when several providers (or a provider plus a full bridge) are
  placed, and makes mixed layouts safe.
- **Busy fall-through** in `processRequest()`: returns `null` when the adjacent Packager
  reports `isTooBusyFor(RESTOCK)` (queue >= 10), so `findPackagersForRequest` falls
  through to the next provider rather than failing the request outright.
- `BridgeBehaviour.getItems()` now skips AE-facing links, so a full bridge does not fold
  its own ME network back into the Create -> AE summary.
- `remountStorage()` is a no-op in provider mode. Without this, AE2 throws
  `The given node is not part of this grid or has no storage provider` as soon as the
  cable bus merges the provider's grid with another.

### Known caveats

- All AE-facing blocks sharing one Create frequency should sit on the same ME network;
  otherwise the single reporter hides the others.
- Frog ports move roughly two packages per second each, so budget about one frog port per
  two providers.
- This fork targets upstream v0.2.0. Rebase if upstream 0.2.x+ lands.

## Not affiliated with

Not affiliated with, endorsed by, or sponsored by tom5454, by the Applied Energistics
project, or by the Create team.