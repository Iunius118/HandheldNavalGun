package iunius118.mods.handheldnavalgun.client.gunfirecontrolsystem;

import javax.annotation.Nullable;

import iunius118.mods.handheldnavalgun.client.gunfirecontrolsystem.Target.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DirectorGun127mmType89 implements IGunDirector
{

    private Target target;

    private static final int MAX_DELTA_COUNT = 8;
    private int deltaPointer = 0;
    private Vec3d[] deltas = new Vec3d[MAX_DELTA_COUNT];

    @Override
    public boolean isValid(@Nullable World world)
    {
        return this.target != null && target.isValid(world);
    }

    @Override
    public void setTarget(@Nullable Target targetIn)
    {
        this.target = targetIn;
        this.deltas = new Vec3d[MAX_DELTA_COUNT];
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
        if(this.isValid(world))
        {
            return this.target.getPos(world);
        }
        else
        {
            return null;

        }
    }

    @Override
    @Nullable
    public Vec3d getTargetVisualPos(@Nullable World world, float partialTicks)
    {
        if(this.isValid(world))
        {
            return this.target.getVisualPos(world, partialTicks);
        }
        else
        {
            return null;

        }
    }

    @Override
    @Nullable
    public Vec3d getTargetMotion(@Nullable World world)
    {
        if(!this.isValid(world))
        {
            return null;
        }

        if(target.type == Type.BLOCK)
        {
            return new Vec3d(0.0D, 0.0D, 0.0D);
        }
        else if(target.type == Type.ENTITY)
        {
            Vec3d vec1 = null;
            Vec3d vec2 = null;
            int deltaCount = 0;

            for(Vec3d delta : this.deltas)
            {
                if(delta != null)
                {
                    if(vec1 != null)
                    {
                        vec2 = vec1.add(delta);
                    }
                    else
                    {
                        vec2 = new Vec3d(delta.xCoord, delta.yCoord, delta.zCoord);
                    }

                    vec1 = vec2;
                    deltaCount++;
                }
            }

            if(vec1 != null && deltaCount > 0)
            {
                return new Vec3d(vec1.xCoord / deltaCount, vec1.yCoord / deltaCount, vec1.zCoord / deltaCount);
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
        if(this.isValid(world) && target.type == Type.ENTITY)
        {
            deltas[deltaPointer] = target.getPosDelta(world);

            if(++deltaPointer >= MAX_DELTA_COUNT)
            {
                deltaPointer = 0;
            }
        }

        computer.setDirector(this);
        computer.update(world);
    }

}
