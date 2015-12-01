package com.hotmail.a.eckard.shopplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(new MyListener(), this);
		getLogger().info("onEnable has been invoked!");
	}
	
	@Override
	public void onDisable(){
		
	}


}
