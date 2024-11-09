package net.dustley.clean_cut

import net.dustley.clean_cut.enchantment.ModEnchantments
import net.dustley.clean_cut.item.ModComponents
import net.dustley.clean_cut.item.ModItems
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

object CleanCut : ModInitializer {
	val MOD_ID = "clean_cut"
	val LOGGER = LoggerFactory.getLogger(MOD_ID)

	fun id(string: String?): Identifier = Identifier.of(MOD_ID, string)

	override fun onInitialize() {
		LOGGER.info("Initialization started for: $MOD_ID")

		ModEnchantments.initialize()
		ModItems.registerModItems()
		ModComponents.registerDataComponentTypes()

		LOGGER.info("Initialization finished for: $MOD_ID")
	}
}

/*
{
  "item.clean_cut.carion_cleaver": "Cleaver of the Carion",
  "tooltip.clean_cut.carion_cleaver": "Peace is a luxury, death is the normality.",
  "tooltip.clean_cut.carion_cleaver.shift": "§7[Shift]",

  "enchantment.clean_cut.huntress": "your victim can be seen through walls, and their footsteps are seen clearly. When your victim dies, no one will know they died at you hands.",
  "enchantment.clean_cut.nursing": "when hitting your victims, it will fill up a blood charge, which will allow you to heal a few hearts. Also when criting, you will cause your victim to halt there moment by criting them.",
  "enchantment.clean_cut.unerving": "when hitting a victim, any hostile creatures, and or players in a close radius, will get a third of the damage.",
  "enchantment.clean_cut.bloodrush": "when hitting your victims, it will fill up a blood charge, once your cleaver's charge fills, it's charge will act like a countdown. During the count down you will gain swiftness (speed 2), and be able to fly through the air, but the more you do, the more it decreases. (When criting whilst the countdown decreases, it will increase it)"
}
 */