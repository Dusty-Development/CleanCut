package net.dustley.clean_cut.item.weapon.cleaver;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.item.ItemStack;

import java.util.Map;

import static net.dustley.clean_cut.item.ModItems.CLEAVER;

public class CleaverVariantHelper {

    // Each variant steps 0.1 for the predicate
    public static CleaverVariant CARRION = new CleaverVariant("carrion", "#ff1353", 0F);
    public static CleaverVariant ROSE_BLOOD = new CleaverVariant("rose_blood", "#f7528e", 0.1F);
    public static CleaverVariant CORAL = new CleaverVariant("coral", "#89d5d1", 0.2F);
    public static CleaverVariant INKY = new CleaverVariant("inky", "#f7e3b9", 0.3F);

    public static Map<String, CleaverVariant> getAllVariants() {
        return Map.of(
                CARRION.id, CARRION,
                ROSE_BLOOD.id, ROSE_BLOOD,
                CORAL.id, CORAL,
                INKY.id, INKY
        );
    }

    public static CleaverVariant getVariantFromString(String id) { return getAllVariants().getOrDefault(id, CARRION); }

    public static ItemStack addCleaverVariantToStack(ItemStack stack, CleaverVariant variant) {
        CleaverItem.setVariant(stack, variant);
        return stack;
    }

    public static void addCleaversToEntries(FabricItemGroupEntries entries) {
        ItemStack defaultStack = CLEAVER.getDefaultStack();
        for (CleaverVariant variant : getAllVariants().values()) {
            entries.add(addCleaverVariantToStack(defaultStack.copy(), variant));
        }
    }
}
