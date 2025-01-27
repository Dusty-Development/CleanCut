package net.dustley.clean_cut.enchantment;

import net.dustley.clean_cut.CleanCut;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class ModEnchantments {

     public static final RegistryKey<Enchantment> HUNTRESS = of("huntress");
    public static final RegistryKey<Enchantment> BLOODRUSH = of("bloodrush");
    public static final RegistryKey<Enchantment> NURSING = of("nursing");
     public static final RegistryKey<Enchantment> CHARLATAN = of("charlatan");

    private static RegistryKey<Enchantment> of(String name) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, CleanCut.identifier(name));
    }

    public static void initialize() {
        // Initialization logic if necessary
    }
}
