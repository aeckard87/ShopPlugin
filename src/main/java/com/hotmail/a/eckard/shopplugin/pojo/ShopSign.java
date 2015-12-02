package com.hotmail.a.eckard.shopplugin.pojo;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;


public class ShopSign {
	public ItemStack item;
	public int itemQuantity;
	public ItemStack price;
	public int priceQuantity;
	public String message="";
	public BlockFace[] chestDir;

	public BlockFace[] getChestDir() {
		return chestDir;
	}

	public void setChestDir(BlockFace[] chestDir) {
		this.chestDir = chestDir;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public int getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(int itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public ItemStack getPrice() {
		return price;
	}

	public void setPrice(ItemStack price) {
		this.price = price;
	}

	public int getPriceQuantity() {
		return priceQuantity;
	}

	public void setPriceQuantity(int priceQuantity) {
		this.priceQuantity = priceQuantity;
	}
	
	public boolean isShopSign(Sign sign){
		String[] lines = sign.getLines();
		int signLength = lines.length;
		if(lines[0].trim().equalsIgnoreCase("[shop]")){
			return true;
		}else{
			return false;
		}
		
	}
	
	public boolean createShop(SignChangeEvent e){
		String[] lines = e.getLines();
		if(e.getLines().length == 4){			
			//Inventory Line update
			if(lines[1].isEmpty() || lines[1].equalsIgnoreCase("")){
				message = "Line 2 is missing Inventory Quantity to sell.";
				e.setLine(0, "[Shop-Error]");
				return false;
			}else if(e.getLine(1).trim().split("[^\\d]").length == 0){
				message = "Please make sure to follow sign format corretly. Second line is not a number!";
				e.setLine(0, "[Shop-Error]");
				return false;
			}else{
				String invLine = e.getLine(1)+ "[" + item.getTypeId() + ":"+ item.getDurability() + "]";
				e.setLine(1, invLine);
				e.setLine(2, "for");
			}
			
			//Price Line update
			if(lines[3].isEmpty() || lines[3].equalsIgnoreCase("")){
				message = "Line 4 is missing Price of purchase number.";
				e.setLine(0, "[Shop-Error]");
				return false;
			}else if(e.getLine(3).trim().split("[^\\d]").length  == 0){
				message = "Please make sure to follow sign format corretly. Fourth line is not a number!";
				e.setLine(0, "[Shop-Error]");
				return false;
			}else{
				String priceLine = e.getLine(3)+ "[" + price.getTypeId() + ":"+ price.getDurability() + "]";
				e.setLine(3, priceLine);
			}

			message = "Shop Created Successfully!";
			e.setLine(0, "[Shop]");
			e.setLine(2, "for");
			return true;
		}else{
			message = "Sign does not have enough arguments to be created successfully.";
			return false;
		}
	}
	
	public boolean findGlassBlock(Block sign){
		//[0]= Inventory
		//[1]= Profit
		if(sign.getLocation().subtract(1,0,0).getBlock().getType() == Material.STAINED_GLASS || sign.getLocation().subtract(1,0,0).getBlock().getType() == Material.GLASS ){
			//Glass is west to sign: inv is south and profit north
			/*System.out.println("1. Location: " + sign.getLocation().subtract(1,0,0).getBlock().getLocation() + " is " + Material.GLASS);
			compare = sign.getLocation().subtract(1,0,0).getBlock();
			result = sign.getFace(compare);
			System.out.println("Sign Location: " + sign.getLocation());
			System.out.println("Result: " + result);*/
			
			BlockFace[] chestDir = {BlockFace.SOUTH,BlockFace.NORTH};
			setChestDir(chestDir);
			return true;
		}
		if(sign.getLocation().add(1,0,0).getBlock().getType() == Material.GLASS || sign.getLocation().add(1,0,0).getBlock().getType() == Material.STAINED_GLASS){
			//Glass is East of sign: inv north, and profit south
			BlockFace[] chestDir = {BlockFace.NORTH,BlockFace.SOUTH};
			setChestDir(chestDir);
			return true;
		}

		if(sign.getLocation().subtract(0,0,1).getBlock().getType() == Material.GLASS || sign.getLocation().subtract(0,0,1).getBlock().getType() == Material.STAINED_GLASS){
			//Glass is North: Inv west and Profit East
			BlockFace[] chestDir = {BlockFace.WEST,BlockFace.EAST};
			setChestDir(chestDir);
			return true;
		}
		if(sign.getLocation().add(0,0,1).getBlock().getType() == Material.GLASS || sign.getLocation().add(0,0,1).getBlock().getType() == Material.STAINED_GLASS){
			//Glass is South: Inv East and Profit West
			BlockFace[] chestDir = {BlockFace.EAST,BlockFace.WEST};
			setChestDir(chestDir);
			return true;
		}
		message = "Please make sure Shop Sign is on a glass block.";
		return false;
	}
	
	public boolean setShopValues(Sign sign){	
		String[] lines = sign.getLines();

		//needs to handle #[#:#]
		String itemParts[] = lines[1].trim().split("[^\\d]");
		String priceParts[] = lines[3].trim().split("[^\\d]");
		
		//hold parts values
		if(itemParts.length == 3){
			itemQuantity = Integer.parseInt(itemParts[0]);//this fails if user only puts in [shop] and no arguments
			item = new ItemStack(Material.getMaterial(Integer.parseInt(itemParts[1])));
			item.setDurability(Short.parseShort(itemParts[2]));
			item.setAmount(itemQuantity+1);//hold 1 to maintain chest
		}else{
			message = "I didn't understand the sign, please make sure it was created successfully. Problem reading line 2";
			return false;
		}
		
		if(priceParts.length == 3){
			priceQuantity = Integer.parseInt(priceParts[0]);
			price = new ItemStack(Material.getMaterial(Integer.parseInt(priceParts[1])));
			price.setDurability(Short.parseShort(priceParts[2]));
			price.setAmount(priceQuantity);
		}else{
			message = "I didn't understand the sign, please make sure it was created successfully. Problem reading line 4";
			return false;
		}
		return true;
	}
	
	public void setItemData(int id, short durability){
		item = new ItemStack(Material.getMaterial(id));
		item.setDurability(durability);
	}
	
	public void setPriceData(int id, short durability){
		price = new ItemStack(Material.getMaterial(id));
		price.setDurability(durability);
	}

	public void clear(){
		
		item = null;
		itemQuantity=0;
		price=null;
		priceQuantity=0;
	}
	
	public boolean isNumeric(String str){
		return str.matches("-?\\d+(\\.\\d+)?");
	}
}
