package iunius118.mods.handheldnavalgun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import iunius118.mods.handheldnavalgun.capability.CapabilityReloadTime;
import iunius118.mods.handheldnavalgun.client.ClientEventHandler;
import iunius118.mods.handheldnavalgun.client.HandheldNavalGunClientRegistry;
import iunius118.mods.handheldnavalgun.client.gunfirecontrolsystem.GunFireControlSystemGun127mmType89;
import iunius118.mods.handheldnavalgun.client.model.ModelItemGun127mmType89Single;
import iunius118.mods.handheldnavalgun.entity.EntityProjectile127mmAntiAircraftCommon;
import iunius118.mods.handheldnavalgun.item.ItemGun127mmType89Single;
import iunius118.mods.handheldnavalgun.item.ItemRound127mmAntiAircraftCommon;
import iunius118.mods.handheldnavalgun.packet.MessageGunShot;
import iunius118.mods.handheldnavalgun.packet.MessageGunShotHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod    (modid = HandheldNavalGun.MOD_ID, name = HandheldNavalGun.MOD_NAME,
        version = HandheldNavalGun.MOD_VERSION,
        // dependencies = HandheldNavalGun.MOD_DEPENDENCIES,
        acceptedMinecraftVersions = HandheldNavalGun.MOD_ACCEPTED_MC_VERSIONS,
        useMetadata = true)
public class HandheldNavalGun
{

