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
			
			if(args.length == 1 && p.isOp()) {
				
				Player target = Bukkit.getPlayer(args[0]);
				
				if(main.skyBlockAPI.inTeam(target.getUniqueId())) {

					UUID ownerUuid = main.skyBlockAPI.getTeamLeader(target.getUniqueId());

					Bukkit.getLogger().info("L'admin : " + p.getName() + " à  ouvert le ischest de l'ile de : " + Bukkit.getPlayer(ownerUuid).getName());

					if(main.inIsChest.contains(ownerUuid)) {

						p.sendMessage("§cUn joueur est déjà dans ce IsChest !");

						return true;
					}

					if(!main.sql.playerIsSet(ownerUuid)) {
						main.sql.insertNewPlayer(ownerUuid);
					}

					openInventory(p, ownerUuid);

					main.inIsChest.add(ownerUuid);

				} else {

					UUID ownerUuid = main.skyBlockAPI.getIslandOwnedBy(target.getUniqueId()).getOwner();

					Bukkit.getLogger().info("L'admin : " + p.getName() + " à  ouvert le ischest de l'ile de : " + target.getName());

					if(!main.sql.playerIsSet(ownerUuid)) {
						main.sql.insertNewPlayer(ownerUuid);
					}

					openInventory(p, ownerUuid);

				}
				
			} else {

				if(!p.getWorld().getName().equalsIgnoreCase("ASkyBlock")) {
					p.sendMessage("§cLe IsChest n'est pas disponible dans le monde actuel !");
					return true;
				}

				if(main.skyBlockAPI.inTeam(p.getUniqueId())) {

					UUID ownerUuid = main.skyBlockAPI.getTeamLeader(p.getUniqueId());

					Bukkit.getLogger().info("Le joueur : " + p.getName() + " à  ouvert le ischest de l'ile de : " + Bukkit.getPlayer(ownerUuid).getName());

					if(main.inIsChest.contains(ownerUuid)) {

						p.sendMessage("§cUn joueur est déjà dans le IsChest de votre île !");

						return true;
					}

					if(!main.sql.playerIsSet(ownerUuid)) {
						main.sql.insertNewPlayer(ownerUuid);
					}

					openInventory(p, ownerUuid);

					main.inIsChest.add(ownerUuid);

				} else {

					UUID ownerUuid = main.skyBlockAPI.getIslandOwnedBy(p.getUniqueId()).getOwner();

					Bukkit.getLogger().info("Le joueur : " + p.getName() + " à  ouvert le ischest de l'ile de son île");

					if(!main.sql.playerIsSet(ownerUuid)) {
						main.sql.insertNewPlayer(ownerUuid);
					}

					openInventory(p, ownerUuid);

				}

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
