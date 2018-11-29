package de.gamelos.scoreboard;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import de.gamelos.PermissionsAPI.PermissionsAPI;
import de.gamelos.jaylosapi.JaylosAPI;
import de.gamelos.knockout.SQLStats;
import de.gamelos.knockout.SpielerUUID;

public class Month {
	public static void createScoreboard(Player p){
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("aaa", "bbb");
		Calendar c = Calendar.getInstance();
		c.setTime(new Timestamp(System.currentTimeMillis()));
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.GERMAN);
		obj.setDisplayName(ChatColor.AQUA+"Stats "+sdf.format(c.getTime()));
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
//		
//	
//					
//			
		List<String> list = SQLStats.getMonth();
			for(int i = 0; i<5; i++){
				int b = Integer.parseInt(""+i)+1;
				if(i<list.size()){
				String spielername = list.get(i);
				int kills = SQLStats.getmonat(spielername);
				Score ss = obj.getScore(ChatColor.RED+"#"+b+" "+ChatColor.GRAY+getUsername(spielername));
				ss.setScore(kills);
				}else{
					Score ss = obj.getScore(ChatColor.RED+"#"+b+" "+ChatColor.GRAY+"-");
					ss.setScore(0);
				}
			}
				obj.getScore(ChatColor.AQUA+"").setScore(-1);
				obj.getScore(ChatColor.GRAY+"Deine Kills:").setScore(-2);
				obj.getScore(""+ChatColor.AQUA+SQLStats.getmonat(p.getUniqueId().toString())).setScore(-3);
				obj.getScore(ChatColor.GREEN+"").setScore(-4);
				obj.getScore(ChatColor.GRAY+"              ●"+ChatColor.DARK_GRAY+"●").setScore(-5);
			
			
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
