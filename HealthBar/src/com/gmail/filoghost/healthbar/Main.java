package com.gmail.filoghost.healthbar;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.filoghost.healthbar.metrics.MetricsLite;
import com.gmail.filoghost.healthbar.utils.Debug;
import com.gmail.filoghost.healthbar.utils.PlayerBarUtils;
import com.gmail.filoghost.healthbar.utils.Utils;


public class Main extends JavaPlugin {
	
	public static 	Main plugin;
	public static	Logger logger;
	
	private static	DamageListener damageListener;
	private static	DeathListener deathListener;
	private static 	MiscListeners miscListeners;

	@Override
	public void onEnable() {
		
		plugin = this;
		logger = getLogger();
		
		
		try {
			String build = Utils.getBukkitBuild();
			if (build != null) {
				if (Integer.parseInt(build) < 2811) {	
					logger.warning("------------------------------------------");
					logger.warning("Your bukkit build (#" + build + ") is old.");
					logger.warning("HealthBar cannot work properly,");
					logger.warning("please update CraftBukkit.");
					logger.warning("------------------------------------------");
					this.setEnabled(false);
					return;
				}
			}
		} catch (Exception ignore) {}
		
		
		damageListener = new DamageListener();
		deathListener = new DeathListener();
		miscListeners = new MiscListeners();
		
		//to check if I've forgot the debug on :)
		Debug.color("§c[HealthBar] Debug ON");		
		
		//create the folder and the file
		if (getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		Utils.loadFile("config.yml", this);
			
		//register events
		getServer().getPluginManager().registerEvents(damageListener, this);
		getServer().getPluginManager().registerEvents(deathListener, this);
		getServer().getPluginManager().registerEvents(miscListeners, this);
		
		//other files
		reloadConfigFromDisk();
		FileConfiguration config = getConfig();
		
		
		//try to check updates
		Updater.UpdaterHandler.setup(this, 54447, "§2[§aHealthBar§2] ", super.getFile(), ChatColor.GREEN, "/hbr update", "health-bar");
		
		if (config.getBoolean("update-notification")) {
			Thread updaterThread = new Thread(new Runnable() { public void run() {
				Updater.UpdaterHandler.startupUpdateCheck();
			}});
			
			updaterThread.start();
		}
		
			
		//setup for command executor
		getCommand("healthbar").setExecutor(new Commands(this));
			
		//metrics
		try {
			MetricsLite metrics = new MetricsLite(this);
			metrics.start();
		} catch (Exception e) {}
			
			
//end of onEnable
	}	

	@Override
	public void onDisable() {
		PlayerBarUtils.removeAllHealthbarTeams(Bukkit.getScoreboardManager().getMainScoreboard());
		PlayerBar.removeBelowObj();
		DamageListener.removeAllMobHealthBars();
		System.out.println("HealthBar disabled, all the health bars have been removed.");
	}
	
	
	public void reloadConfigFromDisk() {
		
		reloadConfig();
		//Utils.checkDefaultNodes(getConfig(), this);
		Configuration.checkConfigYML();
		
		Utils.loadFile("custom-mob-bar.yml", this);
		Utils.loadFile("custom-player-bar.yml", this);
		Utils.loadFile("locale.yml", this);
		Utils.loadFile("config.yml", this);
		
		//forces to generate translations, if missing
		Utils.getTranslationMap(this);
		
		DamageListener.loadConfiguration();
		DeathListener.loadConfiguration();
		PlayerBar.loadConfiguration();
		MiscListeners.loadConfiguration();
	}

	public static MiscListeners getLoginListenerInstance() {
		return miscListeners;
	}
	
	public static File getPluginFile() {
		return plugin.getFile();
	}

//end of the class
}
