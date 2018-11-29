package de.gamelos.knockout;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;




public class SQLStats {

	public static boolean playerExists(String uuid){
		try {
			@SuppressWarnings("static-access")
			ResultSet rs = Main.mysql.querry("SELECT * FROM Knockout WHERE UUID = '"+ uuid + "'");
			
			if(rs.next()){
				return rs.getString("UUID") != null;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void createPlayer(String uuid){
		if(!(playerExists(uuid))){
				Main.mysql.update("INSERT INTO Knockout(UUID, Alltime, Monthstats, Month, Kit, Stick, Spezial) VALUES ('" +uuid+ "', '0', '0', '0', '0', '0', '1');");
		}
	}
	
	//get-----------------------------------------------------------------------------------------------------------------------------------
	public static Integer getAlltime(String uuid){
		Integer i = 0;
		if(playerExists(uuid)){
			try {
				@SuppressWarnings("static-access")
				ResultSet rs = Main.mysql.querry("SELECT * FROM Knockout WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (Integer.valueOf(rs.getInt("Alltime")) == null));
				
				i = rs.getInt("Alltime");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			return 0;
		}
		return i;
	}
	
	public static Integer getmonat(String uuid){
		Integer i = 0;
		if(playerExists(uuid)){
			try {
				@SuppressWarnings("static-access")
				ResultSet rs = Main.mysql.querry("SELECT * FROM Knockout WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (Integer.valueOf(rs.getInt("Monthstats")) == null));
				
				i = rs.getInt("Monthstats");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			return 0;
		}
		return i;
	}
	
	public static Integer getStick(String uuid){
		Integer i = 0;
		if(playerExists(uuid)){
			try {
				@SuppressWarnings("static-access")
				ResultSet rs = Main.mysql.querry("SELECT * FROM Knockout WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (Integer.valueOf(rs.getInt("Stick")) == null));
				
				i = rs.getInt("Stick");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			return 0;
		}
		return i;
	}
	
	public static Integer getSpezial(String uuid){
		Integer i = 0;
		if(playerExists(uuid)){
			try {
				@SuppressWarnings("static-access")
				ResultSet rs = Main.mysql.querry("SELECT * FROM Knockout WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (Integer.valueOf(rs.getInt("Spezial")) == null));
				
				i = rs.getInt("Spezial");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			return 0;
		}
		return i;
	}
	
	
	
	public static String getmonth(String uuid){
		String i = "";
		if(playerExists(uuid)){
			try {
				@SuppressWarnings("static-access")
				ResultSet rs = Main.mysql.querry("SELECT * FROM Knockout WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("Month")) == null));
				
				i = rs.getString("Month");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			return null;
		}
		return i;
	}
	
	public static String getkit(String uuid){
		String i = "";
		if(playerExists(uuid)){
			try {
				@SuppressWarnings("static-access")
				ResultSet rs = Main.mysql.querry("SELECT * FROM Knockout WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("Kit")) == null));
				
				i = rs.getString("Kit");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			return null;
		}
		return i;
	}
	
	//set-----------------------------------------------------------------------------------------------------------------------------------
	
	public static void setalltime(String uuid, Integer kills){
		
		if(playerExists(uuid)){
			Main.mysql.update("UPDATE Knockout SET Alltime= '" + kills+ "' WHERE UUID= '" + uuid+ "';");
		}else{
			createPlayer(uuid);
			setalltime(uuid, kills);
		}
		
	}
	
	public static void setmonat(String uuid, Integer kills){
		
		if(playerExists(uuid)){
			Main.mysql.update("UPDATE Knockout SET Monthstats= '" + kills+ "' WHERE UUID= '" + uuid+ "';");
		}else{
			createPlayer(uuid);
			setalltime(uuid, kills);
		}
		
	}
	
	public static void setStick(String uuid, Integer slot){
		
		if(playerExists(uuid)){
			Main.mysql.update("UPDATE Knockout SET Stick= '" + slot+ "' WHERE UUID= '" + uuid+ "';");
		}else{
			createPlayer(uuid);
			setalltime(uuid, slot);
		}
		
	}
	
	public static void setSpezial(String uuid, Integer slot){
		
		if(playerExists(uuid)){
			Main.mysql.update("UPDATE Knockout SET Spezial= '" + slot+ "' WHERE UUID= '" + uuid+ "';");
		}else{
			createPlayer(uuid);
			setalltime(uuid, slot);
		}
		
	}
	
	public static void setmonth(String uuid, String kills){
		
		if(playerExists(uuid)){
			Main.mysql.update("UPDATE Knockout SET Month= '" + kills+ "' WHERE UUID= '" + uuid+ "';");
		}else{
			createPlayer(uuid);
			setmonth(uuid, kills);
		}
		
	}
	
	public static void setkit(String uuid, String kills){
		
		if(playerExists(uuid)){
			Main.mysql.update("UPDATE Knockout SET Kit= '" + kills+ "' WHERE UUID= '" + uuid+ "';");
		}else{
			createPlayer(uuid);
			setkit(uuid, kills);
		}
		
	}
	
	//add------------------------------------------------------------------------------------------------------------------------------------
	
	public static void addalltime(String uuid, Integer kills){
		
		if(playerExists(uuid)){
			setalltime(uuid, Integer.valueOf(getAlltime(uuid).intValue() + kills.intValue()));
		}else{
			createPlayer(uuid);
			addalltime(uuid, kills);
		}
		
	}
	
	public static void addmonat(String uuid, Integer kills){
		
		if(playerExists(uuid)){
			setmonat(uuid, Integer.valueOf(getmonat(uuid).intValue() + kills.intValue()));
		}else{
			createPlayer(uuid);
			addmonat(uuid, kills);
		}
		
	}
	
	public static void addmonth(String uuid, Integer tode){
		
		if(playerExists(uuid)){
			Calendar c = Calendar.getInstance();
			c.setTime(new Timestamp(System.currentTimeMillis()));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMMM ", Locale.GERMAN);
			String s = getmonth(uuid);
			String[]b = s.split(":");
			String p = b[0];
			if(p.equals(sdf.format(c.getTime()))){
				addmonat(uuid, 1);
			}else{
				setmonth(uuid, sdf.format(c.getTime()));
				setmonat(uuid, 1);
			}
		}else{
			createPlayer(uuid);
			addmonth(uuid, tode);
		}
	}
//	=========================================================================================
	@SuppressWarnings("unchecked")
	public static List<String> getalltime(){
		@SuppressWarnings("rawtypes")
		List<String> list = new ArrayList();
		@SuppressWarnings("static-access")
		ResultSet rs = Main.mysql.querry("SELECT * FROM Knockout ORDER BY Alltime DESC");
		try {
			while(rs.next()){
				String nameUUID = rs.getString("UUID");
				list.add(nameUUID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static List<String> getMonth(){
		List<String> list = new ArrayList<>();
		@SuppressWarnings("static-access")
		ResultSet rs = Main.mysql.querry("SELECT * FROM Knockout ORDER BY Monthstats DESC");
		Calendar c = Calendar.getInstance();
		c.setTime(new Timestamp(System.currentTimeMillis()));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMMM ", Locale.GERMAN);
		try {
			while(rs.next()){
				String ssss = rs.getString("Month");
				if(rs.getString("Month") != null && !ssss.equals("0")){
				if(sdf.format(c.getTime()).equals(ssss)){
				String nameUUID = rs.getString("UUID");
				list.add(nameUUID);
}
				}else{
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
