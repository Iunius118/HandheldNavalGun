package iunius118.mods.handheldnavalgun;

import iunius118.mods.handheldnavalgun.capability.CapabilityReloadTime;
import iunius118.mods.handheldnavalgun.client.ClientEventHandler;
import iunius118.mods.handheldnavalgun.client.util.Target;
import iunius118.mods.handheldnavalgun.entity.EntityProjectile127mmAntiAircraftCommon;
import iunius118.mods.handheldnavalgun.item.ItemGun127mmType89Single;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = HandheldNavalGun.MOD_ID,
	name = HandheldNavalGun.MOD_NAME,
	version = HandheldNavalGun.MOD_VERSION,
	dependencies = HandheldNavalGun.MOD_DEPENDENCIES,
	acceptedMinecraftVersions = HandheldNavalGun.MOD_ACCEPTED_MC_VERSIONS,
	useMetadata = true)
public class HandheldNavalGun {

	public static final String MOD_ID = "handheldnavalgun";
	public static final String MOD_NAME = "HandheldNavalGun";
	public static final String MOD_VERSION = "0.0.1 beta";
	public static final String MOD_DEPENDENCIES = "required-after:Forge@[1.10.2-12.18.1.2011,)";
	public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.10.2,]";

	@Mod.Instance(MOD_ID)
	public static HandheldNavalGun INSTANCE;

	public Target target = null;
	public Vec3d vec3Target = null;
	public Vec3d vec3Marker = null;
	public int ticksFuse = EntityProjectile127mmAntiAircraftCommon.FUSE_MAX;


	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		HandheldNavalGunRegistry.registerCapabilities();
		HandheldNavalGunRegistry.registerItems();
		HandheldNavalGunRegistry.resisterEntities();
		MinecraftForge.EVENT_BUS.register(this);

		if (event.getSide().isClient()) {
			OBJLoader.INSTANCE.addDomain(MOD_ID);
			HandheldNavalGunRegistry.registerItemModels();
			MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
		}
	}

	public static class Capabilities {
		@CapabilityInject(CapabilityReloadTime.IReloadTimeICapability.class)
		private static Capability<CapabilityReloadTime.IReloadTimeICapability> RELOAD_TIMEI_CAPABILITY = null;

		public static final String NAME_RELOAD_TIMEI_CAPABILITY = "reload_timei_capability";

		public static Capability<CapabilityReloadTime.IReloadTimeICapability> getReloadTimeICapability() {
			return RELOAD_TIMEI_CAPABILITY;
		}
	}

	public static class Items {
		public static final String NAME_ITEM_GUN_127MM_TYPE89_SINGLE = "handheldnavalgun.gun_127mm_type89_1";
		public static final Item GUN_127MM_TYPE89_SINGLE  = new ItemGun127mmType89Single()
				.setRegistryName(HandheldNavalGun.Items.NAME_ITEM_GUN_127MM_TYPE89_SINGLE)
				.setUnlocalizedName(HandheldNavalGun.Items.NAME_ITEM_GUN_127MM_TYPE89_SINGLE)
				.setCreativeTab(CreativeTabs.COMBAT)
				.setMaxStackSize(1);
	}

	@SideOnly(Side.CLIENT)
	public static class ModelLocations {
		public static final ModelResourceLocation MRL_ITEM_GUN_127MM_TYPE89_SINGLE = new ModelResourceLocation(HandheldNavalGun.MOD_ID + ":gun_127mm_type89_1", "inventory");

		//public static final ResourceLocation RL_OBJ_ITEM_GUN_127MM_TYPE89_SINGLE = new ResourceLocation(HandheldNavalGun.MOD_ID + ":item/gun_127mm_type89_1.obj");
	}

	@SubscribeEvent
	public void onItemStackLoad(AttachCapabilitiesEvent.Item event) {
		if (event.getItem() == Items.GUN_127MM_TYPE89_SINGLE) {
			event.addCapability(new ResourceLocation(MOD_ID, Capabilities.NAME_RELOAD_TIMEI_CAPABILITY), new CapabilityReloadTime.Provider());
		}
	}

}
