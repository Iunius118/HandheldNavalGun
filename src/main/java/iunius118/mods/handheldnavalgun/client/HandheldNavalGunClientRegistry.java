package iunius118.mods.handheldnavalgun.client;

import com.google.common.base.Function;

import iunius118.mods.handheldnavalgun.HandheldNavalGun;
import iunius118.mods.handheldnavalgun.client.model.ModelBakedItemOBJ;
import iunius118.mods.handheldnavalgun.client.renderer.RenderEntityProjectile127mmAntiAircraftCommon;
import iunius118.mods.handheldnavalgun.client.renderer.RenderItemGun127mmType89Single;
import iunius118.mods.handheldnavalgun.entity.EntityProjectile127mmAntiAircraftCommon;
import iunius118.mods.handheldnavalgun.tileentity.TileEntityItemGun127mmType89Single;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.OBJBakedModel;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HandheldNavalGunClientRegistry {

	@SideOnly(Side.CLIENT)
	public static void registerItemModels() {
		ModelLoader.setCustomModelResourceLocation(HandheldNavalGun.Items.GUN_127MM_TYPE89_SINGLE, 0, HandheldNavalGun.ModelLocations.MRL_ITEM_GUN_127MM_TYPE89_SINGLE);
		ModelLoader.setCustomModelResourceLocation(HandheldNavalGun.Items.ROUND_127MM_AAC, 0, HandheldNavalGun.ModelLocations.MRL_ITEM_ROUND_127MM_AAC);
	}

	@SideOnly(Side.CLIENT)
	public static void registerRenderers() {
		ForgeHooksClient.registerTESRItemStack(HandheldNavalGun.Items.GUN_127MM_TYPE89_SINGLE, 0, TileEntityItemGun127mmType89Single.class);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemGun127mmType89Single.class, new RenderItemGun127mmType89Single());

		RenderingRegistry.registerEntityRenderingHandler(EntityProjectile127mmAntiAircraftCommon.class, renderManager -> new RenderEntityProjectile127mmAntiAircraftCommon<>(renderManager));
	}


	@SideOnly(Side.CLIENT)
	public static void registerSprites(TextureStitchEvent.Pre event) {
		event.getMap().registerSprite(new ResourceLocation(HandheldNavalGun.MOD_ID, "entity/mob_crew"));
	}

	@SideOnly(Side.CLIENT)
	public static void registerBakedModels(ModelBakeEvent event) {
		IBakedModel modelItem = event.getModelRegistry().getObject(HandheldNavalGun.ModelLocations.MRL_ITEM_GUN_127MM_TYPE89_SINGLE);
		OBJBakedModel modelOBJ = null;

		try {
			IModel model = ModelLoaderRegistry.getModel(HandheldNavalGun.ModelLocations.RL_OBJ_ITEM_GUN_127MM_TYPE89_SINGLE);
			HandheldNavalGun.INSTANCE.modelGunPart.registerModel((OBJModel)model);
			Function<ResourceLocation, TextureAtlasSprite> spriteGetter = resource -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(resource.toString());
			modelOBJ = (OBJBakedModel)model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM, spriteGetter);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (modelItem != null && modelOBJ != null) {
			event.getModelRegistry().putObject(HandheldNavalGun.ModelLocations.MRL_ITEM_GUN_127MM_TYPE89_SINGLE, new ModelBakedItemOBJ(modelItem, modelOBJ));
		}
	}

}
