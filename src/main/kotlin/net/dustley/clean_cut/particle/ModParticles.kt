package net.dustley.clean_cut.particle

import net.dustley.clean_cut.CleanCut
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.SimpleParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry


object ModParticles {

    val HUNTRESS_FLOOR_PARTICLE: SimpleParticleType = FabricParticleTypes.simple()

    fun registerParticles() {
        Registry.register(Registries.PARTICLE_TYPE, CleanCut.id("huntress_floor_particle"), HUNTRESS_FLOOR_PARTICLE)
    }
}