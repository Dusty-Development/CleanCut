package net.dustley.clean_cut.enchantment.cleaver_modules;

import net.dustley.clean_cut.entity.cleaver.CleaverProjectileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.Optional;

public final class EmptyEnchantmentModule implements ICleaverEnchantmentModule {

    public void onEntityHit(ItemStack stack, LivingEntity target, LivingEntity attacker) { }
    public Optional<TypedActionResult<ItemStack>> onUseStart(World world, PlayerEntity user, Hand hand) { return Optional.empty(); }
    public void onUseStop(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        CleaverProjectileEntity entity = new CleaverProjectileEntity(world, user, stack.copy());
        entity.setPosition(user.getEyePos());
        entity.setVelocity(user.getRotationVector().multiply(2.5));
        world.spawnEntity(entity);
        stack.decrement(1);
    }
    public Optional<ActionResult> useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) { return Optional.empty(); }
    public Optional<ActionResult> useOnBlock(ItemUsageContext context) { return Optional.empty(); }
    public float inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) { return 1f; }
    public UseAction getUseAction(ItemStack stack) { return UseAction.SPEAR; }
    public void onProjectileHit(CleaverProjectileEntity entity, LivingEntity hitEntity, float damage) {}
    public double getHitBoxHeight() { return 0.5; }
    public double getHitBoxWidth() { return 1.5; }
    public boolean usesBloodCharge(ItemStack stack) { return false; }
    public boolean usesCustomMovement() { return false; }
    public void entityMovementTick(CleaverProjectileEntity entity) { }

    // Singleton //
    private static EmptyEnchantmentModule INSTANCE;
    private EmptyEnchantmentModule() { }
    public static EmptyEnchantmentModule getInstance() {
        if(INSTANCE == null) INSTANCE = new EmptyEnchantmentModule();
        return INSTANCE;
    }
}
