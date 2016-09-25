package iunius118.mods.handheldnavalgun.client;

import org.lwjgl.opengl.GL11;

import iunius118.mods.handheldnavalgun.HandheldNavalGun;
import iunius118.mods.handheldnavalgun.HandheldNavalGunRegistry;
import iunius118.mods.handheldnavalgun.client.util.ClientUtils;
import iunius118.mods.handheldnavalgun.entity.EntityProjectile127mmAntiAircraftCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientEventHandler {
	public static final ClientEventHandler INSTANCE = new ClientEventHandler();

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		HandheldNavalGunRegistry.registerBakedModels(event);
		HandheldNavalGunRegistry.registerRenderers();
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
		HandheldNavalGun.INSTANCE.vec3Target = null;
		HandheldNavalGun.INSTANCE.vec3Marker = null;
		HandheldNavalGun.INSTANCE.ticksFuse = EntityProjectile127mmAntiAircraftCommon.FUSE_MAX;

		if (Minecraft.getMinecraft().getRenderManager().options != null && Minecraft.getMinecraft().getRenderManager().options.thirdPersonView > 0) {
			return;
		}

		if (HandheldNavalGun.INSTANCE.target != null) {
			Vec3d pos = HandheldNavalGun.INSTANCE.target.getPos(Minecraft.getMinecraft().theWorld, event.getPartialTicks());

			if (pos != null) {
				HandheldNavalGun.INSTANCE.vec3Target = ClientUtils.getScreenPos(pos, event.getPartialTicks());
				HandheldNavalGun.INSTANCE.vec3Marker = RangeKeeperGun127mmType89.getTargetFutureScreenPos(Minecraft.getMinecraft().theWorld, HandheldNavalGun.INSTANCE.target, event.getPartialTicks());
			} else {
				HandheldNavalGun.INSTANCE.target = null;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderGameOverlayEventPre(RenderGameOverlayEvent.Pre event) {
		if (event.getType() == ElementType.HOTBAR && Minecraft.getMinecraft().getRenderManager().getFontRenderer() != null) {
			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer vertexbuffer = tessellator.getBuffer();
			Minecraft mc = Minecraft.getMinecraft();
			int displayHeight = mc.displayHeight;
			double markerSize = 4.0D;

			GlStateManager.disableDepth();
			GlStateManager.enableBlend();
			GlStateManager.disableTexture2D();
			GlStateManager.color(0.0F, 1.0F, 0.0F, 1.0F);
			GlStateManager.glLineWidth(1.0F);

			if (HandheldNavalGun.INSTANCE.vec3Target != null) {
				double x = HandheldNavalGun.INSTANCE.vec3Target.xCoord;
				double y = HandheldNavalGun.INSTANCE.vec3Target.yCoord;
				vertexbuffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
				vertexbuffer.pos(x - markerSize, y - markerSize, 0.0D).endVertex();
				vertexbuffer.pos(x + markerSize, y - markerSize, 0.0D).endVertex();
				vertexbuffer.pos(x + markerSize, y + markerSize, 0.0D).endVertex();
				vertexbuffer.pos(x - markerSize, y + markerSize, 0.0D).endVertex();
				tessellator.draw();
			}

			if (HandheldNavalGun.INSTANCE.vec3Marker != null) {
				double x = HandheldNavalGun.INSTANCE.vec3Marker.xCoord;
				double y = HandheldNavalGun.INSTANCE.vec3Marker.yCoord;
				vertexbuffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
				vertexbuffer.pos(x, y - markerSize, 0.0D).endVertex();
				vertexbuffer.pos(x + markerSize, y, 0.0D).endVertex();
				vertexbuffer.pos(x, y + markerSize, 0.0D).endVertex();
				vertexbuffer.pos(x - markerSize, y, 0.0D).endVertex();
				tessellator.draw();
			}

			GlStateManager.enableTexture2D();
			GlStateManager.enableDepth();

		}
	}
}
