# Create Stock Bridge — maric fork

A fork of [Create Stock Bridge](https://github.com/tom5454/Create-Stock-Bridge) by
**tom5454** (MIT). Adds the **AE Stock Provider**.

Not affiliated with, endorsed by, or sponsored by tom5454, the Applied Energistics
project, or the Create team.

## What this fork adds

**AE Stock Provider** (`createstockbridge:ae_provider`) — a unidirectional
**AE -> Create** block.

The original bridge is limited by a single Packager: `PackagerBlockEntity.CYCLE` is 20
ticks, so AE -> Create dispatch caps at roughly one package per second no matter how many
Factory Gauges you run. The provider lets you attach extra Packagers in parallel, so
throughput scales with how many providers you place.

- **AE -> Create only.** Create can request any ME item through the provider's Packager.
- Exposes nothing back to AE2 and does not run AE2 virtual-pattern crafting.
- Create -> AE continues to work through the normal frogport-to-ME-interface package path.
- Craft a bridge to get a provider and vice-versa; they share a block-entity type.

Behavioural fixes that go with it:

- **Reporter election** — among AE-facing links on a frequency that have an adjacent
  Packager, only the lowest `BlockPos` reports the ME inventory, so ME stock is not
  counted more than once. Mixing a full bridge with providers is safe.
- **Busy fall-through** — `processRequest()` returns `null` when the Packager is busy
  (`isTooBusyFor(RESTOCK)`, queue >= 10) so requests fall through to the next provider
  instead of failing.
- `BridgeBehaviour.getItems()` skips AE-facing links, so a full bridge does not fold its
  own ME network back into the Create -> AE summary.
- `remountStorage()` is a no-op in provider mode, which avoids AE2's
  `The given node is not part of this grid or has no storage provider` crash when the
  cable bus merges grids.

## Caveats

- All AE-facing blocks sharing one Create frequency should sit on the **same ME network**;
  otherwise the single reporter hides the others.
- Frog ports move roughly two packages per second each — budget about one frog port per
  two providers.
- Targets upstream v0.2.0 (`d09c6fa`). Rebase if upstream 0.2.x+ lands.

## Building

Requires JDK 21.

    ./gradlew build

## Licensing

The upstream mod is MIT (Copyright (c) 2022 tom5454); the upstream text is preserved in
`LICENSE-UPSTREAM-MIT`.

**This fork is licensed LGPL-3.0-or-later**: `LICENSE` holds the LGPL-3.0 text
verbatim, and `LICENSE-GPL-3.0.txt` holds the GPL-3.0 text that LGPL-3.0 incorporates.
The fork adds one texture derived from `ae2:block/interface` from Applied
Energistics 2, which is LGPL-3.0 and therefore cannot be redistributed under MIT.

See `NOTICE.md` for full attribution and a change log.