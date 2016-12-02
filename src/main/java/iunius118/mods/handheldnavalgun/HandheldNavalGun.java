package iunius118.mods.handheldnavalgun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import iunius118.mods.handheldnavalgun.capability.CapabilityReloadTime;
import iunius118.mods.handheldnavalgun.client.ClientEventHandler;
import iunius118.mods.handheldnavalgun.client.HandheldNavalGunClientRegistry;
import iunius118.mods.handheldnavalgun.client.RangeKeeperGun127mmType89;
import iunius118.mods.handheldnavalgun.client.model.ModelItemGun127mmType89Single;
import iunius118.mods.handheldnavalgun.item.ItemGun127mmType89Single;
import iunius118.mods.handheldnavalgun.item.ItemRound127mmAntiAircraftCommon;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = HandheldNavalGun.MOD_ID,
	name = HandheldNavalGun.MOD_NAME,
	version = HandheldNavalGun.MOD_VERSION,
	dependencies = HandheldNavalGun.MOD_DEPENDENCIES,
	acceptedMinecraftVersions = HandheldNavalGun.MOD_ACCEPTED_MC_VERSIONS,
	useMetadata = true)
public class HandheldNavalGun {

	public static final String MOD_ID = "handheldnavalgun";
	public static final String MOD_NAME = "HandheldNavalGun";
	public static final String MOD_VERSION = "0.0.4-beta";
	public static final String MOD_DEPENDENCIES = "required-after:Forge@[1.10.2-12.18.2.2099,)";
	public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.10.2,]";

	@Mod.Instance(MOD_ID)
	public static HandheldNavalGun INSTANCE;

	public Map<String, Integer> mapShell = new HashMap<>();
	public Item itemCartridge = net.minecraft.init.Items.IRON_INGOT;

	@SideOnly(Side.CLIENT)
	public RangeKeeperGun127mmType89 rangeKeeper;
	@SideOnly(Side.CLIENT)
	public Vec3d vec3Target;
	@SideOnly(Side.CLIENT)
	public Vec3d vec3Marker;
	@SideOnly(Side.CLIENT)
	public ModelItemGun127mmType89Single modelGunPartMainHand;
	@SideOnly(Side.CLIENT)
	public ModelItemGun127mmType89Single modelGunPartOffHand;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		HandheldNavalGunRegistry.registerMessage();
		HandheldNavalGunRegistry.registerCapabilities();
		HandheldNavalGunRegistry.registerItems();
		HandheldNavalGunRegistry.resisterEntities();
		MinecraftForge.EVENT_BUS.register(this);

		if (event.getSide().isClient()) {
			this.rangeKeeper = new RangeKeeperGun127mmType89();
			this.modelGunPartMainHand = new ModelItemGun127mmType89Single();
			this.modelGunPartOffHand = new ModelItemGun127mmType89Single();
			OBJLoader.INSTANCE.addDomain(MOD_ID);
			HandheldNavalGunClientRegistry.registerItemModels();
			MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		List<ItemStack> list = OreDictionary.getOres("ingotBrass");

		if (!list.isEmpty()) {
			this.itemCartridge = list.get(0).getItem();
		}
	}

	public static class PacketHandler {
		public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(HandheldNavalGun.MOD_ID);
	}

	public static class Capabilities {
		@CapabilityInject(CapabilityReloadTime.IReloadTimeICapability.class)
		private static Capability<CapabilityReloadTime.IReloadTimeICapability> RELOAD_TIMEI_CAPABILITY = null;

		public static final String NAME_RELOAD_TIMEI_CAPABILITY = "reload_timei_capability";

		@Nullable
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

		public static final String NAME_ITEM_ROUND_127MM_AAC = "handheldnavalgun.round_127mm_aac";
		public static final Item ROUND_127MM_AAC  = new ItemRound127mmAntiAircraftCommon()
				.setRegistryName(HandheldNavalGun.Items.NAME_ITEM_ROUND_127MM_AAC)
				.setUnlocalizedName(HandheldNavalGun.Items.NAME_ITEM_ROUND_127MM_AAC)
				.setCreativeTab(CreativeTabs.COMBAT);
	}

	@SideOnly(Side.CLIENT)
	public static class ModelLocations {
		public static final ModelResourceLocation MRL_ITEM_GUN_127MM_TYPE89_SINGLE = new ModelResourceLocation(HandheldNavalGun.MOD_ID + ":gun_127mm_type89_1", "inventory");
		public static final ResourceLocation RL_OBJ_ITEM_GUN_127MM_TYPE89_SINGLE = new ResourceLocation(HandheldNavalGun.MOD_ID + ":item/gun_127mm_type89_1.obj");

		public static final ModelResourceLocation MRL_ITEM_ROUND_127MM_AAC = new ModelResourceLocation(HandheldNavalGun.MOD_ID + ":round_127mm_aac", "inventory");
	}

	@SideOnly(Side.CLIENT)
	public static class TextureLocations {
		public static final ResourceLocation TEX_MOB_CREW_MAIN_HAND = new ResourceLocation(HandheldNavalGun.MOD_ID, "entity/mob_crew_mh");
		public static final ResourceLocation TEX_MOB_CREW_OFF_HAND = new ResourceLocation(HandheldNavalGun.MOD_ID, "entity/mob_crew_oh");
	}

	@SubscribeEvent
	public void onItemStackLoad(AttachCapabilitiesEvent.Item event) {
		if (event.getItem() instanceof ItemGun127mmType89Single) {
			event.addCapability(new ResourceLocation(HandheldNavalGun.MOD_ID, Capabilities.NAME_RELOAD_TIMEI_CAPABILITY), new CapabilityReloadTime.Provider());
		}
	}

}
