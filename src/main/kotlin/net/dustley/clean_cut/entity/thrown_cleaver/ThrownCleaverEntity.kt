package net.dustley.clean_cut.entity.thrown_cleaver

import net.dustley.clean_cut.entity.ModEntities
import net.dustley.clean_cut.item.ModItems
import net.dustley.clean_cut.item.cleaver.CarrionCleaverItem
import net.dustley.clean_cut.item.cleaver.CleaverEnchantmentType
import net.dustley.clean_cut.particle.ModParticles
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.FlyingItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World


//https://github.com/dainxt/WeaponThrow/blob/fabric-1.18.2/src/main/java/com/dainxt/weaponthrow/projectile/WeaponThrowEntity.java

class ThrownCleaverEntity(world: World, val isRose:Boolean = false) : PersistentProjectileEntity(ModEntities.THROWN_CLEAVER, world), FlyingItemEntity {

    // Stats
    var thrownCleaverSoundInstance:ThrownCleaverSoundInstance? = null
    private var dealtDamage = false

    // Animation state
    val randomAngle = (Math.random() -0.5) * 25
    private var clientSideRotation = 0f
    private var counterClockwiseBounce = false
    var enchantmentType = CleaverEnchantmentType.NONE
    var hitEntity = false
    var isReturning = false

    // Animation timers
    var returnTimer: Int = RETURN_MAX_TIME
    var hitTimer = 0
    var holdTimer:Int = 0

    constructor(livingEntity: LivingEntity, world: World, power: Double, atkDamage:Double, weaponItem:ItemStack, isRose:Boolean) : this(world, isRose) {
        this.owner = livingEntity
        damage = atkDamage + 1.0;
        stack = weaponItem
        this.setPosition(livingEntity.eyePos.add(livingEntity.getRotationVec(1.0f).multiply(-0.25)).subtract(0.0,height.toDouble() * 0.5,0.0))
        this.setNoGravity(true)
        velocity = livingEntity.getRotationVec(1.0f).multiply(power)

    }

    // EVENTS //
    override fun tick() {
//        if(world.isClient) AAALevel.addParticle(world, true, TRAIL_PARTICLE.clone().position(pos.add(0.0,(height/2f).toDouble(),0.0)).scale(0.05f))

        for(i in 0..random.nextBetween(0,2)) {
            world.addParticle(ModParticles.BLOOD_DRIP_PARTICLE, true, x, y, z, velocity.x * 0.1f, velocity.y * 0.1f, velocity.z * 0.1f)
        }

        if(thrownCleaverSoundInstance == null && world.isClient) {
            thrownCleaverSoundInstance = MinecraftClient.getInstance().player?.let { ThrownCleaverSoundInstance(this, it ) }
            MinecraftClient.getInstance().soundManager.play(thrownCleaverSoundInstance)
        }

        if (this.inGroundTime > HOLD_MAX_TIME && !this.dealtDamage) {
            this.dealtDamage = true
            isReturning = true
        }


        if(returnTimer > 0) returnTimer--
        if(returnTimer <= 0 && !isReturning) setToReturn()

        if(isReturning) {
            inGround = false
            isNoClip = true
            dealtDamage = true


            val difference = (pos.relativize(owner?.eyePos ?: pos))

            addVelocity(difference.normalize().multiply(0.75))
            val speed = Math.clamp(velocity.length(), 0.0, 3.0)
            velocity = velocity.normalize().multiply(speed)
        }

        if(hitTimer > 0) hitTimer -= 1
        if(hitTimer <= 0) attackEntitiesNearby()

        if(holdTimer > 0) {
            holdTimer--
            velocity = Vec3d.ZERO
        }

        super.tick()
    }
    fun setToReturn() {
        if(!isReturning) holdTimer = HOLD_MAX_TIME
        isReturning = true
    }
    fun attackEntitiesNearby() {
        val posA = pos.subtract(Vec3d(1.0,1.0,1.0))
        val posB = pos.add(Vec3d(1.0,1.0,1.0))
        val aabb = Box(posA.x, posA.y, posA.z, posB.x, posB.y, posB.z)

        val entities = world.getOtherEntities(this, aabb)
        entities.forEach {
            val entity = it
            val owner = this.owner

            if(entity is LivingEntity && entity != owner) {

                val damageSource = this.damageSources.thrown(this, owner)
                if (world is ServerWorld) damage = EnchantmentHelper.getDamage(
                    world as ServerWorld,
                    this.weaponStack,
                    entity,
                    damageSource,
                    damage.toFloat()
                ).toDouble()

                if (owner is LivingEntity) owner.onAttacking(entity)

                if (entity.damage(damageSource, damage.toFloat())) {

                    if (entity is LivingEntity) {

                        this.knockback(entity, damageSource)

                        val item = (ModItems.CLEAVER_OF_THE_CARRION as CarrionCleaverItem)
                        item.setBloodCharge(
                            weaponStack,
                            item.getBloodCharge(weaponStack) + CarrionCleaverItem.BLOOD_GAIN_PROJECTILE
                        )

                        if (world is ServerWorld) EnchantmentHelper.onTargetDamaged(
                            world as ServerWorld,
                            entity,
                            damageSource,
                            this.weaponStack
                        )

                        this.onHit(entity)
                        if (entity !== owner && entity is PlayerEntity && owner is ServerPlayerEntity && !this.isSilent) owner.networkHandler.sendPacket(
                            GameStateChangeS2CPacket(
                                GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER,
                                GameStateChangeS2CPacket.DEMO_OPEN_SCREEN.toFloat()
                            )
                        )

                        if(enchantmentType == CleaverEnchantmentType.NURSING) {
                            (owner as LivingEntity).heal(2f)

                            val ownerEffects: MutableCollection<StatusEffectInstance> = mutableListOf()
                            owner.statusEffects.forEach { ownerEffects.add(it) }

                            owner.clearStatusEffects()
                            entity.statusEffects.forEach { effect -> owner.addStatusEffect(effect) }

                            entity.clearStatusEffects()
                            ownerEffects.forEach { entity.addStatusEffect(it) }

                        }
                    }

                    playHitEntityEffects()

                    setToReturn()
                    hitTimer = 10
                    dealtDamage = true
                    hitEntity = true

                }
            }
        }
    }

