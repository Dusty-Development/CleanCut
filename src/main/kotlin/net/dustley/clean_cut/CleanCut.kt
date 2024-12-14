package net.dustley.clean_cut

import net.dustley.clean_cut.block.ModBlocks
import net.dustley.clean_cut.component.ModComponents
import net.dustley.clean_cut.component.ModItemComponents
import net.dustley.clean_cut.enchantment.ModEnchantments
import net.dustley.clean_cut.entity.ModEntities
import net.dustley.clean_cut.item.ModItems
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

object CleanCut : ModInitializer {
	val MOD_ID = "clean_cut"
	val LOGGER = LoggerFactory.getLogger(MOD_ID)

	fun identifier(string: String?): Identifier = Identifier.of(MOD_ID, string)

	override fun onInitialize() {
		LOGGER.info("Initialization started for: $MOD_ID")

		ModEnchantments.initialize()
		ModEntities.registerModEntities()
		ModItems.registerModItems()
		ModItemComponents.registerDataComponentTypes()
		ModComponents.registerModComponents()
		ModBlocks.registerModBlocks()

		LOGGER.info("Initialization finished for: $MOD_ID")
	}
}
