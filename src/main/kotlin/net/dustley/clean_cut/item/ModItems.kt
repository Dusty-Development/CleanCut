package net.dustley.clean_cut.item

import net.dustley.clean_cut.CleanCut
import net.dustley.clean_cut.item.cleaver.CarrionCleaverItem
import net.dustley.clean_cut.item.cleaver.RoseBloodCleaverItem
import net.dustley.clean_cut.item.living_steel.LivingSteelItem
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemGroups
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object ModItems {
    // Item group
    val CLEAN_CUT_ITEM_GROUP_KEY: RegistryKey<ItemGroup> =
        RegistryKey.of(Registries.ITEM_GROUP.key, CleanCut.identifier("item_group"))
    val CLEAN_CUT_ITEM_GROUP: ItemGroup = FabricItemGroup.builder()
        .icon { ItemStack(LIVING_STEEL) }
        .displayName(Text.translatable("itemGroup.${CleanCut.MOD_ID}"))
        .build()

    init { Registry.register(Registries.ITEM_GROUP, CLEAN_CUT_ITEM_GROUP_KEY, CLEAN_CUT_ITEM_GROUP) }


    val CLEAVER_OF_THE_CARRION: Item = registerItem( "carrion_cleaver", CarrionCleaverItem())
    val ROSE_BLOOD_CLEAVER: Item = registerItem( "rose_blood_cleaver", RoseBloodCleaverItem())


    val LIVING_STEEL: Item = registerItem( "living_steel", LivingSteelItem())

    private fun registerItem(name: String, item: Item): Item {
        return Registry.register(Registries.ITEM, Identifier.of(CleanCut.MOD_ID, name), item)
    }

    fun registerModItems() {
        CleanCut.LOGGER.info("Registering Mod Items for " + CleanCut.MOD_ID)

        ItemGroupEvents.modifyEntriesEvent(CLEAN_CUT_ITEM_GROUP_KEY)
            .register(ItemGroupEvents.ModifyEntries { entries: FabricItemGroupEntries ->
                entries.add(CLEAVER_OF_THE_CARRION)
                entries.add(ROSE_BLOOD_CLEAVER)
                entries.add(LIVING_STEEL)
            })

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
            .register(ItemGroupEvents.ModifyEntries { entries: FabricItemGroupEntries ->
                entries.add(CLEAVER_OF_THE_CARRION)
                entries.add(ROSE_BLOOD_CLEAVER)
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