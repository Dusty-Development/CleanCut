package net.dustley.clean_cut.entity.thrown_cleaver

import net.dustley.clean_cut.entity.ModEntities
import net.dustley.clean_cut.item.ModItems
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.FlyingItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.world.World


//https://github.com/dainxt/WeaponThrow/blob/fabric-1.18.2/src/main/java/com/dainxt/weaponthrow/projectile/WeaponThrowEntity.java

class ThrownCleaverEntity(world: World) : PersistentProjectileEntity(ModEntities.THROWN_CLEAVER, world), FlyingItemEntity {

    // Stats
    private var attackDamage = 0f;
    private var dealtDamage = false

    // Animation state
    val randomAngle = (Math.random() -0.5) * 45
    private var clientSideRotation = 0f
    private val counterClockwiseBounce = true

    // Animation timers
    var returnTimer: Int = 0
    var holdTimer:Int = 0

    constructor(livingEntity: LivingEntity, world: World, power: Double, damage:Double, weaponItem: ItemStack) : this(world) {
        this.owner = livingEntity
        attackDamage = damage.toFloat();
        stack = weaponItem
        this.setPosition(livingEntity.pos.add(livingEntity.getRotationVec(1.0f).multiply(-0.25)).add(0.0,1.0,0.0))
        this.setNoGravity(false)
        velocity = livingEntity.getRotationVec(1.0f).multiply(power)
    }

    // EVENTS //
    override fun tick() {
        if (this.inGroundTime > 4 && !this.dealtDamage) {
            this.dealtDamage = true
        }

        super.tick()
    }

    // COLLISION //
    override fun age() { this.age++ }
    override fun tryPickup(player: PlayerEntity): Boolean = super.tryPickup(player) || this.isNoClip && this.isOwner(player) && player.inventory.insertStack(this.asItemStack())
    override fun onPlayerCollision(player: PlayerEntity?) {
        if (this.isOwner(player) || this.owner == null) {
            super.onPlayerCollision(player)
        }
    }

    // EFFECTS //
    override fun getHitSound(): SoundEvent = SoundEvents.ITEM_NETHER_WART_PLANT
    fun playHitEntityEffects() {
        playSound(ENTITY_HIT_SOUND, 1.0f, 1.0f)

        // Play blood particles
        world.addParticle(ParticleTypes.MYCELIUM, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0)
    }

    fun playHitBlockEffects() {
        playSound(BLOCK_HIT_SOUND, 1.0f, 1.0f)

        // Play block particles
        world.addParticle(ParticleTypes.ELECTRIC_SPARK, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0)
    }

    // INVENTORY //
    override fun getWeaponStack(): ItemStack = this.itemStack
    override fun getStack(): ItemStack = this.itemStack
    override fun getDefaultItemStack(): ItemStack = ItemStack(ModItems.CLEAVER_OF_THE_CARION)

    // DATA //
    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(ENCHANTED_DATA, false)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        this.dealtDamage = nbt.getBoolean(DEALT_DAMAGE_NBT)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putBoolean(DEALT_DAMAGE_NBT, this.dealtDamage)
    }

    // RENDER //
    @Environment(EnvType.CLIENT)
    fun getRotationAnimation(partialTicks: Float): Float {
        if (!this.inGround) {
            clientSideRotation = (if (this.counterClockwiseBounce) 1 else -1) * (this.age + partialTicks) * 50f
        }
        return this.clientSideRotation
    }

    companion object {
        private const val DEALT_DAMAGE_NBT = "DealtDamage"
        private val ENCHANTED_DATA: TrackedData<Boolean> = DataTracker.registerData(ThrownCleaverEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)

        val ENTITY_HIT_SOUND = SoundEvents.ITEM_NETHER_WART_PLANT
        val BLOCK_HIT_SOUND = SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT

        val HOLD_MAX_TIME = 20
        val RETURN_MAX_TIME = 100
    }

}