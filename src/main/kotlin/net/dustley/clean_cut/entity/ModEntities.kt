package net.dustley.clean_cut.entity

import net.dustley.clean_cut.CleanCut
import net.dustley.clean_cut.entity.thrown_cleaver.ThrownCleaverEntity
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.client.render.entity.EmptyEntityRenderer
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.projectile.TridentEntity
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier


object ModEntities {

    val THROWN_CLEAVER: EntityType<ThrownCleaverEntity> =
        Registry.register<EntityType<*>, EntityType<ThrownCleaverEntity>>(
            Registries.ENTITY_TYPE,
            CleanCut.id("thrown_cleaver"),
            EntityType.Builder.create(
                (EntityType.EntityFactory {
                        _: EntityType<ThrownCleaverEntity>,
                        world -> ThrownCleaverEntity(world)
                }), SpawnGroup.MISC)
                .dimensions(0.5f,0.5f)
                .makeFireImmune()
                .build("thrown_cleaver")
        )

    fun registerModEntities() {
        CleanCut.LOGGER.info("Registering Entities for " + CleanCut.MOD_ID)


    }
    fun registerModEntityRenderers() {
        CleanCut.LOGGER.info("Registering Entity Renderers for " + CleanCut.MOD_ID)
        EntityRendererRegistry.register(THROWN_CLEAVER, ::EmptyEntityRenderer )
    }
}