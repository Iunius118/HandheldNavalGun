package iunius118.mods.handheldnavalgun.client.gunfirecontrolsystem;

import javax.annotation.Nullable;

import iunius118.mods.handheldnavalgun.client.gunfirecontrolsystem.Target.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DirectorGun127mmType89 implements IGunDirector
{

    private Target target;

    private static final int MAX_DELTA_COUNT = 10;
    private int deltaPointer = 0;
    private Vec3d[] deltas = new Vec3d[MAX_DELTA_COUNT];

    @Override
    public boolean isValid(@Nullable World world)
    {
        return target != null && target.isValid(world);
    }

    @Override
    public void setTarget(@Nullable Target targetIn)
    {
        this.target = targetIn;
    }

    @Override
    @Nullable
    public Target getTarget()
    {
        return this.target;
    }

    @Override
    @Nullable
    public Vec3d getTargetPos(@Nullable World world)
    {
        return this.target.getPos(world);
    }

    @Override
    @Nullable
    public Vec3d getTargetVisualPos(@Nullable World world, float partialTicks)
    {
        return this.target.getVisualPos(world, partialTicks);
    }

    @Override
    @Nullable
    public Vec3d getTargetMotion(@Nullable World world)
    {
        Vec3d motion = null;
        int deltaCount = 0;

        if(!target.isValid(world))
        {
            return null;
        }
        else if(target.type == Type.BLOCK)
        {
            return new Vec3d(0.0D, 0.0D, 0.0D);
        }
        else if(target.type == Type.ENTITY)
        {
            for(Vec3d delta : this.deltas)
            {
                if(delta != null)
                {
                    if(motion != null)
                    {
                        motion.add(delta);
                    }
                    else
                    {
                        motion = new Vec3d(delta.xCoord, delta.yCoord, delta.zCoord);
                    }

                    deltaCount++;
                }
            }

            if(motion != null && deltaCount > 0)
            {
                return new Vec3d(motion.xCoord / deltaCount, motion.yCoord / deltaCount, motion.zCoord / deltaCount);
            }

            return null;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void update(@Nullable World world, IGunComputer computer)
    {
        if(target.isValid(world) && target.type == Type.ENTITY)
        {
            deltas[deltaPointer] = target.getPosDelta(world);

            if(++deltaPointer >= MAX_DELTA_COUNT)
            {
                deltaPointer = 0;
            }
        }
    }

}
