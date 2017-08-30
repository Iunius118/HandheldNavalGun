package iunius118.mods.handheldnavalgun.client.gunfirecontrolsystem;

import javax.annotation.Nullable;

import net.minecraft.world.World;

public interface IGunComputer
{

    /**
     * Check whether this computer is valid. If the computed values were not available, this method should return false.
     *
     * @return The boolean whether the computer is valid.
     */
    public boolean isValid();

    /**
     * Set a director to this computer.
     *
     * @param director A director to set to the computer.
     */
    public void setDirector(@Nullable IGunDirector director);

    /**
     * Get the director set to this computer.
     *
     * @return The director which set to the computer.
     */
    @Nullable
    public IGunDirector getDirector();

    /**
     * Get the target's future yaw which computed by this computer.
     *
     * @return The target's future yaw in degrees.
     */
    public double getTargetFutureYaw();

    /**
     * Get the target's future pitch which computed by this computer.
     *
     * @return The target's future pitch in degrees.
     */
    public double getTargetFuturePitch();

    /**
     * Get the fuse ticks which computed by this computer.
     *
     * @return The fuse time limit in ticks.
     */
    public int getFuse();

    /**
     * Update this computer to compute the target's future yaw and pitch.
     *
     * @param world The world in which the player is.
     * @param director The Director which tracking a target.
     * @see IGunDirector#update(World, IGunComputer)
     */
    public void update(@Nullable World world);

}
