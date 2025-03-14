package net.dustley.clean_cut.entity.cleaver;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class CleaverProjectileSoundInstance extends MovingSoundInstance {

    private int tickCount = 0;
    private static final float VOLUME = 0.5f;

    private final CleaverProjectileEntity entity;
    private final ClientPlayerEntity player;

    protected CleaverProjectileSoundInstance(CleaverProjectileEntity entity, ClientPlayerEntity player) {
        super(SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.AMBIENT, SoundInstance.createRandom());
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = VOLUME;

        this.entity = entity;
        this.player = player;
    }

    @Override
    public void tick() {
        ++tickCount;
        if (!player.isRemoved() && !entity.isRemoved() ) {
            this.x = (entity.getPos().x);
            this.y = (entity.getPos().y);
            this.z = (entity.getPos().z);

            float speed = (float) entity.getVelocity().lengthSquared();

            if (speed >= 1.0E-7) this.volume = MathHelper.clamp(speed / 4.0f, 0.0f, 1.0f); else this.volume = 0.0f;

            float distance = entity.distanceTo(player);
            this.volume = VOLUME / (distance * 0.5f);

            if(entity.getVelocity().length() < 0.1) volume = 0.0f;

            if (this.volume > 0.8f) this.pitch = 1.0f + (this.volume - 0.8f); else this.pitch = 1.0f;

        } else {
            this.setDone();
        }
    }
}
