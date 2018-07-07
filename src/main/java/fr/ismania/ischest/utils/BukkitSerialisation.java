package fr.ismania.ischest.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import fr.ismania.ischest.Main;

public class BukkitSerialisation {
	
	private Main main;
	
	public BukkitSerialisation(Main main) {
		this.main = main;
	}
	
	/**
	 * @param Inventory Insert inventory to convert to base 64.
	 * @return String Return string (base 64 of inventory).
	 */
	public String toBase64(Inventory inventory) {
		
		if(inventory == null) return null;
		
		try {
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

			// Write the size of the inventory
			dataOutput.writeInt(inventory.getSize());

			// Save every element in the list
			for (int i = 0; i < inventory.getSize(); i++) {
				dataOutput.writeObject(inventory.getItem(i));
			}

			// Serialize that array
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
			
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}   
		
	}

	/**
	 * @param String Insert a string to convert to inventory.
	 * @return Inventory Return a inventory.
	 * @throws IOException
	 */
	public Inventory fromBase64(String data) throws IOException {
		
		if(data.isEmpty()) return null;
		
		try {
			
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt(), main.invName);

			// Read the serialized inventory
			for (int i = 0; i < inventory.getSize(); i++) {
				inventory.setItem(i, (ItemStack) dataInput.readObject());
			}
			dataInput.close();
			return inventory;
			
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to decode class type.", e);
		}
		
	}
	
}