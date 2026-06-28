package krelox.spartanfire.trait;

import krelox.spartanfire.SpartanFire;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.api.trait.MeleeCallbackWeaponTrait;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;

/** Ignites and knocks back foes. Magnitude = seconds on fire. */
public class FlamedTrait extends MeleeCallbackWeaponTrait {
    public FlamedTrait() {
        super("flamed", SpartanFire.MODID, WeaponTrait.TraitQuality.POSITIVE);
        setUniversal();
    }

    @Override
    public void onHitEntity(WeaponMaterial material, ItemStack stack, LivingEntity target, LivingEntity attacker, Entity projectile) {
        target.igniteForSeconds((int) getMagnitude());
        target.knockback(1.0F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
    }
}
