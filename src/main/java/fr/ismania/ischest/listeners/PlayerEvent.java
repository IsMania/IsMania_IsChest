package fr.ismania.ischest.listeners;

import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import fr.ismania.ischest.Main;

public class PlayerEvent implements Listener {

	private Main main;

	public PlayerEvent(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent e) {

		Player p = (Player) e.getPlayer();
		Inventory inv = e.getInventory();

		main.sql.connection();

		if(inv.getName().equals(main.invName)) {

			if(main.inVerif.containsKey(p)) {

				UUID ownerUuid = main.skyBlockAPI.getTeamLeader(main.inVerif.get(p));

				System.out.println(main.inVerif.get(p));
				
				if(ownerUuid == null) {
					ownerUuid = main.skyBlockAPI.getIslandOwnedBy(main.inVerif.get(p)).getOwner();
				}
				
				main.inIsChest.remove(ownerUuid);
				main.inVerif.remove(p);

				p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 2F, 2F);

				main.sql.insertContentOfIsChest(ownerUuid, inv);

			} else {

				if(main.skyBlockAPI.inTeam(p.getUniqueId())) {

					UUID ownerUuid = main.skyBlockAPI.getTeamLeader(p.getUniqueId());

					main.inIsChest.remove(ownerUuid);

					p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 2F, 2F);

					main.sql.insertContentOfIsChest(ownerUuid, inv);

				} else {

					UUID ownerUuid = main.skyBlockAPI.getIslandOwnedBy(p.getUniqueId()).getOwner();

					p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 2F, 2F);

					main.sql.insertContentOfIsChest(ownerUuid, inv);

				}

			}

		}

	}

}
