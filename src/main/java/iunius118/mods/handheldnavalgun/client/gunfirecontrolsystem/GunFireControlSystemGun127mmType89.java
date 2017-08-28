package iunius118.mods.handheldnavalgun.client.gunfirecontrolsystem;

import net.minecraft.world.World;

public class GunFireControlSystemGun127mmType89
{
    public IGunDirector director = new DirectorGun127mmType89();
    public IGunComputer computer = new ComputerGun127mmType89();
    public IGunIndicator indicator = new IndicatorGun127mmType89();

    public void update(World world)
    {
        director.update(world, computer);
    }
}
