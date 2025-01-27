package net.dustley.clean_cut.item;

import net.dustley.clean_cut.CleanCut;
import net.dustley.clean_cut.component.ModItemComponents;
import net.dustley.clean_cut.item.weapon.cleaver.CleaverItem;
import net.minecraft.client.item.ModelPredicateProviderRegistry;

public class ModModelPredicates {

    public static void registerModelPredicates() {
        ModelPredicateProviderRegistry.register(
                ModItems.CLEAVER, CleanCut.identifier("cleaver_variant"),

                (stack, world, entity, seed) -> {
                    if(stack.contains(ModItemComponents.CLEAVER_VARIANT)) return CleaverItem.getVariant(stack).predicate;
                    return 0F;
                }
        );
    }

}
