package com.gmail.filoghost.healthbar;

import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.filoghost.healthbar.api.HealthBarAPI;

public class DeathListener extends JavaPlugin implements Listener {

	private static boolean wantDeathListener;
	
	@EventHandler (priority = EventPriority.LOW)
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		if (!wantDeathListener) return;
		try {
		String deathMessage = event.getDeathMessage();
		String victim = event.getEntity().getName();
		EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();
		if(damageEvent instanceof EntityDamageByEntityEvent) {
			Entity damager = ((EntityDamageByEntityEvent)damageEvent).getDamager();
			
			//----------------------NORMAL KILL-------------------------
			if (deathMessage.contains("killed") || deathMessage.contains("slain") || deathMessage.contains("got finished")) {
				if (damager instanceof Player) {
					String itemname = ((Player)damager).getItemInHand().getItemMeta().getDisplayName();
						if (itemname==null) {
							event.setDeathMessage(victim + " was slain by " + ((Player)damager).getName());
							return;
						} else {
							event.setDeathMessage(victim + " was slain by " + ((Player)damager).getName() + " using " + itemname);
							return;
						}
					
					}
				if (damager instanceof LivingEntity) {
					event.setDeathMessage(victim + " was slain by " + getName((LivingEntity) damager));
					return;
					}
				}
			//----------------------EXPLOSION-------------------------
			if (deathMessage.contains("blown up")) {
				if (damager instanceof Player) {
					event.setDeathMessage(victim + " was blown up by " + ((Player)damager).getName());
					return;
					}
				if (damager instanceof LivingEntity) {
					event.setDeathMessage(victim + " was blown up by " + getName((LivingEntity) damager));
					return;
					}
				}
			//-----------------------ARROW---------------------------
			if (deathMessage.contains("shot") || deathMessage.contains("shooted")) {
				if (damager instanceof Projectile) {
					LivingEntity shooter = ((Projectile)damager).getShooter();
					if (shooter instanceof Player) {
						String itemname = ((Player)shooter).getItemInHand().getItemMeta().getDisplayName();
						if (itemname==null) {
							event.setDeathMessage(victim + " was shot by " + ((Player)shooter).getName());
							return;
						} else {
							event.setDeathMessage(victim + " was shot by " + ((Player)shooter).getName() + " using " + itemname);
							return;
						}
					}
					if (shooter instanceof LivingEntity) {
						event.setDeathMessage(victim + " was shot by " + getName(shooter));
						return;
					}
				}
			}
			//-----------------------FIREBALL---------------------------
			if (deathMessage.contains("fireballed")) {
				if (damager instanceof Projectile) {
					LivingEntity shooter = ((Projectile)damager).getShooter();
					if (shooter instanceof Player) {
						event.setDeathMessage(victim + " was fireballed by " + ((Player)shooter).getName());
						return;
						}
					if (shooter instanceof LivingEntity) {
						event.setDeathMessage(victim + "was fireballed by " + getName(shooter));
						return;
					}
				}
			}
		}
		
		if (deathMessage.contains("high place") || deathMessage.contains("doomed to fall") || deathMessage.contains("fell off") || deathMessage.contains("fell out of the water")) {
			event.setDeathMessage(victim + " fell from a high place");
			return; }
		if (deathMessage.contains("lava")) {
			event.setDeathMessage(victim + " tried to swim in lava");
			return; }
		if (deathMessage.contains("blew up")) {
			event.setDeathMessage(victim + " blew up");
			return; }
		if (deathMessage.contains("burned") || deathMessage.contains("crisp")) {
			event.setDeathMessage(victim + " was burned to death");
			return; }
		if (deathMessage.contains("flames") || deathMessage.contains("fire")) {
			event.setDeathMessage(victim + " went up in flames");
			return; }
		if (deathMessage.contains("drowned")) {
			event.setDeathMessage(victim + " drowned");
			return; }
		if (deathMessage.contains("shooted") || deathMessage.contains("shot")) {
			event.setDeathMessage(victim + " was shot by an arrow");
			return; }	
		if (deathMessage.contains("wall")) {
			event.setDeathMessage(victim + " suffucated in a wall");
			return; }
		if (deathMessage.contains("starved")) {
			event.setDeathMessage(victim + " starved to death");
			return; }
		if (deathMessage.contains("magic")) {
			event.setDeathMessage(victim + " was killed by magic");
			return; }
		if (deathMessage.contains("fireball")) {
			event.setDeathMessage(victim + " was fireballed");
			return; }
		if (deathMessage.contains("pricked") || deathMessage.contains("cactus") || deathMessage.contains("cacti")) {
			event.setDeathMessage(victim + " was pricked to death");
			return; }
		if (deathMessage.contains("world")) {
			event.setDeathMessage(victim + " fell out of the world");
			return; }
		if (deathMessage.contains("squashed")) {
			event.setDeathMessage(victim + " was squashed by a falling anvil");
			return; }
		event.setDeathMessage(victim + " died");
		return;
		}
		catch (Exception e) {
			event.setDeathMessage(event.getEntity().getName() + " died");
		}
	}

	private String getName(LivingEntity mob) {
		String customName = HealthBarAPI.getMobName(mob);
		if (customName != null) return customName;

		if (mob.getType() == EntityType.PIG_ZOMBIE) 	return "Zombie Pigman";
		if (mob.getType() == EntityType.MUSHROOM_COW) 	return "Mooshroom";
		return WordUtils.capitalizeFully(mob.getType().toString().replace("_", " "));
	}
	
	public static void loadConfiguration() {
		wantDeathListener = Main.plugin.getConfig().getBoolean(Configuration.Nodes.FIX_DEATH_MESSAGES.getNode());
	}
	
//end of the class
}
