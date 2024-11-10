package net.dustley.clean_cut

import net.dustley.clean_cut.client.ClientSetup
import net.dustley.clean_cut.client.CustomModelLoadingPlugin
import net.dustley.clean_cut.entity.ModEntities
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.impl.client.model.loading.ModelLoadingPluginManager

object CleanCutClient : ClientModInitializer {

    override fun onInitializeClient() {
        ModEntities.registerModEntityRenderers()
//        ModParticles.registerClientParticles()
//        ModNetworking.registerS2CPackets()

        ClientSetup.clientSetup()
        ModelLoadingPluginManager.registerPlugin(CustomModelLoadingPlugin())
        ClientSetup.registerExtraBakedModels(CustomModelLoadingPlugin.MODELS::add);
    }

}