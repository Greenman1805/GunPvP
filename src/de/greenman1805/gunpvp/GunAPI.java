package de.greenman1805.gunpvp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;



public class GunAPI {

	public static ItemStack gap = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
	static ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
	static ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
	static ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
	static ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
	static ItemStack compass = new ItemStack(Material.COMPASS, 1);
	
	public static String weaponmenutitle = "Wähle deine Waffen";
	public static String gunshoptitle = "Kaufe eine Waffe";
	public static String explosiveshoptitle = "Kaufe dein Zubehör";
	public static String firstweapontitle = "Erste Waffe";
	public static String secondweapontitle = "Zweite Waffe";
	public static String thirdweapontitle = "Dritte Waffe";
	
	public static String firstdefaultweapon = "M27";
	public static String seconddefaultweapon = "Mossberg500";
	public static String thirddefaultweapon = "Glock";

	public static void setup() {
		LeatherArmorMeta lam;
		lam = (LeatherArmorMeta) helmet.getItemMeta();
		lam.setColor(Color.fromBGR(51, 127, 102));
		helmet.setItemMeta(lam);
		chestplate.setItemMeta(lam);
		leggings.setItemMeta(lam);
		boots.setItemMeta(lam);
		setItemName(compass, "§5Spieler Radar", null);
		setItemName(gap, "§f", null);
	}

	public static void buy(Player p, String weapon) {
		List<String> bought = Main.playerdata.getStringList("Players." + p.getUniqueId() + ".bought");
		bought.add(weapon);
		Main.playerdata.set("Players." + p.getUniqueId() + ".bought", bought);
	}

	public static boolean hasBought(Player p, String weapon) {
		if (weapon.equalsIgnoreCase("M27") || weapon.equalsIgnoreCase("Mossberg500") || weapon.equalsIgnoreCase("Glock")) {
			return true;
		}
		List<String> bought = Main.playerdata.getStringList("Players." + p.getUniqueId() + ".bought");
		for (String w : bought) {
			if (w.equalsIgnoreCase(weapon)) {
				return true;
			}
		}

		return false;
	}
	
	public static String getFirstWeapon(Player p) {
		String weapon = Main.playerdata.getString("Players." + p.getUniqueId() + ".first");
		if (weapon == null) {
			return firstdefaultweapon;
		} else {
			return weapon;
		}
	}
	
	public static String getSecondWeapon(Player p) {
		String weapon = Main.playerdata.getString("Players." + p.getUniqueId() + ".second");
		if (weapon == null) {
			return seconddefaultweapon;
		} else {
			return weapon;
		}
	}
	
	public static String getThirdWeapon(Player p) {
		String weapon = Main.playerdata.getString("Players." + p.getUniqueId() + ".third");
		if (weapon == null) {
			return thirddefaultweapon;
		} else {
			return weapon;
		}
	}
	
	public static String getSpecial(Player p) {
		return Main.playerdata.getString("Players." + p.getUniqueId() + ".special");
	}

	
	public static void setFirstWeapon(Player p, String weapon) {
		Main.playerdata.set("Players." + p.getUniqueId() + ".first", weapon);
	}
	
	public static void setSecondWeapon(Player p, String weapon) {
		Main.playerdata.set("Players." + p.getUniqueId() + ".second", weapon);
	}
	
	public static void setThirdWeapon(Player p, String weapon) {
		Main.playerdata.set("Players." + p.getUniqueId() + ".third", weapon);
	}
		
	public static void setSpecial(Player p, String weapon) {
		Main.playerdata.set("Players." + p.getUniqueId() + ".special", weapon);
	}
	

	public static void setItemName(ItemStack item, String name, ArrayList<String> lore_list) {
		ItemMeta meta;
		meta = item.getItemMeta();
		meta.setLore(lore_list);
		meta.setDisplayName(name);
		item.setItemMeta(meta);
	}

	public static void openGunShop(Player p) {
		Inventory inv = Bukkit.getServer().createInventory(null, 27, gunshoptitle);
		int i = 0;
		for (Weapon w : Weapon.weapons) {
			if (!hasBought(p, w.name)) {
				ItemStack item = w.item.clone();
				ArrayList<String> lore = (ArrayList<String>) w.lore_list.clone();
				lore.add("§9Preis: §f" + w.price + " Shards");
				setItemName(item, Main.defaultWeapons.getString(w.name + ".Item_Information.Item_Name").replace('&', '§'), lore);
				inv.setItem(i, item);
				i++;
			}
		}

		p.openInventory(inv);
	}
	
