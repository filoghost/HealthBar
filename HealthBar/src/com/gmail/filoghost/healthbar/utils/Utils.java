package com.gmail.filoghost.healthbar.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.healthbar.Main;

public class Utils {
	
	//enforce non-instantiability with a private constructor
	private Utils() {}
	
	
	public static String colorize(String input) {
		if (input == null) return "";
		return ChatColor.translateAlternateColorCodes('&', input);
	}
	
	/*
	 * Replace symbols used for the health bars
	 */	
	public static String replaceSymbols(String input) {
		
		if (input == null || input.length() == 0) return input;
		
		//replaces colors and symbols
		return ChatColor.translateAlternateColorCodes('&', input)
					.replace("<3", "\u2764")
					.replace("[x]", "\u2588")
					.replace("[/]", "\u2588")
					.replace("[*]", "\u2605")
					.replace("[p]", "\u25CF")		
					.replace("[+]", "\u25C6")
					.replace("[++]", "\u2726");

	}
	
	/*
	 * Load and save a custom file
	 */
	public static FileConfiguration loadFile(String path, Plugin plugin) {
		
		if (!path.endsWith(".yml")) path += ".yml";
		
		File file = new File(plugin.getDataFolder(), path);
		
		if (!file.exists()) {
			try {
				plugin.saveResource(path, false);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("-------------------------------------------------");
		        System.out.println("[HealthBar] Cannot save " + path + " to disk!");
		        System.out.println("-------------------------------------------------");
		        return null;
		     }
		}
			 
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		return config;
	}
	
	/*
	 * Loads the names of the mobs from a file
	 */
	public static Map<String, String> getTranslationMap(Plugin plugin) {
		
		FileConfiguration config = loadFile("locale.yml", plugin);
		
		Map<String,String> localeMap = new HashMap<String, String>();
		
		for (EntityType entityType : EntityType.values()) {
			
			if (entityType.isAlive() && !entityType.equals(EntityType.PLAYER)) {
			
				String name = entityType.toString();
			
				if (config.isSet(name)) {
					localeMap.put(name, config.getString(name));
				} else {
					config.set(name, WordUtils.capitalizeFully(name.replace("_", " ")));
					localeMap.put(name, WordUtils.capitalizeFully(name.replace("_", " ")));
				}
			
			}
		}
			
		try {
			config.save(new File(plugin.getDataFolder(), "locale.yml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("-------------------------------------------------");
		    System.out.println("[HealthBar] Cannot save locale.yml to disk!");
		    System.out.println("-------------------------------------------------");
		}
		
		return localeMap;
	}
	
	public static List<EntityType> getTypesFromString(String input) {
		List<EntityType> list = new ArrayList<EntityType>();
		if (input == null || input.length() == 0) return list;
		String[] split = input.split(",");
		
		for (String s : split) {
			EntityType type = getTypeFromString(s);
			if (type == null) {
				Main.logger.warning("Cannot find entity type: '" + s + "'. Valid types are listed in locale.yml (The uppercase names, with the underscore)");
			} else {
				list.add(type);
			}
		}
		
		return list;
	}
	
	//remainder: from 0.0 to 0.5 (included) it's rounded down, from 0.5 (excluded) to 0.999 it's rounded up.
	public static int round(double d) {
		double remainder = d - (int) d;
		if (remainder <= 0.5) {
			return (int) d;
		} else {
			return ((int) d) + 1;
		}
	}
	
	public static int roundUpPositive(double d) {
	    int i = (int) d;
	    double remainder = d - i;
	    if (remainder > 0.0) {
	    	i++;
	    }
	    if (i<0) return 0;
	    return i;
	}
	
	public static int roundUpPositiveWithMax(double d, int max) {
	   int result = roundUpPositive(d);
	   if (d > max) return max;
	   return result;
	}
	
	
	public static EntityType getTypeFromString(String s) {
		for (EntityType type : EntityType.values()) {
			if (s.replace(" ", "").replace("_", "").equalsIgnoreCase(type.toString().replace("_", ""))) {
				return type;
			}
		}
		return null;
	}
	
	
	public static String getBukkitBuild() {
		String version = Bukkit.getVersion();
		Pattern pattern = Pattern.compile("(b)([0-9]+)(jnks)");
		Matcher matcher = pattern.matcher(version);
		 
		if (matcher.find()) {
		return matcher.group(2);
		}
		 
		return null;
	}
}
