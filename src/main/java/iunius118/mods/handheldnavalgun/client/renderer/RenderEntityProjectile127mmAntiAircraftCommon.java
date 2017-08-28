package iunius118.mods.handheldnavalgun.client.renderer;

import java.util.List;

import org.lwjgl.opengl.GL11;

import iunius118.mods.handheldnavalgun.entity.EntityProjectile127mmAntiAircraftCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.pipeline.LightUtil;

public class RenderEntityProjectile127mmAntiAircraftCommon<T extends EntityProjectile127mmAntiAircraftCommon> extends Render<T>
{

    public RenderEntityProjectile127mmAntiAircraftCommon(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

        this.bindEntityTexture(entity);

        // transform
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 0.125F, (float) z);
        GlStateManager.rotate(entityYaw - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks - 90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(entity.spin, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(0.25F, 1.0F, 0.25F);
        GlStateManager.translate(-0.5F, -1.0F, -0.5F);

        // draw faces except bottom
        for (EnumFacing face : EnumFacing.VALUES)
        {
            if (face == EnumFacing.DOWN)
                continue;

            List<BakedQuad> quads = mesher.getItemModel(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.RED.getMetadata())).getQuads(null, face, 0L);
            int size = quads.size();

            vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

            for (int i = 0; i < size; ++i)
            {
                LightUtil.renderQuadColor(vertexbuffer, quads.get(i), -1);
            }

            tessellator.draw();
        }

        // draw bottom face (tracer)
        List<BakedQuad> quads = mesher.getItemModel(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.RED.getMetadata())).getQuads(null, EnumFacing.DOWN, 0L);
        int size = quads.size();

        GlStateManager.disableLighting();

        float lastBrightnessX = OpenGlHelper.lastBrightnessX;
        float lastBrightnessY = OpenGlHelper.lastBrightnessY;
        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

        vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

        for (int i = 0; i < size; ++i)
        {
            LightUtil.renderQuadColor(vertexbuffer, quads.get(i), -1);
        }

        tessellator.draw();

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
        GL11.glPopAttrib();

        // draw tracer point
        GlStateManager.disableTexture2D();

        GL11.glPointSize(1.5F);
        vertexbuffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(0.5F, -0.1F, 0.5F).color(255, 64, 64, 255).endVertex();
        tessellator.draw();
        GL11.glPointSize(1.0F);

        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();

        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

}
