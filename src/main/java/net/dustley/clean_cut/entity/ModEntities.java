package net.dustley.clean_cut.entity;

import net.dustley.clean_cut.CleanCut;
import net.dustley.clean_cut.entity.cleaver.CleaverProjectileEntity;
import net.dustley.clean_cut.entity.cleaver.CleaverProjectileEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntities {

    public static final EntityType<CleaverProjectileEntity> CLEAVER_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            CleanCut.identifier("cleaver_projectile"),
            EntityType.Builder.<CleaverProjectileEntity>create(CleaverProjectileEntity::new, SpawnGroup.MISC)
                    .dimensions(0.75f, 0.15f).build());

    public static void registerModEntities() {

    }

    public static void registerClientModEntities() {
        EntityRendererRegistry.register(CLEAVER_PROJECTILE, CleaverProjectileEntityRenderer::new);
    }

}
