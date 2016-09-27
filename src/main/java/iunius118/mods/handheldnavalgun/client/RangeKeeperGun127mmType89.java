package iunius118.mods.handheldnavalgun.client;

import javax.annotation.Nullable;

import iunius118.mods.handheldnavalgun.client.util.ClientUtils;
import iunius118.mods.handheldnavalgun.client.util.Target;
import iunius118.mods.handheldnavalgun.entity.EntityProjectile127mmAntiAircraftCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RangeKeeperGun127mmType89 {

	public Target target;
	public int ticksFuse = EntityProjectile127mmAntiAircraftCommon.FUSE_MAX;

	public double futureYaw;
	public double futurePitch;
	public double prevFutureYaw;
	public double prevFuturePitch;

	private boolean isValid;

	public boolean isValid() {
		return this.isValid;
	}

	public void setTarget(@Nullable Target targetIn) {
		this.target = targetIn;
	}

	@Nullable
	public Target getTarget() {
		return this.target;
	}

	public void setFuse(int ticks) {
		if (ticks < 0) {
			this.ticksFuse = 0;
		} else if (ticks > EntityProjectile127mmAntiAircraftCommon.FUSE_MAX) {
			this.setFuseMax();
		} else {
			this.ticksFuse = ticks;
		}
	}

	public void setFuseMax() {
		this.ticksFuse = EntityProjectile127mmAntiAircraftCommon.FUSE_MAX;
	}

	public int getFuse() {
		return this.ticksFuse;
	}

	@Nullable
	public Vec3d getTargetScreenPos(World world, float partialTicks) {
		return ClientUtils.getScreenPos(target.getPos(world, partialTicks), partialTicks);
	}

	@Nullable
	public Vec3d getTargetFutureScreenPos(World world, float partialTicks) {
		if (this.isValid) {
			double yaw = futureYaw;
			double pitch = futurePitch;

			return ClientUtils.getScreenPos((float)yaw, (float)pitch, partialTicks);
		}

		return null;
	}

	public boolean updatetFutureTarget(World world) {
		this.prevFutureYaw = this.futureYaw;
		this.prevFuturePitch = this.futurePitch;

		if (this.target == null) {
			return setIsValid(false);
		}

		Vec3d vec3Target1 = this.target.getPos(world, 2.0F);
		Vec3d vec3DeltaTarget = this.target.getDeltaPos(world);

		if (vec3Target1 == null || vec3DeltaTarget == null) {
			return setIsValid(false);
		}

		Entity player = Minecraft.getMinecraft().thePlayer;

		if (player == null) {
			return setIsValid(false);
		}

		Vec3d vec3Player = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		final double v0sq = EntityProjectile127mmAntiAircraftCommon.INITIAL_VELOCITY * EntityProjectile127mmAntiAircraftCommon.INITIAL_VELOCITY;

		int t;

		for (t = 1; t <= EntityProjectile127mmAntiAircraftCommon.FUSE_MAX; t++) {
			double r = vec3Player.distanceTo(vec3Target1);
			int ts = (int)Math.floor(0.00007D * r * r + 0.25D * r - 0.04D);

			if (ts <= t) {
				break;
			}

			vec3Target1 = vec3Target1.add(vec3DeltaTarget);
		}

		double x1 = vec3Target1.xCoord - vec3Player.xCoord;
		double z1 = vec3Target1.zCoord - vec3Player.zCoord;
		double tx1 = Math.sqrt(x1 * x1 + z1 * z1);
		double ty1 = vec3Target1.yCoord - vec3Player.yCoord;
		double v0x1 = tx1 / ClientUtils.ticksToV0Rate(t);
		double v0y1 = (ty1 + 3.0D * t) / ClientUtils.ticksToV0Rate(t) - 3.0D;
		double v0sq1 = v0x1 * v0x1 + v0y1 * v0y1;

		for (; t <= EntityProjectile127mmAntiAircraftCommon.FUSE_MAX; t++) {
			Vec3d vec3Target2 = vec3Target1.add(vec3DeltaTarget);
			double x2 = vec3Target2.xCoord - vec3Player.xCoord;
			double z2 = vec3Target2.zCoord - vec3Player.zCoord;
			double tx2 = Math.sqrt(x2 * x2 + z2 * z2);
			double ty2 = vec3Target2.yCoord - vec3Player.yCoord;
			double v0x2 = tx2 / ClientUtils.ticksToV0Rate(t + 1);
			double v0y2 = (ty2 + 3.0D * (t + 1)) / ClientUtils.ticksToV0Rate(t + 1) - 3.0D;
			double v0sq2 = v0x2 * v0x2 + v0y2 * v0y2;

			if ((v0sq1 > v0sq2 && v0sq1 >= v0sq && v0sq2 < v0sq) || (v0sq1 < v0sq2 && v0sq1 < v0sq && v0sq2 >= v0sq)) {
				if (Math.abs(v0sq1 - v0sq) <= Math.abs(v0sq2 - v0sq)) {
					this.ticksFuse = t;
					this.futureYaw = (tx1 != 0.0D) ? Math.toDegrees(Math.atan2(- vec3Target1.xCoord + vec3Player.xCoord, vec3Target1.zCoord - vec3Player.zCoord)) : 0.0D;
					this.futurePitch = -Math.toDegrees(Math.atan2(v0y1, v0x1));
					return setIsValid(true);
				} else {
					if (t < EntityProjectile127mmAntiAircraftCommon.FUSE_MAX) {
						t++;
					}

					this.ticksFuse = t;
					this.futureYaw = (tx2 != 0.0D) ? Math.toDegrees(Math.atan2(- vec3Target2.xCoord + vec3Player.xCoord, vec3Target2.zCoord - vec3Player.zCoord)) : 0.0D;
					this.futurePitch = -Math.toDegrees(Math.atan2(v0y2, v0x2));
					return setIsValid(true);
				}
			}

			vec3Target1 = vec3Target2;
			tx1 = tx2;
			ty1 = ty2;
			v0x1 = v0x2;
			v0y1 = v0y2;
			v0sq1 = v0sq2;
		}

		return setIsValid(false);
	}

	public boolean setIsValid(boolean is_valid) {
		this.isValid = is_valid;
		return is_valid;
	}

}
