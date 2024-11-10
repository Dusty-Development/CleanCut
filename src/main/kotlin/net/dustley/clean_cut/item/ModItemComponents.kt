package net.dustley.clean_cut.item

import com.mojang.serialization.Codec
import net.dustley.clean_cut.CleanCut
import net.minecraft.block.entity.EnchantingTableBlockEntity
import net.minecraft.component.ComponentType
import net.minecraft.component.type.FoodComponent
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import java.util.function.UnaryOperator


object ModItemComponents {

    val LIVING_STEEL_FOOD: FoodComponent = FoodComponent.Builder().nutrition(10).saturationModifier(0.1f).statusEffect(StatusEffectInstance(StatusEffects.WITHER, 200, 5), 0.5f).build()

    val BLOOD_CHARGE: ComponentType<Float?> = register( "blood_charge" ) { builder: ComponentType.Builder<Float?> -> builder.codec( Codec.FLOAT ) }

    private fun <T> register(name: String, builderOperator: UnaryOperator<ComponentType.Builder<T>>): ComponentType<T> {
        return Registry.register(
            Registries.DATA_COMPONENT_TYPE, Identifier.of(CleanCut.MOD_ID, name),
            builderOperator.apply(ComponentType.builder<T>()).build()
        )
    }

    fun registerDataComponentTypes() {
        CleanCut.LOGGER.info("Registering Data Component Types for " + CleanCut.MOD_ID)
    }
}