	public static void openExplosiveShop(Player p) {
		Inventory inv = Bukkit.getServer().createInventory(null, 27, explosiveshoptitle);
		int i = 0;
		for (Weapon w : Weapon.explosives) {
				ItemStack item = w.item.clone();
				ArrayList<String> lore = (ArrayList<String>) w.lore_list.clone();
				lore.add("§9Preis: §f" + w.price + " Shards");
				setItemName(item, Main.defaultExplosives.getString(w.name + ".Item_Information.Item_Name").replace('&', '§'), lore);
				inv.setItem(i, item);
				i++;
		}

		p.openInventory(inv);
	}
	


	
	public static void openWeaponList(Player p, String title) {
		Inventory inv = Bukkit.getServer().createInventory(null, 27, title);
		int i = 0;
		for (Weapon w : Weapon.weapons) {
			if ((p.hasPermission("gunpvp.all")) || hasBought(p, w.name)) {
				inv.setItem(i, w.item);
				i++;
			}
		}

		p.openInventory(inv);
	}

	public static void chooseWeapon(Player p) {
		Inventory inv = Bukkit.getServer().createInventory(null, 27, weaponmenutitle);

		for (int i = 0; i < inv.getSize(); i++) {
			inv.setItem(i, gap);
		}

		ItemStack button = new ItemStack(Material.BIRCH_BUTTON);
		setItemName(button, "§aÄndern", null);

		inv.setItem(19, button);
		inv.setItem(21, button);
		inv.setItem(23, button);
		//inv.setItem(25, button);

		String first = getFirstWeapon(p);
		String second = getSecondWeapon(p);
		String third = getThirdWeapon(p);
		String special = getSpecial(p);

		if (first == null) {
			inv.setItem(10, Weapon.getWeaponForName(firstdefaultweapon).item);
		} else {
			inv.setItem(10, Weapon.getWeaponForName(first).item);
		}

		if (second == null) {
			inv.setItem(12, Weapon.getWeaponForName(seconddefaultweapon).item);
		}else {
			inv.setItem(12, Weapon.getWeaponForName(second).item);
		}

		if (third == null) {
			inv.setItem(14, Weapon.getWeaponForName(thirddefaultweapon).item);
		}else {
			inv.setItem(14, Weapon.getWeaponForName(third).item);
		}
		
		if (special == null) {
			inv.setItem(16, compass);
		}else {
			//inv.setItem(10, Weapon.getWeaponForName(first).item);
		}
		
		

		p.openInventory(inv);
	}

	public static void clearInv(Player p) {
		p.getInventory().setItem(0, new ItemStack(Material.AIR, 1));
		p.getInventory().setItem(1, new ItemStack(Material.AIR, 1));
		p.getInventory().setItem(2, new ItemStack(Material.AIR, 1));
		p.getInventory().setBoots(new ItemStack(Material.AIR, 1));
		p.getInventory().setLeggings(new ItemStack(Material.AIR, 1));
		p.getInventory().setChestplate(new ItemStack(Material.AIR, 1));
		p.getInventory().setHelmet(new ItemStack(Material.AIR, 1));
		p.updateInventory();
	}

	public static void giveArmorAndWeapons(Player p) {
		if (p.getInventory().getHelmet() == null) {
			p.getInventory().setHelmet(helmet);
		}
		if (p.getInventory().getChestplate() == null) {
			p.getInventory().setChestplate(chestplate);
		}
		if (p.getInventory().getLeggings() == null) {
			p.getInventory().setLeggings(leggings);
		}
		if (p.getInventory().getBoots() == null) {
			p.getInventory().setBoots(boots);
		}
		p.getInventory().setItem(8, compass);
		
		Main.csu.giveWeapon(p, getFirstWeapon(p), 1);
		Main.csu.giveWeapon(p, getSecondWeapon(p), 1);
		Main.csu.giveWeapon(p, getThirdWeapon(p), 1);
		
		p.updateInventory();

	}

}
