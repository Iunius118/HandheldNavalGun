package iunius118.mods.handheldnavalgun.capability;

import javax.annotation.Nullable;

import iunius118.mods.handheldnavalgun.HandheldNavalGun;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CapabilityReloadTime
{

    public static class Provider implements IReloadTimeICapability, ICapabilityProvider
    {

        private int reloadTime;

        public Provider()
        {
            this(0);
        }

        public Provider(int ticks)
        {
            this.setReloadTime(ticks);
        }

        @Override
        public int setReloadTime(int ticks)
        {
            this.reloadTime = ticks;
            return ticks;
        }

        @Override
        public int getReloadTime()
        {
            return this.reloadTime;
        }

        @Override
        public int progress(boolean isReloadable, int timeReload)
        {
            --this.reloadTime;

            if (isReloadable)
            {
                if (this.reloadTime < 0) this.reloadTime = 0;
            }
            else
            {
                if (this.reloadTime < timeReload) this.reloadTime = timeReload;
            }

            return this.reloadTime;
        }

        @Override
        public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
        {
            return HandheldNavalGun.Capabilities.getReloadTimeICapability() != null && HandheldNavalGun.Capabilities.getReloadTimeICapability() == capability;
        }

        @Override
        @Nullable
        public <T> T getCapability(Capability<T> capability,
                @Nullable EnumFacing facing)
        {
            if (hasCapability(capability, facing))
            {
                Capability<IReloadTimeICapability> reloadTimeICapability = HandheldNavalGun.Capabilities .getReloadTimeICapability();

                if (reloadTimeICapability != null)
                {
                    return reloadTimeICapability.cast(this);
                }
            }

            return null;
        }
    }

    public static class DefaultImpl implements IReloadTimeICapability
    {

        @Override
        public int setReloadTime(int ticks)
        {
            return 0;
        }

        @Override
        public int getReloadTime()
        {
            return 0;
        }

        @Override
        public int progress(boolean isReloadable, int timeReload)
        {
            return 0;
        }

    }

    public static class Storage implements IStorage<IReloadTimeICapability>
    {

        @Override
        public NBTBase writeNBT(Capability<IReloadTimeICapability> capability, IReloadTimeICapability instance, EnumFacing side)
        {
            return null;
        }

        @Override
        public void readNBT(Capability<IReloadTimeICapability> capability, IReloadTimeICapability instance, EnumFacing side, NBTBase nbt)
        {

        }

    }

    public static interface IReloadTimeICapability
    {

        public int setReloadTime(int ticks);

        public int getReloadTime();

        public int progress(boolean isReloadable, int timeReload);

    }

}
