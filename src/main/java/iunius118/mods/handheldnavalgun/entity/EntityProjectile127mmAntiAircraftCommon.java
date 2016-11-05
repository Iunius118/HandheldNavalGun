package iunius118.mods.handheldnavalgun.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityProjectile127mmAntiAircraftCommon extends EntityThrowable {

	public static final int FUSE_SAFETY = 2;
	public static final int FUSE_MAX = 80;
	public int fuse = FUSE_MAX;
	public static final float STRENGTH = 4.0F;
	public static final float INITIAL_VELOCITY = 4.0F;
	public static final float INACCURACY = 1.0F;

	public static final String TAG_FUSE = "fuse";
	public static final String TAG_TICKS_EXISTED = "age";

	public float spin = 0.0F;

	public EntityProjectile127mmAntiAircraftCommon(World worldIn, EntityLivingBase throwerIn, int fuseTicks) {
		this(worldIn, throwerIn);
		this.setFuse(fuseTicks);
	}

	public EntityProjectile127mmAntiAircraftCommon(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
		this.setHeadingFromThrower(throwerIn, throwerIn.rotationPitch, throwerIn.rotationYaw, 0.0F, this.INITIAL_VELOCITY, this.INACCURACY);
	}

	public EntityProjectile127mmAntiAircraftCommon(World worldIn) {
		super(worldIn);

		if (this.worldObj.isRemote) {
			this.setRenderDistanceWeight(4.0F);
		}
	}

	public void setFuse(int ticks) {
		if (ticks < 0) {
			this.fuse = 0;
		} else if (ticks > this.FUSE_MAX) {
			this.setFuseMax();
		} else {
			this.fuse = ticks;
		}
	}

	public void setFuseSafety(int ticks) {
		if (ticks < this.FUSE_SAFETY + 1) {
			this.fuse = this.FUSE_SAFETY + 1;
		} else if (ticks > this.FUSE_MAX) {
			this.setFuseMax();
		} else {
			this.fuse = ticks;
		}
	}

	public void setFuseMax() {
		this.fuse = this.FUSE_MAX;
	}

	public int getFuse() {
		return this.fuse;
	}

	public void printDebugLog() {
		if (!this.worldObj.isRemote) {

			System.out.println(
					"T: "
					+ this.ticksExisted
					+ ", F: "
					+ this.fuse
					+ ", Px: "
					+ this.posX
					+ ", Py: "
					+ this.posY
					+ ", Pz: "
					+ this.posZ
					+ ", Vy: "
					+ this.motionY
					+ ", V: "
					+ Math.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ)
					);
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		this.fuse--;
		//this.printDebugLog();

		if (!this.isDead && (this.fuse < 1 || this.isInWater() || this.isInLava())) {
			this.onImpact(new RayTraceResult(this));
		}

		if (this.worldObj.isRemote) {
			this.spin = (this.spin + 30.0F) % 360.0F;
		}

		//this.worldObj.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, true, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.worldObj.isRemote) {
			WorldServer world = (WorldServer)this.worldObj;

			if (this.ticksExisted > this.FUSE_SAFETY) {
				// create explosion
				world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, true, result.hitVec.xCoord, result.hitVec.yCoord, result.hitVec.zCoord, 1, 0.0D, 0.0D, 0.0D, 0.0D, new int[0]);
				world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
				Explosion explosion = world.createExplosion(this.getThrower(), result.hitVec.xCoord, result.hitVec.yCoord, result.hitVec.zCoord, this.STRENGTH, false);
				BlockPos posExp = result.getBlockPos();

				if (posExp == null) {
					posExp = new BlockPos(result.hitVec.xCoord, result.hitVec.yCoord, result.hitVec.zCoord);
				}

				// induced explosion of TNT
				for (int x = -5; x < 6; x++) {
					for (int y = -5; y < 6; y++) {
						for (int z = -5; z < 6; z++) {
							BlockPos pos = posExp.add(x, y, z);
							Block block = world.getBlockState(pos).getBlock();

							if (block instanceof BlockTNT) {
								block.onBlockExploded(world, pos, explosion);
							}
						}
					}
				}
			} else {
				// hit at close distance (in a very short time), deal direct damage
				if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
					if (result.entityHit instanceof EntityPlayer) {
						if (result.entityHit != this.getThrower()) {
							result.entityHit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)this.getThrower()), 40.0F);
						}
					} else {
						result.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.getThrower()), 40.0F);
					}
				}
			}

			// drop shell wreckage
			if (this.getThrower() instanceof EntityPlayer && !((EntityPlayer)this.getThrower()).capabilities.isCreativeMode) {
				EntityItem entityitem = new EntityItem(world, result.hitVec.xCoord, result.hitVec.yCoord, result.hitVec.zCoord, new ItemStack(Items.IRON_INGOT));
				world.spawnEntityInWorld(entityitem);
			}
		}

		this.setDead();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger(this.TAG_FUSE, this.fuse);
		compound.setInteger(this.TAG_TICKS_EXISTED, this.ticksExisted);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setFuse(compound.getInteger(this.TAG_FUSE));
		this.ticksExisted = compound.getInteger(this.TAG_TICKS_EXISTED);
	}

	@Override
	public boolean isInRangeToRenderDist(double distance) {
		return distance < 65536;
	}

	@Override
	public Vec3d getLook(float partialTicks) {
		return new Vec3d(this.motionX, this.motionY, this.motionZ);
	}

}
