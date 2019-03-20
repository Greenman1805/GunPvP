package de.greenman1805.gunpvp;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.shampaggon.crackshot.CSUtility;

import de.greenman1805.gunpvp.Leaderboard.LeaderboardType;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	public static Economy econ = null;
	public static YamlConfiguration playerdata;
	public static YamlConfiguration defaultWeapons;
	public static YamlConfiguration defaultExplosives;
	public static CSUtility csu;
	public static String prefix = "§8[§6GunShop§8] §f";
	public static Main plugin;
	public static String scoreboardtitle = "§9Stats";
	

	@Override
	public void onEnable() {
		if (!setupEconomy()) {
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new Listeners(this), this);
		registerCommands("weapons", new Listeners(this));
		registerCommands("gunshop", new Listeners(this));
		registerCommands("explosiveshop", new Listeners(this));
		csu = new CSUtility();
		checkFiles();
		getValues();
		GunAPI.setup();
		plugin = this;
		new Leaderboard(new Location(Bukkit.getWorld("GunPvP"), 1666, 20, 184), LeaderboardType.Kills, 30);
		new Leaderboard(new Location(Bukkit.getWorld("GunPvP"), 1666, 20, 166), LeaderboardType.Bounty, 30);
	}

	public void registerCommands(String cmd, CommandExecutor exe) {
		getCommand(cmd).setExecutor(exe);
	}

	@Override
	public void onDisable() {
		Leaderboard.deleteAll();
		try {
			playerdata.save(new File("plugins//GunPvP//playerdata.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void checkFiles() {
		File file1 = new File("plugins//GunPvP");
		File file2 = new File("plugins//GunPvP//playerdata.yml");

		if (!file1.isDirectory()) {
			file1.mkdir();
		}
		if (!file2.exists()) {
			try {
				file2.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void getValues() {
		playerdata = YamlConfiguration.loadConfiguration(new File("plugins//GunPvP//playerdata.yml"));
		defaultWeapons = YamlConfiguration.loadConfiguration(new File("plugins//CrackShot//weapons//defaultWeapons.yml"));
		defaultExplosives = YamlConfiguration.loadConfiguration(new File("plugins//CrackShot//weapons//defaultExplosives.yml"));
		Weapon.loadWeapons();
		Weapon.loadExplosives();
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

}
