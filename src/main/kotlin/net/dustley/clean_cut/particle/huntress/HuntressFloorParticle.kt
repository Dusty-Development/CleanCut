package net.dustley.clean_cut.particle.huntress

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.SimpleParticleType
import org.joml.Vector3f


class HuntressFloorParticle(world: ClientWorld?, x: Double, y: Double, z: Double, private var sprites: SpriteProvider, velocityX: Double, velocityY: Double, velocityZ: Double) : SpriteBillboardParticle(world, x, y, z, velocityX, velocityY, velocityZ) {

//    private var shaderEffect: ManagedShaderEffect? = null

    init {
        this.setSpriteForAge(sprites);
        this.gravityStrength = 0.0f;
        this.maxAge = 20*60
        this.collidesWithWorld = false; // Ensures it doesn't get blocked

//        val mc = MinecraftClient.getInstance()
//        this.shaderEffect = ShaderEffectManager.getInstance().manage(
//            CleanCut.id("shaders/post/huntress_particle.json")
//        ) { shader: ManagedShaderEffect ->
//            shader.setSamplerUniform("DepthSampler", (mc.framebuffer as ReadableDepthFramebuffer).stillDepthMap)
//            shader.setUniformValue(
//                "ViewPort",
//                0,
//                0,
//                mc.window.framebufferWidth,
//                mc.window.framebufferHeight
//            )
//        }
    }

    override fun buildGeometry(vertexConsumer: VertexConsumer?, camera: Camera?, tickDelta: Float) {
        // Always align with the ground plane (Y-axis)
        val pos = Vector3f(x.toFloat(), y.toFloat(), z.toFloat())
        pos.y = camera!!.pos.y.toFloat() // Flatten to camera's Y plane

        super.buildGeometry(vertexConsumer, camera, tickDelta)

//        shaderEffect?.render(tickDelta)
    }


    override fun getType(): ParticleTextureSheet {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT
    }

    companion object {
        @Environment(EnvType.CLIENT)
        class Factory(private val sprites: SpriteProvider) : ParticleFactory<SimpleParticleType> {
            override fun createParticle(
                particleType: SimpleParticleType?, level: ClientWorld, x: Double, y: Double, z: Double,
                dx: Double, dy: Double, dz: Double,
            ): Particle {
                return HuntressFloorParticle(level, x, y, z, this.sprites, dx, dy, dz)
            }
        }
    }

}