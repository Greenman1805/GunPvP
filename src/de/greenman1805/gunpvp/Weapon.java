package de.greenman1805.gunpvp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Weapon {
	public static List<Weapon> weapons = new ArrayList<Weapon>();
	public static List<Weapon> explosives = new ArrayList<Weapon>();
	public static List<Weapon> all = new ArrayList<Weapon>();

	public String name;
	public ItemStack item;
	public int price;
	public ArrayList<String> lore_list;

	public Weapon(String name, ItemStack item, int price, ArrayList<String> lore_list) {
		this.name = name;
		this.item = item;
		this.price = price;
		this.lore_list = lore_list;
	}

	public static void loadWeapons() {
		for (String key : Main.defaultWeapons.getConfigurationSection("").getKeys(false)) {

			String type = Main.defaultWeapons.getString(key + ".Item_Information.Item_Type");
			ItemStack i = new ItemStack(Material.getMaterial(type), 1);
			String lore = Main.defaultWeapons.getString(key + ".Item_Information.Item_Lore").replace('&', '§');
			int price = Main.defaultWeapons.getInt(key + ".Item_Information.Price");
			ArrayList<String> lore_list = new ArrayList<String>(Arrays.asList(lore.split("\\|")));
			GunAPI.setItemName(i, Main.defaultWeapons.getString(key + ".Item_Information.Item_Name").replace('&', '§'), lore_list);
			Weapon w = new Weapon(key, i, price, lore_list);
			weapons.add(w);
			all.add(w);
		}
	}

	public static void loadExplosives() {
		for (String key : Main.defaultExplosives.getConfigurationSection("").getKeys(false)) {

			String type = Main.defaultExplosives.getString(key + ".Item_Information.Item_Type");
			ItemStack i = new ItemStack(Material.getMaterial(type), 1);
			String lore = Main.defaultExplosives.getString(key + ".Item_Information.Item_Lore").replace('&', '§');
			int price = Main.defaultExplosives.getInt(key + ".Item_Information.Price");
			ArrayList<String> lore_list = new ArrayList<String>(Arrays.asList(lore.split("\\|")));
			GunAPI.setItemName(i, Main.defaultExplosives.getString(key + ".Item_Information.Item_Name").replace('&', '§'), lore_list);

			Weapon w = new Weapon(key, i, price, lore_list);
			explosives.add(w);
			all.add(w);
		}
	}

	public static String getNameForItem(ItemStack item) {
		for (Weapon w : all) {
			if (w.item.getItemMeta().getDisplayName().equalsIgnoreCase(item.getItemMeta().getDisplayName())) {
				return w.name;
			}
		}
		return null;
	}

	public static Weapon getWeaponForName(String name) {
		for (Weapon w : all) {
			if (name.equalsIgnoreCase(w.name)) {
				return w;
			}
		}
		return null;
	}

}
