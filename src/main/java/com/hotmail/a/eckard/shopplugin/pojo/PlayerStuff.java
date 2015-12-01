package com.hotmail.a.eckard.shopplugin.pojo;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerStuff {
	public Inventory money;
	public String message = "";

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public Inventory getMoney() {
		return money;
	}

	public void setMoney(Inventory money) {
		this.money = money;
	}
	
	public boolean hasEnoughMoney(Inventory inv, ShopSign sign){
		for(int i=0; i<9;i++){
			if(inv.getItem(i)!=null){
				if(inv.getItem(i).getType() == sign.getPrice()){
					if(inv.getItem(i).getAmount() >= sign.getPriceQuantity()){
						return true;
					}
				}
			}
		}
		message = " You do not have enough " + sign.getPrice() + " for this purchase." ;
		return false;
	}
	
	public boolean isMoneyInHotBar(Inventory inv, ShopSign sign){
		for(int i=0; i<9;i++){
			if(inv.getItem(i)!=null){
				if(inv.getItem(i).getType() == sign.getPrice()){
					return true;
				}
			}
		}
		message.concat(" Please make sure your payment is in the hotbar.");
		return false;
	}

	public void payMerchant(ShopSign sign){
		//need lb logging here
		money.removeItem(new ItemStack(sign.getPrice(),sign.getPriceQuantity()));
		money.addItem(new ItemStack(sign.getItem(), sign.getItemQuantity()));
		message = "You have successfully purchased " + sign.getItemQuantity() + " " + sign.getItem() + " for " + sign.getPriceQuantity() + " " + sign.getPrice();
	}
	
	public void clear(){

		money=null;
		message = "";
	}
}
