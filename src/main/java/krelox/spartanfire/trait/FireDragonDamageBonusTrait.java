package krelox.spartanfire.trait;

import com.iafenvoy.iceandfire.entity.FireDragonEntity;
import krelox.spartanfire.SpartanFire;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.api.trait.MeleeCallbackWeaponTrait;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;

/** Extra damage against Fire Dragons. */
public class FireDragonDamageBonusTrait extends MeleeCallbackWeaponTrait {
    public FireDragonDamageBonusTrait() {
        super("fire_dragon_damage_bonus", SpartanFire.MODID, WeaponTrait.TraitQuality.POSITIVE);
        setUniversal();
    }

    @Override
    public float modifyDamageDealt(WeaponMaterial material, float damage, DamageSource source, LivingEntity target, LivingEntity attacker) {
        if (target instanceof FireDragonEntity) {
            return damage + 5.5F + getLevel() * 4.0F;
        }
        return damage;
    }
}
