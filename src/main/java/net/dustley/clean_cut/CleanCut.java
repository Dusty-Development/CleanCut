package net.dustley.clean_cut;

import net.dustley.clean_cut.block.ModBlocks;
import net.dustley.clean_cut.component.ModItemComponents;
import net.dustley.clean_cut.enchantment.ModEnchantments;
import net.dustley.clean_cut.entity.ModEntities;
import net.dustley.clean_cut.item.ModItems;
import net.dustley.clean_cut.particle.ModParticles;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CleanCut implements ModInitializer {
	public static final String MOD_ID = "clean_cut";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier identifier(String id) { return Identifier.of(MOD_ID, id); }

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModEnchantments.initialize();
		ModEntities.registerModEntities();
		ModItems.registerModItems();
		ModItemComponents.registerDataComponentTypes();
		ModParticles.registerParticles();
		ModBlocks.registerModBlocks();

		LOGGER.info("Hello Fabric world!");
	}
}