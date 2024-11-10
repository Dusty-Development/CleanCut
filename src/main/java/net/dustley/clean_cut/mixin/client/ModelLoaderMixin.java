package net.dustley.clean_cut.mixin.client;

import net.dustley.clean_cut.client.ClientSetup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {
    @Shadow protected abstract void add(ModelIdentifier id, UnbakedModel model);

    @Inject(method = "getBakedModelMap", at = @At("RETURN"))
    private void getBakedModelMap(CallbackInfoReturnable<Map<ModelIdentifier, BakedModel>> cir) {
        // Log all baked models to check what's available

        System.out.println("Model baking completed, modifying result");
        ClientSetup.modifyBakingResult(cir.getReturnValue());
    }
}