    // COLLISION //
    override fun age() { this.age++ }
    override fun tryPickup(player: PlayerEntity): Boolean = super.tryPickup(player) || this.isNoClip && this.isOwner(player) && player.inventory.insertStack(asItemStack())
    override fun asItemStack(): ItemStack {
        return itemStack.copyComponentsToNewStack(itemStack.item, 1)
    }
    fun asPublicItemStack():ItemStack = asItemStack()

    override fun onPlayerCollision(player: PlayerEntity?) {
        if (this.isOwner(player) || this.owner == null) {
            super.onPlayerCollision(player)
        }
    }
    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        super.onBlockHit(blockHitResult)
        setToReturn()
        playHitBlockEffects()
    }
    override fun onEntityHit(entityHitResult: EntityHitResult) { }
    override fun onHit(target: LivingEntity?) { this.dealtDamage = true }

    // EFFECTS //
    fun playHitEntityEffects() {
        playSound(ENTITY_HIT_SOUND, 1.0f, 1.0f) // Play blood particles
//        if(world.isClient) AAALevel.addParticle(world, true, HIT_ENTITY_PARTICLE.clone().position(pos.add(0.0,(height/2f).toDouble(),0.0)).scale(1f))
        world.addParticle(ParticleTypes.MYCELIUM, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0)
    }
    fun playHitBlockEffects() {
        playSound(BLOCK_HIT_SOUND, 1.0f, 1.0f) // Play block particles
        counterClockwiseBounce = true
//        if(world.isClient) AAALevel.addParticle(world, true, HIT_BLOCK_PARTICLE.clone().position(pos.add(0.0,(height/2f).toDouble(),0.0)).scale(50f))
    }

    // INVENTORY //
    override fun getWeaponStack(): ItemStack = this.itemStack
    override fun getStack(): ItemStack = this.itemStack
    override fun getDefaultItemStack(): ItemStack = if(isRose) ItemStack(ModItems.ROSE_BLOOD_CLEAVER) else ItemStack(ModItems.CLEAVER_OF_THE_CARRION)

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
        if ((!this.inGround && holdTimer <= 0) && !hitEntity) {
            clientSideRotation = (if (this.counterClockwiseBounce) 1 else -1) * (this.age + partialTicks) * -30f
        }
        return this.clientSideRotation
    }

    companion object {
        private const val DEALT_DAMAGE_NBT = "DealtDamage"
        private val ENCHANTED_DATA: TrackedData<Boolean> = DataTracker.registerData(ThrownCleaverEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)

        val ENTITY_HIT_SOUND = SoundEvents.ITEM_NETHER_WART_PLANT
        val BLOCK_HIT_SOUND = SoundEvents.BLOCK_ANVIL_HIT

        val HOLD_MAX_TIME = 8
        val RETURN_MAX_TIME = 30
    }

}