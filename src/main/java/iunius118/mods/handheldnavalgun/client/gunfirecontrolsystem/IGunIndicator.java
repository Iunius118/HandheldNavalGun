package iunius118.mods.handheldnavalgun.client.gunfirecontrolsystem;

import javax.annotation.Nullable;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface IGunIndicator
{

    /**
     * Check whether this indicator is valid.
     *
     * @return The boolean whether the indicator is valid.
     */
    public boolean isValid();

    /**
     * Set a computer to this indicator.
     *
     * @param director A computer to set to the indicator.
     */
    public void setComputer(@Nullable IGunComputer computer);

    /**
     * Get the computer set to this indicator.
     *
     * @return The computer which set to the indicator.
     */
    @Nullable
    public IGunComputer getComputer();

    /**
     * get the target's present position coordinates on screen.
     *
     * @return A vector containing the double coordinates at which the target is.
     */
    @Nullable
    public Vec3d getTargetScreenPos();

    /**
     * get the target's future position coordinates on screen.
     *
     * @return A vector containing the double coordinates at which the target will be in the future.
     */
    @Nullable
    public Vec3d getTargetFutureScreenPos();

    /**
     * calculate a coordinates of target's present and future position on screen.
     *
     * @param world The world in which the player is.
     * @param partialTicks The sub-frame fraction.
     */
    public void update(@Nullable World world, float partialTicks);

}
