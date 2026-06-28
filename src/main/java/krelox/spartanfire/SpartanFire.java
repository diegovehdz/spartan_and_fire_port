package krelox.spartanfire;

import com.iafenvoy.iceandfire.registry.IafTiers;
import krelox.spartanfire.trait.FireDragonDamageBonusTrait;
import krelox.spartanfire.trait.FlamedTrait;
import krelox.spartanfire.trait.IceDragonDamageBonusTrait;
import krelox.spartanfire.trait.IcedTrait;
import krelox.spartanfire.trait.ShockedTrait;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * SpartanFire port — Spartan Weaponry weapons forged from Ice and Fire materials.
 * Registers the dragon bone tier (base + flamed/iced/lightning) with all weapon types,
 * witherbone components, and the elemental weapon traits, against the Spartan Weaponry
 * Unofficial fork API and Ice and Fire CE tiers.
 */
@Mod(SpartanFire.MODID)
public class SpartanFire {
    public static final String MODID = "spartanfire";

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<WeaponTrait> TRAITS =
            DeferredRegister.create(WeaponTraits.REGISTRY_KEY, MODID);

    /** Display order for the creative tab. */
    private static final List<DeferredHolder<Item, Item>> TAB_ENTRIES = new ArrayList<>();
    /** Registered weapons, keyed by "<material>_<type>". */
    public static final Map<String, DeferredHolder<Item, Item>> WEAPONS = new LinkedHashMap<>();

