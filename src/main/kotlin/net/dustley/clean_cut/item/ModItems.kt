package net.dustley.clean_cut.item

import net.dustley.clean_cut.CleanCut
import net.dustley.clean_cut.item.cleaver.CarionCleaverItem
import net.dustley.clean_cut.item.living_steel.LivingSteelItem
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModItems {

    val CLEAVER_OF_THE_CARION: Item = registerItem( "carion_cleaver", CarionCleaverItem())
    val LIVING_STEEL: Item = registerItem( "living_steel", LivingSteelItem())

    private fun registerItem(name: String, item: Item): Item {
        return Registry.register(Registries.ITEM, Identifier.of(CleanCut.MOD_ID, name), item)
    }

    fun registerModItems() {
        CleanCut.LOGGER.info("Registering Mod Items for " + CleanCut.MOD_ID)

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
            .register(ItemGroupEvents.ModifyEntries { entries: FabricItemGroupEntries ->
                entries.add(CLEAVER_OF_THE_CARION)
            })

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
            .register(ItemGroupEvents.ModifyEntries { entries: FabricItemGroupEntries ->
                entries.add(LIVING_STEEL)
            })

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK)
            .register(ItemGroupEvents.ModifyEntries { entries: FabricItemGroupEntries ->
                entries.add(LIVING_STEEL)
            })
    }
}