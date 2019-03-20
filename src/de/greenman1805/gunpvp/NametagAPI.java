package de.greenman1805.gunpvp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.nametagedit.plugin.NametagEdit;

import de.greenman1805.bountyextra.Bounty;
import de.greenman1805.ranks.Rank;

public class NametagAPI {


	public static void updateNametag(final Player p) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {

			@Override
			public void run() {
				String prefix = Rank.getPlayerPrefixColor(p);
				prefix = "§6 " + Bounty.getBounty(p) + " §7| " + prefix;
				NametagEdit.getApi().setPrefix(p, prefix);
			}

		}, 5);
	}

}
