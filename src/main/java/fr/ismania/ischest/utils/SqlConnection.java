package fr.ismania.ischest.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import fr.ismania.ischest.Main;

public class SqlConnection {

	private String host, db_name, user, pass;
	private int port;
	private Connection connection;
	private Main main;

	/**
	 * @param host Ip for acces at the db. ex. localhost, 178.237.84.36, exemple.fr
	 * @param db_name Name of data base
	 * @param user User for login
	 * @param pass Password for login
	 */
	public SqlConnection(Main main, String host, String db_name, String user, String pass) {
		this(main, host, 3306, db_name, user, pass);
	}

	/**
	 * @param host Ip for acces at the db. ex. localhost, 178.237.84.36, exemple.fr
	 * @param port Port of database.
	 * @param db_name Name of data base
	 * @param user User for login
	 * @param pass Password for login
	 */
	public SqlConnection(Main main, String host, int port, String db_name, String user, String pass) {

		this.main = main;
		this.host = host;
		this.port = port;
		this.db_name = db_name;
		this.user = user;
		this.pass = pass;

	}

	/**
	 * Connection at the database.
	 * @throws SqlException
	 */
	public void connection() {

		try {

			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db_name, user, pass);
			Bukkit.getLogger().info("[IsChest] Connection bdd OK !");

		} catch(SQLException e) {
		}

	}

	/**
	 * Disconnect of database
	 * @throws SqlException
	 */
	public void disconnect() {

		if(isConnected()) {

			try {

				Bukkit.getLogger().info("[IsChest] Connection à la bdd fermée !");
				connection.close();

			} catch(SQLException e) {
				Bukkit.getLogger().info("[IsChest] Une erreur est survenue lors de la déconnection à la bdd !");
				e.printStackTrace();
			}

		}

	}

	/**
	 * @return boolean
	 */
	public boolean isConnected() { 
		return connection != null;
	}

	/**
	 * @return Connection
	 */
	public Connection getConnection() {
		return connection;
	}

	public void insertNewPlayer(UUID uuid) {

		try {

			PreparedStatement q = connection.prepareStatement("INSERT INTO ischest SET uuid = ?, ischest = ?");
			q.setString(1, uuid.toString());
			q.setString(2, "");
			q.executeUpdate();
			q.close();

		} catch(SQLException e) {
			e.printStackTrace();
		}

	}

	public boolean playerIsSet(UUID uuid) {

		try {

			PreparedStatement q = connection.prepareStatement("SELECT * FROM ischest WHERE uuid = ?");
			q.setString(1, uuid.toString());

			ResultSet rs = q.executeQuery();

			boolean hasAccount = rs.next();

			q.close();

			return hasAccount;

		} catch(SQLException e) {
			e.printStackTrace();
		}

		return false;

	}

	public Inventory getContentOfIsChest(UUID uuid) {

		try {

			PreparedStatement q = connection.prepareStatement("SELECT ischest FROM ischest WHERE uuid = ?");
			q.setString(1, uuid.toString());

			ResultSet rs = q.executeQuery();

			if(rs.next()) {

				String inv = rs.getString("ischest");

				q.close();

				return main.bukkitSerialisation.fromBase64(inv);

			}

		} catch(SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public void insertContentOfIsChest(UUID uuid, Inventory inv) {

		try {

			PreparedStatement q = connection.prepareStatement("UPDATE ischest SET ischest = ? WHERE uuid = ?");
			q.setString(1, main.bukkitSerialisation.toBase64(inv));
			q.setString(2, uuid.toString());
			q.executeUpdate();
			q.close();

		} catch(SQLException e) {
			e.printStackTrace();
		}

	}

}
