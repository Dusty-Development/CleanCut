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

// This defines events enchantments for the cleaver can link into
public interface ICleaverEnchantmentModule {

    // Damage
    public void onEntityHit(ItemStack stack, LivingEntity target, LivingEntity attacker);

    // Usage functions:
    public Optional<TypedActionResult<ItemStack>> onUseStart(World world, PlayerEntity user, Hand hand);
    public void onUseStop(ItemStack stack, World world, LivingEntity user, int remainingUseTicks);

    public Optional<ActionResult> useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand);
    public Optional<ActionResult> useOnBlock(ItemUsageContext context);

    // Entity functions
    public boolean usesCustomMovement();
    public void onProjectileHit(CleaverProjectileEntity entity, LivingEntity hitEntity, float damage);

    public double getHitBoxHeight();
    public double getHitBoxWidth();

    // Tick
    public float inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected);
    public void entityMovementTick(CleaverProjectileEntity entity);

    // Visual
    public UseAction getUseAction(ItemStack stack);

    public boolean usesBloodCharge(ItemStack stack);
}
