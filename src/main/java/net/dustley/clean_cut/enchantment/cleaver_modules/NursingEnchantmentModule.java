package net.dustley.clean_cut.enchantment.cleaver_modules;

import net.dustley.clean_cut.entity.cleaver.CleaverProjectileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Optional;

public final class NursingEnchantmentModule implements ICleaverEnchantmentModule {

    public void onEntityHit(ItemStack stack, LivingEntity target, LivingEntity attacker) { }

    public Optional<TypedActionResult<ItemStack>> onUseStart(World world, PlayerEntity user, Hand hand) { return Optional.empty(); }

    public void onUseStop(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        EmptyEnchantmentModule.getInstance().onUseStop(stack, world, user, remainingUseTicks);
    }

    public Optional<ActionResult> useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) { return Optional.empty(); }

    public Optional<ActionResult> useOnBlock(ItemUsageContext context) { return Optional.empty(); }

    public boolean usesCustomMovement() { return false; }
    public void entityMovementTick(CleaverProjectileEntity entity) { }

    public void onProjectileHit(CleaverProjectileEntity entity, LivingEntity hitEntity, float damage) {
        LivingEntity owner = (LivingEntity) entity.getOwner();
        if (owner != null) {
            owner.heal(damage * 0.5f);

            ArrayList<StatusEffectInstance> ownerEffects = new ArrayList<>(owner.getStatusEffects());
            owner.clearStatusEffects();

            for (StatusEffectInstance statusEffect : hitEntity.getStatusEffects()) {
                owner.setStatusEffect(statusEffect, hitEntity);
            }
            hitEntity.clearStatusEffects();

            for (StatusEffectInstance statusEffect : ownerEffects) {
                hitEntity.setStatusEffect(statusEffect, owner);
            }
        }
    }

    public double getHitBoxHeight() { return 0.5; }
    public double getHitBoxWidth() { return 1.5; }

    public float inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) { return 1f; }

    public UseAction getUseAction(ItemStack stack) { return UseAction.SPEAR; }

    public boolean usesBloodCharge(ItemStack stack) { return false; }

    // Singleton //
    private static NursingEnchantmentModule INSTANCE;
    private NursingEnchantmentModule() { }
    public static NursingEnchantmentModule getInstance() {
        if(INSTANCE == null) INSTANCE = new NursingEnchantmentModule();
        return INSTANCE;
    }
}
