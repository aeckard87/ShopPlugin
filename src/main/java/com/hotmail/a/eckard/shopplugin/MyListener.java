package com.hotmail.a.eckard.shopplugin;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.hotmail.a.eckard.shopplugin.pojo.PlayerStuff;
import com.hotmail.a.eckard.shopplugin.pojo.ShopChest;
import com.hotmail.a.eckard.shopplugin.pojo.ShopSign;

public class MyListener implements Listener{
	/*
	 ***In Progress***
	 * 
	 * - Purchasing - 
	 * 	Assuming shop is set up accurately, which currently requires player to know the material
	 * 	ID for shop set up, all the customer needs is the price of the item in their hot bar
	 * 		*Still need to check if customer's inv has enough space
	 * 		*Protect the shop from being edited by the customer if not in a beaconed area.
	 * 
	 * 	Shop requires a double chest to function properly
	 * 
	 * - Current Shop Set up in world- 
	 * C=Chest
	 * S=Sign
	 * X=Block
	 * 		*The Sign must be mounted on a wall and not a sign post currently
	 * 
	 * C	X	C
	 * C	S	C
	 * 
	 ***Things to be done**
	 *
	 *	JUNIT Testing
	 *
	 * - Admin Shop - 
	 * 	How to designate an admin shop from a player shop
	 * 	AdminShop inv never needs to be updated.
	 * 	Does the admin profits chest still fill? Y/N
	 * 
	 * - Shop Creation - 
	 *  DoubleChests on each side with at least 1 item to sell in one and 1 item of cost in the other
	 *  then create sign as follows
	 * 		[shop]
	 * 		10
	 * 		for
	 * 		15
	 * 	assuming item to sell is stone and cost is cobble the sign will be updated to this
	 * 		[shop]
	 * 		10[1]
	 * 		for
	 * 		15[4]
	 * 	where [#] is the id of the number
	 * 	It is suggested to add a sign or item frame to the chests showing which one is which.
	 * 
	 * 	Allow shop to work with single and double chests (currently works only with double chest)
	 */	
	
	/* Example
	 * line
		0	[store]
		1	#[item]
		2	for
		3	#[price]
	 * 
	 */
	
	//Customer Purchase
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		Block b = event.getClickedBlock();
		ShopSign shopSign = new ShopSign();
		ShopChest shopChest = new ShopChest();
		PlayerStuff consumer = new PlayerStuff();
		
		if(event.getAction()== Action.RIGHT_CLICK_BLOCK && b.getType() == Material.WALL_SIGN && b.getState() instanceof Sign){
			//verify shop sign
			if(shopSign.isShopSign((Sign) b.getState())){
				shopSign.setShopValues((Sign) b.getState());//needs a failure message
				
				//check if shop chests are there and have room/inv
				if(shopChest.shopChestExist(b)){
					boolean shopChestSet = shopChest.setShopChest(b, shopSign);
					if(shopChestSet){
						//check if player has payment in hotbar
						consumer.setMoney(player.getInventory());
						if(consumer.isMoneyInHotBar(player.getInventory(), shopSign)){
							if(consumer.hasEnoughMoney(player.getInventory(),shopSign)){
								
								consumer.payMerchant(shopSign);
								shopChest.updateInv(shopSign);
								shopChest.updateProfit(shopSign);
								
								player.sendMessage(consumer.getMessage());
								
								consumer.clear();
								shopChest.clear();
								shopSign.clear();
								
							}else{
								player.sendMessage(consumer.getMessage());
							}
						}else{
							player.sendMessage(consumer.getMessage());
						}
					}else{
						player.sendMessage(shopChest.getMessage());
					}
				}
			}
		}//else not a shop action
	}
	
	//Shop Creation
	@EventHandler
	public void onSignChange(SignChangeEvent event){
		/*
		 * Creating a shop
		 * DoubleChests on each side with at least 1 item to sell in one and 1 item of cost in the other
		 * then create sign as follows
		 * [shop]
		 * 10
		 * for
		 * 15
		 * assuming item to sell is stone and cost is cobble the sign will be updated to this
		 * [shop]
		 * 10[1]
		 * for
		 * 15[4]
		 */
		
	}
}
