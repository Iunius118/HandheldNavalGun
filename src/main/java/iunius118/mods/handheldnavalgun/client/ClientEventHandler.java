package iunius118.mods.handheldnavalgun.client;

import org.lwjgl.opengl.GL11;

import iunius118.mods.handheldnavalgun.HandheldNavalGun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {

	public static final ClientEventHandler INSTANCE = new ClientEventHandler();

	@SubscribeEvent
	public void onClientTickEvent(ClientTickEvent event) {
		if (event.phase == ClientTickEvent.Phase.END) {
			return;
		}

		RangeKeeperGun127mmType89 rangeKeeper = HandheldNavalGun.INSTANCE.rangeKeeper;
		rangeKeeper.setFuseMax();

		if (rangeKeeper.getTarget() != null) {
			rangeKeeper.updatetFutureTarget(Minecraft.getMinecraft().theWorld);
		}
	}

	@SubscribeEvent
	public void onTextureStitchEvent(TextureStitchEvent.Pre event) {
		HandheldNavalGunClientRegistry.registerSprites(event);
	}

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		HandheldNavalGunClientRegistry.registerBakedModels(event);
		HandheldNavalGunClientRegistry.registerRenderers();
	}

	@SubscribeEvent
	public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		HandheldNavalGun.INSTANCE.vec3Target = null;
		HandheldNavalGun.INSTANCE.vec3Marker = null;

		if (mc.getRenderManager().options != null && mc.getRenderManager().options.thirdPersonView > 0) {
			return;
		}

		RangeKeeperGun127mmType89 rangeKeeper = HandheldNavalGun.INSTANCE.rangeKeeper;
		float partialTicks = event.getPartialTicks();

		if (rangeKeeper.getTarget() != null) {
			HandheldNavalGun.INSTANCE.vec3Target = rangeKeeper.getTargetScreenPos(mc.theWorld, partialTicks);
			HandheldNavalGun.INSTANCE.vec3Marker = rangeKeeper.getTargetFutureScreenPos(mc.theWorld, partialTicks);
		}
	}

	@SubscribeEvent
	public void onRenderGameOverlayEventPre(RenderGameOverlayEvent.Pre event) {
		if (event.getType() == ElementType.HOTBAR && Minecraft.getMinecraft().getRenderManager().getFontRenderer() != null) {
			if (HandheldNavalGun.INSTANCE.vec3Target == null && HandheldNavalGun.INSTANCE.vec3Marker == null) {
				return;
			}

			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer vertexbuffer = tessellator.getBuffer();
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
