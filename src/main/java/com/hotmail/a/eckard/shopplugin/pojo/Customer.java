package com.hotmail.a.eckard.shopplugin.pojo;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Customer {
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
				if(inv.getItem(i).getType() == sign.getPrice().getType()){
					if(inv.getItem(i).getAmount() >= sign.getPrice().getAmount()){
						return true;
					}
				}
			}
		}
		message = " You do not have enough " + sign.getPrice().getType() + " for this purchase." ;
		return false;
	}
	
	public boolean isMoneyInHotBar(Inventory inv, ShopSign sign){
		for(int i=0; i<9;i++){
			if(inv.getItem(i)!=null){
				if(inv.getItem(i).getType() == sign.getPrice().getType()){
					return true;
				}
			}
		}
		message.concat(" Please make sure your payment is in the hotbar.");
		return false;
	}

	public void payMerchant(ShopSign sign){
		//need lb logging here
		//money.removeItem(new ItemStack(sign.getPrice(),sign.getPriceQuantity()));
		money.removeItem(sign.getPrice());
		sign.getItem().setAmount(sign.getItem().getAmount()-1);
		money.addItem(sign.getItem());

		String product = sign.getItem().getData().toString();
		product = product.replaceAll("[(\\d)]", "");
		
		String price = sign.getPrice().getData().toString();
		price = price.replaceAll("[(\\d)]", "");
		
		message = "You have successfully purchased " + sign.getItemQuantity() + " " + product + " for " + sign.getPrice().getAmount() + " " + price;
	}
	
	public void clear(){

		money=null;
		message = "";
	}
}
