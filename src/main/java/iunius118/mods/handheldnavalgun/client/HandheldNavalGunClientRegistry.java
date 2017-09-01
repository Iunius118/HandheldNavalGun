package iunius118.mods.handheldnavalgun.client;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import iunius118.mods.handheldnavalgun.HandheldNavalGun;
import iunius118.mods.handheldnavalgun.client.model.ModelBakedItemOBJ;
import iunius118.mods.handheldnavalgun.client.renderer.RenderEntityProjectile127mmAntiAircraftCommon;
import iunius118.mods.handheldnavalgun.client.renderer.RenderItemGun127mmType89Single;
import iunius118.mods.handheldnavalgun.entity.EntityProjectile127mmAntiAircraftCommon;
import iunius118.mods.handheldnavalgun.tileentity.TileEntityItemGun127mmType89Single;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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

public class HandheldNavalGunClientRegistry
{
    /* Models */

    @SideOnly(Side.CLIENT)
    public static class ModelLocations
    {
        public static final ModelResourceLocation MRL_ITEM_GUN_127MM_TYPE89_SINGLE = new ModelResourceLocation(HandheldNavalGun.MOD_ID + ":gun_127mm_type89_1", "inventory");
        public static final ResourceLocation RL_OBJ_ITEM_GUN_127MM_TYPE89_SINGLE = new ResourceLocation(HandheldNavalGun.MOD_ID + ":item/gun_127mm_type89_1.obj");

        public static final ModelResourceLocation MRL_ITEM_ROUND_127MM_AAC = new ModelResourceLocation(HandheldNavalGun.MOD_ID + ":round_127mm_aac", "inventory");
    }

    @SideOnly(Side.CLIENT)
    public static class TextureLocations
    {
        public static final ResourceLocation TEX_MOB_CREW = new ResourceLocation(HandheldNavalGun.MOD_ID, "entity/mob_crew");
        public static final ResourceLocation TEX_MOB_CREW_MAIN_HAND = new ResourceLocation(HandheldNavalGun.MOD_ID, "entity/mob_crew_mh");
        public static final ResourceLocation TEX_MOB_CREW_OFF_HAND = new ResourceLocation(HandheldNavalGun.MOD_ID, "entity/mob_crew_oh");
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemModels()
    {
        ModelLoader.setCustomModelResourceLocation(
                HandheldNavalGun.ITEMS.GUN_127MM_TYPE89_SINGLE, 0,
                HandheldNavalGunClientRegistry.ModelLocations.MRL_ITEM_GUN_127MM_TYPE89_SINGLE);
        ModelLoader.setCustomModelResourceLocation(
                HandheldNavalGun.ITEMS.ROUND_127MM_AAC, 0,
                HandheldNavalGunClientRegistry.ModelLocations.MRL_ITEM_ROUND_127MM_AAC);
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenderers()
    {
        ForgeHooksClient.registerTESRItemStack(HandheldNavalGun.ITEMS.GUN_127MM_TYPE89_SINGLE, 0, TileEntityItemGun127mmType89Single.class);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemGun127mmType89Single.class, new RenderItemGun127mmType89Single());

        RenderingRegistry.registerEntityRenderingHandler(
                EntityProjectile127mmAntiAircraftCommon.class,
                renderManager -> new RenderEntityProjectile127mmAntiAircraftCommon<>(renderManager));
    }

    @SideOnly(Side.CLIENT)
    public static void registerSprites(TextureStitchEvent.Pre event)
    {
        event.getMap()
                .registerSprite(HandheldNavalGunClientRegistry.TextureLocations.TEX_MOB_CREW);
        event.getMap().registerSprite(HandheldNavalGunClientRegistry.TextureLocations.TEX_MOB_CREW_MAIN_HAND);
        event.getMap().registerSprite(HandheldNavalGunClientRegistry.TextureLocations.TEX_MOB_CREW_OFF_HAND);
    }

    @SideOnly(Side.CLIENT)
    public static void registerBakedModels(ModelBakeEvent event)
    {
        IBakedModel modelItem = event.getModelRegistry().getObject(HandheldNavalGunClientRegistry.ModelLocations.MRL_ITEM_GUN_127MM_TYPE89_SINGLE);
        OBJBakedModel modelOBJ = null;

        try
        {
            IModel model = ModelLoaderRegistry.getModel(HandheldNavalGunClientRegistry.ModelLocations.RL_OBJ_ITEM_GUN_127MM_TYPE89_SINGLE);

            HandheldNavalGun.INSTANCE.modelGunPartMainHand.registerModel(
                    (OBJModel) ((OBJModel) model).retexture(ImmutableMap.of("#" + HandheldNavalGunClientRegistry.TextureLocations.TEX_MOB_CREW.toString(), HandheldNavalGunClientRegistry.TextureLocations.TEX_MOB_CREW_MAIN_HAND.toString())));
            HandheldNavalGun.INSTANCE.modelGunPartOffHand.registerModel(
                    (OBJModel) ((OBJModel) model).retexture(ImmutableMap.of("#" + HandheldNavalGunClientRegistry.TextureLocations.TEX_MOB_CREW.toString(), HandheldNavalGunClientRegistry.TextureLocations.TEX_MOB_CREW_OFF_HAND.toString())));

            Function<ResourceLocation, TextureAtlasSprite> spriteGetter = resource -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(resource.toString());
            modelOBJ = (OBJBakedModel) model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM, spriteGetter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (modelItem != null && modelOBJ != null)
        {
            event.getModelRegistry().putObject(HandheldNavalGunClientRegistry.ModelLocations.MRL_ITEM_GUN_127MM_TYPE89_SINGLE, new ModelBakedItemOBJ(modelItem, modelOBJ));
        }
    }

}
