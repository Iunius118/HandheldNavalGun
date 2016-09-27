package iunius118.mods.handheldnavalgun;

import iunius118.mods.handheldnavalgun.capability.CapabilityReloadTime;
import iunius118.mods.handheldnavalgun.client.renderer.RenderEntityProjectile127mmAntiAircraftCommon;
import iunius118.mods.handheldnavalgun.client.renderer.RenderItemGun127mmType89Single;
import iunius118.mods.handheldnavalgun.entity.EntityProjectile127mmAntiAircraftCommon;
import iunius118.mods.handheldnavalgun.tileentity.TileEntityItemGun127mmType89Single;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class HandheldNavalGunRegistry {

	public static void resisterEntities() {
		EntityRegistry.registerModEntity(EntityProjectile127mmAntiAircraftCommon.class, "entity_projectile_127mm_anti_aircraft_common", EntityID.PROJECTILE_127MM_ANTI_AIRCRAFT_COMMON.ordinal(), HandheldNavalGun.INSTANCE, 256, 5, true);
	}

	public enum EntityID {
		PROJECTILE_127MM_ANTI_AIRCRAFT_COMMON,
	}

	public static void registerCapabilities() {
		CapabilityManager.INSTANCE.register(CapabilityReloadTime.IReloadTimeICapability.class, new CapabilityReloadTime.Storage(), CapabilityReloadTime.DefaultImpl.class);
	}

	public static void registerItems() {
		GameRegistry.register(HandheldNavalGun.Items.GUN_127MM_TYPE89_SINGLE);
		registerItemRecipes();
	}

	public static void registerItemRecipes() {
		GameRegistry.addRecipe(
				new ShapedOreRecipe(
						new ItemStack(HandheldNavalGun.Items.GUN_127MM_TYPE89_SINGLE),
						" i ",
						"isi",
						"grc",
						'i', "ingotIron",
						's', "slimeball",
						'g', "paneGlass",
						'r', "dustRedstone",
						'c', Items.CLOCK)
				);
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemModels() {
		ModelLoader.setCustomModelResourceLocation(HandheldNavalGun.Items.GUN_127MM_TYPE89_SINGLE, 0, HandheldNavalGun.ModelLocations.MRL_ITEM_GUN_127MM_TYPE89_SINGLE);
	}

	@SideOnly(Side.CLIENT)
	public static void registerRenderers() {
		ForgeHooksClient.registerTESRItemStack(HandheldNavalGun.Items.GUN_127MM_TYPE89_SINGLE, 0, TileEntityItemGun127mmType89Single.class);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemGun127mmType89Single.class, new RenderItemGun127mmType89Single());

		RenderingRegistry.registerEntityRenderingHandler(EntityProjectile127mmAntiAircraftCommon.class, new RenderEntityProjectile127mmAntiAircraftCommon.RenderFactory<EntityProjectile127mmAntiAircraftCommon>());
	}

	@SideOnly(Side.CLIENT)
	public static void registerBakedModels(ModelBakeEvent event) {

	}

}
