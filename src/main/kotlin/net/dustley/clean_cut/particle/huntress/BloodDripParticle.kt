package net.dustley.clean_cut.particle.huntress

import net.dustley.clean_cut.particle.ModParticles
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.SimpleParticleType

class BloodDripParticle(world: ClientWorld?, x: Double, y: Double, z: Double, private var sprites: SpriteProvider?, velocityX: Double, velocityY: Double, velocityZ: Double) : SpriteBillboardParticle(world, x, y, z, velocityX, velocityY, velocityZ) {

    init {
        this.setBoundingBoxSpacing(0.01f, 0.01f)
        this.gravityStrength = 0.06f
        this.collidesWithWorld = true; // Ensures it doesn't get blocked
        this.maxAge = ((64.0 / (Math.random() * 0.8 + 0.2)).toInt())
        this.setSpriteForAge(sprites)
    }

    override fun tick() {
        this.prevPosX = this.x
        this.prevPosY = this.y
        this.prevPosZ = this.z

        this.updateAge()
        if (!this.dead) {
            this.velocityY -= gravityStrength.toDouble()
            this.move(this.velocityX, this.velocityY, this.velocityZ)
            this.updateVelocity()
            if (!this.dead) {
                this.velocityX *= 0.9950000190734863
                this.velocityY *= 0.9800000190734863
                this.velocityZ *= 0.9950000190734863
            }
        }
    }

    override fun getRotator(): Rotator = Rotator.Y_AND_W_ONLY

    fun updateAge() {
        if (maxAge-- <= 0) {
            this.markDead()
        }
    }

    fun updateVelocity() {
        if (this.onGround) {
            this.markDead()
            world.addParticle(ModParticles.BLOOD_SPLAT_PARTICLE, this.x, this.y, this.z, 0.0, 0.0, 0.0)
        }
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_OPAQUE

    companion object {
        @Environment(EnvType.CLIENT)
        class Factory(private val sprites: SpriteProvider) : ParticleFactory<SimpleParticleType> {
            override fun createParticle(
                particleType: SimpleParticleType?, level: ClientWorld, x: Double, y: Double, z: Double,
                dx: Double, dy: Double, dz: Double,
            ): Particle {
                return BloodDripParticle(level, x, y, z, this.sprites, dx, dy, dz)
            }
        }
    }

}