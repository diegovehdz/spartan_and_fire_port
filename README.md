# SpartanFire — NeoForge 1.21.1 port

Port of [KreloX's **SpartanFire**](https://github.com/KreloX/SpartanFire) ("Spartan Weaponry: Ice and Fire")
from Forge 1.20.1 to **NeoForge 1.21.1**.

Adds Spartan-Weaponry-style weapons forged from **Ice and Fire** materials (dragonbone, dragonsteel),
built against:
- **Spartan Weaponry: Unofficial** (`org.xiyu.spartanweaponryunofficial`) — the maintained 1.21.1 fork, which exposes the addon API (`SpartanWeaponryAPI`, `WeaponMaterial`).
- **Ice and Fire: Community Edition** (`com.iafenvoy.iceandfire`) — provides the material tiers (`IafTiers`).

Original mod is MIT (KreloX). This port keeps that license.

> Standalone project (originally prototyped inside the *Requiem* modpack, now decoupled).

## Status — paused / archived

**Working vertical slice** (verified in-game): **dragonbone** weapons — dagger, longsword, greatsword,
battle hammer, halberd, heavy crossbow. Items register, models/textures render, material tooltip works,
and **Better Combat** animations work via explicit `weapon_attributes`.

Scope target if resumed: dragonbone + dragonsteel (7 materials). Myrmex weapons intentionally out of scope.

### Next steps if resumed (in order)
1. **Elemental dragonbone** (flamed / iced / lightning) — reuse `IafTiers.BLOODED_DRAGONBONE_TOOL_MATERIAL`
   for stats; differentiate via traits. Reimplement the 6 custom traits (Flamed, Iced, Shocked,
   Fire/Ice dragon-damage-bonus, Poisoned, Non-arthropod-bonus) against the fork's `WeaponTrait` API
   (note: traits are assigned by datapack tag in the fork, not by object).
2. **Dragonsteel** (fire / ice / lightning) — `IafTiers.DRAGONSTEEL_*`.
3. **Recipes** (dragon-blood upgrade recipes, witherbone handle/pole) + **per-type item tags**,
   **trait tags**, **weapon_attributes**, **lang** for every new weapon.
4. **Overgeared addon** — separate mod, mirror `overgearedspartan` / `overgeared_iaf` patterns.

See `PORT_NOTES.md` for the full API/registry mapping and porting decisions.

## Building

Requires **JDK 21** (NeoGradle 7.x + Gradle 9.2.1 do not run on newer JDKs).

1. Drop the three dependency jars into `libs/` (they are gitignored — not redistributed):
   - `spartan_weaponry_unofficial-*.jar`
   - `iceandfire-*.jar` (Community Edition)
   (copy them from the Requiem pack's `mods/` / `overgeared-ports/` folders)
2. Build:
   ```sh
   JAVA_HOME=/path/to/jdk-21 ./gradlew build --no-configuration-cache
   ```
3. Output jar: `build/libs/spartanfire-*.jar` → drop into the pack's `mods/`.

The dependency jars are Mojmap (unobfuscated), so they are consumed directly as `compileOnly`
file dependencies — no Maven repo needed.
