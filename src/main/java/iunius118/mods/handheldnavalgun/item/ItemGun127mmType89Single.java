package iunius118.mods.handheldnavalgun.item;

import javax.annotation.Nullable;

import iunius118.mods.handheldnavalgun.HandheldNavalGun;
import iunius118.mods.handheldnavalgun.capability.CapabilityReloadTime;
import iunius118.mods.handheldnavalgun.client.RangeKeeperGun127mmType89;
import iunius118.mods.handheldnavalgun.client.util.ClientUtils;
import iunius118.mods.handheldnavalgun.client.util.Target;
import iunius118.mods.handheldnavalgun.entity.EntityProjectile127mmAntiAircraftCommon;
import net.minecraft.block.BlockTNT;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemGun127mmType89Single extends Item {

	public static final String TAG_RELOAD_TIME = "reload";
	public static final int RELOAD_TIME = 80;

	public ItemGun127mmType89Single() {
		super();
	}

	@Nullable
	public ItemStack findAmmo(EntityPlayer player) {
		if (this.isAmmo(player.getHeldItem(EnumHand.OFF_HAND))) {
			return player.getHeldItem(EnumHand.OFF_HAND);
		} else if (this.isAmmo(player.getHeldItem(EnumHand.MAIN_HAND))) {
			return player.getHeldItem(EnumHand.MAIN_HAND);
		} else {
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
				ItemStack itemstack = player.inventory.getStackInSlot(i);

				if (this.isAmmo(itemstack)) {
					return itemstack;
				}
			}

			return null;
		}
	}

	public boolean isAmmo(@Nullable ItemStack stack) {
		return stack != null && stack.getItem() instanceof ItemBlock && ((ItemBlock)stack.getItem()).getBlock() instanceof BlockTNT;
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {

		if (entityLiving.worldObj.isRemote && entityLiving == Minecraft.getMinecraft().thePlayer) {
			RayTraceResult result = ClientUtils.getMouseOver(256.0D, 1.0F);
			RangeKeeperGun127mmType89 rangeKeeper = HandheldNavalGun.INSTANCE.rangeKeeper;

			if (result != null && result.typeOfHit != RayTraceResult.Type.MISS) {
				double d = result.hitVec.squareDistanceTo(entityLiving.posX, entityLiving.posY, entityLiving.posZ);

				if (d > 100.0D) {
					rangeKeeper.setTarget(new Target(entityLiving.worldObj, result));
					// System.out.println(result);
				} else {
					if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
						rangeKeeper.setTarget(null);
					} else {
						rangeKeeper.setTarget(new Target(entityLiving.worldObj, result));
					}
				}
			} else {
				rangeKeeper.setTarget(null);
			}
		}

		return super.onEntitySwing(entityLiving, stack);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		NBTTagCompound nbt = itemStackIn.getTagCompound();
		CapabilityReloadTime.IReloadTimeICapability cap = itemStackIn.getCapability(HandheldNavalGun.Capabilities.getReloadTimeICapability(), null);

		if (cap.getReloadTime() == 0 && nbt != null && nbt.getInteger(this.TAG_RELOAD_TIME) == 0) {

			if (!worldIn.isRemote) {	// Server: Shot

				if (!playerIn.capabilities.isCreativeMode) {
					ItemStack stackAmmo = this.findAmmo(playerIn);

					if (stackAmmo == null) {
						return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
					} else {
						--stackAmmo.stackSize;

						if (stackAmmo.stackSize <= 0) {
							playerIn.inventory.deleteStack(stackAmmo);
						}
					}
				}

				WorldServer world = (WorldServer)worldIn;

				EntityProjectile127mmAntiAircraftCommon entity = new EntityProjectile127mmAntiAircraftCommon(world, playerIn);
				RangeKeeperGun127mmType89 rangeKeeper = HandheldNavalGun.INSTANCE.rangeKeeper;

				if (rangeKeeper.isValid()) {
					if (rangeKeeper.getFuse() > 3) {
						entity.setFuse(rangeKeeper.getFuse());
					}
				}

				playerIn.worldObj.spawnEntityInWorld(entity);

				Vec3d look = playerIn.getLook(1.0F).scale(2.0D);
				double x = playerIn.posX + look.xCoord;
				double y =playerIn.posY + playerIn.getEyeHeight() + look.yCoord;
				double z =playerIn.posZ + look.zCoord;
				world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D, new int[0]);
				world.playSound(null, x, y, z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.5F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);

				cap.setReloadTime(this.RELOAD_TIME);
				nbt.setInteger(this.TAG_RELOAD_TIME, this.RELOAD_TIME);	// Send to Client
				// System.out.println("SHOT");
			}

			return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
		}

		return new ActionResult<>(EnumActionResult.PASS, itemStackIn);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		NBTTagCompound nbt = stack.getTagCompound();
		CapabilityReloadTime.IReloadTimeICapability cap = stack.getCapability(HandheldNavalGun.Capabilities.getReloadTimeICapability(), null);

		cap.progress();	// Progress reload
		// System.out.println((worldIn.isRemote ? "C " : "S ") + "CAP: " + cap.getReloadTime());

		if (nbt != null) {
			// System.out.println((worldIn.isRemote ? "C " : "S ") + "NBT: " + nbt.getInteger(this.TAG_RELOAD_TIME));

			if (worldIn.isRemote) {
				if (nbt.getInteger(this.TAG_RELOAD_TIME) == this.RELOAD_TIME) {	// Client: Shot in Server
					nbt.setInteger(this.TAG_RELOAD_TIME, 0);
					cap.setReloadTime(this.RELOAD_TIME);
				}
			} else {
				if (cap.getReloadTime() <= 1) {	// Server: Complete reload
					nbt.setInteger(this.TAG_RELOAD_TIME, 0);	// ...and send to Client
				}
			}
		} else {
			stack.setTagCompound(new NBTTagCompound());
		}

	}

	/*
	 * Reload system
	 *  S(cap= 0, nbt= 0), C(cap= 0, nbt= 0) at onUpdate
	 *  S(cap=80, nbt=80), C(cap= 0, nbt= 0) SHOT and update Server NBT and capability at onItemRightClick
	 *   +1
	 *  S(cap=80, nbt=80), C(cap= 0, nbt=80) sync NBT and renew Client ItemStack
	 *  S(cap=79, nbt=80), C(cap= 0, nbt=80) progress reload time at onUpdate
	 *  S(cap=79, nbt=80), C(cap=80, nbt= 0) update Client capability at onUpdate
	 *   +78
	 *  S(cap= 1, nbt=80), C(cap= 2, nbt= 0) at onUpdate
	 *  S(cap= 1, nbt= 0), C(cap= 2, nbt= 0) update Server NBT at onUpdate
	 *    +1
	 *  S(cap= 0, nbt= 0), C(cap= 0 ,nbt= 0) sync NBT and renew Client ItemStack
	 */

}
