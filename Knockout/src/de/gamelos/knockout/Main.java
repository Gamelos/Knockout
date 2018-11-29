package de.gamelos.knockout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.connorlinfoot.titleapi.TitleAPI;

import de.dytanic.cloudnet.bridge.CloudServer;
import de.gamelos.PermissionsAPI.PermissionsAPI;
import de.gamelos.jaylosapi.JaylosAPI;
import de.gamelos.nick.unNickEvent;
import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
public class Main extends JavaPlugin implements Listener{

	public static String Prefix = ChatColor.GRAY+"["+ChatColor.RED+"Knockout"+ChatColor.GRAY+"] ";
	public static File locations;
	public static FileConfiguration loc;
	String map = "Flowers";
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("knockout").setExecutor(this);
		Main.locations = new File("plugins/Knockout", "data.yml");
		Main.loc = YamlConfiguration.loadConfiguration(Main.locations);
		itemhigh = Main.loc.getDouble(map+".itemhigh.y");
		endhigh = Main.loc.getDouble(map+".endhigh.y");
		ConnectMySQL();
		JaylosAPI.showrang(true);
		scoreboardchange();
		map = Main.loc.getStringList("Maps").get(0);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
			@Override
			public void run() {
				CloudServer.getInstance().setMotd("Flowers");
			}
		}, 20*5);
		CloudServer.getInstance().setMotd("Flowers");
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
			@Override
			public void run() {
				Bukkit.broadcastMessage(Main.Prefix+ChatColor.AQUA+"Der Server startet in 5 Minuten neu...");
				CloudServer.getInstance().setMotd("Neustart");
			}
		}, 20*60*25);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
			@Override
			public void run() {
				Bukkit.shutdown();
			}
		}, 20*60*30);
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		super.onDisable();
	}
	
	public static MySQL mysql;
	private void ConnectMySQL(){
		mysql = new MySQL(JaylosAPI.gethost(), JaylosAPI.getuser(), JaylosAPI.getdatabase(), JaylosAPI.getpassword());
		mysql.update("CREATE TABLE IF NOT EXISTS Knockout(UUID varchar(64), Alltime int, Monthstats int, Month varchar(64), Kit varchar(64), Stick int, Spezial int);");
	}
	
	@EventHandler
	public void onjoin(PlayerJoinEvent e){
		e.setJoinMessage(null);
		Player p = e.getPlayer();
		p.setHealth(20);
		p.setLevel(0);
		p.setExp(0);
		p.setFoodLevel(20);
		p.getInventory().clear();
		for (PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());
		}
		Double x1 = Main.loc.getDouble(map+".spawn.x");
		Double y1 = Main.loc.getDouble(map+".spawn.y");
		Double z1 = Main.loc.getDouble(map+".spawn.z");
		Float yaw1 = (float) Main.loc.getDouble(map+".spawn.yaw");
		Float pitch1 = (float) Main.loc.getDouble(map+".spawn.pitch");
		World w1 = Bukkit.getWorld(Main.loc.getString(map+".spawn.world"));
		Location lobbyspawn = new Location(w1,x1,y1,z1,yaw1,pitch1);
		p.teleport(lobbyspawn);
		p.setGameMode(GameMode.SURVIVAL);
		ItemStack chest = new ItemStack(Material.CHEST);
		ItemMeta meta = chest.getItemMeta();
		meta.setDisplayName(ChatColor.RED+"Kits");
		chest.setItemMeta(meta);
		e.getPlayer().getInventory().setItem(4, chest);
		ItemStack settings = new ItemStack(Material.REDSTONE_COMPARATOR);
		ItemMeta settingsmeta = settings.getItemMeta();
		settingsmeta.setDisplayName(ChatColor.YELLOW+"Settings");
		settings.setItemMeta(settingsmeta);
		e.getPlayer().getInventory().setItem(6, settings);
		ItemStack chest1 = new ItemStack(Material.NETHER_STAR);
		ItemMeta meta1 = chest1.getItemMeta();
		meta1.setDisplayName(ChatColor.AQUA+"Maps");
		meta1.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
		chest1.setItemMeta(meta1);
		e.getPlayer().getInventory().setItem(2, chest1);
		
		TitleAPI.sendTabTitle(p, "§8===================§9 Jaylos.net §7- §9Knockout §8===================",
				ChatColor.GRAY + "Reporte Spieler mit " + ChatColor.RED + "/report " + ChatColor.GRAY
						+ "oder erstelle eine Party mit" + ChatColor.DARK_PURPLE + " /party");
		
		if(activescoreboard == 1){
			for(Player pp : Bukkit.getOnlinePlayers()){
		de.gamelos.scoreboard.Month.createScoreboard(pp);
			}
		}else{
			for(Player pp : Bukkit.getOnlinePlayers()){
			de.gamelos.scoreboard.Alltime.createScoreboard(pp);
			}
		}
	}
	

	@EventHandler
	public void ond(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			if(e.getCause().equals(DamageCause.ENTITY_ATTACK)){
				((Player)e.getEntity()).setHealth(20);
				e.setDamage(0);
			}else{
				e.setCancelled(true);
			}
			
			if(e.getEntity().getLocation().getY()>=itemhigh){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onfood(FoodLevelChangeEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler
	public void ondrop(PlayerDropItemEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onp(PlayerPickupItemEvent e){
		e.setCancelled(true);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equals("knockout")){
			Player p = (Player)sender;
			if(p.isOp()){
				if(args.length>=1){
				if(args[0].equals("setspawn")){
				Location loc = p.getLocation();
				if(Main.loc.getStringList("Maps")!= null){
				List<String> list = Main.loc.getStringList("Maps");
				list.add(p.getLocation().getWorld().getName());
				Main.loc.set("Maps", list);
				}else{
					List<String> list = new ArrayList<>();
					list.add(p.getLocation().getWorld().getName());
					Main.loc.set("Maps", list);
				}
				Main.loc.set(p.getLocation().getWorld().getName()+".spawn.x", loc.getX());
				Main.loc.set(p.getLocation().getWorld().getName()+".spawn.y", loc.getY());
				Main.loc.set(p.getLocation().getWorld().getName()+".spawn.z", loc.getZ());
				Main.loc.set(p.getLocation().getWorld().getName()+".spawn.yaw", loc.getYaw());
				Main.loc.set(p.getLocation().getWorld().getName()+".spawn.pitch", loc.getPitch());
				Main.loc.set(p.getLocation().getWorld().getName()+".spawn.world", loc.getWorld().getName());
				try {
					Main.loc.save(Main.locations);
					p.sendMessage(ChatColor.GREEN+"Der Spawn wurde erfolgreich gesetzt");	
				} catch (IOException e) {
					e.printStackTrace();
				}
				}else if(args[0].equals("setitemhigh")){
					Location loc = p.getLocation();
					Main.loc.set(p.getLocation().getWorld().getName()+".itemhigh.y", loc.getY());
					try {
						Main.loc.save(Main.locations);
						p.sendMessage(ChatColor.GREEN+"Die Itemhöhe wurde erfolgreich gesetzt");	
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else if(args[0].equals("setendhigh")){
					Location loc = p.getLocation();
					Main.loc.set(p.getLocation().getWorld().getName()+".endhigh.y", loc.getY());
					try {
						Main.loc.save(Main.locations);
						p.sendMessage(ChatColor.GREEN+"Die Endhöhe wurde erfolgreich gesetzt");	
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					p.sendMessage(ChatColor.GRAY+"===============================");
					p.sendMessage(ChatColor.GOLD+"/knockout setspawn");
					p.sendMessage(ChatColor.GOLD+"/knockout setitemhigh");
					p.sendMessage(ChatColor.GOLD+"/knockout setendhigh");
					p.sendMessage(ChatColor.GRAY+"===============================");
				}
				}else{
					p.sendMessage(ChatColor.GRAY+"===============================");
					p.sendMessage(ChatColor.GOLD+"/knockout setspawn");
					p.sendMessage(ChatColor.GOLD+"/knockout setitemhigh");
					p.sendMessage(ChatColor.GOLD+"/knockout setendhigh");
					p.sendMessage(ChatColor.GRAY+"===============================");
				}
			}else{
				p.sendMessage(Main.Prefix+ChatColor.RED+"Du hast keine Rechte dazu");
			}
		}	
		return false;
	}
//	
	
	public double itemhigh;
	public double endhigh;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList<Player> hasitem = new ArrayList();
	
	@EventHandler
	public void onmo(PlayerMoveEvent e){
		Player p = e.getPlayer();
					if(p.getLocation().getY()<endhigh){
						p.setExp(0);
						Double x1 = Main.loc.getDouble(map+".spawn.x");
						Double y1 = Main.loc.getDouble(map+".spawn.y");
						Double z1 = Main.loc.getDouble(map+".spawn.z");
						Float yaw1 = (float) Main.loc.getDouble(map+".spawn.yaw");
						Float pitch1 = (float) Main.loc.getDouble(map+".spawn.pitch");
						World w1 = Bukkit.getWorld(Main.loc.getString(map+".spawn.world"));
						Location lobbyspawn = new Location(w1,x1,y1,z1,yaw1,pitch1);
						p.teleport(lobbyspawn);
						p.getInventory().clear();
						if(hasitem.contains(p)){
							hasitem.remove(p);
						}
						ItemStack chest = new ItemStack(Material.CHEST);
						ItemMeta meta = chest.getItemMeta();
						meta.setDisplayName(ChatColor.RED+"Kits");
						chest.setItemMeta(meta);
						p.getInventory().setItem(4, chest);
						ItemStack settings = new ItemStack(Material.REDSTONE_COMPARATOR);
						ItemMeta settingsmeta = settings.getItemMeta();
						settingsmeta.setDisplayName(ChatColor.YELLOW+"Settings");
						settings.setItemMeta(settingsmeta);
						p.getInventory().setItem(6, settings);
						ItemStack chest1 = new ItemStack(Material.NETHER_STAR);
						ItemMeta meta1 = chest1.getItemMeta();
						meta1.setDisplayName(ChatColor.AQUA+"Maps");
						meta1.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
						chest1.setItemMeta(meta1);
						p.getInventory().setItem(2, chest1);
						p.playSound(p.getLocation(), Sound.NOTE_BASS, 1F, 1F);
						if(hit.containsKey(p)){
							ActionBar.sendActionBar(p, ChatColor.GRAY+"Du wurdest von "+ChatColor.RED+hit.get(p).getName()+ChatColor.GRAY+" getötet");
							ActionBar.sendActionBar(hit.get(p), ChatColor.GRAY+"Du hast "+ChatColor.RED+p.getName()+ChatColor.GRAY+" umgebracht");
							SQLStats.addalltime(hit.get(p).getUniqueId().toString(), 1);
							SQLStats.addmonth(hit.get(p).getUniqueId().toString(), 1);
							
//							=====================================================
							Player p1 = hit.get(p);
							if(SQLStats.getAlltime(p1.getUniqueId().toString()) == 100){
								p1.playSound(p1.getLocation(), Sound.AMBIENCE_THUNDER, 1F, 1F);
								feuerwerk(p1.getLocation(), Color.YELLOW);
								p1.sendMessage(Prefix+"Du hast das Kit "+ChatColor.YELLOW+"Angel"+ChatColor.GRAY+" freigeschaltet");
							}else if(SQLStats.getAlltime(p1.getUniqueId().toString()) == 150){
								p1.playSound(p1.getLocation(), Sound.AMBIENCE_THUNDER, 1F, 1F);
								feuerwerk(p1.getLocation(), Color.RED);
								p1.sendMessage(Prefix+"Du hast das Kit "+ChatColor.YELLOW+"Doppeljump"+ChatColor.GRAY+" freigeschaltet");
							}else if(SQLStats.getAlltime(p1.getUniqueId().toString()) == 200){
								p1.playSound(p1.getLocation(), Sound.AMBIENCE_THUNDER, 1F, 1F);
								feuerwerk(p1.getLocation(), Color.PURPLE);
								p1.sendMessage(Prefix+"Du hast das Kit "+ChatColor.YELLOW+"Jetpack"+ChatColor.GRAY+" freigeschaltet");
							}
//							=====================================================
							
							hit.get(p).playSound(hit.get(p).getLocation(), Sound.LEVEL_UP, 1F, 1F);
							if(activescoreboard == 1){
							for(Player pp : Bukkit.getOnlinePlayers()){
								de.gamelos.scoreboard.Month.createScoreboard(pp);
							}
							}else{
								for(Player pp : Bukkit.getOnlinePlayers()){
								de.gamelos.scoreboard.Alltime.createScoreboard(pp);
								}
							}
						}else{
							ActionBar.sendActionBar(p, ChatColor.RED+"Du hast dich selber umgebracht");
						}
						if(hit.containsKey(p)){
							hit.remove(p);
						}
						
					}else if(p.getLocation().getY()<itemhigh && !hasitem.contains(p)){
						p.closeInventory();
						p.getInventory().clear();
						int stickslot = 0;
						int spezialslot = 1;
						if(SQLStats.playerExists(p.getUniqueId().toString())){
							stickslot = SQLStats.getStick(p.getUniqueId().toString());
							spezialslot = SQLStats.getSpezial(p.getUniqueId().toString());
						}
						hasitem.add(p);
						ItemStack item = new ItemStack(Material.STICK);
						ItemMeta meta = item.getItemMeta();
						meta.addEnchant(Enchantment.KNOCKBACK, 2, true);
						item.setItemMeta(meta);
						p.getInventory().setItem(stickslot, item);
						String kit = SQLStats.getkit(p.getUniqueId().toString());
						if(!SQLStats.playerExists(p.getUniqueId().toString())||kit.equals("0")){
							p.getInventory().setItem(spezialslot,createitem(Material.ENDER_PEARL, ChatColor.DARK_PURPLE+"Enderperle"));
						}else if(kit.equals("Jetpack")){
							p.getInventory().setItem(spezialslot,createitem(Material.BLAZE_POWDER, ChatColor.BLUE+"Jetpack"));
							p.setExp(1);
						}else if(kit.equals("Doppeljump")){
							p.setAllowFlight(true);
							p.setExp(1);
						}else if(kit.equals("Angel")){
							p.getInventory().setItem(spezialslot,createitem(Material.FISHING_ROD, ChatColor.YELLOW+"Angel"));
						}
					}
					}
	
	
	
	@EventHandler
	public void ond(PlayerDeathEvent e){
		if(hasitem.contains(e.getEntity())){
			hasitem.remove(e.getEntity());
		}
	}
	
	@EventHandler
	public void onq(PlayerQuitEvent e){
		e.setQuitMessage(null);
		if(hasitem.contains(e.getPlayer())){
			hasitem.remove(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onsch(InventoryClickEvent e){
		if(e.getCurrentItem() != null){
			if(e.getInventory().getTitle().equals(ChatColor.GRAY+"Inventar")){
			if(e.getClickedInventory().getType() == InventoryType.PLAYER){
				if(e.getCurrentItem().getType()==Material.AIR){
				e.setCancelled(true);
				}
				if(e.getCurrentItem().getType() == Material.CHEST ||e.getCurrentItem().getType() == Material.REDSTONE_COMPARATOR ){
					
					e.setCancelled(true);
			
				}
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.AQUA+"Maps")){
					e.setCancelled(true);
				}
			}
			}else
			if(e.getCurrentItem().getType() == Material.CHEST ||e.getCurrentItem().getType() == Material.NETHER_STAR ||e.getCurrentItem().getType() == Material.REDSTONE_COMPARATOR ){
				
						e.setCancelled(true);
				
			}

			
		}
	}
	
	@EventHandler
	public void onc(InventoryCloseEvent e){
		if(e.getInventory().getTitle().equals(ChatColor.GRAY+"Inventar")){
			Player p = (Player) e.getPlayer();
			p.playSound(p.getLocation(), Sound.ANVIL_USE, 1F, 1F);
			ItemStack item = new ItemStack(Material.STICK);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW+"Knockbackstick");
			meta.addEnchant(Enchantment.KNOCKBACK, 2, true);
			item.setItemMeta(meta);
			ItemStack sp = createitem(Material.NETHER_STAR, ChatColor.YELLOW+"Spezial");
//			==================================================			
			int i = -1;
			int i1 = -1;
			int b = 0;
			for(ItemStack ii : e.getInventory().getContents()){
				if(ii!=null){
				if(ii.getType() == Material.STICK){
					i = b;
				}
				if(ii.getType() == Material.NETHER_STAR){
					i1 = b;
				}
				}
				b++;
			}
			if(i==-1){
				if(i1!=0){
					i = 0;
				}else{
					i = 1;
				}
			}
			if(i1==-1){
				if(i!=0){
					i1 = 0;
				}else{
					i1 = 1;
				}
			}
			SQLStats.setStick(p.getUniqueId().toString(), i);
			SQLStats.setSpezial(p.getUniqueId().toString(), i1);
			p.sendMessage(Main.Prefix+ChatColor.GREEN+"Das Inventar wurde erfolgreich gespeichert!");
//			==================================================
			if(p.getInventory().contains(sp)){
				p.getInventory().remove(sp);
			}
			if(p.getInventory().contains(item)){
				p.getInventory().remove(item);
			}
//			==================================================
		}
	}
	
	public static HashMap<Player,Player> hit = new HashMap<>();
	
	@EventHandler
	public void onda(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
		hit.put((Player)e.getEntity(), (Player)e.getDamager());
		}
	}
	
	int activescoreboard = 1;
	
	public void scoreboardchange(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			@Override
			public void run() {
				if(activescoreboard == 1){
					activescoreboard = 2;
					for(Player pp : Bukkit.getOnlinePlayers()){
						de.gamelos.scoreboard.Alltime.createScoreboard(pp);
						}
				}else{
					activescoreboard=1;
					for(Player pp : Bukkit.getOnlinePlayers()){
						de.gamelos.scoreboard.Month.createScoreboard(pp);
						}
				}
			}
		}, 20*20, 20*20);
	}
	
	@EventHandler
	public void oninteract(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_AIR||e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(e.getPlayer().getItemInHand() != null){
				if(e.getPlayer().getItemInHand().getType() == Material.CHEST){
					int kills = SQLStats.getAlltime(e.getPlayer().getUniqueId().toString());
					Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GRAY+"Kits - "+ChatColor.YELLOW+kills+" Kills");
					inv.addItem(createitem(Material.ENDER_PEARL, ChatColor.DARK_PURPLE+"Enderperle"));
					if(kills >= 100 || e.getPlayer().hasPermission("kits.all")){
						inv.addItem(createitem(Material.FISHING_ROD, ChatColor.YELLOW+"Angel"));
					}else{
						inv.addItem(createitem(Material.BARRIER, ChatColor.RED+"Erst ab 100 Kills freigeschaltet"));	
					}
					if(kills >= 150 || e.getPlayer().hasPermission("rang.premium")){
						inv.addItem(createitem(Material.POTION, ChatColor.GREEN+"Doppeljump"));
					}else{
						inv.addItem(createitem(Material.BARRIER, ChatColor.RED+"Erst ab 150 Kills freigeschaltet"));	
					}
					if(kills >= 200 || e.getPlayer().hasPermission("rang.premium")){
						inv.addItem(createitem(Material.BLAZE_POWDER, ChatColor.BLUE+"Jetpack"));
					}else{
						inv.addItem(createitem(Material.BARRIER, ChatColor.RED+"Erst ab 200 Kills freigeschaltet"));
					}
					
					e.getPlayer().openInventory(inv);
				}else if(e.getPlayer().getItemInHand().getType() == Material.NETHER_STAR){
					if(e.getPlayer().hasPermission("rang.premium")){
						if(cooldwn == 0){
						Inventory inv = Bukkit.createInventory(null, 9, ChatColor.YELLOW+"Maps");
						List<String> list = Main.loc.getStringList("Maps");
						for(String ss : list){
							inv.addItem(createitem(Material.GRASS, ChatColor.RED+ss));
						}
					e.getPlayer().openInventory(inv);
						}else{
							e.getPlayer().sendMessage(Prefix+"Du kannst die Map erst in "+ChatColor.RED+cooldwn+" Sekunden "+ChatColor.GRAY+"wieder wechseln");
						}
					}else{
						e.getPlayer().sendMessage(Prefix+ChatColor.RED+"Du kannst nur mit "+ChatColor.GOLD+"Premium"+ChatColor.RED+" die Map wechseln");

					}
				}else if(e.getPlayer().getItemInHand().getType() == Material.REDSTONE_COMPARATOR){
					Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GRAY+"Inventar");
					ItemStack item = new ItemStack(Material.STICK);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(ChatColor.YELLOW+"Knockbackstick");
					meta.addEnchant(Enchantment.KNOCKBACK, 2, true);
					item.setItemMeta(meta);
					int stickslot = 0;
					int spezialslot = 1;
					if(SQLStats.playerExists(e.getPlayer().getUniqueId().toString())){
						stickslot = SQLStats.getStick(e.getPlayer().getUniqueId().toString());
						spezialslot = SQLStats.getSpezial(e.getPlayer().getUniqueId().toString());
					}
					inv.setItem(stickslot,item);
					inv.setItem(spezialslot,createitem(Material.NETHER_STAR, ChatColor.YELLOW+"Spezial"));
					e.getPlayer().openInventory(inv);
				}
			}
		}
	}
	
	@EventHandler
	public void invclick(InventoryClickEvent e){
		if(e.getInventory().getTitle().contains(ChatColor.GRAY+"Kits -")){
			e.setCancelled(true);
			if(e.getCurrentItem() != null){
			if(e.getCurrentItem().getType() == Material.BARRIER){
			e.getWhoClicked().sendMessage(ChatColor.RED+"Du hast dieses Item noch nicht freigeschaltet");	
			}else{
				if(e.getCurrentItem().getType() == Material.ENDER_PEARL){
					SQLStats.setkit(e.getWhoClicked().getUniqueId().toString(), "0");
				}else{
				SQLStats.setkit(e.getWhoClicked().getUniqueId().toString(), ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
				}
				e.getWhoClicked().closeInventory();
			}
			}
		}else if(e.getInventory().getTitle().equals(ChatColor.YELLOW+"Maps")){
			e.setCancelled(true);
			if(cooldwn == 0){
			if(e.getCurrentItem()!=null&&e.getCurrentItem().getType() != Material.AIR){
			String mapname = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
			e.getWhoClicked().closeInventory();
			e.getWhoClicked().sendMessage(Prefix+ChatColor.GREEN+"Du hast die Map erfolgreich gewechselt!");
			cooldown();
			File weltordner = new File(mapname);
			weltordner.isDirectory();
//			================
			map = mapname;
			itemhigh = Main.loc.getDouble(map+".itemhigh.y");
			endhigh = Main.loc.getDouble(map+".endhigh.y");
			Double x1 = Main.loc.getDouble(mapname+".spawn.x");
			Double y1 = Main.loc.getDouble(mapname+".spawn.y");
			Double z1 = Main.loc.getDouble(mapname+".spawn.z");
			Float yaw1 = (float) Main.loc.getDouble(mapname+".spawn.yaw");
			Float pitch1 = (float) Main.loc.getDouble(mapname+".spawn.pitch");
			World w1 = Bukkit.getWorld(Main.loc.getString(mapname+".spawn.world"));
			Location lobbyspawn = new Location(w1,x1,y1,z1,yaw1,pitch1);
//			================
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
				@Override
				public void run() {
					for(Player pp: Bukkit.getOnlinePlayers()){
					pp.teleport(lobbyspawn);	
					}
					CloudServer.getInstance().setMotd(map);
				}
			},20*2);
		}
			}
		}
	}
	
	public static ItemStack createitem(Material m, String name){
		ItemStack item = new ItemStack(m);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	
	@EventHandler
	   public void onPlayerMoveEvent (PlayerMoveEvent event) {
	   Player p = event.getPlayer();
	      
	   if(p.getGameMode() != GameMode.CREATIVE && SQLStats.getkit(p.getUniqueId().toString()).equals("Doppeljump")){
	      if(p.isFlying()){
	    	  Vector v = p.getLocation().getDirection().multiply(3D).setY(1D);
	    	  p.setAllowFlight(false);
	    	  p.setVelocity(v);
	    	  p.playSound(p.getLocation(), Sound.CAT_HISS, 1F, 1F);
	    	  p.setExp(0);
	      }
	   }
	   }
	
	@EventHandler
	public void onchat(PlayerChatEvent e){
		Player p = e.getPlayer();
		e.setCancelled(true);
//		
		String Massage = e.getMessage();
//		
		for(Player pp:Bukkit.getOnlinePlayers()){
			pp.sendMessage(JaylosAPI.getchatname(p, pp)+ChatColor.DARK_GRAY+" >> "+ChatColor.GRAY+Massage);
		}
	}
	
	@EventHandler
	public void onplayerfisch(PlayerFishEvent e){
		Player p = e.getPlayer();
		FishHook hook = e.getHook();
		if(hook.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR){
			Location playerLocation = p.getLocation();
			Location hookLocation = hook.getLocation();
			
			Vector vector = p.getVelocity();
			double distance = playerLocation.distance(hookLocation);
			
			vector.setX((1.08D*distance)*(hookLocation.getX()-playerLocation.getX())/distance);
			vector.setY((1D*distance)*(hookLocation.getY()-playerLocation.getY())/distance - -0.5D * distance);
			vector.setZ((1.08D*distance)*(hookLocation.getZ()-playerLocation.getZ())/distance);
		
			p.setVelocity(vector);
			p.getItemInHand().setDurability((short) (p.getItemInHand().getDurability()+10));
		}
	}
	
	public static ArrayList<Player> jetpack = new ArrayList<>();
	
	@EventHandler
	public void onSneak(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_AIR||e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(p.getItemInHand().getType() == Material.BLAZE_POWDER){
				if(!jetpack.contains(p)){
					jetpack.add(p);
				e.getPlayer().setItemInHand(createitem(Material.BLAZE_POWDER, ChatColor.GREEN+"Jetpack"));
			new BukkitRunnable() {
				@Override
				public void run() {
					if(jetpack.contains(p) && p.getExp()>0){
						p.setExp(p.getExp()-0.05F);
						p.setVelocity(p.getLocation().getDirection().multiply(0.7D).setY(0.3D));
					}else{
						cancel();
					}
				}
			}.runTaskTimer(this, 1L, 4L);
			new BukkitRunnable() {
				@Override
				public void run() {
					if(jetpack.contains(p) && p.getExp()>0){
					p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 10);
					}else{
						cancel();
					}
				}
			}.runTaskTimer(this, 1L, 4L);
				}else{
					jetpack.remove(p);
					e.getPlayer().setItemInHand(createitem(Material.BLAZE_POWDER, ChatColor.BLUE+"Jetpack"));
				}
		}
		}
	}
	
	public int cooldwn = 0;
	int count = 0;
	public void cooldown(){
		cooldwn = 300;
		count = Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable(){
			@Override
			public void run() {
				if(cooldwn>0){
					cooldwn--;
				}else{
					Bukkit.getScheduler().cancelTask(count);
					cooldwn=0;
				}
			}
		}, 20, 20);
	}
	
	
    public static void feuerwerk(Location loc, Color c){
		Firework firework = loc.getWorld().spawn(loc, Firework.class);
		FireworkEffect effect = FireworkEffect.builder()
				.withColor(c)
				.flicker(true)
				.trail(false)
				.withFade(Color.WHITE)
				.with(FireworkEffect.Type.BALL_LARGE)
				.build();
		FireworkMeta meta = firework.getFireworkMeta();
		meta.addEffect(effect);
		meta.setPower(0);
		firework.setFireworkMeta(meta);
	}
    
    @EventHandler
    public void onchan(WeatherChangeEvent e){
    	e.setCancelled(true);
    }
    
    @EventHandler
    public void onbreak(BlockBreakEvent e){
    	e.setCancelled(true);
    }
    
    @EventHandler
    public void onplace(BlockPlaceEvent e){
    	e.setCancelled(true);
    }
    
    @EventHandler
    public void ons(PlayerToggleSneakEvent e){
    	if(jetpack.contains(e.getPlayer())){
    		jetpack.remove(e.getPlayer());
    	}
    }
    
    @EventHandler
    public void onspawne(EntitySpawnEvent e){
    	if(e.getEntityType() == EntityType.ENDERMITE){
    		e.setCancelled(true);
    	}
    }
    
    @EventHandler
    public void unnick(unNickEvent e) {
    	Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				if(activescoreboard == 1){
					for(Player pp : Bukkit.getOnlinePlayers()){
						de.gamelos.scoreboard.Alltime.createScoreboard(pp);
						}
				}else{
					for(Player pp : Bukkit.getOnlinePlayers()){
						de.gamelos.scoreboard.Month.createScoreboard(pp);
						}
				}
			}
    	}, 20);
    }
}
