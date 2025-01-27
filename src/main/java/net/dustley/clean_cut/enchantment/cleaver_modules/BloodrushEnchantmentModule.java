package net.dustley.clean_cut.enchantment.cleaver_modules;

import net.dustley.clean_cut.entity.cleaver.CleaverProjectileEntity;
import net.dustley.clean_cut.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;

import static net.dustley.clean_cut.item.weapon.cleaver.CleaverItem.*;

public final class BloodrushEnchantmentModule implements ICleaverEnchantmentModule {


    public void onEntityHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setVelocity(0.0, 0.0, 0.0);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 2, true, false));
    }

    public Optional<TypedActionResult<ItemStack>> onUseStart(World world, PlayerEntity user, Hand hand) { return Optional.empty(); }

    public void onUseStop(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if(user.isInCreativeMode()) setIsActive(stack,true); else
        if(getBloodCharge(stack) > 0.6f) setIsActive(stack,true);

        if(getIsActive(stack)) {
            if (user instanceof PlayerEntity) ((PlayerEntity) user).getItemCooldownManager().set(ModItems.CLEAVER, 20);

            user.setVelocity(user.getRotationVector());
            if (getIsActive(stack) && user instanceof PlayerEntity) ((PlayerEntity) user).useRiptide(20, 8f, stack);
        }
    }

    public Optional<ActionResult> useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) { return Optional.empty(); }
    public Optional<ActionResult> useOnBlock(ItemUsageContext context) { return Optional.empty(); }

    public boolean usesCustomMovement() { return false; }
    public void entityMovementTick(CleaverProjectileEntity entity) { }

    public float inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (getIsActive(stack)) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 5, 2, true, false));
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 5, 0, true, false));
            }
        }

        if (entity instanceof LivingEntity livingEntity && livingEntity.isUsingRiptide()) {
            Vec3d velocityDirection = livingEntity.getVelocity().normalize();
            Vec3d velocityTarget = livingEntity.getRotationVector().normalize();
            livingEntity.setVelocity(velocityDirection.lerp(velocityTarget, 0.2).multiply(2.0));

            // spawnDashParticles(livingEntity, livingEntity.getVelocity().normalize());

            return 2f; // Drain blood at 2x speed
        }
        return 1f;
    }

    public void onProjectileHit(CleaverProjectileEntity entity, LivingEntity hitEntity, float damage) {}
    public UseAction getUseAction(ItemStack stack) { return UseAction.BOW; }
    public boolean usesBloodCharge(ItemStack stack) { return true; }

    public double getHitBoxHeight() { return 0.5; }
    public double getHitBoxWidth() { return 1.5; }

    // Singleton //
    private static BloodrushEnchantmentModule INSTANCE;
    private BloodrushEnchantmentModule() { }
    public static BloodrushEnchantmentModule getInstance() {
        if(INSTANCE == null) INSTANCE = new BloodrushEnchantmentModule();
        return INSTANCE;
    }
}
