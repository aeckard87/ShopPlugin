package com.hotmail.a.eckard.shopplugin.pojo;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.gson.Gson;

public class ShopChest {
	public Inventory inv;
	public Inventory profits;
	public String message ="ShopMessage:";
	public boolean shopChestExists;
	public boolean chestHasRoom;
	public boolean isInvAvailable;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isShopChestExists() {
		return shopChestExists;
	}

	public void setShopChestExists(boolean shopChestExists) {
		this.shopChestExists = shopChestExists;
	}

	public boolean isChestHasRoom() {
		return chestHasRoom;
	}

	public void setChestHasRoom(boolean chestHasRoom) {
		this.chestHasRoom = chestHasRoom;
	}

	public boolean isInvAvailable() {
		return isInvAvailable;
	}

	public void setInvAvailable(boolean isInvAvailable) {
		this.isInvAvailable = isInvAvailable;
	}

	public Inventory getInv() {
		return inv;
	}

	public void setInv(Inventory inv) {
		this.inv = inv;
	}

	public Inventory getProfits() {
		return profits;
	}

	public void setProfits(Inventory profits) {
		this.profits = profits;
	}
	
	public boolean shopChestExist(Block clickedBlock){
		BlockFace[][] blockFaces = {{BlockFace.NORTH,BlockFace.SOUTH},{BlockFace.EAST,BlockFace.WEST}};
		
		for(int i=0;i<blockFaces.length;i++){
			Block chestBlockA = clickedBlock.getRelative(blockFaces[i][0]);
			if(chestBlockA.getState() instanceof Chest){
				Block chestBlockB = clickedBlock.getRelative(blockFaces[i][1]);
				if(chestBlockB.getState() instanceof Chest){
					return true;
				}
			}
		}
		message = " I couldn't find the shop's chests. Please make sure the shop has been set up properly.";
		return false;
	}
	
	public boolean setShopChest(Block clickedBlock, ShopSign sign){
		BlockFace[][] blockFaces = {{BlockFace.NORTH,BlockFace.SOUTH},{BlockFace.EAST,BlockFace.WEST}};
		boolean containsItem = false;
		boolean containsProfit = false;
		//inv.contains(material) is deprecated use inv.contains(ItemStack)
		ItemStack itemStack = new ItemStack(sign.getItem());
		ItemStack profitStack = new ItemStack(sign.getPrice());
		
		for(int i=0;i<blockFaces.length;i++){
			Block chestBlockA = clickedBlock.getRelative(blockFaces[i][0]);
			
			if(chestBlockA.getState() instanceof Chest){
				Chest chestA = (Chest) chestBlockA.getState();

				if(chestA.getInventory() instanceof DoubleChestInventory){
					DoubleChestInventory dInv = (DoubleChestInventory) chestA.getInventory();

					containsItem = dInv.containsAtLeast(itemStack, 1);
					containsProfit = dInv.containsAtLeast(profitStack,1);
					
					if(containsItem){
						if(!isInvAvailable(dInv, itemStack, sign.getItemQuantity())){
							return false;
						}
						setInv(dInv);
						
					}else if(containsProfit){
						if(!chestHasRoom(dInv, profitStack, sign.getPriceQuantity())){
							return false;
						}
						setProfits(dInv);
					}
					
				}

				
				Block chestBlockB = clickedBlock.getRelative(blockFaces[i][1]);
				if(chestBlockB.getState() instanceof Chest){
					Chest chestB = (Chest) chestBlockB.getState();
					
					if(chestB.getInventory() instanceof DoubleChestInventory){
						DoubleChestInventory dInvB = (DoubleChestInventory) chestB.getInventory();

						if(!containsItem){
							containsItem = dInvB.containsAtLeast(itemStack,1);
							if(containsItem){
								if(!isInvAvailable(dInvB, itemStack, sign.getItemQuantity())){
									return false;
								}
								setInv(dInvB);
							}
						}else if(!containsProfit){
							containsProfit = dInvB.containsAtLeast(profitStack,1);
							if(containsProfit){
								if(!chestHasRoom(dInvB, profitStack, sign.getPriceQuantity())){
									return false;
								}
								setProfits(dInvB);
							}
						}
					}
				}
			}
		}
		if(getInv()== null || getProfits() == null){
			message = "The shop may have not been set up properly. Please come back another time.";
			return false;
		}
		return true;
	}
	
	public boolean chestHasRoom(DoubleChestInventory inv, ItemStack item, int quantity){
		item.setAmount(quantity);
		int found = quantity;
		boolean hasRoom = false;
		for(ItemStack stack : inv.getContents()){
			if(stack == null) {
				found -= item.getMaxStackSize();
			}else if(stack.getType()== item.getType()){//error null pointer
				found -= item.getMaxStackSize() - stack.getAmount();
			}
		}
		hasRoom = found <=0;
		if(!hasRoom) message = " Shop Chest is too full to accept anymore " + item.getType() + "!";
		return hasRoom;
	}
	
	public boolean isInvAvailable(DoubleChestInventory inv, ItemStack item, int quantity){
		item.setAmount(quantity+1);
		int total = 0;

		ItemStack[] contents = inv.getContents();

		for(ItemStack stack:contents){
			if(stack != null){
				if(stack.getType() == item.getType()){
					total = total + stack.getAmount();
				}
			}
		}
		
		if(!(total >= quantity+1)){
			message = " We are sorry, we're currently out of " + item.getType() + ". Please check back soon!";
		}
		return total >= quantity+1;
	}
	
	public void updateProfit(ShopSign sign){
		//needs logging
		profits.addItem(new ItemStack(sign.getPrice(),sign.getPriceQuantity()));
	}
	
	public void updateInv(ShopSign sign){
		//needs logging
		inv.removeItem(new ItemStack(sign.getItem(),sign.getItemQuantity()));
	}
	
	public void clear(){
		inv=null;
		profits=null;
		message ="";
		shopChestExists=false;
		chestHasRoom=false;
		isInvAvailable=false;
	}

}
