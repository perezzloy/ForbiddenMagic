package fox.spiteful.forbidden.items;

import java.util.List;

import fox.spiteful.forbidden.Forbidden;
import fox.spiteful.forbidden.compat.Compat;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipeRegistry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDivineOrb extends Item implements IBloodOrb, IBindable {
	public ItemDivineOrb() {
		setCreativeTab(Forbidden.tab);
		AltarRecipeRegistry.registerAltarOrbRecipe(new ItemStack(this), 5, 140);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon("forbidden:divineorb");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		list.add("Stores raw Life Essence");

		if (!(stack.stackTagCompound == null)) {
			list.add("Current owner: " + stack.stackTagCompound.getString("ownerName"));
		}

	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
		if (Compat.bm)
			SoulNetworkHandler.checkAndSetItemOwner(itemstack, player);

		if (world != null) {
			double posX = player.posX;
			double posY = player.posY;
			double posZ = player.posZ;
			world.playSoundEffect((double) ((float) posX + 0.5F), (double) ((float) posY + 0.5F), (double) ((float) posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
		}
		if (!world.isRemote) {
			return itemstack;
		}

		NBTTagCompound itemTag = itemstack.stackTagCompound;

		if (itemTag == null || itemTag.getString("ownerName").equals("")) {
			return itemstack;
		}

		player.setHealth(player.getHealth() - 1);
		SoulNetworkHandler.addCurrentEssenceToMaximum(itemTag.getString("ownerName"), 200, 700000000);

		return itemstack;
	}

	@Override
	public int getMaxEssence() {
		return 70000000;
	}

	@Override
	public int getOrbLevel() {
		return 5;
	}
}