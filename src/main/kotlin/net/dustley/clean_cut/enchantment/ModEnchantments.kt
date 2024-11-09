package net.dustley.clean_cut.enchantment

import net.dustley.clean_cut.CleanCut
import net.minecraft.enchantment.Enchantment
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier


object ModEnchantments {

    val HUNTRESS: RegistryKey<Enchantment> = of("huntress")
    val BLOODRUSH: RegistryKey<Enchantment> = of("bloodrush")
    val NURSING: RegistryKey<Enchantment> = of("nursing")
    val UNERVING: RegistryKey<Enchantment> = of("unerving")

    private fun of(name: String): RegistryKey<Enchantment> {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(CleanCut.MOD_ID, name))
    }

    fun initialize() {
    }
}