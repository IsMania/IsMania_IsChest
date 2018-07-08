package fr.ismania.ischest.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.ismania.ischest.Main;

public class IsChestCommand implements CommandExecutor {

	private Main main;

	public IsChestCommand(Main main) {
		this.main = main;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof Player) {

			Player p = (Player) sender;

			main.sql.connection();

			if(!p.getWorld().getName().equalsIgnoreCase("ASkyBlock")) {
				p.sendMessage("§cLe IsChest n'est pas disponible dans le monde actuel !");
				return true;
			}

			if(main.skyBlockAPI.inTeam(p.getUniqueId())) {

				UUID ownerUuid = main.skyBlockAPI.getTeamLeader(p.getUniqueId());

				if(main.inIsChest.contains(ownerUuid)) {

					p.sendMessage("§cUn joueur est déjà dans le IsChest de votre île !");

					return true;
				}

				if(!main.sql.playerIsSet(ownerUuid)) {
					main.sql.insertNewPlayer(ownerUuid);
				}

				openInventory(p, ownerUuid);

				main.inIsChest.add(ownerUuid);

				main.sql.updateInfos(Bukkit.getPlayer(ownerUuid));

			} else if(main.skyBlockAPI.hasIsland(p.getUniqueId())){

				UUID ownerUuid = main.skyBlockAPI.getIslandOwnedBy(p.getUniqueId()).getOwner();
				
				if(!main.sql.playerIsSet(ownerUuid)) {
					main.sql.insertNewPlayer(ownerUuid);
				}

				openInventory(p, ownerUuid);

				main.sql.updateInfos(Bukkit.getPlayer(ownerUuid));

			} else {
				sender.sendMessage("§cVous n'avez pas 'île !");
			}

		}

		return true;
	}

	public void openInventory(Player user, UUID uuid) {

		Inventory inv = main.sql.getContentOfIsChest(uuid);

		if(inv == null) {
			inv = Bukkit.createInventory(null, 54, main.invName);
		}

		user.playSound(user.getLocation(), Sound.BLOCK_CHEST_OPEN, 2F, 2F);
		user.openInventory(inv);

	}

}
