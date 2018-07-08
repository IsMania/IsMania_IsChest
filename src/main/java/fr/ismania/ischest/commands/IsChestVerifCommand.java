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

public class IsChestVerifCommand implements CommandExecutor {

	private Main main;
	
	public IsChestVerifCommand(Main main) {
		this.main = main;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!sender.isOp()) {
			sender.sendMessage("§cVous n'avez pas la permission !");
			return true;
		}
		
		if(args.length != 1) {
			sender.sendMessage("§c/ischestverif <player>");
			return true;
		}
		
		Player p = (Player) sender;
		
		UUID targetUuid = main.sql.getUuidFromPlayer(args[0]);
		
		if(targetUuid == null) {
			p.sendMessage("§cCe joueur n'existe pas !");
			return true;
		}
		
		if(main.skyBlockAPI.inTeam(targetUuid)) {

			UUID ownerUuid = main.skyBlockAPI.getTeamLeader(targetUuid);
			
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

			UUID ownerUuid = main.skyBlockAPI.getIslandOwnedBy(targetUuid).getOwner();

			if(!main.sql.playerIsSet(ownerUuid)) {
				main.sql.insertNewPlayer(ownerUuid);
			}

			openInventory(p, ownerUuid);

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
		
		main.inVerif.put(user, uuid);

	}

}