    public static final String MOD_ID = "handheldnavalgun";
    public static final String MOD_NAME = "HandheldNavalGun";
    public static final String MOD_VERSION = "%MOD_VERSION%";
    // public static final String MOD_DEPENDENCIES = "required-after:forge@[1.10.2-12.18.3.2185,)";
    public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.10.2,]";

    @Mod.Instance(MOD_ID)
    public static HandheldNavalGun INSTANCE;

    public Map<String, Integer> mapShell = new HashMap<>();
    public ItemStack itemCartridge = new ItemStack(net.minecraft.init.Items.IRON_INGOT);

    @SideOnly(Side.CLIENT)
    public GunFireControlSystemGun127mmType89 gunFireControlSystem;
    @SideOnly(Side.CLIENT)
    public Vec3d vec3Target;
    @SideOnly(Side.CLIENT)
    public Vec3d vec3Marker;
    @SideOnly(Side.CLIENT)
    public ModelItemGun127mmType89Single modelGunPartMainHand;
    @SideOnly(Side.CLIENT)
    public ModelItemGun127mmType89Single modelGunPartOffHand;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        this.registerMessage();
        this.registerCapabilities();
        this.registerItems();
        this.registerRecipes();
        this.resisterEntities();
        MinecraftForge.EVENT_BUS.register(this);

        if (event.getSide().isClient())
        {
            this.gunFireControlSystem = new GunFireControlSystemGun127mmType89();
            this.modelGunPartMainHand = new ModelItemGun127mmType89Single();
            this.modelGunPartOffHand = new ModelItemGun127mmType89Single();
            OBJLoader.INSTANCE.addDomain(MOD_ID);
            HandheldNavalGunClientRegistry.registerItemModels();
            MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        List<ItemStack> list = OreDictionary.getOres("ingotBrass");

        if (!list.isEmpty())    // if brass ingot exists...
        {
            // save a copy stack of brass ingot for empty cartridge item
            this.itemCartridge = list.get(0).copy();
        }
    }

    /* Packets */

    public static class PacketHandler
    {
        public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(HandheldNavalGun.MOD_ID);
    }

    public void registerMessage()
    {
        PacketHandler.INSTANCE.registerMessage(MessageGunShotHandler.class, MessageGunShot.class, 0, Side.SERVER);
    }

    /* Capabilities */

    public static class Capabilities
    {
        @CapabilityInject(CapabilityReloadTime.IReloadTimeICapability.class)
        private static Capability<CapabilityReloadTime.IReloadTimeICapability> RELOAD_TIMEI_CAPABILITY = null;

        public static final String NAME_RELOAD_TIMEI_CAPABILITY = "reload_timei_capability";

        @Nullable
        public static Capability<CapabilityReloadTime.IReloadTimeICapability> getReloadTimeICapability()
        {
            return RELOAD_TIMEI_CAPABILITY;
        }
    }

    public void registerCapabilities()
    {
        CapabilityManager.INSTANCE.register(
                CapabilityReloadTime.IReloadTimeICapability.class,
                new CapabilityReloadTime.Storage(),
                CapabilityReloadTime.DefaultImpl.class);
    }

    @SubscribeEvent
    public void onItemStackLoad(AttachCapabilitiesEvent.Item event)
    {
        if (event.getItem() instanceof ItemGun127mmType89Single)
        {
            event.addCapability(
                    new ResourceLocation(HandheldNavalGun.MOD_ID, Capabilities.NAME_RELOAD_TIMEI_CAPABILITY),
                    new CapabilityReloadTime.Provider());
        }
    }

    /* Items */

    public static class ITEMS
    {
        public static final String NAME_ITEM_GUN_127MM_TYPE89_SINGLE = "handheldnavalgun.gun_127mm_type89_1";
        public static final String NAME_ITEM_ROUND_127MM_AAC = "handheldnavalgun.round_127mm_aac";

        public static final Item GUN_127MM_TYPE89_SINGLE = new ItemGun127mmType89Single()
                .setRegistryName(HandheldNavalGun.ITEMS.NAME_ITEM_GUN_127MM_TYPE89_SINGLE)
                .setUnlocalizedName(HandheldNavalGun.ITEMS.NAME_ITEM_GUN_127MM_TYPE89_SINGLE)
                .setCreativeTab(CreativeTabs.COMBAT)
                .setMaxStackSize(1);

        public static final Item ROUND_127MM_AAC = new ItemRound127mmAntiAircraftCommon()
                .setRegistryName(HandheldNavalGun.ITEMS.NAME_ITEM_ROUND_127MM_AAC)
                .setUnlocalizedName(HandheldNavalGun.ITEMS.NAME_ITEM_ROUND_127MM_AAC)
                .setCreativeTab(CreativeTabs.COMBAT);
    }

    public void registerItems()
    {
        GameRegistry.register(ITEMS.GUN_127MM_TYPE89_SINGLE);
        GameRegistry.register(ITEMS.ROUND_127MM_AAC);
    }

    /* Recipes */

    public void registerRecipes()
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(HandheldNavalGun.ITEMS.GUN_127MM_TYPE89_SINGLE),
                "ii ",
                "gii",
                "cS ",
                'i', "ingotIron",
                'g', "paneGlass",
                'c', Items.CLOCK,
                'S', Blocks.STICKY_PISTON));

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(HandheldNavalGun.ITEMS.ROUND_127MM_AAC, 16),
                " c ",
                "ITI",
                "ITI",
                'c', Items.CLOCK,
                'I', "blockIron",
                'T', Blocks.TNT));

        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(HandheldNavalGun.ITEMS.ROUND_127MM_AAC, 16),
                " c ",
                "STS",
                "BTB",
                'c', Items.CLOCK,
                'S', "blockSteel",
                'B', "blockBrass",
                'T', Blocks.TNT));
    }

    /* Entities */

    public enum EntityID
    {
        PROJECTILE_127MM_ANTI_AIRCRAFT_COMMON,
    }

    public void resisterEntities()
    {
        EntityRegistry.registerModEntity(
                EntityProjectile127mmAntiAircraftCommon.class,
                "entity_projectile_127mm_anti_aircraft_common",
                EntityID.PROJECTILE_127MM_ANTI_AIRCRAFT_COMMON.ordinal(),
                HandheldNavalGun.INSTANCE, 256, 5, true);
    }

}
