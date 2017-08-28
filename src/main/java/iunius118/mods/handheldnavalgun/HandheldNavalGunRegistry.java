package iunius118.mods.handheldnavalgun;

import iunius118.mods.handheldnavalgun.HandheldNavalGun.PacketHandler;
import iunius118.mods.handheldnavalgun.capability.CapabilityReloadTime;
import iunius118.mods.handheldnavalgun.entity.EntityProjectile127mmAntiAircraftCommon;
import iunius118.mods.handheldnavalgun.packet.MessageGunShot;
import iunius118.mods.handheldnavalgun.packet.MessageGunShotHandler;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class HandheldNavalGunRegistry
{

    public static void registerMessage()
    {
        PacketHandler.INSTANCE.registerMessage(MessageGunShotHandler.class, MessageGunShot.class, 0, Side.SERVER);
    }

    public static void resisterEntities()
    {
        EntityRegistry.registerModEntity(
                EntityProjectile127mmAntiAircraftCommon.class,
                "entity_projectile_127mm_anti_aircraft_common",
                EntityID.PROJECTILE_127MM_ANTI_AIRCRAFT_COMMON.ordinal(),
                HandheldNavalGun.INSTANCE, 256, 5, true);
    }

    public enum EntityID
    {
        PROJECTILE_127MM_ANTI_AIRCRAFT_COMMON,
    }

    public static void registerCapabilities()
    {
        CapabilityManager.INSTANCE.register(
                CapabilityReloadTime.IReloadTimeICapability.class,
                new CapabilityReloadTime.Storage(),
                CapabilityReloadTime.DefaultImpl.class);
    }

}
