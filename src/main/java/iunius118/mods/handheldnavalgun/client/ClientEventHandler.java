package iunius118.mods.handheldnavalgun.client;

import org.lwjgl.opengl.GL11;

import iunius118.mods.handheldnavalgun.HandheldNavalGun;
import iunius118.mods.handheldnavalgun.client.gunfirecontrolsystem.GunFireControlSystemGun127mmType89;
import iunius118.mods.handheldnavalgun.item.ItemGun127mmType89Single;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
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
public class ClientEventHandler
{

    public static final ClientEventHandler INSTANCE = new ClientEventHandler();

    @SubscribeEvent
    public void onClientTickEvent(ClientTickEvent event)
    {
        if (event.phase == ClientTickEvent.Phase.END)
        {
            return;
        }

        GunFireControlSystemGun127mmType89 gfcs = HandheldNavalGun.INSTANCE.gunFireControlSystem;
        World world = Minecraft.getMinecraft().theWorld;
        gfcs.updateDirectorAndComputer(world);
    }

    @SubscribeEvent
    public void onTextureStitchEvent(TextureStitchEvent.Pre event)
    {
        HandheldNavalGunClientRegistry.registerSprites(event);
    }

    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event)
    {
        HandheldNavalGunClientRegistry.registerBakedModels(event);
        HandheldNavalGunClientRegistry.registerRenderers();
    }

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event)
    {
        GunFireControlSystemGun127mmType89 gfcs = HandheldNavalGun.INSTANCE.gunFireControlSystem;
        World world = Minecraft.getMinecraft().theWorld;
        float partialTicks = event.getPartialTicks();
        gfcs.updateIndicator(world, partialTicks);
    }

    @SubscribeEvent
    public void onRenderGameOverlayEventPre(RenderGameOverlayEvent.Pre event)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (event.getType() == ElementType.HOTBAR && mc.getRenderManager().getFontRenderer() != null && mc.getRenderManager().options.thirdPersonView < 1)
        {
            Item itemMainHand = null;

            if(mc.thePlayer.getHeldItem(EnumHand.MAIN_HAND) != null)
            {
                itemMainHand = mc.thePlayer.getHeldItem(EnumHand.MAIN_HAND).getItem();
            }

            if(!(itemMainHand instanceof ItemGun127mmType89Single))
            {
                return;
            }

            GunFireControlSystemGun127mmType89 gfcs = HandheldNavalGun.INSTANCE.gunFireControlSystem;

            Vec3d vec3Target = gfcs.indicator.getTargetScreenPos();
            Vec3d vec3Marker = gfcs.indicator.getTargetFutureScreenPos();

            if (vec3Target == null && vec3Marker == null)
            {
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

            if (vec3Target != null)
            {
                double x = vec3Target.xCoord;
                double y = vec3Target.yCoord;
                vertexbuffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
                vertexbuffer.pos(x - markerSize, y - markerSize, 0.0D).endVertex();
                vertexbuffer.pos(x + markerSize, y - markerSize, 0.0D).endVertex();
                vertexbuffer.pos(x + markerSize, y + markerSize, 0.0D).endVertex();
                vertexbuffer.pos(x - markerSize, y + markerSize, 0.0D).endVertex();
                tessellator.draw();
            }

            if (vec3Marker != null)
            {
                double x = vec3Marker.xCoord;
                double y = vec3Marker.yCoord;
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
