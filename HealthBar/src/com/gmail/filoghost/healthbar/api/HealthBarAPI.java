package com.gmail.filoghost.healthbar.api;

import org.bukkit.entity.LivingEntity;

import com.gmail.filoghost.healthbar.DamageListener;
import com.gmail.filoghost.healthbar.MiscListeners;

public class HealthBarAPI {
	
	/**
	 * Checks if a mob has a health bar.
	 */
	public static boolean mobHasBar(LivingEntity mob) {
		
		String tagName = mob.getCustomName();
		
		 if (tagName != null) {
			if (tagName.startsWith("§r")) {
					return true;
			}
		 }
		 
		 return false;
	}
	
	/**
	 * Hides the bar restoring the custom name.
	 */
	public static void mobHideBar(LivingEntity mob) {
		DamageListener.hideBar(mob);
	}
	
	/**
	 * Gets the real name of the mob, even if it doesn't have the health bar.
	 */
	public static String getMobName(LivingEntity mob) {
		return DamageListener.getNameWhileHavingBar(mob);
	}
	
	/**
	 * HealthBar has a method to remove team prefix & suffix in the tab list.
	 * This disabled that function.
	 */
	public static void disableWhiteTabNames() {
		MiscListeners.disableTabNamesFix();
	}
	
}
