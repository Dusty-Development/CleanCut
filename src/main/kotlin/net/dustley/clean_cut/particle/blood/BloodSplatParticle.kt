package net.dustley.clean_cut.particle.blood

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.SimpleParticleType
import org.joml.Quaternionf

class BloodSplatParticle(world: ClientWorld?, x: Double, y: Double, z: Double, private var sprites: SpriteProvider?, velocityX: Double, velocityY: Double, velocityZ: Double) : SpriteBillboardParticle(world, x, y, z, velocityX, velocityY, velocityZ) {

    private var rotation = (random.nextDouble() * 360.0) - 180.0

    init {
        this.gravityStrength = 0f
        this.maxAge = ((600.0 + (Math.random() * 0.8 + 0.2) * 20).toInt())
        this.scale = (random.nextFloat() * 0.25f) + 0.1f
        this.setSpriteForAge(sprites)
    }

    override fun buildGeometry(vertexConsumer: VertexConsumer?, camera: Camera?, tickDelta: Float) {
        val offset = 0.01f

        this.setPos(x,y + offset,z)
        this.method_60373(vertexConsumer, camera, Quaternionf().rotateY(Math.toRadians(rotation).toFloat()).rotateX(Math.toRadians(-90.0).toFloat()), 1f)
        this.setPos(x,y - (offset*2),z)
        this.method_60373(vertexConsumer, camera, Quaternionf().rotateY(Math.toRadians(-rotation).toFloat()).rotateX(Math.toRadians(90.0).toFloat()), 1f)
        this.setPos(x,y + offset,z)
    }

    override fun getRotator(): Rotator? = Rotator.ALL_AXIS

    override fun tick() {
        this.updateAge()
    }

    private fun updateAge() {
        val fadeTicks = 60

        age += 1

        this.alpha = when {
            this.age >= this.maxAge -> 0f // Particle is expired
            this.age >= this.maxAge - fadeTicks -> (this.maxAge - this.age).toFloat() / fadeTicks // Fade out
            this.age < this.maxAge - fadeTicks -> 1f
            else -> 1f // Fully visible
        }

        if (this.age >= this.maxAge) {
            this.markDead()
        }
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT

    companion object {
        @Environment(EnvType.CLIENT)
        class Factory(private val sprites: SpriteProvider) : ParticleFactory<SimpleParticleType> {
            override fun createParticle(
                particleType: SimpleParticleType?, level: ClientWorld, x: Double, y: Double, z: Double,
                dx: Double, dy: Double, dz: Double,
            ): Particle {
                return BloodSplatParticle(level, x, y, z, this.sprites, dx, dy, dz)
            }
        }
    }

}