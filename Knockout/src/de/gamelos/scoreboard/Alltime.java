package de.gamelos.scoreboard;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import de.gamelos.jaylosapi.JaylosAPI;
import de.gamelos.knockout.SQLStats;
import de.gamelos.knockout.SpielerUUID;

public class Alltime {
	public static void createScoreboard(Player p){
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("aaa", "bbb");
		obj.setDisplayName(ChatColor.AQUA+"Stats Alltime");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
//		
//	
//					
//			
		List<String> list = SQLStats.getalltime();
			for(int i = 0; i<5; i++){
				int b = Integer.parseInt(""+i)+1;
				if(i<list.size()){
				String spielername = list.get(i);
				int kills = SQLStats.getAlltime(spielername);
				Score ss = obj.getScore(ChatColor.RED+"#"+b+" "+ChatColor.GRAY+getUsername(spielername));
				ss.setScore(kills);
				}else{
					Score ss = obj.getScore(ChatColor.RED+"#"+b+" "+ChatColor.GRAY+"-");
					ss.setScore(0);
				}
			}
				obj.getScore(ChatColor.AQUA+"").setScore(-1);
				obj.getScore(ChatColor.GRAY+"Deine Kills:").setScore(-2);
				obj.getScore(""+ChatColor.AQUA+SQLStats.getAlltime(p.getUniqueId().toString())).setScore(-3);
				obj.getScore(ChatColor.GREEN+"").setScore(-4);
				obj.getScore(ChatColor.DARK_GRAY+"              ●"+ChatColor.GRAY+"●").setScore(-5);
			
			
					p.setScoreboard(board);
					JaylosAPI.updaterang();
//					
	}

	public static HashMap<String,String> play = new HashMap<>();
	
	public static String getUsername(String uuid){
		String username = SpielerUUID.getSpielername(uuid);
		return username;
	}
}
