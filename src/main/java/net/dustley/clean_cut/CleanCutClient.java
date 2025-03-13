package net.dustley.clean_cut;

import net.dustley.clean_cut.block.ModBlocks;
import net.dustley.clean_cut.client.ClientSetup;
import net.dustley.clean_cut.client.CustomModelLoadingPlugin;
import net.dustley.clean_cut.entity.ModEntities;
import net.dustley.clean_cut.item.ModModelPredicates;
import net.dustley.clean_cut.particle.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.impl.client.model.loading.ModelLoadingPluginManager;
import net.minecraft.client.particle.SweepAttackParticle;
import net.minecraft.client.render.RenderLayer;

public class CleanCutClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientSetup.clientSetup();
		ModelLoadingPluginManager.registerPlugin(new CustomModelLoadingPlugin());
		ClientSetup.registerExtraBakedModels(CustomModelLoadingPlugin.MODELS::add);
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TENDON_CHAIN, RenderLayer.getCutout());
		ModEntities.registerClientModEntities();
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SEETHING_LANTERN, RenderLayer.getCutout());
		ParticleFactoryRegistry.getInstance().register(ModParticles.CARRION_CLEAVER_SWEEP_PARTICLE, SweepAttackParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.INKY_CLEAVER_SWEEP_PARTICLE, SweepAttackParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.CAUTION_CLEAVER_SWEEP_PARTICLE, SweepAttackParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.ROSE_BLOOD_CLEAVER_SWEEP_PARTICLE, SweepAttackParticle.Factory::new);




		ModModelPredicates.registerModelPredicates();
	}

}