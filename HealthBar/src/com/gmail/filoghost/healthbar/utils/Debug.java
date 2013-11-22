package com.gmail.filoghost.healthbar.utils;

import org.bukkit.Bukkit;

public class Debug {
	
	private static final boolean DEBUG_MODE = false;
	
	public static void log(String s) {
		if (DEBUG_MODE) {
			System.out.println(s);
		}
	}
	
	public static void color(String s) {
		if (DEBUG_MODE) {
			Bukkit.getConsoleSender().sendMessage(s.replace("&", "§"));
		}
	}
}
