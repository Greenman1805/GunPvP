package de.greenman1805.gunpvp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import de.greenman1805.bountyextra.Bounty;
import de.greenman1805.shards.MoneyChangeEvent;

import org.bukkit.event.player.PlayerQuitEvent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import protocolsupport.api.ProtocolSupportAPI;

public class Listeners implements Listener, CommandExecutor {
	public static Main plugin;

	public Listeners(Main m) {
		Listeners.plugin = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			final Player p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("weapons")) {
				if (args.length == 0) {
					GunAPI.chooseWeapon(p);
				}
			}
			if (cmd.getName().equalsIgnoreCase("gunshop")) {
				if (args.length == 0) {
					GunAPI.openGunShop(p);
				}
			}
			if (cmd.getName().equalsIgnoreCase("explosiveshop")) {
				if (args.length == 0) {
					GunAPI.openExplosiveShop(p);
				}
			}
		} else {
			sender.sendMessage(Main.prefix + "§cNur Spieler können diesen Befehl verwenden!");
		}
		return false;
	}

	@EventHandler
	public void disableOffHand(PlayerSwapHandItemsEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onMoneyChange(MoneyChangeEvent e) {
		OfflinePlayer op = Bukkit.getOfflinePlayer(e.getUniqueId());
		if (op != null && op.isOnline()) {
			Player p = (Player) op;
			ScoreboardAPI.updateScoreboard(p);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerKill(PlayerDeathEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = e.getEntity();
			if (p.getKiller() instanceof Player) {
				Player k = p.getKiller();
				if (p != k) {
					ScoreboardAPI.updateScoreboard(p);
					ScoreboardAPI.updateScoreboard(k);
					NametagAPI.updateNametag(p);
					NametagAPI.updateNametag(k);
					Leaderboard.updateAll();
				}
			}
		}

	}

	@EventHandler
	public void PlayerQuitListener(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}

	@EventHandler
	public void RespawnListener(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		GunAPI.clearInv(p);
		GunAPI.giveArmorAndWeapons(p);
	}

	@EventHandler
	public void PlayerJoinListener(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		GunAPI.clearInv(p);
		GunAPI.giveArmorAndWeapons(p);
		ScoreboardAPI.updateScoreboard(p);
		NametagAPI.updateNametag(p);
		Leaderboard.updateAll();

		int version = ProtocolSupportAPI.getProtocolVersion(p).getId();
		if (version >= 315 && version <= 340) {
			p.setResourcePack("https://www.dropbox.com/s/xwnx47vukct1gsf/skyshardgunpvp.zip?dl=1");
		}
	}

	@EventHandler
	public void cancelItemPickup(EntityPickupItemEvent e) {
		if (e.getEntity() instanceof Player) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void cancelItemDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void clickedOnItem(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (e.getClickedInventory() != null) {

			if (e.getSlot() == 0 || e.getSlot() == 1 || e.getSlot() == 2 || e.getSlot() == 8) {
				e.setCancelled(true);
			}

			if (e.getClickedInventory().getTitle().equalsIgnoreCase(GunAPI.weaponmenutitle)) {
				if (e.getCurrentItem() != null) {
					if (e.getSlot() == 19) {
						GunAPI.openWeaponList(p, GunAPI.firstweapontitle);
					} else if (e.getSlot() == 21) {
						GunAPI.openWeaponList(p, GunAPI.secondweapontitle);
					} else if (e.getSlot() == 23) {
						GunAPI.openWeaponList(p, GunAPI.thirdweapontitle);
					}
				}
				e.setCancelled(true);
			}

			if (e.getClickedInventory().getTitle().equalsIgnoreCase(GunAPI.gunshoptitle)) {
				if (e.getCurrentItem() != null) {
					if (e.isLeftClick()) {
						if (!e.getCurrentItem().getType().name().equalsIgnoreCase("AIR")) {
							ItemStack item = e.getCurrentItem();
							String name = Weapon.getNameForItem(item);
							Weapon w = Weapon.getWeaponForName(name);
							int account_after = (int) (Main.econ.getBalance(p) - w.price);
							if (account_after >= 0) {
								Main.econ.withdrawPlayer(p, w.price);
								GunAPI.buy(p, name);
								p.sendMessage(Main.prefix + "§7Du hast " + w.item.getItemMeta().getDisplayName() + "§7 für §9" + w.price + " Shards §7gekauft.");
								GunAPI.setFirstWeapon(p, name);
								GunAPI.clearInv(p);
								GunAPI.giveArmorAndWeapons(p);
								p.closeInventory();
								GunAPI.openGunShop(p);
							} else {
								p.sendMessage(Main.prefix + "§cDu hast nicht genug Geld!");
							}
						}
					}
				}
				e.setCancelled(true);
			}

			if (e.getClickedInventory().getTitle().equalsIgnoreCase(GunAPI.explosiveshoptitle)) {
				if (e.getCurrentItem() != null) {
					if (e.isLeftClick()) {
						if (!e.getCurrentItem().getType().name().equalsIgnoreCase("AIR")) {
							ItemStack item = e.getCurrentItem();
							String name = Weapon.getNameForItem(item);
							Weapon w = Weapon.getWeaponForName(name);
							int account_after = (int) (Main.econ.getBalance(p) - w.price);
							if (account_after >= 0) {
								Main.econ.withdrawPlayer(p, w.price);
								p.sendMessage(Main.prefix + "§7Du hast " + w.item.getItemMeta().getDisplayName() + "§7 für §9" + w.price + " Shards §7gekauft.");
								Main.csu.giveWeapon(p, name, 1);
							} else {
								p.sendMessage(Main.prefix + "§cDu hast nicht genug Geld!");
							}
						}
					}
				}
				e.setCancelled(true);
			}

			if (e.getClickedInventory().getTitle().equalsIgnoreCase(GunAPI.firstweapontitle)) {
				if (e.getCurrentItem() != null) {
					if (e.isLeftClick()) {
						if (!e.getCurrentItem().getType().name().equalsIgnoreCase("AIR")) {
							ItemStack item = e.getCurrentItem();
							String name = Weapon.getNameForItem(item);
							if (!GunAPI.getSecondWeapon(p).equalsIgnoreCase(name) && !GunAPI.getThirdWeapon(p).equalsIgnoreCase(name) && !GunAPI.getFirstWeapon(p).equalsIgnoreCase(name)) {
								GunAPI.setFirstWeapon(p, name);
								GunAPI.chooseWeapon(p);
								GunAPI.clearInv(p);
								GunAPI.giveArmorAndWeapons(p);
							} else {
								p.sendMessage(Main.prefix + "§4Du hast diese Waffe bereits ausgerüstet!");
							}
						}
					}
				}
				e.setCancelled(true);
			}

			if (e.getClickedInventory().getTitle().equalsIgnoreCase(GunAPI.secondweapontitle)) {
				if (e.getCurrentItem() != null) {
					if (e.isLeftClick()) {
						if (!e.getCurrentItem().getType().name().equalsIgnoreCase("AIR")) {
							ItemStack item = e.getCurrentItem();
							String name = Weapon.getNameForItem(item);
							if (!GunAPI.getSecondWeapon(p).equalsIgnoreCase(name) && !GunAPI.getThirdWeapon(p).equalsIgnoreCase(name) && !GunAPI.getFirstWeapon(p).equalsIgnoreCase(name)) {
								GunAPI.setSecondWeapon(p, name);
								GunAPI.chooseWeapon(p);
								GunAPI.clearInv(p);
								GunAPI.giveArmorAndWeapons(p);
							} else {
								p.sendMessage(Main.prefix + "§4Du hast diese Waffe bereits ausgerüstet!");
							}
						}
					}
				}
				e.setCancelled(true);
			}

			if (e.getClickedInventory().getTitle().equalsIgnoreCase(GunAPI.thirdweapontitle)) {
				if (e.getCurrentItem() != null) {
					if (e.isLeftClick()) {
						if (!e.getCurrentItem().getType().name().equalsIgnoreCase("AIR")) {
							ItemStack item = e.getCurrentItem();
							String name = Weapon.getNameForItem(item);
							if (!GunAPI.getSecondWeapon(p).equalsIgnoreCase(name) && !GunAPI.getThirdWeapon(p).equalsIgnoreCase(name) && !GunAPI.getFirstWeapon(p).equalsIgnoreCase(name)) {
								GunAPI.setThirdWeapon(p, name);
								GunAPI.chooseWeapon(p);
								GunAPI.clearInv(p);
								GunAPI.giveArmorAndWeapons(p);
							} else {
								p.sendMessage(Main.prefix + "§4Du hast diese Waffe bereits ausgerüstet!");
							}
						}
					}
				}
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void PlayerTracker(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
			double closest = Double.MAX_VALUE;
			Player closestp = null;
			for (Player i : Bukkit.getOnlinePlayers()) {
				if ((i != p) && (i.getWorld() == p.getWorld())) {
					double dist = i.getLocation().distance(e.getPlayer().getLocation());
					if (closest == Double.MAX_VALUE || dist < closest) {
						closest = dist;
						closestp = i;
					}
				}
			}
			if (closestp == null) {
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cKein Spieler in deiner Nähe"));
			} else {
				int kopfgeld = Bounty.getBounty(closestp);
				int dist = (int) (closest + 0.5);
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§9Spieler: " + closestp.getDisplayName() + "  §9Distanz: §f" + dist + "  §9Kopfgeld: §f" + kopfgeld));
				// p.setCompassTarget(closestp.getLocation());
			}
		}
	}

}
