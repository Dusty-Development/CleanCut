package net.dustley.clean_cut.entity.thrown_cleaver

import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import org.joml.Quaternionf


class ThrownCleaverEntityRenderer(ctx: EntityRendererFactory.Context) : FlyingItemEntityRenderer<ThrownCleaverEntity>(ctx) {


    private var itemRenderer: ItemRenderer? = null

    init { this.itemRenderer = ctx.itemRenderer }

    override fun render(
        entityIn: ThrownCleaverEntity, entityYaw: Float, partialTicks: Float, matrixStackIn: MatrixStack,
        bufferIn: VertexConsumerProvider?, packedLightIn: Int,
    ) {
        val degrees: Float = entityIn.getRotationAnimation(partialTicks) * 2
        val scale = 0.75f

        matrixStackIn.push()

        // Translate the position upwards slightly
        matrixStackIn.translate(0f, 0.15f, 0.0f)

        val yawQuaternion = Quaternionf().rotateY(Math.toRadians(degrees.toDouble()).toFloat())
        matrixStackIn.multiply(yawQuaternion)

        val rollQuaternion = Quaternionf().rotateX(Math.toRadians(90.0).toFloat())
        matrixStackIn.multiply(rollQuaternion)

        val pitchQuaternion = Quaternionf().rotateX(Math.toRadians(entityIn.randomAngle).toFloat())
        matrixStackIn.multiply(pitchQuaternion)

        matrixStackIn.scale(scale, scale, scale)


        // Render the item
        itemRenderer?.renderItem(
            entityIn.asPublicItemStack(),
            ModelTransformationMode.FIXED,
            packedLightIn,
            OverlayTexture.DEFAULT_UV,
            matrixStackIn,
            bufferIn,
            entityIn.world,
            entityIn.id
        )

        matrixStackIn.pop()
    }


}