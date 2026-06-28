# Port notes — SpartanFire 1.20.1 (Forge) → 1.21.1 (NeoForge)

## Architecture
Original SpartanFire builds on KreloX's `krelox.spartantoolkit` library (`SpartanAddon`, `SpartanMaterial`,
`WeaponMap`), which was **never ported to 1.21.1**. Rather than port the whole toolkit, this port rewrites
directly against the **xiyu fork's native addon API**.

## API mapping (fork: `org.xiyu.spartanweaponryunofficial.api`)
- Weapons: `SpartanWeaponryAPI.createDagger/createLongsword/createGreatsword/createBattleHammer/createHalberd/createHeavyCrossbow/...(WeaponMaterial)` → returns an `Item`; register it in your own `DeferredRegister.Items`.
- Material: `WeaponMaterial.builder(name, modId).tier(Tier).repairTag(TagKey<Item>).traitsTag(TagKey<WeaponTrait>).build()`.
- Trait registry key: `WeaponTraits.REGISTRY_KEY` (registry id `spartan_weaponry_unofficial:weapon_traits`).
- 24 weapon types in `SpartanWeaponryAPI.WeaponItemType`.

## Ice and Fire mapping (CE: `com.iafenvoy.iceandfire`, NOT `com.github.alexthe666`)
- Tiers: `com.iafenvoy.iceandfire.registry.IafTiers` — `DRAGONBONE_TOOL_MATERIAL`, `BLOODED_DRAGONBONE_TOOL_MATERIAL`,
  `DRAGONSTEEL_FIRE`, `DRAGONSTEEL_ICE`, `DRAGONSTEEL_LIGHTNING`.
- ⚠️ CE collapsed the elemental dragonbone tiers — original had separate `FIRE/ICE/LIGHTNING_DRAGONBONE_TOOL_MATERIAL`;
  CE only has `DRAGONBONE` + `BLOODED_DRAGONBONE`. Elemental variants will reuse `BLOODED_DRAGONBONE_TOOL_MATERIAL`
  (same stats) and differ by traits.
- Items: `com.iafenvoy.iceandfire.registry.IafItems` (dragon blood, etc.). Dragonbone item id = `iceandfire:dragonbone`.

## Data carry-over (mechanical transforms)
- **Models**: identical structure to original; only the namespace changes `spartanweaponry:` → `spartan_weaponry_unofficial:`
  (parent, `oil_coated_item` loader, `coating` texture, blocking/throwing predicates).
- **Tags**: 1.21 uses singular dirs (`tags/item/`). Forge `forge:*` common tags → NeoForge `c:*`.
- **Per-type item tags**: each weapon must be listed in `data/spartan_weaponry_unofficial/tags/item/<type>.json`
  (merges with the fork's). Used for recipes, EMI grouping, and integrations.
- **Material trait tag**: the fork errors if `<traitsTag>` doesn't exist. Ship
  `data/<modid>/tags/spartan_weaponry_unofficial/weapon_traits/materials/<material>.json` (empty for trait-less
  materials; type traits like reach/sweep are applied automatically per weapon type).
- **Material tooltip**: add lang key `tooltip.spartan_weaponry_unofficial.material.<material>` (fork builds the key
  under its own namespace).

## Better Combat compatibility
Better Combat resolves a stack's animations in `WeaponRegistry.getAttributes(ItemStack)` by the item's **registry id**,
looking up a container loaded from any mod's `data/<ns>/weapon_attributes/<path>.json` (the file path == the item id
it applies to). A container may set `"parent": "bettercombat:<category>"` to inherit a base attribute set.

The fork ships **no** Better Combat data; base Spartan relies on `config/bettercombat/fallback_compatibility.json`
(regex on item id). Relying on that fuzzy fallback proved unreliable for the ported items, so this port assigns
Better Combat **explicitly and deterministically**: one `data/spartanfire/weapon_attributes/<weapon>.json` per weapon
with a `parent` matching what base Spartan would get:

| weapon type    | parent                                |
|----------------|---------------------------------------|
| dagger         | `bettercombat:dagger`                 |
| longsword      | `bettercombat:sword`                  |
| greatsword     | `bettercombat:claymore`               |
| battle_hammer  | `bettercombat:mace`                   |
| halberd        | `bettercombat:halberd`                |
| heavy_crossbow | `bettercombat:crossbow_two_handed_heavy` |

When scaling, generate one of these per (material × weapon type), keyed only by the weapon type (material is irrelevant
to the animation). Explicit registration overrides the fallback, so behaviour is deterministic.
