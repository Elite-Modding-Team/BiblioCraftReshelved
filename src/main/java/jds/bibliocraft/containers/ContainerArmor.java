package jds.bibliocraft.containers;

import jds.bibliocraft.slots.SlotArmorBoots;
import jds.bibliocraft.slots.SlotArmorCuirass;
import jds.bibliocraft.slots.SlotArmorGreaves;
import jds.bibliocraft.slots.SlotArmorHelm;
import jds.bibliocraft.tileentities.TileEntityArmorStand;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ContainerArmor extends Container
{
	
	protected TileEntityArmorStand tileEntity;
	protected SlotArmorHelm helmSlot;
	protected SlotArmorCuirass cuirassSlot;
	protected SlotArmorGreaves greavesSlot;
	protected SlotArmorBoots bootsSlot;
	
	public ContainerArmor (InventoryPlayer inventoryPlayer, TileEntityArmorStand tile)
	{
		tileEntity = tile;
		
		addSlotToContainer(this.helmSlot = new SlotArmorHelm(this, tileEntity, 0, 80, 8));
		addSlotToContainer(this.cuirassSlot = new SlotArmorCuirass(this, tileEntity, 1, 80, 26));
		addSlotToContainer(this.greavesSlot = new SlotArmorGreaves(this, tileEntity, 2, 80, 44));
		addSlotToContainer(this.bootsSlot = new SlotArmorBoots(this, tileEntity, 3, 80, 62));
		
		bindPlayerInventory(inventoryPlayer);
		
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tileEntity.isUsableByPlayer(player);
	}
	
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 84+i*18));
			}
		}
		for (int i = 0; i < 9; i++) 
		{
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18,142));
		}
		addSlotToContainer(new SlotArmorHelm(this, inventoryPlayer, 39, 126, 8));
		addSlotToContainer(new SlotArmorCuirass(this, inventoryPlayer, 38, 126, 26));
		addSlotToContainer(new SlotArmorGreaves(this, inventoryPlayer, 37, 126, 44));
		addSlotToContainer(new SlotArmorBoots(this, inventoryPlayer, 36, 126, 62));
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot)
	{
		if (slot < 0 || slot >= inventorySlots.size())
		{
			return ItemStack.EMPTY;
		}

		Slot slotObject = inventorySlots.get(slot);
		if (slotObject == null || !slotObject.getHasStack() || !slotObject.canTakeStack(player))
		{
			return ItemStack.EMPTY;
		}

		ItemStack stackInSlot = slotObject.getStack();
		ItemStack stack = stackInSlot.copy();
		EntityEquipmentSlot armorType = EntityLiving.getSlotForItemStack(stackInSlot);
		boolean moved = false;

		if (slot < 4)
		{
			int playerArmorSlot = getPlayerArmorContainerSlot(slot);
			if (playerArmorSlot >= 0 && !inventorySlots.get(playerArmorSlot).getHasStack())
			{
				moved = mergeItemStack(stackInSlot, playerArmorSlot, playerArmorSlot + 1, true);
			}
			if (!moved)
			{
				moved = mergeItemStack(stackInSlot, 4, 40, true);
			}
		}
		else if (slot < 40)
		{
			int standSlot = getStandSlot(armorType);
			int playerArmorSlot = getPlayerArmorContainerSlot(armorType);
			if (standSlot >= 0 && playerArmorSlot >= 0 && !inventorySlots.get(playerArmorSlot).getHasStack())
			{
				moved = mergeItemStack(stackInSlot, playerArmorSlot, playerArmorSlot + 1, false);
			}
			if (!moved && standSlot >= 0 && !inventorySlots.get(standSlot).getHasStack())
			{
				moved = mergeItemStack(stackInSlot, standSlot, standSlot + 1, false);
			}
		}
		else
		{
			int standSlot = getStandSlot(armorType);
			if (standSlot >= 0 && !inventorySlots.get(standSlot).getHasStack())
			{
				moved = mergeItemStack(stackInSlot, standSlot, standSlot + 1, false);
			}
			if (!moved)
			{
				moved = mergeItemStack(stackInSlot, 4, 40, false);
			}
		}

		if (!moved)
		{
			return ItemStack.EMPTY;
		}

		if (stackInSlot.isEmpty())
		{
			slotObject.putStack(ItemStack.EMPTY);
		}
		else
		{
			slotObject.onSlotChanged();
		}
		slotObject.onTake(player, stackInSlot);
		return stack;
	}

	private int getPlayerArmorContainerSlot(int standSlot)
	{
		return standSlot >= 0 && standSlot < 4 ? 40 + standSlot : -1;
	}

	private int getPlayerArmorContainerSlot(EntityEquipmentSlot armorType)
	{
		int standSlot = getStandSlot(armorType);
		return getPlayerArmorContainerSlot(standSlot);
	}

	private int getStandSlot(EntityEquipmentSlot armorType)
	{
		if (armorType == EntityEquipmentSlot.HEAD)
		{
			return 0;
		}
		if (armorType == EntityEquipmentSlot.CHEST)
		{
			return 1;
		}
		if (armorType == EntityEquipmentSlot.LEGS)
		{
			return 2;
		}
		if (armorType == EntityEquipmentSlot.FEET)
		{
			return 3;
		}
		return -1;
	}
	
	public boolean armorTest(Item armorItem, int armortype, int slot)
	{
		
		if (armorItem instanceof ItemArmor && armortype == slot)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
}
