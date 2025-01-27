package net.dustley.clean_cut;

import net.dustley.clean_cut.client.ClientSetup;
import net.dustley.clean_cut.client.CustomModelLoadingPlugin;
import net.dustley.clean_cut.entity.ModEntities;
import net.dustley.clean_cut.item.ModModelPredicates;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.impl.client.model.loading.ModelLoadingPluginManager;

public class CleanCutClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		ClientSetup.clientSetup();
		ModelLoadingPluginManager.registerPlugin(new CustomModelLoadingPlugin());
		ClientSetup.registerExtraBakedModels(CustomModelLoadingPlugin.MODELS::add);

		ModEntities.registerClientModEntities();

		ModModelPredicates.registerModelPredicates();
	}

}