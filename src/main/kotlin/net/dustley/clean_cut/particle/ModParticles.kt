package net.dustley.clean_cut.particle

import net.dustley.clean_cut.CleanCut
import net.dustley.clean_cut.particle.huntress.BloodDashParticle
import net.dustley.clean_cut.particle.huntress.BloodDripParticle
import net.dustley.clean_cut.particle.huntress.BloodSplatParticle
import net.dustley.clean_cut.particle.huntress.HuntressFloorParticle
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.SimpleParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry


object ModParticles {

    val HUNTRESS_FLOOR_PARTICLE: SimpleParticleType = FabricParticleTypes.simple()

    val BLOOD_DRIP_PARTICLE: SimpleParticleType = FabricParticleTypes.simple()
    val BLOOD_SPLAT_PARTICLE: SimpleParticleType = FabricParticleTypes.simple()

    val BLOOD_DASH_PARTICLE: SimpleParticleType = FabricParticleTypes.simple()


    fun registerParticles() {
        Registry.register(Registries.PARTICLE_TYPE, CleanCut.identifier("huntress_floor_particle"), HUNTRESS_FLOOR_PARTICLE)

        Registry.register(Registries.PARTICLE_TYPE, CleanCut.identifier("blood_drip_particle"), BLOOD_DRIP_PARTICLE)
        Registry.register(Registries.PARTICLE_TYPE, CleanCut.identifier("blood_splat_particle"), BLOOD_SPLAT_PARTICLE)

        Registry.register(Registries.PARTICLE_TYPE, CleanCut.identifier("blood_dash_particle"), BLOOD_DASH_PARTICLE)
    }

    fun registerClientParticles() {
        ParticleFactoryRegistry.getInstance().register(HUNTRESS_FLOOR_PARTICLE) { sprites -> HuntressFloorParticle.Companion.Factory(sprites) }
        ParticleFactoryRegistry.getInstance().register(BLOOD_DRIP_PARTICLE) { sprites -> BloodDripParticle.Companion.Factory(sprites) }
        ParticleFactoryRegistry.getInstance().register(BLOOD_SPLAT_PARTICLE) { sprites -> BloodSplatParticle.Companion.Factory(sprites) }
        ParticleFactoryRegistry.getInstance().register(BLOOD_DASH_PARTICLE) { sprites -> BloodDashParticle.Companion.Factory(sprites) }
    }
}