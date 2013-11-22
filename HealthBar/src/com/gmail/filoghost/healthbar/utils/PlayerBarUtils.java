package com.gmail.filoghost.healthbar.utils;

import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PlayerBarUtils {
	
	//enforce non-instantiability with a private constructor
	private PlayerBarUtils() {}
	
	public static void create10DefaultTeams(Scoreboard sb, int style) {
		
		if (style == 2) {
		      sb.registerNewTeam("hbr1").setSuffix(" §c▌");
		      sb.registerNewTeam("hbr2").setSuffix(" §c█");
		      sb.registerNewTeam("hbr3").setSuffix(" §e█▌");
		      sb.registerNewTeam("hbr4").setSuffix(" §e██");
		      sb.registerNewTeam("hbr5").setSuffix(" §e██▌");
		      sb.registerNewTeam("hbr6").setSuffix(" §a███");
		      sb.registerNewTeam("hbr7").setSuffix(" §a███▌");
		      sb.registerNewTeam("hbr8").setSuffix(" §a████");
		      sb.registerNewTeam("hbr9").setSuffix(" §a████▌");
		      sb.registerNewTeam("hbr10").setSuffix(" §a█████");
			return; 
		} else if (style == 3) {
		      sb.registerNewTeam("hbr1").setSuffix(" §cI§8IIIIIIIII");
		      sb.registerNewTeam("hbr2").setSuffix(" §cII§8IIIIIIII");
		      sb.registerNewTeam("hbr3").setSuffix(" §eIII§8IIIIIII");
		      sb.registerNewTeam("hbr4").setSuffix(" §eIIII§8IIIIII");
		      sb.registerNewTeam("hbr5").setSuffix(" §eIIIII§8IIIII");
		      sb.registerNewTeam("hbr6").setSuffix(" §aIIIIII§8IIII");
		      sb.registerNewTeam("hbr7").setSuffix(" §aIIIIIII§8III");
		      sb.registerNewTeam("hbr8").setSuffix(" §aIIIIIIII§8II");
		      sb.registerNewTeam("hbr9").setSuffix(" §aIIIIIIIII§8I");
		      sb.registerNewTeam("hbr10").setSuffix(" §aIIIIIIIIII");
			return;
		} else if (style == 4) {
		      sb.registerNewTeam("hbr1").setSuffix(" §c1❤");
		      sb.registerNewTeam("hbr2").setSuffix(" §c2❤");
		      sb.registerNewTeam("hbr3").setSuffix(" §e3❤");
		      sb.registerNewTeam("hbr4").setSuffix(" §e4❤");
		      sb.registerNewTeam("hbr5").setSuffix(" §e5❤");
		      sb.registerNewTeam("hbr6").setSuffix(" §a6❤");
		      sb.registerNewTeam("hbr7").setSuffix(" §a7❤");
		      sb.registerNewTeam("hbr8").setSuffix(" §a8❤");
		      sb.registerNewTeam("hbr9").setSuffix(" §a9❤");
		      sb.registerNewTeam("hbr10").setSuffix(" §a10❤");
			return; 
		} else if (style == 5) {
		      sb.registerNewTeam("hbr1").setSuffix(" §c♦§7♦♦♦♦ ");
		      sb.registerNewTeam("hbr2").setSuffix(" §c♦§7♦♦♦♦ ");
		      sb.registerNewTeam("hbr3").setSuffix(" §e♦♦§7♦♦♦ ");
		      sb.registerNewTeam("hbr4").setSuffix(" §e♦♦§7♦♦♦ ");
		      sb.registerNewTeam("hbr5").setSuffix(" §a♦♦♦§7♦♦ ");
		      sb.registerNewTeam("hbr6").setSuffix(" §a♦♦♦§7♦♦ ");
		      sb.registerNewTeam("hbr7").setSuffix(" §a♦♦♦♦§7♦ ");
		      sb.registerNewTeam("hbr8").setSuffix(" §a♦♦♦♦§7♦ ");
		      sb.registerNewTeam("hbr9").setSuffix(" §a♦♦♦♦♦ ");
		      sb.registerNewTeam("hbr10").setSuffix(" §a♦♦♦♦♦ ");
			return;
		} else if (style == 6) {
		      sb.registerNewTeam("hbr1").setSuffix(" §c❤§7❤❤❤❤");
		      sb.registerNewTeam("hbr2").setSuffix(" §c❤§7❤❤❤❤");
		      sb.registerNewTeam("hbr3").setSuffix(" §c❤❤§7❤❤❤");
		      sb.registerNewTeam("hbr4").setSuffix(" §c❤❤§7❤❤❤");
		      sb.registerNewTeam("hbr5").setSuffix(" §c❤❤❤§7❤❤");
		      sb.registerNewTeam("hbr6").setSuffix(" §c❤❤❤§7❤❤");
		      sb.registerNewTeam("hbr7").setSuffix(" §c❤❤❤❤§7❤");
		      sb.registerNewTeam("hbr8").setSuffix(" §c❤❤❤❤§7❤");
		      sb.registerNewTeam("hbr9").setSuffix(" §c❤❤❤❤❤");
		      sb.registerNewTeam("hbr10").setSuffix(" §c❤❤❤❤❤");
			return;
		} else if (style == 7) {
		      sb.registerNewTeam("hbr1").setSuffix(" §c▌§8▌▌▌▌▌▌▌▌▌");
		      sb.registerNewTeam("hbr2").setSuffix(" §c▌▌§8▌▌▌▌▌▌▌▌");
		      sb.registerNewTeam("hbr3").setSuffix(" §e▌▌▌§8▌▌▌▌▌▌▌");
		      sb.registerNewTeam("hbr4").setSuffix(" §e▌▌▌▌§8▌▌▌▌▌▌");
		      sb.registerNewTeam("hbr5").setSuffix(" §e▌▌▌▌▌§8▌▌▌▌▌");
		      sb.registerNewTeam("hbr6").setSuffix(" §a▌▌▌▌▌▌§8▌▌▌▌");
		      sb.registerNewTeam("hbr7").setSuffix(" §a▌▌▌▌▌▌▌§8▌▌▌");
		      sb.registerNewTeam("hbr8").setSuffix(" §a▌▌▌▌▌▌▌▌§8▌▌");
		      sb.registerNewTeam("hbr9").setSuffix(" §a▌▌▌▌▌▌▌▌▌§8▌");
		      sb.registerNewTeam("hbr10").setSuffix(" §a▌▌▌▌▌▌▌▌▌▌");
			return;
		} else {
		//style == 1 or > 7
		    sb.registerNewTeam("hbr1").setSuffix(" §c|§8|||||||||");
		    sb.registerNewTeam("hbr2").setSuffix(" §c||§8||||||||");
		    sb.registerNewTeam("hbr3").setSuffix(" §e|||§8|||||||");
		    sb.registerNewTeam("hbr4").setSuffix(" §e||||§8||||||");
		    sb.registerNewTeam("hbr5").setSuffix(" §e|||||§8|||||");
		    sb.registerNewTeam("hbr6").setSuffix(" §a||||||§8||||");
		    sb.registerNewTeam("hbr7").setSuffix(" §a|||||||§8|||");
		    sb.registerNewTeam("hbr8").setSuffix(" §a||||||||§8||");
		    sb.registerNewTeam("hbr9").setSuffix(" §a|||||||||§8|");
		    sb.registerNewTeam("hbr10").setSuffix(" §a||||||||||");
			return;
		}
	}
	
	/*
	 * Adds the teams from a file to the scoreboard
	 */
	public static void create10CustomTeams(Scoreboard sb, FileConfiguration c) {
		for (int i=1; i<11; i++) {
			try {
				
				Team t = sb.registerNewTeam("hbr" + i);
				if (!c.isSet(i + "0" + "-percent.prefix")) {
					c.set(i + "0" + "-percent.prefix", "");
				}
				if (!c.isSet(i + "0" + "-percent.suffix")) {
					c.set(i + "0" + "-percent.suffix", "");
				}
				String prefix = c.getString(i + "0" + "-percent.prefix");
				String suffix = c.getString(i + "0" + "-percent.suffix");
				
				if ((prefix != null) && (!prefix.equals("")))
					t.setPrefix(Utils.replaceSymbols(prefix));
				if ((suffix != null) && (!suffix.equals("")))
					t.setSuffix(Utils.replaceSymbols(suffix));
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	/*
	 * By default players in the same team can see each other while invisible
	 */
	public static void setAllTeamsInvisibility(Scoreboard sb) {
		
		Set<Team> teamList = sb.getTeams();
		for (Team team : teamList) {
			//teams used by healthbar - they contains hbr
			if (team.getName().contains("hbr")) {
				team.setCanSeeFriendlyInvisibles(false);
			}
		}
	}
	
	/*
	 * Removes all the teams created by HealthBar
	 */
	public static void removeAllHealthbarTeams(Scoreboard sb) {
		
		Set<Team> teamList = sb.getTeams();
		for (Team team : teamList) {
			if (team.getName().contains("hbr")) {
				team.unregister();
			}
		}
	}	
	
}
