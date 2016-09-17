package iunius118.mods.handheldnavalgun.client.util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.sun.istack.internal.Nullable;

@SideOnly(Side.CLIENT)
public class ClientUtils {
	private static final ClientUtils INSTANCE = new ClientUtils();
	private static double[] v0rate;

	public ClientUtils() {
		this.v0rate = new double[128];

		for (int i = 0; i < this.v0rate.length; i++){
			this.v0rate[i] = 100 * (1 - Math.exp(-0.01 * i));
		}
	}

	public static double ticksToV0Rate(int t) {
		if (v0rate == null) {
			return INSTANCE.ticksToV0Rate(t);
		}

		double d;

		if (t >= 0 && t < v0rate.length) {
			d = v0rate[t];
		} else {
			d = 100 * (1 - Math.exp(-0.01 * t));
		}

		return d;
	}

	public static Vec3d getScreenPos(Vec3d pos, float partialTicks) {
		if (pos != null) {
			Entity viewEntity = Minecraft.getMinecraft().getRenderViewEntity();
			double x = pos.xCoord - (viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks);
			double y = pos.yCoord - (viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks) - viewEntity.getEyeHeight();
			double z = pos.zCoord - (viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks);

			if (x == 0.0D && y == 0.0D && z == 0.0D) {
				return null;
			}

			Vec3d look = viewEntity.getLook(partialTicks);

			if (Minecraft.getMinecraft().getRenderManager().options != null && Minecraft.getMinecraft().getRenderManager().options.thirdPersonView == 2) {
				look = look.scale(-1);
			}

			double deg = Math.toDegrees(Math.acos((x * look.xCoord + y * look.yCoord + z * look.zCoord) / (Math.sqrt(x * x + y * y + z * z) * look.lengthVector())));

			if (deg < 90) {
				return ClientUtils.getScreenCoordsFrom3dCoords((float)x, (float)y + viewEntity.getEyeHeight(), (float)z);
			}
		}

		return null;
	}

	public static Vec3d getScreenPos(float yaw, float pitch, float partialTicks) {
		float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
		float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
		float f2 = -MathHelper.cos(-pitch * 0.017453292F);
		float f3 = MathHelper.sin(-pitch * 0.017453292F);
		Vec3d pos = new Vec3d(f1 * f2, f3, f * f2).scale(5.0D);

		if (pos != null) {
			Entity viewEntity = Minecraft.getMinecraft().getRenderViewEntity();
			double x = pos.xCoord;
			double y = pos.yCoord;
			double z = pos.zCoord;

			if (x == 0.0D && y == 0.0D && z == 0.0D) {
				return null;
			}

			Vec3d look = viewEntity.getLook(partialTicks);

			if (Minecraft.getMinecraft().getRenderManager().options != null && Minecraft.getMinecraft().getRenderManager().options.thirdPersonView == 2) {
				look = look.scale(-1);
			}

			double deg = Math.toDegrees(Math.acos((x * look.xCoord + y * look.yCoord + z * look.zCoord) / (Math.sqrt(x * x + y * y + z * z) * look.lengthVector())));

			if (deg < 90) {
				return ClientUtils.getScreenCoordsFrom3dCoords((float)x, (float)y + viewEntity.getEyeHeight(), (float)z);
			}
		}

		return null;
	}

	public static Vec3d getScreenCoordsFrom3dCoords(float x, float y, float z) {
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
		FloatBuffer projection = BufferUtils.createFloatBuffer(16);
		FloatBuffer screenCoords = BufferUtils.createFloatBuffer(4);

		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

		boolean result = GLU.gluProject(x, y, z, modelview, projection, viewport, screenCoords);

		if (result) {
			return new Vec3d(screenCoords.get(0) / 2.0D, (Minecraft.getMinecraft().displayHeight - screenCoords.get(1)) / 2.0D, 0);
		} else {
			return null;
		}
	}

	public static RayTraceResult getMouseOver(double distance, float partialTicks) {
		if (distance < 0.0D) {
			return null;
		}

		Minecraft mc = Minecraft.getMinecraft();
		World world = mc.theWorld;
		Entity viewEntity = mc.getRenderViewEntity();
		Entity pointedEntity = null;
		RayTraceResult objectMouseOver = null;

		if (viewEntity != null && world != null) {
			pointedEntity = null;
			Vec3d vec3EntityPos = null;
			Vec3d vec3Eyes = viewEntity.getPositionEyes(partialTicks);
			Vec3d vec3Look = viewEntity.getLook(partialTicks);
			Vec3d vec3Reach = vec3Eyes.add(vec3Look.scale(distance));

			double d0 = distance;
			Vec3d vec3EyesBlock = viewEntity.getPositionEyes(partialTicks);
			Vec3d vec3ReachBlock = vec3EyesBlock.add(vec3Look.scale((d0 > 100.0D) ? 100.0D : d0));

	        for (int c = (int)Math.ceil(d0 / 100.0D); c > 0; c--) {
	        	objectMouseOver = world.rayTraceBlocks(vec3EyesBlock, vec3ReachBlock, true, false, true);
	        	vec3EyesBlock = vec3ReachBlock;

	        	if (objectMouseOver != null) {
	        		if (objectMouseOver.typeOfHit != RayTraceResult.Type.MISS) {
	        			break;
	        		} else if (objectMouseOver.hitVec != null) {
	        			vec3EyesBlock = objectMouseOver.hitVec;
	        		}
	        	}

	        	d0 -= 100.0D;
	        	vec3ReachBlock = vec3ReachBlock.add(vec3Look.scale((d0 > 100.0D) ? 100.0D : d0));
	        }

	        double d1 = distance;

			if (objectMouseOver != null) {
				d1 = objectMouseOver.hitVec.distanceTo(vec3Eyes);
			}

			float f = 1.0F;
			List<Entity> list = world.getEntitiesInAABBexcluding(viewEntity,
					viewEntity.getEntityBoundingBox().addCoord(vec3Look.xCoord * d1, vec3Look.yCoord * d1, vec3Look.zCoord * d1).expand(f, f, f),
					Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
						@Override
						public boolean apply(@Nullable Entity entity) {
							return entity != null && entity.canBeCollidedWith();
						}
					}));
			double d2 = d1;

			for (Entity entity1 : list) {
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expandXyz(entity1.getCollisionBorderSize());
				RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3Eyes, vec3Reach);

				if (axisalignedbb.isVecInside(vec3Eyes)) {
					if (d2 >= 0.0D) {
						pointedEntity = entity1;
						vec3EntityPos = raytraceresult == null ? vec3Eyes : raytraceresult.hitVec;
						d2 = 0.0D;
					}
				} else if (raytraceresult != null) {
					double d3 = vec3Eyes.distanceTo(raytraceresult.hitVec);

					if (d3 < d2 || d2 == 0.0D) {
						if (entity1.getLowestRidingEntity() == viewEntity.getLowestRidingEntity() && !viewEntity.canRiderInteract()) {
							if (d2 == 0.0D) {
								pointedEntity = entity1;
								vec3EntityPos = raytraceresult.hitVec;
							}
						} else {
							pointedEntity = entity1;
							vec3EntityPos = raytraceresult.hitVec;
							d2 = d3;
						}
					}
				}
			}

			if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
				objectMouseOver = new RayTraceResult(pointedEntity, vec3EntityPos);
			}
		}

		return objectMouseOver;
	}

}
