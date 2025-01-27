package net.dustley.clean_cut.item;

import net.dustley.clean_cut.CleanCut;
import net.dustley.clean_cut.item.food.living_steel.LivingSteelItem;
import net.dustley.clean_cut.item.weapon.cleaver.CleaverItem;
import net.dustley.clean_cut.item.weapon.cleaver.CleaverVariantHelper;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;

import static net.dustley.clean_cut.item.weapon.cleaver.CleaverVariantHelper.addCleaversToEntries;

public class ModItems {

    // Items //
    public static final Item CLEAVER = registerItem("cleaver", new CleaverItem());
    public static final Item LIVING_STEEL = registerItem("living_steel", new LivingSteelItem());

    // Item group //
    public static final RegistryKey<ItemGroup> CLEAN_CUT_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), CleanCut.identifier("item_group"));
    public static final ItemGroup CLEAN_CUT_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(CLEAVER))
            .displayName(Text.translatable("itemGroup." + CleanCut.MOD_ID))
            .build();

    // Utility Functions //
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, CleanCut.identifier(name), item);
    }

    // Initializer //
    public static void registerModItems() {
        CleanCut.LOGGER.info("Registering Mod Items for " + CleanCut.MOD_ID);
        Registry.register(Registries.ITEM_GROUP, CLEAN_CUT_ITEM_GROUP_KEY, CLEAN_CUT_ITEM_GROUP);

        // Main group
        ItemGroupEvents.modifyEntriesEvent(CLEAN_CUT_ITEM_GROUP_KEY)
                .register(entries -> {
                    addCleaversToEntries(entries);
                    entries.add(LIVING_STEEL);
                });

        // Cleavers
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(CleaverVariantHelper::addCleaversToEntries);

        // Living steel
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> entries.add(LIVING_STEEL));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> entries.add(LIVING_STEEL));
    }
}
