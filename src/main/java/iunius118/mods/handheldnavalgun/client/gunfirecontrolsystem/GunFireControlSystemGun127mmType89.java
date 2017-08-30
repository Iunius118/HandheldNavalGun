package iunius118.mods.handheldnavalgun.client.gunfirecontrolsystem;

import net.minecraft.world.World;

public class GunFireControlSystemGun127mmType89
{
    public IGunDirector director = new DirectorGun127mmType89();
    public IGunComputer computer = new ComputerGun127mmType89();
    public IGunIndicator indicator = new IndicatorGun127mmType89();

    public void updateDirectorAndComputer(World world)
    {
        director.update(world, computer);
    }

    public void updateIndicator(World world, float partialTicks)
    {
        indicator.setComputer(computer);
        indicator.update(world, partialTicks);
    }
}
