package fr.ismania.ischest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.wasteofplastic.askyblock.ASkyBlockAPI;

import fr.ismania.ischest.commands.IsChestCommand;
import fr.ismania.ischest.commands.IsChestVerifCommand;
import fr.ismania.ischest.listeners.PlayerEvent;
import fr.ismania.ischest.utils.BukkitSerialisation;
import fr.ismania.ischest.utils.SqlConnection;

public class Main extends JavaPlugin {

	/**
	 * SqlConnection variable.
	 * 
	 */
	public SqlConnection sql;
	
	/**
	 * BukkitSerialisation variable.
	 */
	public BukkitSerialisation bukkitSerialisation = new BukkitSerialisation(this);
	
	/**
	 * ASkyBlockAPI variable
	 */
	public ASkyBlockAPI skyBlockAPI;
	
	/**
	 * Name of isChest.
	 */
	public String invName = "§7[§aIs§2Chest§7]";
	
	public List<UUID> inIsChest = new ArrayList<UUID>();
	
	public Map<Player, UUID> inVerif = new HashMap<Player, UUID>();
	
	@Override
	public void onEnable() {
		
		getLogger().info("Plugin IsChest activé !");
		
		skyBlockAPI = ASkyBlockAPI.getInstance();
		
		sql = new SqlConnection(this, "minecraft1135.omgserv.com", 3306, "bc_189505", "******", "******");
		sql.connection();
		
		getCommand("ischest").setExecutor(new IsChestCommand(this));
		getCommand("ischestverif").setExecutor(new IsChestVerifCommand(this));
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerEvent(this), this);
		
	}
	
	@Override
	public void onDisable() {
		
		getLogger().info("Plugin IsChest désactivé !");
		
	}
	
}
