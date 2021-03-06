package iunius118.mods.handheldnavalgun.item;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import iunius118.mods.handheldnavalgun.HandheldNavalGun;
import iunius118.mods.handheldnavalgun.HandheldNavalGun.PacketHandler;
import iunius118.mods.handheldnavalgun.capability.CapabilityReloadTime;
import iunius118.mods.handheldnavalgun.client.gunfirecontrolsystem.IGunComputer;
import iunius118.mods.handheldnavalgun.client.gunfirecontrolsystem.IGunDirector;
import iunius118.mods.handheldnavalgun.client.gunfirecontrolsystem.Target;
import iunius118.mods.handheldnavalgun.client.util.ClientUtils;
import iunius118.mods.handheldnavalgun.entity.EntityProjectile127mmAntiAircraftCommon;
import iunius118.mods.handheldnavalgun.packet.MessageGunShot;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemGun127mmType89Single extends Item
{

    public static final String TAG_UNIQUE_ID = "UUID";
    public static final String TAG_RELOAD_TIME = "reload";
    public static final String TAG_IS_RELOADABLE = "reloadable";
    public static final int RELOAD_TIME = 80;
    public static final int RELOAD_STARTING_TIME = 50;

    public ItemGun127mmType89Single()
    {
        super();
    }

    @Nullable
    public ItemStack findAmmo(EntityPlayer player)
    {
        if (this.isAmmo(player.getHeldItem(EnumHand.OFF_HAND)))
        {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (this.isAmmo(player.getHeldItem(EnumHand.MAIN_HAND)))
        {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        else
        {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isAmmo(itemstack))
                {
                    return itemstack;
                }
            }

            return null;
        }
    }

    public boolean isAmmo(@Nullable ItemStack stack)
    {
        return (stack != null) && (stack.getItem() instanceof ItemRound127mmAntiAircraftCommon);
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
    {
        if (entityLiving.worldObj.isRemote && entityLiving == Minecraft.getMinecraft().thePlayer)
        {
            // Targeting process on Client
            RayTraceResult result = ClientUtils.getMouseOver(256.0D, 1.0F);
            IGunDirector director = HandheldNavalGun.INSTANCE.gunFireControlSystem.director;

            if (Minecraft.getMinecraft().thePlayer.isSneaking())
            {
                // When sneaking, release target
                director.setTarget(null);
            }
            else if (result != null && result.typeOfHit != RayTraceResult.Type.MISS)
            {
                // When found target, set target to range-keeper
                double d = result.hitVec.squareDistanceTo(entityLiving.posX, entityLiving.posY, entityLiving.posZ);

                if (d > 36.0D)
                {
                    // Targeting only over some distance (6 meters)
                    director.setTarget(new Target(entityLiving.worldObj, result));
                    // System.out.println(result);
                }
            }
            else
            {
                // The other case: Release target
                director.setTarget(null);
            }
        }

        return super.onEntitySwing(entityLiving, stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        NBTTagCompound nbt = itemStackIn.getTagCompound();

        if (nbt == null)
        {
            return new ActionResult<>(EnumActionResult.PASS, itemStackIn);
        }

        // Shooting and Reloading process

        CapabilityReloadTime.IReloadTimeICapability cap = itemStackIn.getCapability(HandheldNavalGun.Capabilities.getReloadTimeICapability(), null);

        if (cap.getReloadTime() == 0 && nbt.getInteger(this.TAG_RELOAD_TIME) == 0 && nbt.getBoolean(this.TAG_IS_RELOADABLE))
        {
            if (!worldIn.isRemote)
            {
                // Server: Shoot
                ItemStack heldItem = playerIn.getHeldItem(EnumHand.MAIN_HAND);

                if (hand == EnumHand.OFF_HAND && (heldItem == null || heldItem.getItem() != this))
                {
                    return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
                }

                if (playerIn.capabilities.isCreativeMode)
                {
                    // Creative Mode
                    nbt.setBoolean(this.TAG_IS_RELOADABLE, true);
                }
                else
                {
                    // Survival Mode
                    ItemStack stackAmmo = this.findAmmo(playerIn);

                    if (stackAmmo == null)
                    {
                        // When out of ammo, shoot without reload
                        nbt.setBoolean(this.TAG_IS_RELOADABLE, false);
                    }
                    else
                    {
                        // Reload and Consume ammo
                        nbt.setBoolean(this.TAG_IS_RELOADABLE, true);
                        --stackAmmo.stackSize;

                        if (stackAmmo.stackSize <= 0)
                        {
                            playerIn.inventory.deleteStack(stackAmmo);
                        }
                    }

                    // Drop empty cartridge item
                    EntityItem entityitem = new EntityItem(worldIn, playerIn.posX, playerIn.posY + 0.5, playerIn.posZ, HandheldNavalGun.INSTANCE.itemCartridge.copy());
                    worldIn.spawnEntityInWorld(entityitem);
                }

                WorldServer world = (WorldServer) worldIn;

                EntityProjectile127mmAntiAircraftCommon entity = new EntityProjectile127mmAntiAircraftCommon(world, playerIn);

                if (nbt.hasUniqueId(this.TAG_UNIQUE_ID))
                {
                    synchronized (HandheldNavalGun.INSTANCE.mapShell)
                    {
                        // Server: set fuse from Client packet
                        String strUUID = nbt.getUniqueId(this.TAG_UNIQUE_ID).toString();
                        Integer fuse = HandheldNavalGun.INSTANCE.mapShell.get(strUUID);

                        if (fuse != null)
                        {
                            HandheldNavalGun.INSTANCE.mapShell.remove(strUUID);
                        }
                        else
                        {
                            fuse = EntityProjectile127mmAntiAircraftCommon.FUSE_MAX;
                        }

                        entity.setFuseSafety(fuse.intValue());
                        entity.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, EntityProjectile127mmAntiAircraftCommon.INITIAL_VELOCITY, EntityProjectile127mmAntiAircraftCommon.INACCURACY);
                        world.spawnEntityInWorld(entity);
                    }
                }

                // Generate gun smoke and sound
                Vec3d look = playerIn.getLook(1.0F).scale(2.0D);
                double x = playerIn.posX + look.xCoord;
                double y = playerIn.posY + playerIn.getEyeHeight() + look.yCoord;
                double z = playerIn.posZ + look.zCoord;
                world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                world.playSound(null, x, y, z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.5F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);

                cap.setReloadTime(this.RELOAD_TIME);
                nbt.setInteger(this.TAG_RELOAD_TIME, this.RELOAD_TIME); // Send to Client
                // System.out.println("SHOT");
            }
            else
            {
                // Client: Shoot
                IGunComputer computer = HandheldNavalGun.INSTANCE.gunFireControlSystem.computer;

                if (computer.isValid() && nbt.hasUniqueId(this.TAG_UNIQUE_ID))
                {
                    // Client: Send fuse tick to Server
                    PacketHandler.INSTANCE.sendToServer(new MessageGunShot(nbt.getUniqueId(this.TAG_UNIQUE_ID), computer.getFuse()));
                }
            }

            return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);

        }
        else if (cap.getReloadTime() <= this.RELOAD_STARTING_TIME && !nbt.getBoolean(this.TAG_IS_RELOADABLE))
        {
            if (!worldIn.isRemote)
            {
                // Server: Reload late
                if (playerIn.capabilities.isCreativeMode)
                {
                    // Creative Mode (not consume ammo)
                    nbt.setBoolean(this.TAG_IS_RELOADABLE, true);
                    nbt.setInteger(this.TAG_RELOAD_TIME, this.RELOAD_STARTING_TIME);
                    return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
                }
                else
                {
                    // Survival Mode
                    ItemStack stackAmmo = this.findAmmo(playerIn);

                    if (stackAmmo != null)
                    {
                        --stackAmmo.stackSize;

                        if (stackAmmo.stackSize <= 0)
                        {
                            playerIn.inventory.deleteStack(stackAmmo);
                        }

                        nbt.setBoolean(this.TAG_IS_RELOADABLE, true);
                        nbt.setInteger(this.TAG_RELOAD_TIME, this.RELOAD_STARTING_TIME);
                        return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
                    }
                }
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        NBTTagCompound nbt = stack.getTagCompound();
        CapabilityReloadTime.IReloadTimeICapability cap = stack.getCapability(HandheldNavalGun.Capabilities.getReloadTimeICapability(), null);

        if (nbt != null)
        {
            cap.progress(nbt.getBoolean(this.TAG_IS_RELOADABLE), this.RELOAD_STARTING_TIME); // Progress reload

            // System.out.println((worldIn.isRemote ? "C " : "S ") + "CAP: " + cap.getReloadTime());
            // System.out.println((worldIn.isRemote ? "C " : "S ") + "NBT: " + nbt.getInteger(this.TAG_RELOAD_TIME));

            if (worldIn.isRemote)
            {   // Client
                if (nbt.getInteger(this.TAG_RELOAD_TIME) == this.RELOAD_TIME)
                {
                    // Shot
                    nbt.setInteger(this.TAG_RELOAD_TIME, 0);
                    cap.setReloadTime(this.RELOAD_TIME);
                }
                else if (nbt.getInteger(this.TAG_RELOAD_TIME) == this.RELOAD_STARTING_TIME)
                {
                    // Reload
                    nbt.setInteger(this.TAG_RELOAD_TIME, 0);
                    cap.setReloadTime(this.RELOAD_STARTING_TIME);
                }
            }
            else
            {   // Server
                if (cap.getReloadTime() <= 1 && nbt.getInteger(this.TAG_RELOAD_TIME) != 0)
                {
                    // Complete reloading
                    nbt.setInteger(this.TAG_RELOAD_TIME, 0); // ... and Synchronize Client
                }
                else if (cap.getReloadTime() <= this.RELOAD_STARTING_TIME && !nbt.getBoolean(this.TAG_IS_RELOADABLE))
                {
                    // Not reload-able (run out of ammo)
                    nbt.setInteger(this.TAG_RELOAD_TIME, this.RELOAD_STARTING_TIME); // ... and Synchronize Client
                }
            }
        }
        else
        {
            // Set NBT when NBT is null
            NBTTagCompound tag = new NBTTagCompound();
            tag.setUniqueId(this.TAG_UNIQUE_ID, MathHelper.getRandomUuid(worldIn.rand));
            stack.setTagCompound(tag);
        }

    }

    /*
     * Reload system
     *  * Reload-able
     *  S(cap= 0, nbt= 0 T), C(cap= 0, nbt= 0 F) at onUpdate
     *  S(cap=80, nbt=80 T), C(cap= 0, nbt= 0 F) SHOT and update Server NBT and capability at onItemRightClick
     *   +1
     *  S(cap=80, nbt=80 T), C(cap= 0, nbt=80 T) sync NBT and renew Client ItemStack
     *  S(cap=79, nbt=80 T), C(cap= 0, nbt=80 T) progress reload time at onUpdate
     *  S(cap=79, nbt=80 T), C(cap=80, nbt= 0 T) update Client capability at onUpdate
     *   +78
     *  S(cap= 1, nbt=80 T), C(cap= 2, nbt= 0 T) at onUpdate
     *  S(cap= 1, nbt= 0 T), C(cap= 2, nbt= 0 T) update Server NBT at onUpdate
     *   +1
     *  S(cap= 0, nbt= 0 T), C(cap= 0 ,nbt= 0 T) sync NBT and renew Client ItemStack
     *
     *  * Not Reload-able (run out of ammo)
     *   SHOT, +1 and +29
     *  S(cap=50, nbt=80 F), C(cap=51, nbt= 0 F) at onUpdate
     *  S(cap=50, nbt=50 F), C(cap=51, nbt= 0 F) update Server NBT at onUpdate
     *   +1
     *  S(cap=50, nbt=50 F), C(cap= 0 ,nbt=50 F) sync NBT and renew Client ItemStack
     *  S(cap=50, nbt=50 F), C(cap=50, nbt= 0 F) update Client capability at onUpdate
     *
     *  * Reload late
     *  S(cap=50, nbt=50 T), C(cap=50, nbt= 0 F) RELOAD and update Server NBT and capability at onItemRightClick
     *   +1
     *  S(cap=50, nbt=50 F), C(cap= 0 ,nbt=50 T) sync NBT and renew Client ItemStack
     *  S(cap=49, nbt=50 T), C(cap= 0, nbt=50 T) progress reload time at onUpdate
     *  S(cap=49, nbt=50 T), C(cap=50, nbt= 0 T) update Client capability at onUpdate
     *   +48
     *  S(cap= 1, nbt=60 T), C(cap= 2, nbt= 0 T) at onUpdate
     *  S(cap= 1, nbt= 0 T), C(cap= 2, nbt= 0 T) update Server NBT at onUpdate
     *   +1
     *  S(cap= 0, nbt= 0 T), C(cap= 0 ,nbt= 0 T) sync NBT and renew Client ItemStack
     */

    /*
    // show reload progress by durability bar
    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        CapabilityReloadTime.IReloadTimeICapability cap = stack.getCapability(HandheldNavalGun.Capabilities.getReloadTimeICapability(), null);
        return (cap.getReloadTime() > 0);
    }
    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        CapabilityReloadTime.IReloadTimeICapability cap = stack.getCapability(HandheldNavalGun.Capabilities.getReloadTimeICapability(), null);
        return (double)cap.getReloadTime() / this.RELOAD_TIME;
    }
    // */

    public boolean compareUUIDFromItemStack(ItemStack stackA, ItemStack stackB)
    {
        if (stackA != null && stackB != null && stackA.hasTagCompound() && stackB.hasTagCompound())
        {
            NBTTagCompound tagA = stackA.getTagCompound();
            NBTTagCompound tagB = stackB.getTagCompound();

            if (tagA.hasUniqueId("UUID") && tagB.hasUniqueId("UUID"))
            {
                boolean ret = tagA.getUniqueId("UUID").equals(tagB.getUniqueId("UUID"));
                return ret;
            }
        }

        return false;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(
            EntityEquipmentSlot slot, ItemStack stack)
    {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

        if (slot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(this.ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 10.0D, 0));
        }

        return multimap;
    }

}
