package iunius118.mods.handheldnavalgun.client.gunfirecontrolsystem;

import javax.annotation.Nullable;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface IGunDirector
{

    public boolean isValid(@Nullable World world);

    public void setTarget(@Nullable Target targetIn);

    @Nullable
    public Target getTarget();

    @Nullable
    public Vec3d getTargetPos(@Nullable World world);

    @Nullable
    public Vec3d getTargetVisualPos(@Nullable World world, float partialTicks);

    @Nullable
    public Vec3d getTargetMotion(@Nullable World world);

    public void update(@Nullable World world, IGunComputer computer);

}
