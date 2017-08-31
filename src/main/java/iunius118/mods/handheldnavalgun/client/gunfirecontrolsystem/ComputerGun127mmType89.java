package iunius118.mods.handheldnavalgun.client.gunfirecontrolsystem;

import javax.annotation.Nullable;

import iunius118.mods.handheldnavalgun.entity.EntityProjectile127mmAntiAircraftCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ComputerGun127mmType89 implements IGunComputer
{

    private boolean isValid = false;
    private IGunDirector director;
    private Vec3d futureLoF;
    private int ticksFuse = EntityProjectile127mmAntiAircraftCommon.FUSE_MAX;

    @Override
    public boolean isValid()
    {
        return this.isValid;
    }

    @Override
    public void setDirector(@Nullable IGunDirector director)
    {
        this.director = director;
    }

    @Override
    @Nullable
    public IGunDirector getDirector()
    {
        return this.director;
    }

    @Override
    public Vec3d getTargetFutureLineOfFire()
    {
        return this.futureLoF;
    }

    @Override
    public int getFuse()
    {
        return this.ticksFuse;
    }

    @Override
    public void update(@Nullable World world)
    {
        /*
         * Compute future target direction from the player and the fuse time for EntityThrowable (gravity velocity: 0.03, attenuation rate: 0.99) of which initial velocity is 4 m/ticks.
         */

        if (director == null && !director.isValid(world))
        {
            this.isValid = false;
            return;
        }

        Vec3d vec3TargetDelta = director.getTargetMotion(world);

        if (vec3TargetDelta == null)
        {
            this.isValid = false;
            return;
        }

        Vec3d vec3Target1 = director.getTargetPos(world).add(vec3TargetDelta);

        if (vec3Target1 == null)
        {
            this.isValid = false;
            return;
        }

        Entity player = Minecraft.getMinecraft().thePlayer;

        if (player == null)
        {
            this.isValid = false;
            return;
        }

        Vec3d vec3Player = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        final double v0sq = EntityProjectile127mmAntiAircraftCommon.INITIAL_VELOCITY * EntityProjectile127mmAntiAircraftCommon.INITIAL_VELOCITY;

        int t; // Fuse tick to set

        // Skip tick by distance
        if(this.director.getTarget().type != Target.Type.ENTITY) {
            double r = vec3Player.distanceTo(vec3Target1);
            t = (int) Math.floor(0.00007D * r * r + 0.25D * r - 0.04D);
        }
        else
        {
            for (t = 1; t <= EntityProjectile127mmAntiAircraftCommon.FUSE_MAX; t++)
            {
                double r = vec3Player.distanceTo(vec3Target1);
                int ts = (int) Math.floor(0.00007D * r * r + 0.25D * r - 0.04D);

                if (ts <= t)
                {
                    break;
                }

                vec3Target1 = vec3Target1.add(vec3TargetDelta);
            }
        }

        // Calculate initial velocity from tick, height and distance
        double x1 = vec3Target1.xCoord - vec3Player.xCoord;
        double z1 = vec3Target1.zCoord - vec3Player.zCoord;
        double tx1 = Math.sqrt(x1 * x1 + z1 * z1);
        double ty1 = vec3Target1.yCoord - vec3Player.yCoord;
        double v0x1 = tx1 / ticksToV0Rate(t);
        double v0y1 = (ty1 + 3.0D * t) / ticksToV0Rate(t) - 3.0D;
        double v0sq1 = v0x1 * v0x1 + v0y1 * v0y1;

        for (; t <= EntityProjectile127mmAntiAircraftCommon.FUSE_MAX; t++)
        {
            // Calculate initial velocity from tick + 1, height and distance
            Vec3d vec3Target2 = vec3Target1.add(vec3TargetDelta);
            double x2 = vec3Target2.xCoord - vec3Player.xCoord;
            double z2 = vec3Target2.zCoord - vec3Player.zCoord;
            double tx2 = Math.sqrt(x2 * x2 + z2 * z2);
            double ty2 = vec3Target2.yCoord - vec3Player.yCoord;
            double v0x2 = tx2 / ticksToV0Rate(t + 1);
            double v0y2 = (ty2 + 3.0D * (t + 1)) / ticksToV0Rate(t + 1) - 3.0D;
            double v0sq2 = v0x2 * v0x2 + v0y2 * v0y2;

            // If the initial velocity which calculated is closest to the real one (4 m/ticks), update the future target direction from the player and the fuse tick, and return
            if ((v0sq1 > v0sq2 && v0sq1 >= v0sq && v0sq2 < v0sq) || (v0sq1 < v0sq2 && v0sq1 < v0sq && v0sq2 >= v0sq))
            {
                if (Math.abs(v0sq1 - v0sq) <= Math.abs(v0sq2 - v0sq))
                {
                    this.futureLoF = new Vec3d(vec3Target1.xCoord, vec3Player.yCoord + (v0y1 / v0x1 * tx1), vec3Target1.zCoord);

                    // double futureYaw = (tx1 != 0.0D) ? Math.toDegrees(Math.atan2(-vec3Target1.xCoord + vec3Player.xCoord, vec3Target1.zCoord - vec3Player.zCoord)) : 0.0D;
                    // double futurePitch = -Math.toDegrees(Math.atan2(v0y1, v0x1));
                }
                else
                {
                    if (t < EntityProjectile127mmAntiAircraftCommon.FUSE_MAX)
                    {
                        t++;
                    }

                    this.futureLoF = new Vec3d(vec3Target2.xCoord, vec3Player.yCoord + (v0y2 / v0x2 * tx2), vec3Target2.zCoord);

                    // double futureYaw = (tx2 != 0.0D) ? Math.toDegrees(Math.atan2(-vec3Target2.xCoord + vec3Player.xCoord, vec3Target2.zCoord - vec3Player.zCoord)) : 0.0D;
                    // double futurePitch = -Math.toDegrees(Math.atan2(v0y2, v0x2));
                }

                this.ticksFuse = t;
                this.isValid = (t > EntityProjectile127mmAntiAircraftCommon.FUSE_SAFETY);
                return;
            }

            // Tick progress
            vec3Target1 = vec3Target2;
            tx1 = tx2;
            ty1 = ty2;
            v0x1 = v0x2;
            v0y1 = v0y2;
            v0sq1 = v0sq2;
        }

        // Out of range
        this.isValid = false;
        return;
    }

    private static double[] v0rate;
    static
    {
        v0rate = new double[128];

        for (int i = 0; i < v0rate.length; i++)
        {
            v0rate[i] = 100 * (1 - Math.exp(-0.01 * i));
        }
    }

    private double ticksToV0Rate(int t)
    {
        if (t >= 0 && t < v0rate.length)
        {
            return v0rate[t];
        }
        else
        {
            return 100 * (1 - Math.exp(-0.01 * t));
        }
    }

}
