package krelox.spartanfire.trait;

import com.iafenvoy.iceandfire.event.handler.ServerEvents;
import krelox.spartanfire.SpartanFire;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.api.trait.MeleeCallbackWeaponTrait;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;

/** Knocks back foes and strikes them with lightning (one bolt per swing). */
public class ShockedTrait extends MeleeCallbackWeaponTrait {
    public ShockedTrait() {
        super("shocked", SpartanFire.MODID, WeaponTrait.TraitQuality.POSITIVE);
        setUniversal();
    }

    @Override
    public void onHitEntity(WeaponMaterial material, ItemStack stack, LivingEntity target, LivingEntity attacker, Entity projectile) {
        target.knockback(1.0F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());

        // Only on a fully-charged swing (mirrors the original's attack-cooldown gate).
        boolean strike = true;
        if (attacker instanceof Player && attacker.attackAnim > 0.2F) {
            strike = false;
        }

        if (!attacker.level().isClientSide && strike) {
            LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(target.level());
            if (lightning != null) {
                lightning.getTags().add(ServerEvents.BOLT_DONT_DESTROY_LOOT);
                lightning.getTags().add(attacker.getStringUUID());
                lightning.moveTo(target.position());
                target.level().addFreshEntity(lightning);
            }
        }
    }
}
