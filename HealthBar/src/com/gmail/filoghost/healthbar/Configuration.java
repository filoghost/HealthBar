package com.gmail.filoghost.healthbar;

import org.bukkit.configuration.file.FileConfiguration;

public class Configuration {
	
	public static void checkConfigYML() {
		FileConfiguration config = Main.plugin.getConfig();
		Nodes[] nodes = Nodes.values();
		
		for (Nodes node : nodes) {
			if (!config.isSet(node.getNode())) {
				config.set(node.getNode(), node.getValue());
			}
		}
		
		Main.plugin.saveConfig();
		Main.plugin.reloadConfig();
	}	
	
	public enum Nodes {
		
		PLAYERS_ENABLE("player-bars.enable", true),
		PLAYERS_AFTER_ENABLE("player-bars.after-name.enable", true),
		PLAYERS_AFTER_STYLE("player-bars.after-name.display-style", 1),
		PLAYERS_AFTER_ALWAYS_SHOWN("player-bars.after-name.always-shown", false),
		PLAYERS_AFTER_TEXT_MODE("player-bars.after-name.text-mode", false),
		PLAYERS_AFTER_DELAY("player-bars.after-name.hide-delay-seconds", 5),
		PLAYERS_AFTER_USE_CUSTOM("player-bars.after-name.use-custom-file", false),
		
		PLAYERS_BELOW_ENABLE("player-bars.below-name.enable", true),
		PLAYERS_BELOW_TEXT("player-bars.below-name.text", "% &cHealth"),
		PLAYERS_BELOW_DISPLAY_RAW_HEARTS("player-bars.below-name.display-raw-hearts", false),
		PLAYERS_BELOW_USE_PROPORTION("player-bars.below-name.use-proportion", true),
		PLAYERS_BELOW_PROPORTIONAL_TO("player-bars.below-name.proportional-to", 100),
		
		PLAYERS_WORLD_DISABLING("player-bars.world-disabling", false),
		PLAYERS_DISABLED_WORLDS("player-bars.disabled-worlds", "world_nether,world_the_end"),
		
		MOB_ENABLE("mob-bars.enable", true),
		MOB_SHOW_ON_NAMED("mob-bars.show-on-named-mobs", true),
		MOB_STYLE("mob-bars.display-style", 1),
		MOB_ALWAYS_SHOWN("mob-bars.always-shown", false),
		MOB_TEXT_MODE("mob-bars.text-mode", false),
		MOB_CUSTOM_TEXT_ENABLE("mob-bars.custom-text-enable", false),
		MOB_CUSTOM_TEXT("mob-bars.custom-text", "{name} - &a{health}/{max}"),
		MOB_DELAY("mob-bars.hide-delay-seconds", 5),
		MOB_SHOW_IF_LOOKING("mob-bars.show-only-if-looking", false),
		MOB_USE_CUSTOM("mob-bars.use-custom-file", false),
		MOB_WORLD_DISABLING("mob-bars.world-disabling", false),
		MOB_DISABLED_WORLDS("mob-bars.disabled-worlds", "world_nether,world_the_end"),
		MOB_TYPE_DISABLING("mob-bars.type-disabling", false),
		MOB_DISABLED_TYPES("mob-bars.disabled-types", "creeper,zombie,skeleton,iron_golem"),
		
		HOOKS_EPIBOSS("hooks.epicboss", false),
		
		FIX_TAB_NAMES("fix-tab-names", true),
		FIX_DEATH_MESSAGES("fix-death-messages", true),
		UPDATE_NOTIFICATION("update-notification", true),
		USE_PLAYER_PERMISSIONS("use-player-bar-permissions", false),
		OVERRIDE_OTHER_SCOREBOARD("override-other-scoreboard", false);
		
		private String node;
		private Object value;
		
		private Nodes(String node, Object defaultValue) {
			this.node = node;
			value = defaultValue;
		}
		
		public String getNode() {
			return this.node;
		}
		
		public Object getValue() {
			return this.value;
		}
	}
}
