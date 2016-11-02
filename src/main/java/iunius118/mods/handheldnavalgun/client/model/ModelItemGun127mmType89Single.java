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
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelItemGun127mmType89Single {

	public ModelPartRenderer root = new ModelPartRenderer(null, 0, 0, 0);

	public ModelPartRenderer handle;
	public ModelPartRenderer turret;
	public ModelPartRenderer slide ;
	public ModelPartRenderer platform;
	public ModelPartRenderer rammer;
	public ModelPartRenderer tray  ;
	public ModelPartRenderer cartridge;
	public ModelPartRenderer shell ;
	public ModelPartRenderer gun   ;
	public ModelPartRenderer breech;

	public ModelPartRenderer crew1;
	public ModelPartRenderer skirt1;
	public ModelPartRenderer legL1;
	public ModelPartRenderer legR1;
	public ModelPartRenderer body1;
	public ModelPartRenderer armL1;
	public ModelPartRenderer armR1;
	public ModelPartRenderer head1;
	public ModelPartRenderer eyeL1;
	public ModelPartRenderer eyeR1;
	public ModelPartRenderer tailB1;
	public ModelPartRenderer tailL1;
	public ModelPartRenderer tailR1;

	public ModelPartRenderer crew2;
	public ModelPartRenderer skirt2;
	public ModelPartRenderer legL2;
	public ModelPartRenderer legR2;
	public ModelPartRenderer body2;
	public ModelPartRenderer armL2;
	public ModelPartRenderer armR2;
	public ModelPartRenderer head2;
	public ModelPartRenderer eyeL2;
	public ModelPartRenderer eyeR2;
	public ModelPartRenderer tailB2;
	public ModelPartRenderer tailL2;
	public ModelPartRenderer tailR2;

	public final static float LOWEST_PITCH = 8;

	public void registerModel(OBJModel obj) {
		// Register Group Models
		handle	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Handle")), 0, 0, 0);
		turret	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Turret")), 0, 0, 0);
		slide 	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Slide")), 0, 0.234375, 0.03125);
		platform	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Platform")), 0.078125, 0.265625, 0.0625);
		rammer	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Rammer")), 0, 0, 0);
		tray  	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Tray")), 0.03125, 0.203125, 0.125);
		cartridge	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Cartridge")), 0.03125, 0.203125, 0.125);
		shell 	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Shell")), 0, 0, 0);
		gun   	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Gun")), 0, 0, 0);
		breech	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Breech")), 0, 0, 0);

		crew1	= new ModelPartRenderer(null, 0, 0, 0);
		skirt1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Skirt1")), 0, 0, 0);
		legL1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("LegLeft1")), 0, 0, 0);
		legR1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("LegRight1")), 0, 0, 0);
		body1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Body1")), 0, 0, 0);
		armL1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("ArmLeft1")), 0, 0, 0);
		armR1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("ArmRight1")), 0, 0, 0);
		head1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Head1", "Hair1", "ChignonBack1", "ChignonLeft1", "ChignonRight1")), 0, 0, 0);
		eyeL1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("EyeLaft1")), 0, 0, 0);
		eyeR1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("EyeRight1")), 0, 0, 0);
		tailB1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Ponytail1")), 0, 0, 0);
		tailL1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("SidetailLeft1")), 0, 0, 0);
		tailR1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("SidetailRight1")), 0, 0, 0);

		crew2	= new ModelPartRenderer(null, 0, 0, 0);
		skirt2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Skirt2")), 0, 0, 0);
		legL2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("LegLeft2")), 0, 0, 0);
		legR2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("LegRight2")), 0, 0, 0);
		body2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Body2")), 0, 0, 0);
		armL2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("ArmLeft2")), 0, 0, 0);
		armR2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("ArmRight2")), 0, 0, 0);
		head2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Head2", "Hair2", "ChignonBack2", "ChignonLeft2", "ChignonRight2")), 0, 0, 0);
		eyeL2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("EyeLaft2")), 0, 0, 0);
		eyeR2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("EyeRight2")), 0, 0, 0);
		tailB2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Ponytail2")), 0, 0, 0);
		tailL2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("SidetailLeft2")), 0, 0, 0);
		tailR2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("SidetailRight2")), 0, 0, 0);

		// Register Group Trees
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

		// Register Renderers
		this.registerGunMountRenderer();
	}

	public void registerGunMountRenderer() {
		/*
		 * 80-75 recoil and draw rammer
		 * 75-60 counter-recoil (75-70 release breech, 75-65 eject cartridge)
		 * 60-45 set round (60-50) on tray (50-)
		 * 45-35 push tray
		 * 35-15 load round and push rammer
		 * 15-05 draw tray
		 * 05-02 close breech
		 */

		handle.renderPart = context -> {
			if (context.getValue().isThirdPersonView) {
				GlStateManager.translate(0, 0, 0.0625);
			}

			context.getKey().renderPart(context);
		};

		rammer.renderPart = context -> {
			// render only on the player
			if (context.getValue().isThePlayer) {
				int t = context.getValue().reloadTick;
				float rt = t - Animation.getPartialTickTime();

				if (t <= 1) {
					// on breech
					GlStateManager.translate(0, 0, -0.09375);
				} else if (t > 75) {
					// 80-75 draw by recoil
					double d = rt - 75.0;
					GlStateManager.translate(0.015625, 0, -d / 160.0 - 0.0625);
				} else if (t > 60) {
					// 75-60 draw by counter-recoil
					double d = rt - 60.0;
					GlStateManager.translate(0.015625, 0, -d / 240.0);
				} else if (t > 35) {
					// 60-35 drawn
					GlStateManager.translate(0.015625, 0, 0);
				} else if (t > 15) {
					// 35-15 push to load
					double d = 20.0 - (rt - 15.0);
					GlStateManager.translate(0.015625, 0, -d * 3.0 / 640.0);
				} else if (t > 3) {
					// 15-03 loaded
					GlStateManager.translate(0.015625, 0, -0.09375);
				} else if (t > 1) {
					// 03-01 pushed by breech-block
					double d = rt - 1.0;
					GlStateManager.translate(d / 128.0, 0, -0.09375);
				}

				context.getKey().renderPart(context);
			}
		};

		tray.renderPart = context -> {
			if (context.getValue().isThePlayer) {
				int t = context.getValue().reloadTick;
				float rt = t - Animation.getPartialTickTime();

				if (t <= 1 || t > 45) {
					// 80-45 home position
				} else if (t > 35) {
					// 45-35 push
					float d = 10 - (rt - 35);
					Vec3d offset = context.getKey().offset;
					GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);
					GlStateManager.rotate(d * 9, 0, 0, 1);
					GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
				} else if (t > 15) {
					// 35-15 loading
					Vec3d offset = context.getKey().offset;
					GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);
					GlStateManager.rotate(90, 0, 0, 1);
					GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
				} else if (t > 5) {
					// 15-5 draw back
					float d = rt - 5;
					Vec3d offset = context.getKey().offset;
					GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);
					GlStateManager.rotate(d * 9, 0, 0, 1);
					GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
				}
			}

			context.getKey().renderPart(context);
		};

		cartridge.renderPart = context -> {
			// render only on the player
			if (context.getValue().isThePlayer) {
				int t = context.getValue().reloadTick;
				float rt = t - Animation.getPartialTickTime();
				boolean isRendering = false;

				if (t <= 1 || t > 75) {
					// 80-75 not render
				} else if (t > 65) {
					// 75-65 eject empty cartridge
					double d = 10.0 - (rt - 65.0);
					GlStateManager.translate(-0.0625, -d / 32.0, -0.04);
					isRendering = true;
				} else if (t > 50) {
					// 65-50 not render
				} else if (t > 35) {
					// 50-35 on tray
					isRendering = true;
				} else if (t > 15) {
					// 35-15 loading
					double d = 20.0 - (rt - 15.0);
					GlStateManager.translate(0, 0, -d / 256.0);
					isRendering = true;
				} else if (t > 5) {
					// 15-05 loaded, during drawing back tray
					float d = 10 - (rt - 5);
					Vec3d offset = context.getKey().offset;
					GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord - 0.078125);
					GlStateManager.rotate(d * 9, 0, 0, 1);
					GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
					isRendering = true;
				} else if (t > 1) {
					// 05-01 loaded, during closing breech-block
					Vec3d offset = context.getKey().offset;
					GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord - 0.078125);
					GlStateManager.rotate(90, 0, 0, 1);
					GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
					isRendering = true;
				}

				if (isRendering) context.getKey().renderPart(context);
			}
		};

		shell.renderPart = context -> {
			// render only on the player
			if (context.getValue().isThePlayer) {
				int t = context.getValue().reloadTick;
				float rt = t - Animation.getPartialTickTime();

				if (t <= 1 || t > 50) {
					// 80-50 not render
				} else if (t > 35) {
					// 50-35 on tray
					context.getKey().renderPart(context);
				} else if (t > 15) {
					// 35-15 loading
					double d = 20.0 - (rt - 15.0);
					GlStateManager.translate(0, 0, -d / 256.0);
					context.getKey().renderPart(context);
				}
			}
		};

		gun.renderPart = context -> {
			if (context.getValue().isThePlayer) {
				int t = context.getValue().reloadTick;
				float rt = t - Animation.getPartialTickTime();

				if (t <= 60) {
					// 60-01 home position
				} else if (t > 75) {
					// 80-75 recoil
					double d = 5.0 - (rt - 75.0);
					GlStateManager.translate(0, 0, d / 160.0);
				} else if (t > 60) {
					// 75-60 counter-recoil
					GlStateManager.translate(0, 0, (rt - 60.0) / 480.0);
				}
			}

			context.getKey().renderPart(context);
		};

		breech.renderPart = context -> {
			if (context.getValue().isThePlayer) {
				int t = context.getValue().reloadTick;
				float rt = t - Animation.getPartialTickTime();
				boolean isRendering = false;

				if (t <= 1 || t > 75) {
					// 80-75 closed
					isRendering = true;
				} else if (t > 70) {
					// 75-70 opening
					double d = 5.0 - (rt - 70.0);
					GlStateManager.translate(d / 160.0, 0, 0);
					isRendering = true;
				} else if (t > 5) {
					// 70-05 opened, not render
				} else if (t > 1) {
					// 05-01 closing
					double d = rt - 1.0;
					GlStateManager.translate(d / 128.0, 0, 0);
					isRendering = true;
				}

				if (isRendering) context.getKey().renderPart(context);

			} else {
				context.getKey().renderPart(context);
			}
		};
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
