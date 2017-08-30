package iunius118.mods.handheldnavalgun.client.gunfirecontrolsystem;

import javax.annotation.Nullable;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface IGunDirector
{

    /**
     * Check whether this director is valid.
     *
     * @param world The world in which the player is.
     * @return The boolean whether the director is valid.
     */
    public boolean isValid(@Nullable World world);


    /**
     * Set a target to this director.
     *
     * @param targetIn The target to set.
     */
    public void setTarget(@Nullable Target targetIn);

    /**
     * Get a target which is being tracked by this director.
     *
     * @return The target.
     */
    @Nullable
    public Target getTarget();

    /**
     * Get the target position coordinates.
     *
     * @param world The world in which the player is.
     * @return A vector containing the double coordinates at which the target is.
     */
    @Nullable
    public Vec3d getTargetPos(@Nullable World world);

    /**
     * Get the target visual position coordinates.
     *
     * @param world The world in which the player is.
     * @param partialTicks The sub-frame fraction.
     * @return A vector containing the double coordinates at which the target is.
     */
    @Nullable
    public Vec3d getTargetVisualPos(@Nullable World world, float partialTicks);

    /**
     * Get the target motion vector.
     *
     * @param world The world in which the player is.
     * @return A vector containing the double coordinates which is the motion (m/tick) of the target.
     */
    @Nullable
    public Vec3d getTargetMotion(@Nullable World world);

    /**
     * Update this director and the computer.
     *
     * @param world The world in which the player is.
     * @param computer The computer linked from this director
     */
    public void update(@Nullable World world, IGunComputer computer);

}
