package net.dustley.clean_cut.block

import net.dustley.clean_cut.CleanCut
import net.dustley.clean_cut.block.lantern.SeethingLanternBlock
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.MapColor
import net.minecraft.block.PillarBlock
import net.minecraft.block.enums.NoteBlockInstrument
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup

object ModBlocks {

    val FLESH: Block = registerBlock("flesh", Block(AbstractBlock.Settings.create()
        .mapColor(MapColor.RED).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0f, 4.0f).sounds(BlockSoundGroup.SLIME)))

    val CARRION_BONE: Block = registerBlock("carrion_bone", PillarBlock(AbstractBlock.Settings.create()
        .mapColor(MapColor.DULL_RED).instrument(NoteBlockInstrument.PIGLIN).requiresTool().strength(2.0f).sounds(BlockSoundGroup.BONE)))

    val COMPACTED_LIVING_STEEL: Block = registerBlock("compacted_living_steel", Block(AbstractBlock.Settings.create()
            .mapColor(MapColor.DULL_RED).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(5.0f, 6.0f).sounds(BlockSoundGroup.COPPER)))

    val DENTED_LIVING_STEEL: Block = registerBlock("dented_living_steel", Block(AbstractBlock.Settings.create()
        .mapColor(MapColor.BROWN).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(5.0f, 6.0f).sounds(BlockSoundGroup.COPPER)))

    val SEETHING_LANTERN: Block = registerBlock("seething_lantern",
        SeethingLanternBlock(
            AbstractBlock.Settings.create().mapColor(MapColor.IRON_GRAY).solid().requiresTool().strength(3.5f)
                .sounds(BlockSoundGroup.LANTERN).luminance { 13 }
                .nonOpaque().pistonBehavior(PistonBehavior.DESTROY)
        ))

    private fun registerBlockWithoutBlockItem(name: String, block: Block): Block {
        return Registry.register(Registries.BLOCK, CleanCut.identifier(name), block)
    }

    private fun registerBlock(name: String, block: Block): Block {
        registerBlockItem(name, block)
        return Registry.register(Registries.BLOCK, CleanCut.identifier(name), block)
    }

    private fun registerBlockItem(name: String, block: Block) {
        Registry.register(
            Registries.ITEM, CleanCut.identifier(name),
            BlockItem(block, Item.Settings())
        )
    }

    fun registerModBlocks() {
        CleanCut.LOGGER.info("Registering Mod Blocks for " + CleanCut.MOD_ID)

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS)
            .register(ItemGroupEvents.ModifyEntries { entries: FabricItemGroupEntries ->
                entries.add(FLESH)
                entries.add(CARRION_BONE)
                entries.add(COMPACTED_LIVING_STEEL)
                entries.add(DENTED_LIVING_STEEL)
                entries.add(SEETHING_LANTERN)
            })
    }
}