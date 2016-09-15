package iunius118.mods.handheldnavalgun;

import iunius118.mods.handheldnavalgun.client.Target;
import iunius118.mods.handheldnavalgun.client.util.ClientUtils;
import iunius118.mods.handheldnavalgun.entity.EntityProjectile127mmAntiAircraftCommon;
import iunius118.mods.handheldnavalgun.item.ItemGun127mmType89Single;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@Mod(modid = HandheldNavalGun.MOD_ID,
	name = HandheldNavalGun.MOD_NAME,
	version = HandheldNavalGun.MOD_VERSION,
	dependencies = HandheldNavalGun.MOD_DEPENDENCIES,
	acceptedMinecraftVersions = HandheldNavalGun.MOD_ACCEPTED_MC_VERSIONS,
	useMetadata = true)
public class HandheldNavalGun {

	public static final String MOD_ID = "handheldnavalgun";
	public static final String MOD_NAME = "HandheldNavalGun";
	public static final String MOD_VERSION = "0.0.1";
	public static final String MOD_DEPENDENCIES = "required-after:Forge@[1.9.4-12.17.0.1976,)";
	public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.9.4,]";

	@Mod.Instance(MOD_ID)
	public static HandheldNavalGun INSTANCE;

	public Target target = null;
	public Vec3d vec3Target = null;
	public Vec3d vec3Marker = null;
	public int ticksFuse = EntityProjectile127mmAntiAircraftCommon.FUSE_MAX;


	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		HandheldNavalGunRegistry.registerItems();
		HandheldNavalGunRegistry.resisterEntities();

		if (event.getSide().isClient()) {
			OBJLoader.INSTANCE.addDomain(MOD_ID);
			MinecraftForge.EVENT_BUS.register(this);
			HandheldNavalGunRegistry.registerItemModels();
		}
	}

	public static class Items {
		public static final String NAME_ITEM_GUN_127MM_TYPE89_SINGLE = "handheldnavalgun.gun_127mm_type89_1";
		public static final Item GUN_127MM_TYPE89_SINGLE  = new ItemGun127mmType89Single()
				.setRegistryName(HandheldNavalGun.Items.NAME_ITEM_GUN_127MM_TYPE89_SINGLE)
				.setUnlocalizedName(HandheldNavalGun.Items.NAME_ITEM_GUN_127MM_TYPE89_SINGLE)
				.setCreativeTab(CreativeTabs.COMBAT);
	}

	@SideOnly(Side.CLIENT)
	public static class ModelLocations {
		public static final ModelResourceLocation MRL_ITEM_GUN_127MM_TYPE89_SINGLE = new ModelResourceLocation(MOD_ID + ":gun_127mm_type89_1", "inventory");

		public static final ResourceLocation RL_OBJ_ITEM_GUN_127MM_TYPE89_SINGLE = new ResourceLocation(MOD_ID + ":item/gun_127mm_type89_1.obj");
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		HandheldNavalGunRegistry.registerBakedModels(event);
		HandheldNavalGunRegistry.registerRenderers();
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
		this.vec3Target = null;
		this.vec3Marker = null;
		this.ticksFuse = EntityProjectile127mmAntiAircraftCommon.FUSE_MAX;

		if (Minecraft.getMinecraft().getRenderManager().options != null && Minecraft.getMinecraft().getRenderManager().options.thirdPersonView > 0) {
			return;
		}

		if (this.target != null) {
			Vec3d pos = this.target.getPos(Minecraft.getMinecraft().theWorld, event.getPartialTicks());

			if (pos != null) {
				int fuseMax = EntityProjectile127mmAntiAircraftCommon.FUSE_MAX;
				double initialVelocity = EntityProjectile127mmAntiAircraftCommon.INITIAL_VELOCITY;
				this.vec3Target = ClientUtils.getScreenPos(pos, event.getPartialTicks());
				this.vec3Marker = ClientUtils.getTargetFutureScreenPos(Minecraft.getMinecraft().theWorld, this.target, fuseMax, initialVelocity, event.getPartialTicks());
			} else {
				this.target = null;
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

			if (vec3Target != null) {
				double x = this.vec3Target.xCoord;
				double y = this.vec3Target.yCoord;
				vertexbuffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
				vertexbuffer.pos(x - markerSize, y - markerSize, 0.0D).endVertex();
				vertexbuffer.pos(x + markerSize, y - markerSize, 0.0D).endVertex();
				vertexbuffer.pos(x + markerSize, y + markerSize, 0.0D).endVertex();
				vertexbuffer.pos(x - markerSize, y + markerSize, 0.0D).endVertex();
				tessellator.draw();
			}

			if (vec3Marker != null) {
				double x = this.vec3Marker.xCoord;
				double y = this.vec3Marker.yCoord;
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
