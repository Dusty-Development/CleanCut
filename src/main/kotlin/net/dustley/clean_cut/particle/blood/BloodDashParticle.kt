//package net.dustley.clean_cut.particle.blood
//
//import net.fabricmc.api.EnvType
//import net.fabricmc.api.Environment
//import net.minecraft.client.particle.*
//import net.minecraft.client.world.ClientWorld
//import net.minecraft.particle.SimpleParticleType
//
//class BloodDashParticle(world: ClientWorld?, x: Double, y: Double, z: Double, sprites: SpriteProvider?, velocityX: Double, velocityY: Double, velocityZ: Double) : SpriteBillboardParticle(world, x, y, z, velocityX, velocityY, velocityZ) {
//
//    var rotation = (random.nextDouble() * 360.0) - 180.0
//
//    init {
//        this.gravityStrength = 0f
//        this.maxAge = ((20.0 / (Math.random() * 0.8 + 0.2)).toInt())
//        this.scale = (random.nextFloat() * 0.05f) + 0.05f
//        this.setSpriteForAge(sprites)
//    }
//
//    override fun tick() {
//        this.prevPosX = this.x
//        this.prevPosY = this.y
//        this.prevPosZ = this.z
//
//        this.updateAge()
//    }
//
//    fun updateAge() {
//        if (maxAge-- <= 0) {
//            this.markDead()
//        }
//    }
//
//
//    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_OPAQUE
//
//    companion object {
//        @Environment(EnvType.CLIENT)
//        class Factory(private val sprites: SpriteProvider) : ParticleFactory<SimpleParticleType> {
//            override fun createParticle(
//                particleType: SimpleParticleType?, level: ClientWorld, x: Double, y: Double, z: Double,
//                dx: Double, dy: Double, dz: Double,
//            ): Particle {
//                return BloodDashParticle(level, x, y, z, this.sprites, dx, dy, dz)
//            }
//        }
//    }
//
//}