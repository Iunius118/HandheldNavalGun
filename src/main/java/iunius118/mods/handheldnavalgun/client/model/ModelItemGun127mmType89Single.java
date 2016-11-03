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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelItemGun127mmType89Single {

	public ModelPartRenderer root = new ModelPartRenderer(null, "root", 0, 0, 0);

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
		// Register Model Parts
		this.handle	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Handle")), "handle", 0, -0.09375, 0);
		this.turret	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Turret")), "turret", 0, 0, 0);
		this.slide 	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Slide")), "slide", 0, 0.234375, 0.03125);
		this.platform	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Platform")), "platform", 0.078125, 0.265625, 0.0625);
		this.rammer	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Rammer")), "rammer", -0.03125, 0.21875, 0.1875);
		this.tray  	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Tray")), "tray", 0.03125, 0.203125, 0.125);
		this.cartridge	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Cartridge")), "cartridge", 0.03125, 0.203125, 0.125);
		this.shell 	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Shell")), "shell", 0.03125, 0.203125, 0.125);
		this.gun   	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Gun")), "gun", 0, 0.234375, 0.03125);
		this.breech	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Breech")), "breech", 0, 0.234375, 0.109375);

		this.crew1	= new ModelPartRenderer(null, "crew1", -0.125, 0.125, 0.015625);
		this.skirt1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Skirt1")), "skirt1", -0.125, 0.125, 0.015625);
		this.legL1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("LegLeft1")), "legL1", -0.1328125, 0.170625, 0.015625);
		this.legR1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("LegRight1")), "legR1", -0.1171875, 0.170625, 0.015625);
		this.body1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Body1")), "body1", -0.125, 0.170625, 0.015625);
		this.armL1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("ArmLeft1")), "armL1", -0.1455, 0.2011715, 0.015625);
		this.armR1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("ArmRight1")), "armR1", -0.1047, 0.2011715, 0.015625);
		this.head1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Head1", "Hair1", "ChignonBack1", "ChignonLeft1", "ChignonRight1")), "head1", -0.125, 0.20625, 0.015625);
		this.eyeL1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("EyeLaft1")), "eyeL1", -0.125, 0.20625, 0.015625);
		this.eyeR1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("EyeRight1")), "eyeR1", -0.125, 0.20625, 0.015625);
		this.tailB1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Ponytail1")), "tailB1", -0.125, 0.23265, 0.04625);
		this.tailL1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("SidetailLeft1")), "tailL1", -0.15, 0.23265, 0.025);
		this.tailR1	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("SidetailRight1")), "tailR1", -0.1, 0.23265, 0.025);

		this.crew2	= new ModelPartRenderer(null, "crew2", 0.109375, 0.15625, 0.15625);
		this.skirt2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Skirt2")), "skirt2", 0.109375, 0.2125, 0.15625);
		this.legL2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("LegLeft2")), "legL2", 0.109375, 0.201875, 0.1640625);
		this.legR2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("LegRight2")), "legR2", 0.109375, 0.201875, 0.1484375);
		this.body2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Body2")), "body2", 0.109375, 0.201875, 0.15625);
		this.armL2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("ArmLeft2")), "armL2", 0.109375, 0.2324215, 0.17675);
		this.armR2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("ArmRight2")), "armR2", 0.109375, 0.2324215, 0.13595);
		this.head2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Head2", "Hair2", "ChignonBack2", "ChignonLeft2", "ChignonRight2")), "head2", 0.109375, 0.2375, 0.15625);
		this.eyeL2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("EyeLaft2")), "eyeL2", 0.109375, 0.2375, 0.15625);
		this.eyeR2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("EyeRight2")), "eyeR2", 0.109375, 0.2375, 0.15625);
		this.tailB2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("Ponytail2")), "tailB2", 0.14, 0.2639, 0.15625);
		this.tailL2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("SidetailLeft2")), "tailL2", 0.11875, 0.2668, 0.18125);
		this.tailR2	= new ModelPartRenderer(this.getPartQuads(obj, ImmutableList.of("SidetailRight2")), "tailR2", 0.11875, 0.2668, 0.13125);

		// Register Group Trees
		this.root.children = ImmutableList.of(this.handle);
		this.handle.children	= ImmutableList.of(this.turret);
		this.turret.children	= ImmutableList.of(this.slide, this.crew1);
		this.slide.children	= ImmutableList.of(this.rammer, this.tray, this.gun, this.platform);
		this.tray.children	= ImmutableList.of(this.cartridge, this.shell);
		this.gun.children	= ImmutableList.of(this.breech);
		this.platform.children	= ImmutableList.of(this.crew2);

		this.crew1.children	= ImmutableList.of(this.legL1, this.legR1, this.body1, this.skirt1);
		this.body1.children	= ImmutableList.of(this.armL1, this.armR1, this.head1);
		this.head1.children	= ImmutableList.of(this.eyeL1, this.eyeR1, this.tailB1, this.tailL1, this.tailR1);

		this.crew2.children	= ImmutableList.of(this.legL2, this.legR2, this.body2, this.skirt2);
		this.body2.children	= ImmutableList.of(this.armL2, this.armR2, this.head2);
		this.head2.children	= ImmutableList.of(this.eyeL2, this.eyeR2, this.tailB2, this.tailL2, this.tailR2);

		// Register Renderers
		this.registerRendererGunMount();
		this.registerRendererCrewA();
		this.registerRendererCrewB();
	}

	/* Reload Animation
	 *
	 * 80-75 recoil and draw rammer
	 * 75-60 counter-recoil (75-70 release breech, 75-65 eject cartridge)
	 * 60-45 set round (60-50) on tray (50-)
	 * 45-35 push tray
	 * 35-15 load round and push rammer
	 * 15-05 draw tray
	 * 05-02 close breech
	 */

	public void registerRendererGunMount() {
		this.handle.renderPart = context -> {
			if (context.getValue().isThirdPersonView) {
				GlStateManager.translate(0, 0, 0.0625);
			}

			context.getKey().renderPart(context);
		};

		this.turret.renderPart = context -> {
			//GlStateManager.translate(-0.6, 0.3, 0.4);
			//GlStateManager.rotate(0, 0, 1, 0);
			context.getKey().renderPart(context);
		};

		this.slide.renderPart = context -> {
			context.getKey().renderPart(context);
		};

		this.rammer.renderPart = context -> {
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

		this.tray.renderPart = context -> {
			if (context.getValue().isThePlayer) {
				int t = context.getValue().reloadTick;
				float rt = t - Animation.getPartialTickTime();

				if (t <= 1 || t > 45) {
					// 80-45 home position
				} else if (t > 35) {
					// 45-35 push
					float f = 10 - (rt - 35);
					Vec3d offset = context.getKey().offset;
					GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);
					GlStateManager.rotate(f * 9, 0, 0, 1);
					GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
				} else if (t > 15) {
					// 35-15 loading
					Vec3d offset = context.getKey().offset;
					GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);
					GlStateManager.rotate(90, 0, 0, 1);
					GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
				} else if (t > 5) {
					// 15-5 draw back
					float f = rt - 5;
					Vec3d offset = context.getKey().offset;
					GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);
					GlStateManager.rotate(f * 9, 0, 0, 1);
					GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
				}
			}

			context.getKey().renderPart(context);
		};

		this.cartridge.renderPart = context -> {
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
					float f = 10 - (rt - 5);
					Vec3d offset = context.getKey().offset;
					GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord - 0.078125);
					GlStateManager.rotate(f * 9, 0, 0, 1);
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

		this.shell.renderPart = context -> {
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

		this.gun.renderPart = context -> {
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

		this.breech.renderPart = context -> {
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

	public void registerRendererCrewA() {
		// Crew A
		this.crew1.renderPart = context -> {
			// render crew only on the player
			if (context.getValue().isThePlayer) {
				this.crew1.children = ImmutableList.of(this.legL1, this.legR1, this.body1, this.skirt1);
			} else {
				this.crew1.children = Collections.emptyList();
			}
		};

		this.armL1.renderPart = context -> {
			Vec3d offset = context.getKey().offset;
			float f = MathHelper.sin(Animation.getWorldTime(Minecraft.getMinecraft().theWorld, Animation.getPartialTickTime()) % 4 * (float)Math.PI / 2) * 2;
			GlStateManager.translate(offset.xCoord, offset.yCoord - 0.00625, offset.zCoord);
			GlStateManager.rotate(45 + f, 1, 0, 0);
			GlStateManager.rotate(20, 0, 0, 1);
			GlStateManager.rotate(20, 0, 1, 0);
			GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);

			context.getKey().renderPart(context);
		};

		this.armR1.renderPart = context -> {
			Vec3d offset = context.getKey().offset;
			float f = MathHelper.sin(Animation.getWorldTime(Minecraft.getMinecraft().theWorld, Animation.getPartialTickTime()) % 4 * (float)Math.PI / 2) * 2;
			GlStateManager.translate(offset.xCoord, offset.yCoord - 0.00625, offset.zCoord);
			GlStateManager.rotate(45 + f, 1, 0, 0);
			GlStateManager.rotate(-20, 0, 0, 1);
			GlStateManager.rotate(-20, 0, 1, 0);
			GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);

			context.getKey().renderPart(context);
		};

		this.eyeL1.renderPart = context -> {
			int t = context.getValue().reloadTick;

			if (context.getValue().isBlinkingA() || t > 75) {
				context.getKey().renderPart(context);
			}
		};

		this.eyeR1.renderPart = context -> {
			int t = context.getValue().reloadTick;

			if (context.getValue().isBlinkingA() || t > 75) {
				context.getKey().renderPart(context);
			}
		};
	}

	public void registerRendererCrewB() {
		// Crew B
		this.crew2.renderPart = context -> {
			// render crew only on the player
			if (context.getValue().isThePlayer) {
				this.crew2.children = ImmutableList.of(this.legL2, this.legR2, this.body2, this.skirt2);

				int t = context.getValue().reloadTick;
				float rt = t - Animation.getPartialTickTime();
				Vec3d offset = context.getKey().offset;

				if (t <= 5 || t > 45) {
					// 80-45 home position
				} else if (t > 35) {
					// 45-35 push tray
					float f = 10 - (rt - 35);
					GlStateManager.translate(offset.xCoord - f / 480, offset.yCoord + f / 3200, offset.zCoord);
					GlStateManager.rotate(f * 2, 0, 0, 1);
					GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
				} else if (t > 15) {
					// 35-15 load round
					GlStateManager.translate(offset.xCoord - 0.0208333, offset.yCoord + 0.003125, offset.zCoord);
					GlStateManager.rotate(20, 0, 0, 1);
					GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
				} else if (t > 5) {
					// 15-05 draw tray
					float f = rt - 5;
					GlStateManager.translate(offset.xCoord - f / 480, offset.yCoord + f / 3200, offset.zCoord);
					GlStateManager.rotate(f * 2, 0, 0, 1);
					GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
				}
			} else {
				this.crew2.children = Collections.emptyList();
			}
		};

		this.legL2.renderPart = context -> {
			int t = context.getValue().reloadTick;
			float rt = t - Animation.getPartialTickTime();
			Vec3d offset = context.getKey().offset;

			if (t <= 5 || t > 45) {
				// 80-45 home position
			} else if (t > 35) {
				// 45-35 push tray
				float f = 10 - (rt - 35);
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);
				GlStateManager.rotate(f * 2, 0, 0, 1);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 15) {
				// 35-15 load round
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);
				GlStateManager.rotate(20, 0, 0, 1);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 5) {
				// 15-05 draw tray
				float f = rt - 5;
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);
				GlStateManager.rotate(f * 2, 0, 0, 1);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			}

			context.getKey().renderPart(context);
		};

		this.legR2.renderPart = context -> {
			int t = context.getValue().reloadTick;
			float rt = t - Animation.getPartialTickTime();
			Vec3d offset = context.getKey().offset;

			if (t <= 5 || t > 45) {
				// 80-45 home position
			} else if (t > 35) {
				// 45-35 push tray
				float f = 10 - (rt - 35);
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);
				GlStateManager.rotate(-f, 0, 0, 1);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 15) {
				// 35-15 load round
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);
				GlStateManager.rotate(-10, 0, 0, 1);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 5) {
				// 15-05 draw tray
				float f = rt - 5;
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);
				GlStateManager.rotate(-f, 0, 0, 1);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			}

			context.getKey().renderPart(context);
		};

		this.body2.renderPart = context -> {
			int t = context.getValue().reloadTick;
			float rt = t - Animation.getPartialTickTime();
			Vec3d offset = context.getKey().offset;

			if (t <= 5 || t > 45) {
				// 80-45 home position
			} else if (t > 35) {
				// 45-35 push tray
				float f = 10 - (rt - 35);
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);
				GlStateManager.rotate(f * 2, 0, 0, 1);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 15) {
				// 35-15 load round
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);
				GlStateManager.rotate(20, 0, 0, 1);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 5) {
				// 15-05 draw tray
				float f = rt - 5;
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);
				GlStateManager.rotate(f * 2, 0, 0, 1);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			}

			context.getKey().renderPart(context);
		};

		this.armL2.renderPart = context -> {
			int t = context.getValue().reloadTick;
			float rt = t - Animation.getPartialTickTime();
			Vec3d offset = context.getKey().offset;

			if (t <= 1 || t > 60) {
				// 80-60 home position
				float f = MathHelper.sin(Animation.getWorldTime(Minecraft.getMinecraft().theWorld, Animation.getPartialTickTime()) % 4 * (float)Math.PI / 2) * 2;
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord - 0.003125);
				GlStateManager.rotate(-15 + f, 1, 0, 0);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 50) {
				// 60-50 set round
				float f = 10 - (rt - 50);
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord - 0.003125);
				GlStateManager.rotate(-f * 11, 0, 0, 1);
				GlStateManager.rotate(-5 - (rt - 50), 1, 0, 0);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 45) {
				// 50-45 hold tray
				float f = (rt - 45);
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord - 0.003125);
				GlStateManager.rotate(-90 - f * 4, 0, 0, 1);
				GlStateManager.rotate(-5, 1, 0, 0);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 35) {
				// 45-35 push tray
				float f = 10 - (rt - 35);
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord - 0.003125);
				GlStateManager.rotate(-90 - f * 8, 0, 0, 1);
				GlStateManager.rotate(-5, 1, 0, 0);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 15) {
				// 35-15 load round
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord - 0.003125);
				GlStateManager.rotate(-170, 0, 0, 1);
				GlStateManager.rotate(-5, 1, 0, 0);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 5) {
				// 15-05 draw tray
				float f = rt - 5;
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord - 0.003125);
				GlStateManager.rotate(-90 - f * 8, 0, 0, 1);
				GlStateManager.rotate(-5, 1, 0, 0);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 1) {
				// 05-01 return home position
				float f = rt - 1;
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord - 0.003125);
				GlStateManager.rotate(f * -22.5F, 0, 0, 1);
				GlStateManager.rotate(-5 - (5 - rt) * 2.5F, 1, 0, 0);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			}

			context.getKey().renderPart(context);
		};

		this.armR2.renderPart = context -> {
			int t = context.getValue().reloadTick;
			float rt = t - Animation.getPartialTickTime();
			Vec3d offset = context.getKey().offset;

			if (t <= 1 || t > 60) {
				// 80-60 home position
				float f = MathHelper.sin(Animation.getWorldTime(Minecraft.getMinecraft().theWorld, Animation.getPartialTickTime()) % 4 * (float)Math.PI / 2) * 2;
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord + 0.003125);
				GlStateManager.rotate(15 - f, 1, 0, 0);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 50) {
				// 60-50 set round
				float f = 10 - (rt - 50);
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord + 0.003125);
				GlStateManager.rotate(-f * 11, 0, 0, 1);
				GlStateManager.rotate(5 + (rt - 50), 1, 0, 0);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 45) {
				// 50-45 hold tray
				float f = (rt - 45);
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord + 0.003125);
				GlStateManager.rotate(-90 - f * 4, 0, 0, 1);
				GlStateManager.rotate(5, 1, 0, 0);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 35) {
				// 45-35 push tray
				float f = 10 - (rt - 35);
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord + 0.003125);
				GlStateManager.rotate(-90 - f * 8, 0, 0, 1);
				GlStateManager.rotate(5, 1, 0, 0);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 15) {
				// 35-15 load round
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord + 0.003125);
				GlStateManager.rotate(-170, 0, 0, 1);
				GlStateManager.rotate(5, 1, 0, 0);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 5) {
				// 15-05 draw tray
				float f = rt - 5;
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord + 0.003125);
				GlStateManager.rotate(-90 - f * 8, 0, 0, 1);
				GlStateManager.rotate(5, 1, 0, 0);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 1) {
				// 05-01 return home position
				float f = rt - 1;
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord + 0.003125);
				GlStateManager.rotate(f * -22.5F, 0, 0, 1);
				GlStateManager.rotate(5 + (5 - rt) * 2.5F, 1, 0, 0);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			}

			context.getKey().renderPart(context);
		};

		this.head2.renderPart = context -> {
			int t = context.getValue().reloadTick;
			float rt = t - Animation.getPartialTickTime();
			Vec3d offset = context.getKey().offset;

			if (t <= 5 || t > 45) {
				// 80-45 home position
			} else if (t > 35) {
				// 45-35 push tray
				float f = 10 - (rt - 35);
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);
				GlStateManager.rotate(-f * 4, 0, 0, 1);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 15) {
				// 35-15 load round
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);
				GlStateManager.rotate(-40, 0, 0, 1);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			} else if (t > 5) {
				// 15-05 draw tray
				float f = rt - 5;
				GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);
				GlStateManager.rotate(-f * 4, 0, 0, 1);
				GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			}

			context.getKey().renderPart(context);
		};

		this.eyeL2.renderPart = context -> {
			int t = context.getValue().reloadTick;

			if (context.getValue().isBlinkingB() || t > 75 || (t < 59 && t > 51) || (t < 44 && t > 36)) {
				context.getKey().renderPart(context);
			}
		};

		this.eyeR2.renderPart = context -> {
			int t = context.getValue().reloadTick;

			if (context.getValue().isBlinkingB() || t > 75 || (t < 59 && t > 51) || (t < 44 && t > 36)) {
				context.getKey().renderPart(context);
			}
		};
	}

	public static class ModelPartRenderer {

		public String name;
		public List<BakedQuad> quads;
		public List<ModelPartRenderer> children = Collections.emptyList();
		public Vec3d offset;
		public Consumer<Pair<ModelPartRenderer, RenderItemGun127mmType89Single.RenderContext>> renderPart = context -> context.getKey().renderPart(context);

		public ModelPartRenderer(@Nullable List<BakedQuad> listQuads, String partName, double offsetX, double offsetY, double offsetZ) {
			this.quads = (listQuads != null) ? listQuads : Collections.emptyList();
			this.name = partName;
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
