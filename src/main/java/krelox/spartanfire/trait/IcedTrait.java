package krelox.spartanfire.trait;

import com.iafenvoy.iceandfire.registry.IafMobEffects;
import krelox.spartanfire.SpartanFire;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.api.trait.MeleeCallbackWeaponTrait;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;

/** Freezes (Ice and Fire's Frozen effect) and slows foes. Duration scales with level. */
public class IcedTrait extends MeleeCallbackWeaponTrait {
    public IcedTrait() {
        super("iced", SpartanFire.MODID, WeaponTrait.TraitQuality.POSITIVE);
        setUniversal();
    }

    @Override
    public void onHitEntity(WeaponMaterial material, ItemStack stack, LivingEntity target, LivingEntity attacker, Entity projectile) {
        int duration = 100 + getLevel() * 100;
        target.addEffect(new MobEffectInstance(IafMobEffects.FROZEN, duration, 0));
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, 2));
    }
}
