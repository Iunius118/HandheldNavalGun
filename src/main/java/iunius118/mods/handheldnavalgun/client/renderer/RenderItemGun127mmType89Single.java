package iunius118.mods.handheldnavalgun.client.renderer;

import iunius118.mods.handheldnavalgun.HandheldNavalGun;
import iunius118.mods.handheldnavalgun.capability.CapabilityReloadTime;
import iunius118.mods.handheldnavalgun.client.model.ModelBakedItemOBJ;
import iunius118.mods.handheldnavalgun.item.ItemGun127mmType89Single;
import iunius118.mods.handheldnavalgun.tileentity.TileEntityItemGun127mmType89Single;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHandSide;
import net.minecraft.world.World;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.common.util.Constants.NBT;

public class RenderItemGun127mmType89Single extends TileEntitySpecialRenderer<TileEntityItemGun127mmType89Single> {

	@Override
	public void renderTileEntityAt(TileEntityItemGun127mmType89Single te, double x, double y, double z, float partialTicks, int destroyStage) {
		if(te != null) return;

		Minecraft mc = Minecraft.getMinecraft();
		ModelBakedItemOBJ model = (ModelBakedItemOBJ)mc.getRenderItem().getItemModelMesher().getModelManager().getModel(HandheldNavalGun.ModelLocations.MRL_ITEM_GUN_127MM_TYPE89_SINGLE);
		RenderContext context = new RenderContext(model.item, model.player, model.transformTypeCamera);

		GlStateManager.popMatrix();

		if (context.isMainHand) {
			HandheldNavalGun.INSTANCE.modelGunPartMainHand.root.doRender(context);
		} else {
			HandheldNavalGun.INSTANCE.modelGunPartOffHand.root.doRender(context);
		}

		GlStateManager.pushMatrix();
	}

	public static class RenderContext {

		public TransformType transformTypeCamera;
		public int reloadTick;
		public boolean isThePlayer;
		public boolean isFirstPersonView;
		public boolean isThirdPersonView;
		public boolean isRightHand;
		public boolean isMainHand;
		public boolean isReloadable;
		public float playerPitch;
		public ItemStack stack;

		public RenderContext(ItemStack item, EntityLivingBase entity, TransformType cameraTransformType) {
			this.stack = item;
			this.transformTypeCamera = cameraTransformType;
			this.isFirstPersonView = false;
			this.isThirdPersonView = false;
			this.isRightHand = false;
			this.isMainHand = false;
			this.isReloadable = false;

			switch (cameraTransformType) {
			case FIRST_PERSON_LEFT_HAND:
				this.isFirstPersonView = true;
				break;
			case FIRST_PERSON_RIGHT_HAND:
				this.isFirstPersonView = isRightHand = true;
				break;
			case THIRD_PERSON_LEFT_HAND:
				this.isThirdPersonView = true;
				break;
			case THIRD_PERSON_RIGHT_HAND:
				this.isThirdPersonView = isRightHand = true;
				break;
			default:
				break;
			}

			if (item != null) {
				CapabilityReloadTime.IReloadTimeICapability cap = item.getCapability(HandheldNavalGun.Capabilities.getReloadTimeICapability(), null);
				this.reloadTick = (cap != null) ? cap.getReloadTime() : 0;

				NBTTagCompound tag = item.getTagCompound();

				if (tag != null) {
					this.isReloadable = tag.getBoolean(ItemGun127mmType89Single.TAG_IS_RELOADABLE);
				}

			} else {
				this.reloadTick = 0;
			}

			if (entity != null) {
				this.playerPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * Animation.getPartialTickTime();
				this.isThePlayer = (Minecraft.getMinecraft().thePlayer == entity);
				EnumHandSide mainHandSide = entity.getPrimaryHand();

				if ((mainHandSide == EnumHandSide.RIGHT && this.isRightHand) || (mainHandSide == EnumHandSide.LEFT && !this.isRightHand)) {
					this.isMainHand = true;
				}
			} else {
				this.playerPitch = 0;
				this.isThePlayer = false;
			}

		}

		public boolean isBlinkingA() {
			World world = Minecraft.getMinecraft().theWorld;
			if (stack != null && stack.hasTagCompound()) {
				NBTTagCompound tag = stack.getTagCompound();
				long time = world.getTotalWorldTime();

				if (tag.hasKey("blink1", NBT.TAG_LONG)) {
					long blink = tag.getLong("blink1");

					if (blink < time) {
						tag.setLong("blink1", time + world.rand.nextInt(120) + 21);
					} else if (blink == time) {
						return true;
					}
				} else {
					tag.setLong("blink1", time + world.rand.nextInt(120) + 21);
				}
			}

			return false;
		}

		public boolean isBlinkingB() {
			World world = Minecraft.getMinecraft().theWorld;
			if (stack != null && stack.hasTagCompound()) {
				NBTTagCompound tag = stack.getTagCompound();
				long time = world.getTotalWorldTime();

				if (tag.hasKey("blink2", NBT.TAG_LONG)) {
					long blink = tag.getLong("blink2");

					if (blink < time) {
						tag.setLong("blink2", time + world.rand.nextInt(120) + 21);
					} else if (blink == time) {
						return true;
					}
				} else {
					tag.setLong("blink2", time + world.rand.nextInt(120) + 21);
				}
			}

			return false;
		}

	}

}
