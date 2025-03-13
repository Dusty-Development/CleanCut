package net.dustley.clean_cut.mixin;

import net.dustley.clean_cut.item.ModItems;
import net.dustley.clean_cut.item.weapon.cleaver.CleaverVariantHelper;
import net.dustley.clean_cut.particle.ModParticles;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {


    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Inject(method = "spawnSweepAttackParticles", at = @At(value = "HEAD"),cancellable = true)
    private void injectSpawnSweepAttackParticles(CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (player.getMainHandStack().isOf(ModItems.CLEAVER) && player.getMainHandStack() == CleaverVariantHelper.addCleaverVariantToStack(player.getMainHandStack(),CleaverVariantHelper.CARRION)) {
            double d = (double) (-MathHelper.sin(player.getYaw() * 0.017453292F));
            double e = (double) MathHelper.cos(player.getYaw() * 0.017453292F);
            if (player.getWorld() instanceof ServerWorld gg) {
                ((ServerWorld) player.getWorld()).spawnParticles(ModParticles.CARRION_CLEAVER_SWEEP_PARTICLE, player.getX() + d, player.getBodyY(0.5), player.getZ() + e, 0, d, 0.0, e, 0.0);
            }
            info.cancel();
        } else         if (player.getMainHandStack().isOf(ModItems.CLEAVER) && player.getMainHandStack() == CleaverVariantHelper.addCleaverVariantToStack(player.getMainHandStack(),CleaverVariantHelper.CAUTION)) {
            double d = (double) (-MathHelper.sin(player.getYaw() * 0.017453292F));
            double e = (double) MathHelper.cos(player.getYaw() * 0.017453292F);
            if (player.getWorld() instanceof ServerWorld gg) {
                ((ServerWorld) player.getWorld()).spawnParticles(ModParticles.CAUTION_CLEAVER_SWEEP_PARTICLE, player.getX() + d, player.getBodyY(0.5), player.getZ() + e, 0, d, 0.0, e, 0.0);
            }
            info.cancel();
        } else         if (player.getMainHandStack().isOf(ModItems.CLEAVER) && player.getMainHandStack() == CleaverVariantHelper.addCleaverVariantToStack(player.getMainHandStack(),CleaverVariantHelper.INKY)) {
            double d = (double) (-MathHelper.sin(player.getYaw() * 0.017453292F));
            double e = (double) MathHelper.cos(player.getYaw() * 0.017453292F);
            if (player.getWorld() instanceof ServerWorld gg) {
                ((ServerWorld) player.getWorld()).spawnParticles(ModParticles.INKY_CLEAVER_SWEEP_PARTICLE, player.getX() + d, player.getBodyY(0.5), player.getZ() + e, 0, d, 0.0, e, 0.0);
            }
            info.cancel();
        } else         if (player.getMainHandStack().isOf(ModItems.CLEAVER) && player.getMainHandStack() == CleaverVariantHelper.addCleaverVariantToStack(player.getMainHandStack(),CleaverVariantHelper.ROSE_BLOOD)) {
            double d = (double) (-MathHelper.sin(player.getYaw() * 0.017453292F));
            double e = (double) MathHelper.cos(player.getYaw() * 0.017453292F);
            if (player.getWorld() instanceof ServerWorld gg) {
                ((ServerWorld) player.getWorld()).spawnParticles(ModParticles.ROSE_BLOOD_CLEAVER_SWEEP_PARTICLE, player.getX() + d, player.getBodyY(0.5), player.getZ() + e, 0, d, 0.0, e, 0.0);
            }
            info.cancel();
        }
    }


/*
    @Inject(method = "onDeath", at = @At("TAIL"))
    private void onDeathGoGoGajetFuckingDie(DamageSource source, CallbackInfo ci) {
        if (source.getAttacker() instanceof LivingEntity diansuUltrakill && source.getWeaponStack().isOf(ModItems.CRIMSON_EDGE) ) {
            diansuUltrakill.discard();
        }
    }*/
}
