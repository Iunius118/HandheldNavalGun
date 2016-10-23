package iunius118.mods.handheldnavalgun.client.model;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;

import iunius118.mods.handheldnavalgun.client.renderer.RenderItemGun127mmType89Single;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelItemGun127mmType89Single {

	public ModelPartRenderer root = new ModelPartRenderer(null, 0, 0, 0);

	public final static float LOWEST_PITCH = 8;

	public void registerModel(OBJModel obj) {
		// Register Group Model
		ModelPartRenderer handle	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Handle")), 0, 0, 0);
		ModelPartRenderer turret	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Turret")), 0, 0, 0);
		ModelPartRenderer slide	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Slide")), 0, 0.234375, 0.03125);
		ModelPartRenderer platform	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Platform")), 0.078125, 0.265625, 0.0625);
		ModelPartRenderer rammer	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Rammer")), 0, 0, 0);
		ModelPartRenderer tray	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Tray")), 0, 0, 0);
		ModelPartRenderer cartridge	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Cartridge")), 0, 0, 0);
		ModelPartRenderer shell	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Shell")), 0, 0, 0);
		ModelPartRenderer gun	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Gun")), 0, 0, 0);
		ModelPartRenderer breech	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Breech")), 0, 0, 0);

		ModelPartRenderer crew1	= new ModelPartRenderer(null, 0, 0, 0);
		ModelPartRenderer skirt1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Skirt1")), 0, 0, 0);
		ModelPartRenderer legL1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("LegLeft1")), 0, 0, 0);
		ModelPartRenderer legR1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("LegRight1")), 0, 0, 0);
		ModelPartRenderer body1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Body1")), 0, 0, 0);
		ModelPartRenderer armL1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("ArmLeft1")), 0, 0, 0);
		ModelPartRenderer armR1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("ArmRight1")), 0, 0, 0);
		ModelPartRenderer head1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Head1", "Hair1", "ChignonBack1", "ChignonLeft1", "ChignonRight1")), 0, 0, 0);
		ModelPartRenderer eyeL1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("EyeLaft1")), 0, 0, 0);
		ModelPartRenderer eyeR1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("EyeRight1")), 0, 0, 0);
		ModelPartRenderer tailB1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Ponytail1")), 0, 0, 0);
		ModelPartRenderer tailL1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("SidetailLeft1")), 0, 0, 0);
		ModelPartRenderer tailR1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("SidetailRight1")), 0, 0, 0);

		ModelPartRenderer crew2	= new ModelPartRenderer(null, 0, 0, 0);
		ModelPartRenderer skirt2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Skirt2")), 0, 0, 0);
		ModelPartRenderer legL2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("LegLeft2")), 0, 0, 0);
		ModelPartRenderer legR2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("LegRight2")), 0, 0, 0);
		ModelPartRenderer body2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Body2")), 0, 0, 0);
		ModelPartRenderer armL2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("ArmLeft2")), 0, 0, 0);
		ModelPartRenderer armR2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("ArmRight2")), 0, 0, 0);
		ModelPartRenderer head2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Head2", "Hair2", "ChignonBack2", "ChignonLeft2", "ChignonRight2")), 0, 0, 0);
		ModelPartRenderer eyeL2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("EyeLaft2")), 0, 0, 0);
		ModelPartRenderer eyeR2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("EyeRight2")), 0, 0, 0);
		ModelPartRenderer tailB2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Ponytail2")), 0, 0, 0);
		ModelPartRenderer tailL2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("SidetailLeft2")), 0, 0, 0);
		ModelPartRenderer tailR2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("SidetailRight2")), 0, 0, 0);

		// Register Group Tree
		this.root = handle;
		handle.children	= ImmutableList.of(turret);
		turret.children	= ImmutableList.of(slide, crew1);
		slide.children	= ImmutableList.of(rammer, tray, gun, platform);
		tray.children	= ImmutableList.of(cartridge, shell);
		gun.children	= ImmutableList.of(breech);
		platform.children	= ImmutableList.of(crew2);

		crew1.children	= ImmutableList.of(legL1, legR1, body1, skirt1);
		body1.children	= ImmutableList.of(armL1, armR1, head1);
		head1.children	= ImmutableList.of(eyeL1, eyeR1, tailB1, tailL1, tailR1);

		crew2.children	= ImmutableList.of(legL2, legR2, body2, skirt2);
		body2.children	= ImmutableList.of(armL2, armR2, head2);
		head2.children	= ImmutableList.of(eyeL2, eyeR2, tailB2, tailL2, tailR2);

		// Register Renderer
		handle.renderPart = context -> {
			if (context.getValue().isThirdPersonView) {
				GlStateManager.translate(0, 0, 0.0625);
			}

			context.getKey().renderPart(context);	};
	}

	public static class ModelPartRenderer {

		public List<BakedQuad> quads;
		public List<ModelPartRenderer> children = Collections.emptyList();
		public Vec3d offset;
		public Consumer<Pair<ModelPartRenderer, RenderItemGun127mmType89Single.RenderContext>> renderPart = context -> context.getKey().renderPart(context);

		public ModelPartRenderer(@Nullable List<BakedQuad> listQuads, double offsetX, double offsetY, double offsetZ) {
			this.quads = (listQuads != null) ? listQuads : Collections.emptyList();
			this.offset = new Vec3d(offsetX, offsetY, offsetZ);
		}

		public static void renderPart(Pair<ModelPartRenderer, RenderItemGun127mmType89Single.RenderContext> context) {
			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer vertexbuffer = tessellator.getBuffer();

			vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

			for (BakedQuad quad : context.getKey().quads) {
				LightUtil.renderQuadColor(vertexbuffer, quad, -1);
			}

			tessellator.draw();
		}

		public void doRender(RenderItemGun127mmType89Single.RenderContext context) {
			GlStateManager.pushMatrix();

			this.renderPart.accept(Pair.of(this, context));

			for (ModelPartRenderer child : children) {
				child.doRender(context);
			}

			GlStateManager.popMatrix();
		}

	}

	public List<BakedQuad> getPartQuads(OBJModel obj, final List<String> visibleGroups) {
		List<BakedQuad> quads = Collections.emptyList();

		try {
			Function<ResourceLocation, TextureAtlasSprite> spriteGetter = resource -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(resource.toString());

			// ModelState for handling visibility of each group.
			IModelState modelState = part -> {
				if (part.isPresent()) {
					UnmodifiableIterator<String> parts = Models.getParts(part.get());

					if (parts.hasNext()) {
						String name = parts.next();

						if (!parts.hasNext()
								&& visibleGroups.contains(name)) {
							// Return Absent for NOT invisible group.
							return Optional.absent();
						} else {
							// Return Present for invisible group.
							return Optional.of(TRSRTransformation
									.identity());
						}
					}
				}

				return Optional.absent();
			};

			// Bake model of visible groups.
			IBakedModel bakedModel = obj.bake(modelState, DefaultVertexFormats.ITEM, spriteGetter);

			quads = bakedModel.getQuads(null, null, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return quads;
	}

}
