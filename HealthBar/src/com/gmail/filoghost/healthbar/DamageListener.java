package com.gmail.filoghost.healthbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ThaH3lper.com.API.EpicBossAPI;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.gmail.filoghost.healthbar.utils.MobBarsUtils;
import com.gmail.filoghost.healthbar.utils.Utils;

public class DamageListener implements Listener {
	
	private final static 	Plugin plugin = Main.plugin;
	private static			BukkitScheduler scheduler = Bukkit.getScheduler();
	
	//mob vars
	public static 		boolean mobEnabled;
	private static		String[] barArray;
	private static		boolean mobUseText;
	private static		boolean mobUseCustomText;
	private static		String mobCustomText;
	private static		boolean customTextContains_Name;
	private static		boolean mobSemiHidden;
	protected static	long mobHideDelay;
	private static		boolean mobUseCustomBar;
	private static		boolean showOnCustomNames;
	private static		BarType barStyle;
	
	//player vars
	private static		boolean playerEnabled;
	private static		long playerHideDelay;
	private static		boolean playerUseAfter;
	
	//hooks and instances of other plugins
	private static 		boolean hookEpicboss;
	
	private static 		Map<String,String> localeMap = new HashMap<String, String>();
	private static 		Map<String,Integer> playerTable = new HashMap<String, Integer>();
	private static		Map<Integer,Integer> mobTable = new HashMap<Integer, Integer>();
	private static		Map<Integer,StringBoolean> namesTable = new HashMap<Integer, StringBoolean>();
	
	//disabled worlds names
	private static 		boolean mobUseDisabledWorlds;
	private static 		List<String> mobDisabledWorlds = new ArrayList<String>();	
	
	//disabled mobs
	private static		boolean mobTypeDisabling;
	private static		List<EntityType> mobDisabledTypes = new ArrayList<EntityType>();
	
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onEntityDamageEvent(EntityDamageEvent event) {

		Entity entity = event.getEntity();
		 
		if (!(entity instanceof LivingEntity)) return;
		LivingEntity living = (LivingEntity) entity;
		if (living.getNoDamageTicks() > living.getMaximumNoDamageTicks() / 2F) return;

		 
		if (entity instanceof Player) {
			if (playerEnabled) {
				parsePlayerHit((Player) entity, event instanceof EntityDamageByEntityEvent);
				return;
			}
		 }
		 
		 if (mobEnabled) {
			parseMobHit(living, event instanceof EntityDamageByEntityEvent);
		 }		 
	}
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onEntityRegain(EntityRegainHealthEvent event) {
		
		Entity entity = event.getEntity();
		
		if (playerEnabled) {
			if (entity instanceof Player) {
				parsePlayerHit((Player) entity, event.getRegainReason() != RegainReason.SATIATED && event.getAmount() > 0.0);
				return;
			}
		}
		if (mobEnabled) {
			if (entity instanceof LivingEntity) {
				parseMobHit((LivingEntity) entity, true);
			}
		}
	}
	
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onEntitySpawn(CreatureSpawnEvent event) {
		if (mobHideDelay == 0L && mobEnabled) {
			//show the bar on all the mobs
			final LivingEntity mob = event.getEntity();
			scheduler.scheduleSyncDelayedTask(plugin, new Runnable() { public void run() {
				parseMobHit(mob, true);
			}}, 1L);
		}
	}
	
	
/*
 * 			##################################################
 *		 	#				END OF THE LISTENERS 			 #
 * 			##################################################
 */
	
	
	private static void parsePlayerHit (final Player player, boolean damagedByEntity) {
		
		String pname = player.getName();
		 
		 //first of all update the health under the name, whatever is the cause
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable(){ public void run() {
			PlayerBar.updateHealthBelow(player);
		}});				

		 //if not enabled return
		 if (!playerUseAfter) return;
		 
		 //check for delay == 0
		 if (playerHideDelay == 0L) {
			 showPlayerHealthBar(player);
			 return;
		 }
	 
