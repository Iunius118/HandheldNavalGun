package iunius118.mods.handheldnavalgun.client;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Target {

	public Type type = Type.NONE;
	public Vec3d pos;
	public int entityId;
	public int worldHashCode;

	public Target(World world, Entity entity) {
		this.setEntity(world, entity);
	}

	public Target(World world, double x, double y, double z) {
		setCoord(world, x, y, z);
	}

	public Target(World world, Vec3d v) {
		this.setCoord(world, v.xCoord, v.yCoord, v.zCoord);
	}

	public Target(World world, RayTraceResult result) {
		if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
			this.setEntity(world, result.entityHit);
		} else if (result.hitVec != null) {
			this.setCoord(world, result.hitVec.xCoord, result.hitVec.yCoord, result.hitVec.zCoord);
		}
	}

	public void setEntity(World world, Entity entity) {
		Entity targetEntity = entity;

		if (targetEntity instanceof EntityDragonPart) {
			targetEntity = (Entity)((EntityDragonPart)targetEntity).entityDragonObj;
		}

		this.type = Type.ENTITY;
		this.entityId = targetEntity.getEntityId();
		this.worldHashCode = world.hashCode();
	}

	public void setCoord(World world, double x, double y, double z) {
		this.type = Type.BLOCK;
		this.pos = new Vec3d(x, y, z);
		this.worldHashCode = world.hashCode();;
	}

	public Vec3d getPos(World world, float partialTicks) {
		if (worldHashCode != world.hashCode()) {
			return null;
		} else if (type == Type.BLOCK) {
			return pos;
		} else if (type == Type.ENTITY) {
			Entity entity = world.getEntityByID(entityId);

			if (entity != null && !entity.isDead) {
				double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
				double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks + entity.height / 2.0D;
				double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
				return new Vec3d(x, y, z);
			}
		}

		return null;
	}

	public static enum Type {
		NONE,
		BLOCK,
		ENTITY
	}

}
