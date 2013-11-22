package com.gmail.filoghost.healthbar;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.gmail.filoghost.healthbar.api.BarHideEvent;
import com.gmail.filoghost.healthbar.utils.PlayerBarUtils;
import com.gmail.filoghost.healthbar.utils.Utils;

public class PlayerBar {
	private final static	 	Plugin instance = Main.plugin;
	private static	 			Scoreboard sb = instance.getServer().getScoreboardManager().getMainScoreboard();

	private static boolean playerEnabled;
	private static boolean textMode;
	private static boolean useBelow;
	private static boolean belowUseProportion;
	private static int belowNameProportion;
	private static Objective belowObj;
	
	private static boolean useCustomBar;
	
	//static class
	private PlayerBar() {}
	
	public static void setupBelow() {
		
		//remove previous objectives under the name
		removeBelowObj();
		
		if (playerEnabled && useBelow) {
			//create the objective
			belowObj = sb.registerNewObjective("healthbarbelow", "dummy");
			belowObj.setDisplayName(Utils.replaceSymbols(instance.getConfig().getString("player-bars.below-name.text")));
			belowObj.setDisplaySlot(DisplaySlot.BELOW_NAME);
		}
		
	}
	
	public static void removeBelowObj() {
		if (sb.getObjective(DisplaySlot.BELOW_NAME)!=null)
			sb.getObjective(DisplaySlot.BELOW_NAME).unregister();
		if (sb.getObjective("healthbarbelow") != null)
			sb.getObjective("healthbarbelow").unregister();
	}
	
	public static boolean hasHealthDisplayed(Player player) {
		Team team = sb.getPlayerTeam((OfflinePlayer)player);
		if (team == null){
			return false;
		}
		if (sb.getPlayerTeam((OfflinePlayer)player).getName().contains("hbr")) return true;
		return false;
	}
	
	public static void hideHealthBar (Player player) {
		Team team = sb.getTeam("hbr0");
		if (team == null) {
			team = sb.registerNewTeam("hbr0");
			team.setCanSeeFriendlyInvisibles(false);
		}
		OfflinePlayer offPlayer = (OfflinePlayer) player;
		team.addPlayer(offPlayer);
		
		//api - call the custom event after hiding the bar
		instance.getServer().getPluginManager().callEvent(new BarHideEvent(offPlayer));
	}
	
	public static void updateHealthBelow (final Player player) {
		if (useBelow && playerEnabled) {
			int score = 0;
			
			if (belowUseProportion)
				score = roundUp((player.getHealth()) * ((double) belowNameProportion) / (player.getMaxHealth()));
			else
				score = roundUp(player.getHealth());
					
			belowObj.getScore(player).setScore(score);
		}
	}
	
	public static void setHealthSuffix (Player player, double health, double max) {
		
		OfflinePlayer op = (OfflinePlayer) player;
		
		if (useCustomBar || (!textMode)) {
			int healthOn10 = roundToNearest((health * 10.0)/max);
			sb.getTeam("hbr" + Integer.toString(healthOn10)).addPlayer((op));
			return;
		} else {
			
			int intHealth = roundUp(health);
			int intMax = roundUp(max);
			
			String color = getColor(health, max);
			Team team = sb.getTeam("hbr" + intHealth + "-" + intMax);
			if (team == null) {
				team = sb.registerNewTeam("hbr" + intHealth + "-" + intMax);
				team.setSuffix(" - " + color + intHealth + "§7/§a" + intMax);
				team.setCanSeeFriendlyInvisibles(false);
			}
			team.addPlayer(op);	
			return;
		}
	}

	public static String getColor (double health, double max) {
		double ratio = health/max;
		if (ratio > 0.5) 	return "§a"; //more than half health -> green
		if (ratio > 0.25) 	return "§e"; //more than quarter health -> yellow
		return "§c"; //critical health -> red
	}

	private static int roundToNearest(double d) {
	    int i = (int) d;
	    if ((d - (double)i)>0.00001) {
	    	i++;
	    }
	    if (i<1) { 	return 1; }
	    if (i>10) {	return 10; }
	    return i;
	}
	
	private static int roundUp(double d) {
	    int i = (int) d;
	    if ((d - (double)i)>0.00001) {
	    	i++;
	    }
	    if (i<0) return 0;
	    
	    return i;  
	}
	
	public static void loadConfiguration() {
		        
        //remove all teams
		sb = instance.getServer().getScoreboardManager().getMainScoreboard();
		PlayerBarUtils.removeAllHealthbarTeams(sb);
		
		FileConfiguration config = instance.getConfig();

		playerEnabled = 		config.getBoolean("player-bars.enable");
		textMode = 				config.getBoolean("player-bars.after-name.text-mode");
		useCustomBar = 			config.getBoolean("player-bars.after-name.use-custom-file");
		useBelow = 				config.getBoolean("player-bars.below-name.enable");
		belowUseProportion = 	config.getBoolean("player-bars.below-name.use-proportion");
		belowNameProportion = 	config.getInt("player-bars.below-name.proportional-to");
		
		setupBelow();
		
		if (useCustomBar) {
			PlayerBarUtils.create10CustomTeams(sb, Utils.loadFile("custom-player-bar.yml", instance));
		} else if (!textMode) {
			PlayerBarUtils.create10DefaultTeams(sb, config.getInt("player-bars.after-name.display-style"));
		}
		//else creates the teams at the moment
		
		PlayerBarUtils.setAllTeamsInvisibility(sb);
	}
}
