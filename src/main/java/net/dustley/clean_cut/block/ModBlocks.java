package net.dustley.clean_cut.block;

import net.dustley.clean_cut.CleanCut;
import net.dustley.clean_cut.block.custom.FleshBlock;
import net.dustley.clean_cut.block.custom.ForgorFleshBlock;
import net.dustley.clean_cut.block.custom.TendonChain;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import static net.dustley.clean_cut.item.ModItems.CLEAN_CUT_ITEM_GROUP_KEY;

public class ModBlocks {

    public static final Block FLESH = registerBlock("flesh",
            new FleshBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.HONEY)));
/*
    public static final Block FLESH_STAIRS = registerBlock("flesh_stairs",
            new StairsBlock(ModBlocks.FLESH_STAIRS.getDefaultState(),AbstractBlock.Settings.create().sounds(BlockSoundGroup.HONEY)));
    public static final Block FLESH_SLAB = registerBlock("flesh_slab",
            new SlabBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.HONEY)));
*/




    public static final Block FORGOTTEN_FLESH = registerBlock("forgotten_flesh",
            new ForgorFleshBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.HONEY).jumpVelocityMultiplier(-0.05f)));



    public static final Block CARRION_BONE_BLOCK = registerBlock("carrion_bone_block",
            new PillarBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.BONE).requiresTool().strength(2.0F)));
    public static final Block CARRION_BONE_BUNDLE = registerBlock("carrion_bone_bundle",
            new PillarBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.BONE).requiresTool().strength(2.0F)));



    public static final Block DENTED_LIVING_STEEL = registerBlock("dented_living_steel",
            new Block(AbstractBlock.Settings.create().sounds(BlockSoundGroup.COPPER).requiresTool().strength(3.0F, 6.0F)));



    public static final Block COMPACTED_LIVING_STEEL = registerBlock("compacted_living_steel",
            new Block(AbstractBlock.Settings.create().sounds(BlockSoundGroup.COPPER).requiresTool().strength(3.0F, 6.0F)));
    public static final Block FRAMED_LIVING_STEEL = registerBlock("framed_living_steel",
            new Block(AbstractBlock.Settings.create().sounds(BlockSoundGroup.COPPER).requiresTool().strength(3.0F, 6.0F)));
    public static final Block SEETHING_LANTERN = registerBlock("seething_lantern",
            new LanternBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.LANTERN).requiresTool().strength(3.5F).sounds(BlockSoundGroup.LANTERN).luminance((state) -> {
                return 15;
            }).nonOpaque().pistonBehavior(PistonBehavior.DESTROY).solid()));
    public static final Block TENDON_CHAIN = registerBlock("tendon_chain",new TendonChain(AbstractBlock.Settings.create().solid().requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.CHAIN).nonOpaque()));





    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        ItemGroupEvents.modifyEntriesEvent(CLEAN_CUT_ITEM_GROUP_KEY)
                .register(entries -> {
                entries.add(block);

                });
        return Registry.register(Registries.BLOCK, Identifier.of(CleanCut.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(CleanCut.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        CleanCut.LOGGER.info("Registering Mod Blocks for " + CleanCut.MOD_ID);


    }
}