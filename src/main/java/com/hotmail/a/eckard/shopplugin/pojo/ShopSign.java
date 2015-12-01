package com.hotmail.a.eckard.shopplugin.pojo;

import org.bukkit.Material;
import org.bukkit.block.Sign;

public class ShopSign {
	public Material item;
	public int itemQuantity;
	public Material price;
	public int priceQuantity;
	public ShopChest shopChest;

	public ShopChest getShopChest() {
		return shopChest;
	}

	public void setShopChest(ShopChest shopChest) {
		this.shopChest = shopChest;
	}

	public Material getItem() {
		return item;
	}

	public void setItem(Material item) {
		this.item = item;
	}

	public int getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(int itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public Material getPrice() {
		return price;
	}

	public void setPrice(Material price) {
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
		if(lines[0].trim().equalsIgnoreCase("[shop]") && signLength == 4){
			return true;
		}else{
			return false;
		}
		
	}
	
	public void setShopValues(Sign sign){
		String[] lines = sign.getLines();

		String itemParts[] = lines[1].trim().split("[\\[\\]]");
		String priceParts[] = lines[3].trim().split("[\\[\\]]");
		
		//hold parts values
		if(itemParts.length == 2){
			itemQuantity = Integer.parseInt(itemParts[0]);
			item = Material.getMaterial(Integer.parseInt(itemParts[1]));
		}
		
		if(priceParts.length == 2){
			priceQuantity = Integer.parseInt(priceParts[0]);
			price = Material.getMaterial(Integer.parseInt(priceParts[1]));
		}
	}

	public void clear(){
		
		item = null;
		itemQuantity=0;
		price=null;
		priceQuantity=0;
		shopChest=null;
	}
}
