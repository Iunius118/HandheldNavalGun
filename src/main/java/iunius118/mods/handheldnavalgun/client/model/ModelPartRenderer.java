package iunius118.mods.handheldnavalgun.client.model;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Optional;
import com.google.common.collect.UnmodifiableIterator;

import iunius118.mods.handheldnavalgun.client.renderer.RenderItemGun127mmType89Single.RenderContext;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelPartRenderer
{

    public String name;
    public List<BakedQuad> quads;
    public List<ModelPartRenderer> children = Collections.emptyList();
    public Vec3d offset;
    public Consumer<Pair<ModelPartRenderer, RenderContext>> renderPart = context -> context.getKey().renderPart(context);

    public ModelPartRenderer(@Nullable List<BakedQuad> listQuads, String partName, double offsetX, double offsetY, double offsetZ)
    {
        this.quads = (listQuads != null) ? listQuads : Collections.emptyList();
        this.name = partName;
        this.offset = new Vec3d(offsetX, offsetY, offsetZ);
    }

    public static void renderPart(Pair<ModelPartRenderer, RenderContext> context)
    {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();

        vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

        for (BakedQuad quad : context.getKey().quads)
        {
            LightUtil.renderQuadColor(vertexbuffer, quad, -1);
        }

        tessellator.draw();
    }

    public void doRender(RenderContext context)
    {
        GlStateManager.pushMatrix();

        this.renderPart.accept(Pair.of(this, context));

        for (ModelPartRenderer child : children)
        {
            child.doRender(context);
        }

        GlStateManager.popMatrix();
    }

    public static List<BakedQuad> getPartQuads(OBJModel obj, final List<String> visibleGroups)
    {
        List<BakedQuad> quads = Collections.emptyList();

        try
        {
            // ModelState for handling visibility of each group.
            IModelState modelState = part -> {
                if (part.isPresent())
                {
                    UnmodifiableIterator<String> parts = Models.getParts(part.get());

                    if (parts.hasNext())
                    {
                        String name = parts.next();

                        if (!parts.hasNext() && visibleGroups.contains(name))
                        {
                            // Return Absent for NOT invisible group.
                            return Optional.absent();
                        }
                        else
                        {
                            // Return Present for invisible group.
                            return Optional.of(TRSRTransformation.identity());
                        }
                    }
                }

                return Optional.absent();
            };

            // Bake model of visible groups.
            IBakedModel bakedModel = obj.bake(modelState, DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());

            quads = bakedModel.getQuads(null, null, 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return quads;
    }

}
