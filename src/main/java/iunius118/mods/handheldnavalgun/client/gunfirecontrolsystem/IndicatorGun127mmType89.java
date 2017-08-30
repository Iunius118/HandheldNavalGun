package iunius118.mods.handheldnavalgun.client.gunfirecontrolsystem;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.annotation.Nullable;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class IndicatorGun127mmType89 implements IGunIndicator
{
    private IGunComputer computer;

    private int tempDisplayWidth = 1;
    private int tempDisplayHeight = 1;
    private int tempGuiScale = 1;
    private int scaleFactor = 1;

    private Vec3d targetScreenPos;
    private Vec3d targetFutureScreenPos;

    @Override
    public boolean isValid()
    {
        return computer != null && computer.isValid();
    }

    @Override
    public void setComputer(@Nullable IGunComputer computer)
    {
        this.computer = computer;
    }

    @Override
    @Nullable
    public IGunComputer getComputer()
    {
        return this.computer;
    }

    @Override
    @Nullable
    public Vec3d getTargetScreenPos()
    {
        return this.targetScreenPos;
    }

    @Override
    @Nullable
    public Vec3d getTargetFutureScreenPos()
    {
        return this.targetFutureScreenPos;
    }

    @Override
    public void update(@Nullable World world, float partialTicks)
    {
        this.targetScreenPos = null;
        this.targetFutureScreenPos = null;

        IGunDirector director = computer.getDirector();

        if(director != null && director.isValid(world))
        {
            this.targetScreenPos = getScreenPos(director.getTargetVisualPos(world, partialTicks), partialTicks);
        }

        if(this.isValid())
        {
            this.targetFutureScreenPos = getScreenPos(computer.getTargetFutureLineOfFire(), partialTicks);
        }

        return;
    }

    @Nullable
    private Vec3d getScreenPos(@Nullable Vec3d pos, float partialTicks)
    {
        Entity viewEntity = Minecraft.getMinecraft().getRenderViewEntity();

        if (pos != null && viewEntity != null)
        {

            double x = pos.xCoord - (viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks);
            double y = pos.yCoord - (viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks) - viewEntity.getEyeHeight();
            double z = pos.zCoord - (viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks);
            double lenSq = x * x + y * y + z * z;
            Vec3d look = viewEntity.getLook(partialTicks);

            if (Minecraft.getMinecraft().getRenderManager().options != null && Minecraft.getMinecraft().getRenderManager().options.thirdPersonView == 2)
            {
                look = look.scale(-1);
            }

            if (lenSq >= 1e-8)
            {
                double d = (x * look.xCoord + y * look.yCoord + z * look.zCoord) / (Math.sqrt(lenSq) * look.lengthVector());
                if (d < 1e-8)
                {
                    return null;
                }
                else
                {
                    return getScreenCoordsFrom3dCoords((float) x, (float) y + viewEntity.getEyeHeight(), (float) z);
                }
            }
        }

        return null;
    }

    @Nullable
    private Vec3d getScreenPos(float yaw, float pitch, float partialTicks)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        Vec3d pos = new Vec3d(f1 * f2, f3, f * f2).scale(5.0D);
        Entity viewEntity = Minecraft.getMinecraft().getRenderViewEntity();

        if (viewEntity != null)
        {
            double x = pos.xCoord;
            double y = pos.yCoord;
            double z = pos.zCoord;
            double lenSq = x * x + y * y + z * z;

            if (lenSq < 1e-8)
            {
                return null;
            }

            Vec3d look = viewEntity.getLook(partialTicks);

            if (Minecraft.getMinecraft().getRenderManager().options != null && Minecraft.getMinecraft().getRenderManager().options.thirdPersonView == 2)
            {
                look = look.scale(-1);
            }

            double deg = Math.toDegrees(Math.acos((x * look.xCoord + y * look.yCoord + z * look.zCoord) / (Math.sqrt(lenSq) * look.lengthVector())));

            if (deg < 90)
            {
                return getScreenCoordsFrom3dCoords((float) x, (float) y + viewEntity.getEyeHeight(), (float) z);
            }
        }

        return null;
    }

    private static final IntBuffer viewport = BufferUtils.createIntBuffer(16);
    private static final FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer projection = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer screenCoords = BufferUtils.createFloatBuffer(4);

    @Nullable
    private Vec3d getScreenCoordsFrom3dCoords(float x, float y, float z)
    {
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

        boolean result = GLU.gluProject(x, y, z, modelview, projection, viewport, screenCoords);

        if (result)
        {
            Minecraft mc = Minecraft.getMinecraft();

            if (tempDisplayWidth != mc.displayWidth || tempDisplayHeight != mc.displayHeight || tempGuiScale != mc.gameSettings.guiScale)
            {
                scaleFactor = new ScaledResolution(mc).getScaleFactor();
                tempDisplayWidth = mc.displayWidth;
                tempDisplayHeight = mc.displayHeight;
                tempGuiScale = mc.gameSettings.guiScale;
            }

            return new Vec3d(screenCoords.get(0) / scaleFactor, (mc.displayHeight - screenCoords.get(1)) / scaleFactor, 0);
        }
        else
        {
            return null;
        }
    }

}
