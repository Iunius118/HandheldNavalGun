package iunius118.mods.handheldnavalgun;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import iunius118.mods.handheldnavalgun.capability.CapabilityReloadTime;
import iunius118.mods.handheldnavalgun.client.ClientEventHandler;
import iunius118.mods.handheldnavalgun.client.RangeKeeperGun127mmType89;
import iunius118.mods.handheldnavalgun.client.model.ModelItemGun127mmType89Single;
import iunius118.mods.handheldnavalgun.item.ItemGun127mmType89Single;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
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
	public static final String MOD_VERSION = "0.0.1 beta";
	public static final String MOD_DEPENDENCIES = "required-after:Forge@[1.10.2-12.18.1.2011,)";
	public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.10.2,]";

	@Mod.Instance(MOD_ID)
	public static HandheldNavalGun INSTANCE;

	public final RangeKeeperGun127mmType89 rangeKeeper = new RangeKeeperGun127mmType89();
	public Vec3d vec3Target = null;
	public Vec3d vec3Marker = null;
	public final ModelItemGun127mmType89Single modelGunPart = new ModelItemGun127mmType89Single();
	public Item itemCartridge = net.minecraft.init.Items.IRON_INGOT;

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

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		List<ItemStack> list = OreDictionary.getOres("ingotBrass");

		if (!list.isEmpty()) {
			this.itemCartridge = list.get(0).getItem();
		}
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

		public static final String NAME_ITEM_ROUND_127MM_TYPE0_AAC = "handheldnavalgun.round_127mm_type0_aac";
		public static final Item ROUND_127MM_TYPE0_AAC  = new Item() {

			@Override
			public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
				Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

				if (slot == EntityEquipmentSlot.MAINHAND) {
					multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(this.ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 5.0D, 0));
				}

				return multimap;
			}

		}
				.setRegistryName(HandheldNavalGun.Items.NAME_ITEM_ROUND_127MM_TYPE0_AAC)
				.setUnlocalizedName(HandheldNavalGun.Items.NAME_ITEM_ROUND_127MM_TYPE0_AAC)
				.setCreativeTab(CreativeTabs.COMBAT);
	}

	@SideOnly(Side.CLIENT)
	public static class ModelLocations {
		public static final ModelResourceLocation MRL_ITEM_GUN_127MM_TYPE89_SINGLE = new ModelResourceLocation(HandheldNavalGun.MOD_ID + ":gun_127mm_type89_1", "inventory");
		public static final ResourceLocation RL_OBJ_ITEM_GUN_127MM_TYPE89_SINGLE = new ResourceLocation(HandheldNavalGun.MOD_ID + ":item/gun_127mm_type89_1.obj");

		public static final ModelResourceLocation MRL_ITEM_ROUND_127MM_TYPE0_AAC = new ModelResourceLocation(HandheldNavalGun.MOD_ID + ":round_127mm_type0_aac", "inventory");
	}

	@SubscribeEvent
	public void onItemStackLoad(AttachCapabilitiesEvent.Item event) {
		if (event.getItem() instanceof ItemGun127mmType89Single) {
			event.addCapability(new ResourceLocation(HandheldNavalGun.MOD_ID, Capabilities.NAME_RELOAD_TIMEI_CAPABILITY), new CapabilityReloadTime.Provider());
		}
	}

}
