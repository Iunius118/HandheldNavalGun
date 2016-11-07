package iunius118.mods.handheldnavalgun;

import iunius118.mods.handheldnavalgun.HandheldNavalGun.PacketHandler;
import iunius118.mods.handheldnavalgun.capability.CapabilityReloadTime;
import iunius118.mods.handheldnavalgun.entity.EntityProjectile127mmAntiAircraftCommon;
import iunius118.mods.handheldnavalgun.packet.MessageGunShot;
import iunius118.mods.handheldnavalgun.packet.MessageGunShotHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class HandheldNavalGunRegistry {

	public static void registerMessage() {
		PacketHandler.INSTANCE.registerMessage(MessageGunShotHandler.class, MessageGunShot.class, 0, Side.SERVER);
	}

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
		GameRegistry.register(HandheldNavalGun.Items.ROUND_127MM_AAC);
		registerItemRecipes();
	}

	public static void registerItemRecipes() {
		GameRegistry.addRecipe(
				new ShapedOreRecipe(
						new ItemStack(HandheldNavalGun.Items.GUN_127MM_TYPE89_SINGLE),
						"ii ",
						"gii",
						"cS ",
						'i', "ingotIron",
						'g', "paneGlass",
						'c', Items.CLOCK,
						'S', Blocks.STICKY_PISTON)
				);

		GameRegistry.addRecipe(
				new ShapedOreRecipe(
						new ItemStack(HandheldNavalGun.Items.ROUND_127MM_AAC, 16),
						" c ",
						"ITI",
						"ITI",
						'c', Items.CLOCK,
						'I', "blockIron",
						'T', Blocks.TNT)
				);

		GameRegistry.addRecipe(
				new ShapedOreRecipe(
						new ItemStack(HandheldNavalGun.Items.ROUND_127MM_AAC, 16),
						" c ",
						"STS",
						"BTB",
						'c', Items.CLOCK,
						'S', "blockSteel",
						'B', "blockBrass",
						'T', Blocks.TNT)
				);
	}

}
