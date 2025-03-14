package net.dustley.clean_cut.entity.cleaver;

import net.dustley.clean_cut.enchantment.cleaver_modules.EmptyEnchantmentModule;
import net.dustley.clean_cut.enchantment.cleaver_modules.ICleaverEnchantmentModule;
import net.dustley.clean_cut.entity.ModEntities;
import net.dustley.clean_cut.item.ModItems;
import net.dustley.clean_cut.item.weapon.cleaver.CleaverItem;
import net.dustley.clean_cut.item.weapon.cleaver.CleaverVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CleaverProjectileEntity extends PersistentProjectileEntity {

    //==// CONSTRUCTORS \\==\\
    public CleaverProjectileEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public CleaverProjectileEntity(World world, LivingEntity player, ItemStack stack) {
        super(ModEntities.CLEAVER_PROJECTILE, player, world, stack, null);
        setNoGravity(true);
        setOwner(player);
        setCleaverStack(stack);
        setStack(stack);
        updateModule();
    }

    //==// ANIMATION / RENDERING \\==\\
    public float RandomAngle = (float) ((Math.random() -0.5) * 25);
    public float CurrentSpinAngle = 0f;
    public boolean SpinningReversed = false;

    @Environment(EnvType.CLIENT)
    public float getRotationAnimation(Float partialTicks) {
        CurrentSpinAngle = (SpinningReversed ? 1 : -1) * (this.age + partialTicks) * -30f;
        return CurrentSpinAngle * 2;
    }

    //==// SFX \\==\\
    public CleaverProjectileSoundInstance soundInstance;
    public void updateAudio() {
        ClientPlayerEntity localPlayer = MinecraftClient.getInstance().player;

        if(localPlayer != null) {
            if (soundInstance == null) {
                soundInstance = new CleaverProjectileSoundInstance(this, localPlayer);
                MinecraftClient.getInstance().getSoundManager().play(soundInstance);
            }
        }
    }

    //==// DAMAGE \\==\\
    public int hitTimer = 0;
    public boolean hasHitEntity = false;
    public void onHitEntity() {
        hitTimer = 10;
        hasHitEntity = true;
        holdingStarted = true;
    }

    public Box getHitBox() { return Box.of(Vec3d.ZERO, enchantmentModule.getHitBoxWidth(), enchantmentModule.getHitBoxHeight(), enchantmentModule.getHitBoxWidth()); }

    public List<Entity> getEntitiesInHitBox() {
        Box hitbox = getHitBox().offset(getPos());

        List<Entity> hitEntities = new ArrayList<>();
        for (Entity otherEntity : getWorld().getOtherEntities(this, hitbox)) {
            if(otherEntity != getOwner()) {
                hitEntities.add(otherEntity);
            }
        }

        return hitEntities;
    }

    public void tryDamageEntitiesInHitBox() {
        if(hitTimer <= 0) {
            List<Entity> hitEntities = getEntitiesInHitBox();

            for (Entity hitEntity : hitEntities) {
                // Handle damage

                DamageSource damageSource = getDamageSources().thrown(this, getOwner());
                if (getWorld() instanceof ServerWorld) {
                    setDamage(EnchantmentHelper.getDamage((ServerWorld) getWorld(), getCleaverStack(), hitEntity, damageSource, (float) getDamage()));
                }

                if (getOwner() instanceof LivingEntity) ((LivingEntity) getOwner()).onAttacking(hitEntity);

                boolean hitSuccessful = hitEntity.damage(damageSource, (float) getDamage());
                if (hitSuccessful && hitEntity instanceof LivingEntity hitLivingEntity) {

                    knockback(hitLivingEntity, damageSource);

                    // Call default functions
                    if (getWorld() instanceof ServerWorld) {
                        EnchantmentHelper.onTargetDamaged((ServerWorld) getWorld(), hitLivingEntity, damageSource, getCleaverStack());
                    }

                    onHit(hitLivingEntity);

                    if (hitLivingEntity != getOwner() && hitLivingEntity instanceof PlayerEntity && getOwner() instanceof ServerPlayerEntity && !isSilent())
                        ((ServerPlayerEntity) getOwner()).networkHandler.sendPacket(
                                new GameStateChangeS2CPacket(
                                        GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER,
                                        GameStateChangeS2CPacket.DEMO_OPEN_SCREEN
                                )
                        );

                    enchantmentModule.onProjectileHit(this, hitLivingEntity, (float) getDamage());
                    onHitEntity();
                }
            }
        } else {
            hitTimer--;
        }
    }

    //==// ABILITY \\==\\
    public ICleaverEnchantmentModule enchantmentModule = EmptyEnchantmentModule.getInstance();
    public void updateModule() { enchantmentModule = CleaverItem.getCurrentModule(getCleaverStack()); }

    @Override
    public void tick() {
        if(hasHitEntity) { isReturning = true; }

        // Return after time
        totalTicks++;
        if(totalTicks > 30) { isReturning = true; }

        if(holdingStarted) { holdingTicks--; }

        // Movement
        if(isReturning) {
            setOnGround(false);
            setNoClip(true);
        }
        if(!enchantmentModule.usesCustomMovement()) updateMovement();
        else enchantmentModule.entityMovementTick(this);

        // Damage
        tryDamageEntitiesInHitBox();

        updateModule();
        updateAudio();
        super.tick();
    }

    //==// MOVEMENT \\==\\
    public boolean holdingStarted = false;
    public int holdingTicks = 8;
    public int totalTicks = 0;
    public boolean isReturning = false;

    public void updateMovement() {
        if(holdingStarted && holdingTicks <= 0) {
            setVelocity(0.0,0.0,0.0);
            return;
        }
        if(hasHitEntity) { isReturning = true; }

        if(isReturning) {
            if(getOwner() != null) {
                // Returning force
                var difference = (getPos().relativize(getOwner().getEyePos()));

                addVelocity(difference.normalize().multiply(0.75));
                var speed = Math.clamp(getVelocity().length(), 0.0, 3.0);
                setVelocity(getVelocity().normalize().multiply(speed));
            } else {
                // Drag force
                addVelocity(getVelocity().multiply(-0.1));
            }
        }
    }

    //==// Entity \\==\\
    @Override protected void age() { this.age++; }
    @Override protected void onEntityHit(EntityHitResult entityHitResult) { }
    @Override protected void onHit(LivingEntity target) { }

    //==// DATA TRACKING \\==\\
    private static final TrackedData<ItemStack> DATA_ID_CLEAVER_STACK = DataTracker.registerData(CleaverProjectileEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(DATA_ID_CLEAVER_STACK, ModItems.CLEAVER.getDefaultStack());
    }

    private ItemStack getCleaverStack() { return this.dataTracker.get(DATA_ID_CLEAVER_STACK); }
    private void setCleaverStack(ItemStack stack) { this.dataTracker.set(DATA_ID_CLEAVER_STACK, stack); }

    //==// NBT \\==\\
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("CleaverStack", getCleaverStack().encode(getRegistryManager()));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("CleaverStack", 10)) {
            NbtCompound nbtCompound = nbt.getCompound("CleaverStack");
            setCleaverStack(ItemStack.fromNbt(getRegistryManager(), nbtCompound).orElse(ModItems.CLEAVER.getDefaultStack()));
        } else {
            setCleaverStack(ModItems.CLEAVER.getDefaultStack());
        }
    }

    //==// INVENTORY \\==\\
    @Override protected ItemStack getDefaultItemStack() { return ModItems.CLEAVER.getDefaultStack(); }
    @Override public ItemStack getItemStack() { return getCleaverStack(); }
    @Override public ItemStack getWeaponStack() { return getCleaverStack(); }
    @Override protected boolean tryPickup(PlayerEntity player) { return super.tryPickup(player) || isNoClip() && isOwner(player) && player.getInventory().insertStack(asItemStack()); }

    public CleaverVariant getVariantFromStack() {return CleaverItem.getVariant(getCleaverStack()); }

}