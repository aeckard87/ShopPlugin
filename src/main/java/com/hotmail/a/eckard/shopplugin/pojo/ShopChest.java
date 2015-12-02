package com.hotmail.a.eckard.shopplugin.pojo;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
	
	public boolean shopChestExist(Block clickedBlock, ShopSign sign){
		//0=no found chests
		//1= 1 chest found
		//2 both chests found
		
		int result=0;
		BlockFace[] blockFace = sign.getChestDir();
		for(BlockFace face: blockFace){
			Block chest = clickedBlock.getRelative(face);
			if(chest.getState() instanceof Chest){
				result += 1;
			}
		}
		if(result == 2) return true;

		message = " I couldn't find the shop's chests. Please make sure the shop has been set up properly.";
		return false;
		
		
	/*	BlockFace[][] blockFaces = {{BlockFace.NORTH,BlockFace.SOUTH},{BlockFace.EAST,BlockFace.WEST}};
		
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
		return false;*/
	}
	
	public boolean saveInventory(Block block, ItemStack itemStack, ItemStack profitStack){
		boolean containsItem = false;
		boolean containsProfit = false;
		if(block.getState() instanceof Chest){
			Chest chest = (Chest) block.getState();

			if(chest.getInventory() instanceof DoubleChestInventory){
				DoubleChestInventory dInv = (DoubleChestInventory) chest.getInventory();

				//we also need to check it the chest has different stack items and fail on this 
				containsItem = doesChestHaveItem(dInv, itemStack);
				containsProfit = doesChestHaveItem(dInv, profitStack);
				
				if(containsItem){
					if(!isInvAvailable(dInv, itemStack)){
						return false;
					}
					setInv(dInv);
					
				}else if(containsProfit){
					if(!chestHasRoom(dInv, profitStack)){
						return false;
					}
					setProfits(dInv);
				}
				
			}
		}
		return true;
	}
	
	public boolean setShopChest(Block clickedBlock, ShopSign sign){
		//this needs to handle inv chest as always on the left of a sign and price always on the right of a sign
		BlockFace[][] blockFaces = {{BlockFace.NORTH,BlockFace.SOUTH},{BlockFace.EAST,BlockFace.WEST}};
		
		/*
		 * Test code for yaw of player to dertmine left/inv and right/profits
		 */
		//amt -10(left) 10(right)
	//	int amt = 0;
		//direction 1(front/back) 0(left/right)
	//	int direction = 0;
	//	float z = (float) (player.getLocation().getZ() + 0 *Math.sin(Math.toRadians(player.getLocation().getYaw() +90 * -10)));
		
		
		
		
		//-------------------------------------------------------------
		
		
		for(int i=0;i<blockFaces.length;i++){
			Block chestBlockA = clickedBlock.getRelative(blockFaces[i][0]);
			if(saveInventory(chestBlockA, sign.getItem(), sign.getPrice())){
				Block chestBlockB = clickedBlock.getRelative(blockFaces[i][1]);
				saveInventory(chestBlockB, sign.getItem(),sign.getPrice());
			}
		}
		if(getInv()== null || getProfits() == null){
			message = "The shop may have not been set up properly. Please come back another time.";
			return false;
		}
		return true;
	}
	
	public boolean chestHasRoom(DoubleChestInventory inv, ItemStack item){
		int found = item.getAmount();
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
	
	public boolean isInvAvailable(DoubleChestInventory inv, ItemStack item){
		int total = 0;

		ItemStack[] contents = inv.getContents();

		for(ItemStack stack:contents){
			if(stack != null){
				if(stack.getType() == item.getType()){
					total = total + stack.getAmount();
				}
			}
		}
		
		if(!(total >= item.getAmount())){
			message = " We are sorry, we're currently out of " + item.getData() + ". Please check back soon!";
		}
		return total >= item.getAmount();
	}
	
	public void updateProfit(ShopSign sign){
		//needs logging
		//profits.addItem(new ItemStack(sign.getPrice(),sign.getPriceQuantity()));
		profits.addItem(sign.getPrice());
	}
	
	public void updateInv(ShopSign sign){
		//needs logging
		//inv.removeItem(new ItemStack(sign.getItem(),sign.getItemQuantity()));
		inv.removeItem(sign.getItem());
	}
	
	
	public boolean findValues(ShopSign shopSign, Block clickedBlock){
		//what happens when thre are different materials in the chest...
		BlockFace[][] blockFaces = {{BlockFace.NORTH,BlockFace.SOUTH},{BlockFace.EAST,BlockFace.WEST}};
		
		for(int i=0;i<blockFaces.length;i++){
			Block chestBlockA = clickedBlock.getRelative(blockFaces[i][0]);
			
			if(chestBlockA.getState() instanceof Chest){

				Chest chestA = (Chest) chestBlockA.getState();

				if(chestA.getInventory() instanceof DoubleChestInventory){
					DoubleChestInventory dInv = (DoubleChestInventory) chestA.getInventory();

					ItemStack[] contents = dInv.getContents();
					for(ItemStack stack : contents){
						//handle mulitple types of stacks!
						if(stack !=null){
							shopSign.setItemData(stack.getTypeId(), stack.getDurability());
						}
					}
					
				}

				
				Block chestBlockB = clickedBlock.getRelative(blockFaces[i][1]);
				if(chestBlockB.getState() instanceof Chest){
					Chest chestB = (Chest) chestBlockB.getState();
					
					if(chestB.getInventory() instanceof DoubleChestInventory){
						DoubleChestInventory dInvB = (DoubleChestInventory) chestB.getInventory();

						ItemStack[] contentsB = dInvB.getContents();
						for(ItemStack stack : contentsB){
							if(stack !=null){
								shopSign.setPriceData(stack.getTypeId(), stack.getDurability());
							}
						}
					}
				}
			}
		}	
		
		if(shopSign.getItem() == null || shopSign.getPrice() == null){
			message = "Shop was not successfully created! Please make sure your chests have inventory or price in them and are in the correctly location.";
			return false;
		}else if(shopSign.getItem() != null && shopSign.getPrice() != null){
			return true;
		}
		message = "what did you do to get here";
		return false;
	}
	
	public boolean doesChestHaveItem(Inventory inv, ItemStack itemStack){
		for(ItemStack stack: inv.getContents()){
			if(stack !=null){
				if(stack.getType() == itemStack.getType() && stack.getDurability() == itemStack.getDurability()){
					return true;
				}
			}
		}
		return false;
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