		 if (damagedByEntity) {
			 
			 //display always if hit by entity
			 Integer eventualTaskID = playerTable.remove(pname);
			 
			 if (eventualTaskID != null) {
				 //eventually remove from tables
				 scheduler.cancelTask(eventualTaskID);
			 }
			 
			 showPlayerHealthBar(player);
			 hidePlayerBarLater(player);
			 return;
		 }
		 else {
			 //it's not damaged by entity
			 if (playerTable.containsKey(pname)) {
				 showPlayerHealthBar(player);
			 }
			 return;
		 }
	}
	
	
	protected static void parseMobHit (LivingEntity mob, boolean damagedByEntity) {
		
		/*
		 * Type check
		 */
		final EntityType type = mob.getType();
		if (mobTypeDisabling && mobDisabledTypes.contains(type)) return;
		if (type == EntityType.WITHER || type == EntityType.ENDER_DRAGON) return;
	    if (type == EntityType.HORSE && !mob.isEmpty()) {
	    	//the horse has a passenger that could open his inventory.
	    	//the bar is not shown by the client, so it's not a big problem.
	    	return;
	    }
	    
	    /*
	     * World check
	     */
	    if (mobUseDisabledWorlds) {
	    	if (mobDisabledWorlds.contains(mob.getWorld().getName().toLowerCase())) {
	    		//the world is disabled
				return;
			}
		}
	    
		/*
		 * Custom name check
		 */
		String customName = mob.getCustomName();
		if (customName != null) {
			if (!customName.startsWith("§r")) {
				if (showOnCustomNames) {
					namesTable.put(mob.getEntityId(), new StringBoolean(customName, mob.isCustomNameVisible()));
				} else return;
			}
		}
		
		
		if (mobHideDelay == 0L) {
			showMobHealthBar(mob);
			return;
		}
		

			 
		 if (damagedByEntity) {
			 //display always if hit by entity
			 
			 //remove eventual task
			 Integer eventualTaskID = mobTable.remove(mob.getEntityId());
			 
			 if (eventualTaskID != null) {
				 //eventually cancel previous tasks
				 scheduler.cancelTask(eventualTaskID);
			 }
			 showMobHealthBar(mob);
			 hideMobBarLater(mob);
			 return;
		 }
		 else {
			 
			 //it's not damaged by entity, if the health was displayed only update it
			 if (mobTable.containsKey(mob.getEntityId())) {
				 showMobHealthBar(mob);
			 }
			 return;
		 }
	}
	
	private static void showMobHealthBar (final LivingEntity mob) {
		
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
	    	  public void run() {
	    		  
	    		  //check for compatibility
	    		  double health = mob.getHealth();
	    		  double max = mob.getMaxHealth();
	    		  
	    		  
	    		  //if the health is 0 remove the bar and return
	    		  if (health <= 0.0) {
	    			  mob.setCustomName("");
			    	  mob.setCustomNameVisible(false);
			    	  return;
	    		  }

	    		  //what type of health should be displayed?
			      if (barStyle == BarType.BAR)
			      {
			    	  mob.setCustomName("§r" + barArray[Utils.roundUpPositiveWithMax(((health/max) * 20.0), 20)]);
			    	  
			      }
			      else if (barStyle == BarType.CUSTOM_TEXT)
			      {
						String displayString = mobCustomText.replace("{h}", String.valueOf(Utils.roundUpPositive(health)));
						displayString = displayString.replace("{m}", String.valueOf(Utils.roundUpPositive(max)));
							
						//optimization, you don't need to check always if a string contains {n}
						if (customTextContains_Name) 
							displayString = displayString.replace("{n}", getName(mob, mob.getType().toString()));
							
						mob.setCustomName("§r" + displayString);
							
			      }
			      else if (barStyle == BarType.DEFAULT_TEXT)
			      {
			    	  StringBuilder sb = new StringBuilder("§rHealth: ");
			    	  sb.append(Utils.roundUpPositive(health));
			    	  sb.append("/");
			    	  sb.append(Utils.roundUpPositive(max));
			    	  mob.setCustomName(sb.toString());
			      }
			      
			      //check for visibility
			      if (!mobSemiHidden) mob.setCustomNameVisible(true);
	    	  }
		});
	}
	
	private static void hideMobBarLater(final LivingEntity mob) {
		final int id = mob.getEntityId();
		mobTable.put(id, scheduler.scheduleSyncDelayedTask(plugin, new Runnable() { public void run() {
	    		  
	    		hideBar(mob);

	    }}, mobHideDelay));
	}
	
	
	public static void hidePlayerBarLater(final Player player) {		
		playerTable.put(player.getName(), scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
	    	  public void run() {
		    		  playerTable.remove(player.getName());
		    		  PlayerBar.hideHealthBar(player);
	    	  }
		    }, playerHideDelay));
	}
	
	
	
	public static void hideBar(LivingEntity mob) {
		
		String cname = mob.getCustomName();
		if (cname != null && !cname.startsWith("§r")) {
			//it's a real name! Don't touch it!
			return;
		}
		
		//cancel eventual tasks
		Integer id = mobTable.remove(mob.getEntityId());
		if (id != null) {
			scheduler.cancelTask(id);
		}
		
		if (showOnCustomNames) {
			int idForName = mob.getEntityId();
			StringBoolean sb = namesTable.remove(idForName);
			if (sb != null) {
				//return only if found, else hide normally
				mob.setCustomName(sb.getString());
				mob.setCustomNameVisible(sb.getBoolean());
				return;
			}
		}
		
		//not a custom named mob, use default method (hide the name)
		mob.setCustomName("");
		mob.setCustomNameVisible(false);
	}
	
	public static String getNameWhileHavingBar(LivingEntity mob) {
		
		String cname = mob.getCustomName();
		if (cname == null) return null;
		
		if (cname.startsWith("§r")) {
			if (showOnCustomNames) {
				int id = mob.getEntityId();
				StringBoolean sb = namesTable.get(id);
				if (sb != null) {
					return sb.getString();
				}
			}
			return null;
		} else {
			//real name, return it
			return cname;
		}
	}
	
	private static void showPlayerHealthBar (final Player p) {
		
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
	    	  public void run() {
	    		  
	    		  //declares variables
	    		  double health = p.getHealth();
	    		  double max = p.getMaxHealth();
	    		  

	    		  //if the health is 0 remove the bar and return
	    		  if (health == 0) {
	    			  PlayerBar.hideHealthBar(p);
			    	  return;
	    		  }
	    		  
	    		  PlayerBar.setHealthSuffix(p, health, max);
	    	  }
		});
	}

	public static void removeAllMobHealthBars() {
		scheduler.cancelTasks(plugin);
		mobTable.clear();
		List<World> worldsList = plugin.getServer().getWorlds();
		for (World w : worldsList) {
			List<LivingEntity> entityList = w.getLivingEntities();
			for (LivingEntity e : entityList) {
				if (e.getType() != EntityType.PLAYER) {
					hideBar(e);
				}
			}
		}
	}

	
	private static String getName(LivingEntity mob, String mobType) {
		
		
		if (hookEpicboss) {
			try {
				if (EpicBossAPI.isBoss(mob)) {
					return Utils.colorize(EpicBossAPI.getBossDisplayName(mob));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				Main.logger.warning("Could not get boss name from EpicBoss. Hook disabled. Is it updated?");
				hookEpicboss = false;
			}
		  }
		
		String name = (String) localeMap.get(mobType);

	    if (name != null) {
	      return name;
	    }
	    return "";
	}
	
	
	
	public static void loadConfiguration() {
		
		removeAllMobHealthBars();
		
        FileConfiguration config = plugin.getConfig();
        
		//setup mobs
		mobEnabled = 			config.getBoolean(Configuration.Nodes.MOB_ENABLE.getNode());
		mobUseText = 			config.getBoolean(Configuration.Nodes.MOB_TEXT_MODE.getNode());
		mobUseCustomText = 		config.getBoolean(Configuration.Nodes.MOB_CUSTOM_TEXT_ENABLE.getNode());
		mobCustomText = 		Utils.replaceSymbols(config.getString(Configuration.Nodes.MOB_CUSTOM_TEXT.getNode()));
		mobSemiHidden = 		config.getBoolean(Configuration.Nodes.MOB_SHOW_IF_LOOKING.getNode());
		
		mobHideDelay = (long) 	config.getInt(Configuration.Nodes.MOB_DELAY.getNode())*20;
		if (config.getBoolean(Configuration.Nodes.MOB_ALWAYS_SHOWN.getNode(), false)) {
			mobHideDelay = 0L;
		}
		
		mobUseCustomBar = 		config.getBoolean(Configuration.Nodes.MOB_USE_CUSTOM.getNode());
		showOnCustomNames = 	config.getBoolean(Configuration.Nodes.MOB_SHOW_ON_NAMED.getNode());
		mobUseDisabledWorlds = config.getBoolean(Configuration.Nodes.MOB_WORLD_DISABLING.getNode());

		if (mobUseDisabledWorlds) {
			mobDisabledWorlds = Arrays.asList(plugin.getConfig()
												.getString(Configuration.Nodes.MOB_DISABLED_WORLDS.getNode())
												.toLowerCase()
												.replace(" ", "")
												.split(","));
		}
		
		mobTypeDisabling =		config.getBoolean(Configuration.Nodes.MOB_TYPE_DISABLING.getNode());
		
		
		
		//setup players
		playerEnabled = config.getBoolean(Configuration.Nodes.PLAYERS_ENABLE.getNode());
		
		playerHideDelay = (long) config.getInt("player-bars.after-name.hide-delay-seconds")*20;
		if (config.getBoolean(Configuration.Nodes.PLAYERS_AFTER_ALWAYS_SHOWN.getNode(), false)) {
			playerHideDelay = 0L;
		}
		
		
		playerUseAfter = config.getBoolean(Configuration.Nodes.PLAYERS_AFTER_ENABLE.getNode());
		
		//setup for epicboss
		hookEpicboss = config.getBoolean(Configuration.Nodes.HOOKS_EPIBOSS.getNode());
		
		if (hookEpicboss) {
			if (!Bukkit.getPluginManager().isPluginEnabled("EpicBoss Gold Edition")) {
				//if epicboss is not loaded, disable hook
				hookEpicboss = false;
				Bukkit.getConsoleSender().sendMessage("§a[HealthBar] §fCould not find plugin EpicBoss Gold Edition, " +
						"check that you have installed it and it's correctly loaded. If not, set 'hooks, epicboss: false' in the configs. " +
						"If you think that is an error, contact the developer.");
			} else {
				Main.logger.info("Hooked plugin EpicBoss Gold Edition.");
			}
		}
		
		//setup for eventual custom text, not to run extra checks while plugin is running
		if (mobCustomText.contains("{name}")) {
			customTextContains_Name = true;
			mobCustomText = mobCustomText.replace("{name}", "{n}");
		} else customTextContains_Name = false;
		
		//setup for health array
		barArray = new String[21];
		
		//custom bars - highest priority on configs
		if (mobUseCustomBar) {
			barStyle = BarType.BAR;
		
		//text - maybe custom - medium priority on configs
		} else if (mobUseText) {
			if (mobUseCustomText) {
				mobCustomText = mobCustomText.replace("{health}", "{h}");
				mobCustomText = mobCustomText.replace("{max}", "{m}");
				barStyle = BarType.CUSTOM_TEXT;
			} else {
				barStyle = BarType.DEFAULT_TEXT;
			}
		} else {
			
			//default bar - low priority on configs
			barStyle = BarType.BAR;
		}
		
		if (barStyle == BarType.BAR) {
			if (mobUseCustomBar) {
				barArray = MobBarsUtils.getCustomBars(Utils.loadFile("custom-mob-bar.yml", plugin));
			} else {
				barArray = MobBarsUtils.getDefaultsBars(config);
			}
		}
		
		if (mobUseCustomText && customTextContains_Name) {
			//only load if needed
			localeMap = Utils.getTranslationMap(plugin);
		}
		
		if (mobTypeDisabling) {
			mobDisabledTypes = Utils.getTypesFromString(config.getString(Configuration.Nodes.MOB_DISABLED_TYPES.getNode()));
		}
		
		if (mobHideDelay == 0L) {
			for (World world : Bukkit.getWorlds()) {
				for (LivingEntity mob : world.getLivingEntities()) {
					if (mob.getType() != EntityType.PLAYER) {
						parseMobHit(mob, true);
					}
				}
			}
		}
	}

	//end of the class
}
