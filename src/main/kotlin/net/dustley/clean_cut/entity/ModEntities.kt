package net.dustley.clean_cut.entity

import net.dustley.clean_cut.CleanCut
import net.dustley.clean_cut.entity.thrown_cleaver.ThrownCleaverEntity
import net.dustley.clean_cut.entity.thrown_cleaver.ThrownCleaverEntityRenderer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry


object ModEntities {

    val THROWN_REGULAR_CLEAVER: EntityType<ThrownCleaverEntity> =
        Registry.register<EntityType<*>, EntityType<ThrownCleaverEntity>>(
            Registries.ENTITY_TYPE,
            CleanCut.identifier("thrown_cleaver"),
            EntityType.Builder.create(
                (EntityType.EntityFactory {
                        t: EntityType<ThrownCleaverEntity>,
                        world -> ThrownCleaverEntity(world, false, t)
                }), SpawnGroup.MISC)
                .dimensions(0.5f,0.5f)
                .makeFireImmune()
                .build("thrown_cleaver")
        )

    val THROWN_ROSE_CLEAVER: EntityType<ThrownCleaverEntity> =
        Registry.register<EntityType<*>, EntityType<ThrownCleaverEntity>>(
            Registries.ENTITY_TYPE,
            CleanCut.identifier("thrown_rose_cleaver"),
            EntityType.Builder.create(
                (EntityType.EntityFactory {
                        t: EntityType<ThrownCleaverEntity>,
                        world -> ThrownCleaverEntity(world, true, t)
                }), SpawnGroup.MISC)
                .dimensions(0.5f,0.5f)
                .makeFireImmune()
                .build("thrown_rose_cleaver")
        )


    // Server
    fun registerModEntities() {
        CleanCut.LOGGER.info("Registering Entities for " + CleanCut.MOD_ID)


    }

    // Client
    fun registerModEntityRenderers() {
        CleanCut.LOGGER.info("Registering Entity Renderers for " + CleanCut.MOD_ID)

        EntityRendererRegistry.register(THROWN_REGULAR_CLEAVER, ::ThrownCleaverEntityRenderer)
        EntityRendererRegistry.register(THROWN_ROSE_CLEAVER, ::ThrownCleaverEntityRenderer)
    }
}