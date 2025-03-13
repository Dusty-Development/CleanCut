package net.dustley.clean_cut.mixin;

import archives.tater.marksman.Marksman;
import archives.tater.marksman.Ricoshottable;
import net.dustley.clean_cut.entity.cleaver.CleaverProjectileEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CleaverProjectileEntity.class)
public abstract class MarksmanCleaverProjectileEntityMixin extends PersistentProjectileEntity implements Ricoshottable {
    @Unique
    private boolean marksman$ricoshotted = false;

    protected MarksmanCleaverProjectileEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public ProjectileDeflection getProjectileDeflection(ProjectileEntity projectile) {
        return marksman$canBeRicoshotted() ? Marksman.AIM_AT_TARGET : super.getProjectileDeflection(projectile);
    }

    @Override
    public boolean marksman$canBeRicoshotted() {
        return !marksman$ricoshotted && age > 6 && !isOnGround();
    }

    @Override
    public void marksman$setRicoshotted() {
        marksman$ricoshotted = true;
    }
}
