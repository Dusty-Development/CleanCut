package net.dustley.clean_cut.entity.thrown_cleaver

import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.sound.MovingSoundInstance
import net.minecraft.client.sound.SoundInstance
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.MathHelper

class ThrownCleaverSoundInstance(
    private var entity: ThrownCleaverEntity,
    private var player: ClientPlayerEntity
) : MovingSoundInstance(SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.AMBIENT, SoundInstance.createRandom()) {
    private var tickCount = 0

    init{
        this.repeat = true
        this.repeatDelay = 0
        this.volume = 0.5f
    }

    override fun tick() {

        ++this.tickCount
        if (!player.isRemoved && !entity.isRemoved ) {
            this.x = (entity.x.toFloat()).toDouble()
            this.y = (entity.y.toFloat()).toDouble()
            this.z = (entity.z.toFloat()).toDouble()

            val speed = entity.velocity.lengthSquared().toFloat()

            if (speed.toDouble() >= 1.0E-7) this.volume = MathHelper.clamp(speed / 4.0f, 0.0f, 1.0f) else this.volume = 0.0f

            val distance = entity.distanceTo(player)
            this.volume = 1.0f / (distance * 0.5f)

            if(entity.velocity.length() < 0.1) volume = 0.0f

            if (this.volume > 0.8f) this.pitch = 1.0f + (this.volume - 0.8f) else this.pitch = 1.0f

        } else {
            this.setDone()
        }
    }

}