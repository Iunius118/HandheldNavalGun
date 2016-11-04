package iunius118.mods.handheldnavalgun.item;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import iunius118.mods.handheldnavalgun.HandheldNavalGun;
import iunius118.mods.handheldnavalgun.capability.CapabilityReloadTime;
import iunius118.mods.handheldnavalgun.client.RangeKeeperGun127mmType89;
import iunius118.mods.handheldnavalgun.client.util.ClientUtils;
import iunius118.mods.handheldnavalgun.client.util.Target;
import iunius118.mods.handheldnavalgun.entity.EntityProjectile127mmAntiAircraftCommon;
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

public class ItemGun127mmType89Single extends Item {

	public static final String TAG_UNIQUE_ID = "UUID";
	public static final String TAG_RELOAD_TIME = "reload";
	public static final String TAG_IS_RELOADABLE = "reloadable";
	public static final int RELOAD_TIME = 80;
	public static final int RELOAD_STARTING_TIME = 60;


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
		return (stack != null) && (stack.getItem() == HandheldNavalGun.Items.ROUND_127MM_AAC);
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		if (entityLiving.worldObj.isRemote && entityLiving == Minecraft.getMinecraft().thePlayer) {
			RayTraceResult result = ClientUtils.getMouseOver(256.0D, 1.0F);
			RangeKeeperGun127mmType89 rangeKeeper = HandheldNavalGun.INSTANCE.rangeKeeper;

			if (Minecraft.getMinecraft().thePlayer.isSneaking()) {
				// if sneaking, release target
				rangeKeeper.setTarget(null);
			} else if (result != null && result.typeOfHit != RayTraceResult.Type.MISS) {
				// set target to range-keeper
				double d = result.hitVec.squareDistanceTo(entityLiving.posX, entityLiving.posY, entityLiving.posZ);

				if (d > 36.0D) {
					// targeting only over some distance
					rangeKeeper.setTarget(new Target(entityLiving.worldObj, result));
					// System.out.println(result);
				}
			} else {
				// the other case: release target
				rangeKeeper.setTarget(null);
			}
		}

		return super.onEntitySwing(entityLiving, stack);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		NBTTagCompound nbt = itemStackIn.getTagCompound();

		if (nbt == null) {
			return new ActionResult<>(EnumActionResult.PASS, itemStackIn);
		}

		CapabilityReloadTime.IReloadTimeICapability cap = itemStackIn.getCapability(HandheldNavalGun.Capabilities.getReloadTimeICapability(), null);

