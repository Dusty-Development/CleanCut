package net.dustley.clean_cut.item

import com.mojang.serialization.Codec
import net.dustley.clean_cut.CleanCut
import net.minecraft.component.ComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import java.util.function.UnaryOperator

object ModComponents {

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