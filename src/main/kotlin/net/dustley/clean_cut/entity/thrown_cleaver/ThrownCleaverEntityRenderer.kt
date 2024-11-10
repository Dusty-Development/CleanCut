package net.dustley.clean_cut.entity.thrown_cleaver

import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f


class ThrownCleaverEntityRenderer(ctx: EntityRendererFactory.Context) : FlyingItemEntityRenderer<ThrownCleaverEntity>(ctx) {


    private var itemRenderer: ItemRenderer? = null

    init {
        this.itemRenderer = ctx.itemRenderer
    }

    override fun render(
        entityIn: ThrownCleaverEntity, entityYaw: Float, partialTicks: Float, matrixStackIn: MatrixStack,
        bufferIn: VertexConsumerProvider?, packedLightIn: Int,
    ) {
        val degrees: Float = entityIn.getRotationAnimation(partialTicks)

        val scale = 0.75f
        matrixStackIn.push()
        matrixStackIn.translate(0f, 0.15f, 0.0f)
//        matrixStackIn.multiply(Vec3d.POSITIVE_Y.getDegreesQuaternion( MathHelper.lerp(partialTicks, entityIn.prevYaw, entityIn.getYaw()) - 90.0f ))
//        matrixStackIn.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-degrees))
        matrixStackIn.scale(scale, scale, scale)

        val count: Int = entityIn.getItemStack().getCount()

//        itemRenderer.renderItem(
//            entityIn.getStack(), ModelTransformation.Mode.FIXED, packedLightIn,
//            OverlayTexture.DEFAULT_UV, matrixStackIn, bufferIn, entityIn.getId()
//        )
//        if (count > 32) {
//            matrixStackIn.translate(-0.05f, -0.05f, -0.05f)
//            itemRenderer.renderItem(
//                entityIn.getStack(), ModelTransformation.Mode.FIXED,
//                packedLightIn, OverlayTexture.DEFAULT_UV, matrixStackIn, bufferIn, entityIn.getId()
//            )
//        }

        matrixStackIn.pop()
    }


}