    private static TagKey<Item> repairTag(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MODID, "repairs/" + name));
    }

    // Material trait tag, following the fork's convention: weapon_traits/materials/<name>.
    private static TagKey<WeaponTrait> traitTag(String name) {
        return TagKey.create(WeaponTraits.REGISTRY_KEY,
                ResourceLocation.fromNamespaceAndPath(MODID, "materials/" + name));
    }

    // --- Weapon traits (referenced by the material trait tags) ---
    static {
        TRAITS.register("flamed_1", () -> new FlamedTrait().setLevel(1).setMagnitude(5.0F));
        TRAITS.register("flamed_2", () -> new FlamedTrait().setLevel(2).setMagnitude(15.0F));
        TRAITS.register("iced_1", () -> new IcedTrait().setLevel(1));
        TRAITS.register("iced_2", () -> new IcedTrait().setLevel(2));
        TRAITS.register("shocked", ShockedTrait::new);
        TRAITS.register("fire_dragon_damage_bonus_1", () -> new FireDragonDamageBonusTrait().setLevel(1));
        TRAITS.register("fire_dragon_damage_bonus_2", () -> new FireDragonDamageBonusTrait().setLevel(2));
        TRAITS.register("ice_dragon_damage_bonus_1", () -> new IceDragonDamageBonusTrait().setLevel(1));
        TRAITS.register("ice_dragon_damage_bonus_2", () -> new IceDragonDamageBonusTrait().setLevel(2));
    }

    /** The 24 Spartan weapon types, mapped to the fork's factory methods. */
    private static final Map<String, Function<WeaponMaterial, Item>> WEAPON_TYPES = new LinkedHashMap<>();
    static {
        WEAPON_TYPES.put("dagger", SpartanWeaponryAPI::createDagger);
        WEAPON_TYPES.put("parrying_dagger", SpartanWeaponryAPI::createParryingDagger);
        WEAPON_TYPES.put("longsword", SpartanWeaponryAPI::createLongsword);
        WEAPON_TYPES.put("katana", SpartanWeaponryAPI::createKatana);
        WEAPON_TYPES.put("saber", SpartanWeaponryAPI::createSaber);
        WEAPON_TYPES.put("rapier", SpartanWeaponryAPI::createRapier);
        WEAPON_TYPES.put("greatsword", SpartanWeaponryAPI::createGreatsword);
        WEAPON_TYPES.put("battle_hammer", SpartanWeaponryAPI::createBattleHammer);
        WEAPON_TYPES.put("warhammer", SpartanWeaponryAPI::createWarhammer);
        WEAPON_TYPES.put("spear", SpartanWeaponryAPI::createSpear);
        WEAPON_TYPES.put("halberd", SpartanWeaponryAPI::createHalberd);
        WEAPON_TYPES.put("pike", SpartanWeaponryAPI::createPike);
        WEAPON_TYPES.put("lance", SpartanWeaponryAPI::createLance);
        WEAPON_TYPES.put("longbow", SpartanWeaponryAPI::createLongbow);
        WEAPON_TYPES.put("heavy_crossbow", SpartanWeaponryAPI::createHeavyCrossbow);
        WEAPON_TYPES.put("throwing_knife", SpartanWeaponryAPI::createThrowingKnife);
        WEAPON_TYPES.put("tomahawk", SpartanWeaponryAPI::createTomahawk);
        WEAPON_TYPES.put("javelin", SpartanWeaponryAPI::createJavelin);
        WEAPON_TYPES.put("boomerang", SpartanWeaponryAPI::createBoomerang);
        WEAPON_TYPES.put("battleaxe", SpartanWeaponryAPI::createBattleaxe);
        WEAPON_TYPES.put("flanged_mace", SpartanWeaponryAPI::createFlangedMace);
        WEAPON_TYPES.put("glaive", SpartanWeaponryAPI::createGlaive);
        WEAPON_TYPES.put("quarterstaff", SpartanWeaponryAPI::createQuarterstaff);
        WEAPON_TYPES.put("scythe", SpartanWeaponryAPI::createScythe);
    }

    // --- Materials ---
    public static final WeaponMaterial DRAGON_BONE = WeaponMaterial
            .builder("dragon_bone", MODID)
            .tier(IafTiers.DRAGONBONE_TOOL_MATERIAL)
            .repairTag(repairTag("dragon_bone"))
            .traitsTag(traitTag("dragon_bone"))
            .build();

    // Elemental variants share Ice and Fire CE's blooded-dragonbone stats; they differ by traits.
    public static final WeaponMaterial FLAMED_DRAGON_BONE = elementalDragonBone("flamed_dragon_bone");
    public static final WeaponMaterial ICED_DRAGON_BONE = elementalDragonBone("iced_dragon_bone");
    public static final WeaponMaterial LIGHTNING_DRAGON_BONE = elementalDragonBone("lightning_dragon_bone");

    private static WeaponMaterial elementalDragonBone(String name) {
        return WeaponMaterial.builder(name, MODID)
                .tier(IafTiers.BLOODED_DRAGONBONE_TOOL_MATERIAL)
                .repairTag(repairTag("dragon_bone"))
                .traitsTag(traitTag(name))
                .build();
    }

    // --- Weapons (all 24 types for every dragon bone material) ---
    static {
        registerWeapons("dragon_bone", DRAGON_BONE);
        registerWeapons("flamed_dragon_bone", FLAMED_DRAGON_BONE);
        registerWeapons("iced_dragon_bone", ICED_DRAGON_BONE);
        registerWeapons("lightning_dragon_bone", LIGHTNING_DRAGON_BONE);
    }

    private static void registerWeapons(String materialName, WeaponMaterial material) {
        WEAPON_TYPES.forEach((type, factory) -> {
            String name = materialName + "_" + type;
            DeferredHolder<Item, Item> holder = ITEMS.register(name, () -> factory.apply(material));
            WEAPONS.put(name, holder);
            TAB_ENTRIES.add(holder);
        });
    }

    // --- Misc components (witherbone handle & pole, used to craft dragon bone weapons) ---
    public static final DeferredHolder<Item, Item> WITHERBONE_HANDLE = miscItem("witherbone_handle");
    public static final DeferredHolder<Item, Item> WITHERBONE_POLE = miscItem("witherbone_pole");

    private static DeferredHolder<Item, Item> miscItem(String name) {
        DeferredHolder<Item, Item> holder = ITEMS.register(name, () -> new Item(new Item.Properties()));
        TAB_ENTRIES.add(holder);
        return holder;
    }

    // --- Creative tab ---
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SPARTAN_FIRE_TAB = TABS.register(MODID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + MODID))
                    .icon(() -> WEAPONS.get("flamed_dragon_bone_greatsword").get().getDefaultInstance())
                    .displayItems((params, output) -> TAB_ENTRIES.forEach(h -> output.accept(h.get())))
                    .build());

    public SpartanFire(IEventBus modBus) {
        ITEMS.register(modBus);
        TABS.register(modBus);
        TRAITS.register(modBus);
    }
}
