package iunius118.mods.handheldnavalgun.client.renderer;

import iunius118.mods.handheldnavalgun.HandheldNavalGun;
import iunius118.mods.handheldnavalgun.capability.CapabilityReloadTime;
import iunius118.mods.handheldnavalgun.client.model.ModelBakedItemOBJ;
import iunius118.mods.handheldnavalgun.tileentity.TileEntityItemGun127mmType89Single;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.animation.Animation;

public class RenderItemGun127mmType89Single extends TileEntitySpecialRenderer<TileEntityItemGun127mmType89Single> {

	@Override
	public void renderTileEntityAt(TileEntityItemGun127mmType89Single te, double x, double y, double z, float partialTicks, int destroyStage) {
		if(te != null) return;

		Minecraft mc = Minecraft.getMinecraft();
		ModelBakedItemOBJ model = (ModelBakedItemOBJ)mc.getRenderItem().getItemModelMesher().getModelManager().getModel(HandheldNavalGun.ModelLocations.MRL_ITEM_GUN_127MM_TYPE89_SINGLE);

		GlStateManager.popMatrix();

		HandheldNavalGun.INSTANCE.modelGunPart.root.doRender(new RenderContext(model.item, model.player, model.transformTypeCamera));

		GlStateManager.pushMatrix();
	}

	public static class RenderContext {

		public TransformType transformTypeCamera;
		public int reloadTick;
		public boolean isThePlayer;
		public boolean isFirstPersonView;
		public boolean isThirdPersonView;
		public boolean isLeftHand;
		public float playerPitch;

		public RenderContext(ItemStack item, EntityLivingBase entity, TransformType cameraTransformType) {
			if (item != null) {
				CapabilityReloadTime.IReloadTimeICapability cap = item.getCapability(HandheldNavalGun.Capabilities.getReloadTimeICapability(), null);
				this.reloadTick = (cap != null) ? cap.getReloadTime() : 0;
			} else {
				this.reloadTick = 0;
			}

			if (entity != null) {
				this.playerPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * Animation.getPartialTickTime();
				this.isThePlayer = (Minecraft.getMinecraft().thePlayer == entity);
			} else {
				this.playerPitch = 0;
				this.isThePlayer = false;
			}

			this.transformTypeCamera = cameraTransformType;
			this.isFirstPersonView = false;
			this.isThirdPersonView = false;
			this.isLeftHand = false;

			switch (cameraTransformType) {
			case FIRST_PERSON_LEFT_HAND:
				this.isFirstPersonView = this.isLeftHand = true;
				break;
			case FIRST_PERSON_RIGHT_HAND:
				this.isFirstPersonView = true;
				break;
			case THIRD_PERSON_LEFT_HAND:
				this.isThirdPersonView = this.isLeftHand = true;
				break;
			case THIRD_PERSON_RIGHT_HAND:
				this.isThirdPersonView = true;
				break;
			default:
				break;
			}
		}

	}

}
