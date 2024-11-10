package net.dustley.clean_cut.entity.thrown_cleaver

import net.dustley.clean_cut.entity.ModEntities
import net.dustley.clean_cut.item.ModItems
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EntityType
import net.minecraft.entity.FlyingItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

//https://github.com/dainxt/WeaponThrow/blob/fabric-1.18.2/src/main/java/com/dainxt/weaponthrow/projectile/WeaponThrowEntity.java

class ThrownCleaverEntity(world: World) : PersistentProjectileEntity(ModEntities.THROWN_CLEAVER, world), FlyingItemEntity {


    private val clientSideRotation = 0f
    private val counterClockwiseBounce = true
    private var dealtDamage = false
    var returnTimer: Int = 0

    constructor(livingEntity: LivingEntity, world: World, power: Double) : this(world) {
        this.owner = livingEntity

        this.setPosition(livingEntity.pos.add(livingEntity.getRotationVec(1.0f).multiply(-0.25)).add(0.0,1.0,0.0))
        this.setNoGravity(false)
        velocity = livingEntity.getRotationVec(1.0f).multiply(power)
    }

    override fun tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true
        }

        val entity = this.owner
        val i = 0// dataTracker.get(TridentEntity.LOYALTY).toInt()
        if (i > 0 && (this.dealtDamage || this.isNoClip) && entity != null) {
            if (!this.isOwnerAlive()) {
                if (!world.isClient && this.pickupType == PickupPermission.ALLOWED) {
                    this.dropStack(this.asItemStack(), 0.1f)
                }

                this.discard()
            } else {
                this.isNoClip = true
                val vec3d = entity.eyePos.subtract(this.pos)
                this.setPos(this.x, this.y + vec3d.y * 0.015 * i.toDouble(), this.z)
                if (world.isClient) {
                    this.lastRenderY = this.y
                }

                val d = 0.05 * i.toDouble()
                this.velocity = velocity.multiply(0.95).add(vec3d.normalize().multiply(d))
                if (this.returnTimer == 0) {
                    this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0f, 1.0f)
                }

                this.returnTimer++
            }
        }

        super.tick()
    }

    private fun isOwnerAlive(): Boolean {
        val entity = this.owner
        return if (entity == null || !entity.isAlive) false else entity !is ServerPlayerEntity || !entity.isSpectator()
    }

    override fun getEntityCollision(currentPosition: Vec3d?, nextPosition: Vec3d?): EntityHitResult? {
        return if (this.dealtDamage) null else super.getEntityCollision(currentPosition, nextPosition)
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        val entity = entityHitResult.entity
        var f = 8.0f
        val entity2 = this.owner
        val damageSource = this.damageSources.trident(this, (entity2 ?: this))
        if (world is ServerWorld) {
//            f = EnchantmentHelper.getDamage(serverWorld, this.weaponStack, entity, damageSource, f)
        }

        this.dealtDamage = true
        if (entity.damage(damageSource, f)) {
            if (entity.type === EntityType.ENDERMAN) {
                return
            }

            if (world is ServerWorld) {
//                EnchantmentHelper.onTargetDamaged(serverWorld, entity, damageSource, this.weaponStack)
            }

            if (entity is LivingEntity) {
                this.knockback(entity, damageSource)
                this.onHit(entity)
            }
        }

        this.velocity = velocity.multiply(-0.01, -0.1, -0.01)
        this.playSound(SoundEvents.ITEM_TRIDENT_HIT, 1.0f, 1.0f)
    }

    override fun onBlockHitEnchantmentEffects(
        world: ServerWorld,
        blockHitResult: BlockHitResult,
        weaponStack: ItemStack?,
    ) {
        val vec3d = blockHitResult.blockPos.clampToWithin(blockHitResult.pos)
        EnchantmentHelper.onHitBlock(
            world,
            weaponStack,
            if (owner is LivingEntity) owner as LivingEntity else null,
            this,
            null,
            vec3d,
            world.getBlockState(blockHitResult.blockPos)
        ) { item: Item? -> this.kill() }
    }


    // INVENTORY //
    override fun getWeaponStack(): ItemStack = this.itemStack
    override fun getStack(): ItemStack = this.itemStack

    override fun tryPickup(player: PlayerEntity): Boolean {
        return super.tryPickup(player) || this.isNoClip && this.isOwner(player) && player.inventory.insertStack(this.asItemStack())
    }

    override fun getDefaultItemStack(): ItemStack {
        return ItemStack(ModItems.CLEAVER_OF_THE_CARION)
    }

    override fun onPlayerCollision(player: PlayerEntity?) {
        if (this.isOwner(player) || this.owner == null) {
            super.onPlayerCollision(player)
        }
    }

    // EFFECTS //
    override fun getHitSound(): SoundEvent = SoundEvents.ITEM_NETHER_WART_PLANT

    // DATA //


    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
        builder.add(ENCHANTED_DATA, false)
    }


    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
//        this.dealtDamage = nbt.getBoolean("DealtDamage")
//        dataTracker.set(TridentEntity.LOYALTY, this.getLoyalty(this.itemStack))
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
//        nbt.putBoolean("DealtDamage", this.dealtDamage)
    }

//    private fun getLoyalty(stack: ItemStack): Byte {
//        return if (world is ServerWorld) MathHelper.clamp(
//            EnchantmentHelper.getTridentReturnAcceleration(
//                serverWorld, stack,
//                this
//            ), 0, 127
//        ).toByte() else
//            0
//    }

    public override fun age() {
//        val i = dataTracker.get(TridentEntity.LOYALTY).toInt()
        if (this.pickupType != PickupPermission.ALLOWED || 0 <= 0) {
            super.age()
        }
    }

    override fun shouldRender(cameraX: Double, cameraY: Double, cameraZ: Double): Boolean = true

    companion object {
        private val ENCHANTED_DATA: TrackedData<Boolean> = DataTracker.registerData(ThrownCleaverEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
    }

}