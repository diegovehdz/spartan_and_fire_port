package krelox.spartanfire;

import com.iafenvoy.iceandfire.registry.IafTiers;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.xiyu.spartanweaponryunofficial.api.SpartanWeaponryAPI;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.api.WeaponTraits;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Vertical slice of the SpartanFire port: registers a representative set of dragonbone
 * weapons against the Spartan Weaponry Unofficial fork API, using Ice and Fire CE tiers.
 */
@Mod(SpartanFire.MODID)
public class SpartanFire {
    public static final String MODID = "spartanfire";

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    private static final List<DeferredHolder<Item, Item>> WEAPONS = new ArrayList<>();

    // Repair + trait tags (datapack tags shipped in resources/data).
    private static TagKey<Item> repairTag(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MODID, "repairs/" + name));
    }

    // Material trait tag, following the fork's own convention: weapon_traits/materials/<name>.
    private static TagKey<WeaponTrait> traitTag(String name) {
        return TagKey.create(WeaponTraits.REGISTRY_KEY,
                ResourceLocation.fromNamespaceAndPath(MODID, "materials/" + name));
    }

    // --- Materials ---
    public static final WeaponMaterial DRAGON_BONE = WeaponMaterial
            .builder("dragon_bone", MODID)
            .tier(IafTiers.DRAGONBONE_TOOL_MATERIAL)
            .repairTag(repairTag("dragon_bone"))
            .traitsTag(traitTag("dragon_bone"))
            .build();

    // --- Weapons (vertical slice: a spread across melee / polearm / ranged) ---
    static {
        weapon("dragon_bone_dagger", () -> SpartanWeaponryAPI.createDagger(DRAGON_BONE));
        weapon("dragon_bone_longsword", () -> SpartanWeaponryAPI.createLongsword(DRAGON_BONE));
        weapon("dragon_bone_greatsword", () -> SpartanWeaponryAPI.createGreatsword(DRAGON_BONE));
        weapon("dragon_bone_battle_hammer", () -> SpartanWeaponryAPI.createBattleHammer(DRAGON_BONE));
        weapon("dragon_bone_halberd", () -> SpartanWeaponryAPI.createHalberd(DRAGON_BONE));
        weapon("dragon_bone_heavy_crossbow", () -> SpartanWeaponryAPI.createHeavyCrossbow(DRAGON_BONE));
    }

    private static void weapon(String name, Supplier<Item> factory) {
        WEAPONS.add(ITEMS.register(name, factory));
    }

    // --- Creative tab ---
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SPARTAN_FIRE_TAB = TABS.register(MODID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + MODID))
                    .icon(() -> WEAPONS.get(2).get().getDefaultInstance()) // greatsword
                    .displayItems((params, output) -> WEAPONS.forEach(w -> output.accept(w.get())))
                    .build());

    public SpartanFire(IEventBus modBus) {
        ITEMS.register(modBus);
        TABS.register(modBus);
    }
}