		if (cap.getReloadTime() == 0 && nbt.getInteger(this.TAG_RELOAD_TIME) == 0 && nbt.getBoolean(this.TAG_IS_RELOADABLE)) {

			if (!worldIn.isRemote) {	// Server: Shot
				ItemStack heldItem = playerIn.getHeldItem(EnumHand.MAIN_HAND);

				if (hand == EnumHand.OFF_HAND && (heldItem == null || heldItem.getItem() != this)) {
					return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
				}

				if (!playerIn.capabilities.isCreativeMode) {
					ItemStack stackAmmo = this.findAmmo(playerIn);

					if (stackAmmo == null) {
						nbt.setBoolean(this.TAG_IS_RELOADABLE, false);
					} else {
						nbt.setBoolean(this.TAG_IS_RELOADABLE, true);
						--stackAmmo.stackSize;

						if (stackAmmo.stackSize <= 0) {
							playerIn.inventory.deleteStack(stackAmmo);
						}
					}

					// drop empty cartridge
					EntityItem entityitem = new EntityItem(worldIn, playerIn.posX, playerIn.posY + 0.5, playerIn.posZ, new ItemStack(HandheldNavalGun.INSTANCE.itemCartridge));
					worldIn.spawnEntityInWorld(entityitem);
				} else {
					nbt.setBoolean(this.TAG_IS_RELOADABLE, true);
				}

				WorldServer world = (WorldServer)worldIn;

				EntityProjectile127mmAntiAircraftCommon entity = new EntityProjectile127mmAntiAircraftCommon(world, playerIn);
				RangeKeeperGun127mmType89 rangeKeeper = HandheldNavalGun.INSTANCE.rangeKeeper;

				if (rangeKeeper.isValid()) {
					entity.setFuseSafety(rangeKeeper.getFuse());
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

		} else if (cap.getReloadTime() <= this.RELOAD_STARTING_TIME && !nbt.getBoolean(this.TAG_IS_RELOADABLE)) {
			if (!worldIn.isRemote) { // Server: Reload late
				ItemStack stackAmmo = this.findAmmo(playerIn);
				if (!playerIn.capabilities.isCreativeMode) {
					if (stackAmmo != null) {
						--stackAmmo.stackSize;

						if (stackAmmo.stackSize <= 0) {
							playerIn.inventory.deleteStack(stackAmmo);
						}

						nbt.setBoolean(this.TAG_IS_RELOADABLE, true);
						nbt.setInteger(this.TAG_RELOAD_TIME, this.RELOAD_STARTING_TIME);
						return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
					}
				} else {
					nbt.setBoolean(this.TAG_IS_RELOADABLE, true);
					nbt.setInteger(this.TAG_RELOAD_TIME, this.RELOAD_STARTING_TIME);
					return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
				}
			}
		}

		return new ActionResult<>(EnumActionResult.PASS, itemStackIn);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		NBTTagCompound nbt = stack.getTagCompound();
		CapabilityReloadTime.IReloadTimeICapability cap = stack.getCapability(HandheldNavalGun.Capabilities.getReloadTimeICapability(), null);

		if (nbt != null) {
			cap.progress(nbt.getBoolean(this.TAG_IS_RELOADABLE), this.RELOAD_STARTING_TIME);	// Progress reload
			// System.out.println((worldIn.isRemote ? "C " : "S ") + "CAP: " + cap.getReloadTime());

			// System.out.println((worldIn.isRemote ? "C " : "S ") + "NBT: " + nbt.getInteger(this.TAG_RELOAD_TIME));

			if (worldIn.isRemote) {
				if (nbt.getInteger(this.TAG_RELOAD_TIME) == this.RELOAD_TIME) {	// Client: Shot in Server
					nbt.setInteger(this.TAG_RELOAD_TIME, 0);
					cap.setReloadTime(this.RELOAD_TIME);
				} else if (nbt.getInteger(this.TAG_RELOAD_TIME) == this.RELOAD_STARTING_TIME) {	// Client: Reload in Server
					nbt.setInteger(this.TAG_RELOAD_TIME, 0);
					cap.setReloadTime(this.RELOAD_STARTING_TIME);
				}
			} else {
				if (cap.getReloadTime() <= 1 && nbt.getInteger(this.TAG_RELOAD_TIME) != 0) {	// Server: Complete reload
					nbt.setInteger(this.TAG_RELOAD_TIME, 0);	// ...and send to Client
				} else if (cap.getReloadTime() <= this.RELOAD_STARTING_TIME && !nbt.getBoolean(this.TAG_IS_RELOADABLE)) {	// Server: Not Reload-able
					nbt.setInteger(this.TAG_RELOAD_TIME, this.RELOAD_STARTING_TIME);	// ...and send to Client
				}
			}
		} else {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setUniqueId(this.TAG_UNIQUE_ID, MathHelper.getRandomUuid(worldIn.rand));
			stack.setTagCompound(tag);
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
	 *   +1
	 *  S(cap= 0, nbt= 0), C(cap= 0 ,nbt= 0) sync NBT and renew Client ItemStack
	 */

	/*
	// show reload progress by durability bar
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		CapabilityReloadTime.IReloadTimeICapability cap = stack.getCapability(HandheldNavalGun.Capabilities.getReloadTimeICapability(), null);
		return (cap.getReloadTime() > 0);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		CapabilityReloadTime.IReloadTimeICapability cap = stack.getCapability(HandheldNavalGun.Capabilities.getReloadTimeICapability(), null);
		return (double)cap.getReloadTime() / this.RELOAD_TIME;
	}
	// */

	public boolean compareUUIDFromItemStack(ItemStack stackA, ItemStack stackB) {
		if (stackA != null && stackB != null && stackA.hasTagCompound() && stackB.hasTagCompound()) {
			NBTTagCompound tagA = stackA.getTagCompound();
			NBTTagCompound tagB = stackB.getTagCompound();

			if (tagA.hasUniqueId("UUID") && tagB.hasUniqueId("UUID")) {
				boolean ret = tagA.getUniqueId("UUID").equals(tagB.getUniqueId("UUID"));
				return ret;
			}
		}

		return false;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(this.ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 10.0D, 0));
		}

		return multimap;
	}

}
