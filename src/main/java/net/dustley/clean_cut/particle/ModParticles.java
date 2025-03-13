package net.dustley.clean_cut.particle;


import net.dustley.clean_cut.CleanCut;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ModParticles {
    public static final SimpleParticleType CARRION_CLEAVER_SWEEP_PARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType INKY_CLEAVER_SWEEP_PARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType CAUTION_CLEAVER_SWEEP_PARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType ROSE_BLOOD_CLEAVER_SWEEP_PARTICLE = FabricParticleTypes.simple();



    public static void registerParticles() {
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CleanCut.MOD_ID, "carrion_cleaver_sweep_particle"),
                CARRION_CLEAVER_SWEEP_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CleanCut.MOD_ID, "inky_cleaver_sweep_particle"),
                INKY_CLEAVER_SWEEP_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CleanCut.MOD_ID, "caution_cleaver_sweep_particle"),
                CAUTION_CLEAVER_SWEEP_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(CleanCut.MOD_ID, "rose_blood_cleaver_sweep_particle"),
                ROSE_BLOOD_CLEAVER_SWEEP_PARTICLE);



    }
}
