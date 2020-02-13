package cc.sfclb.noautofish.event;

import cc.sfclb.noautofish.NAFPlayer;
import cc.sfclb.noautofish.NoAutoFish;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.List;

public class Fish implements Listener {
    NoAutoFish NAF;

    public Fish(NoAutoFish i) {
        NAF = i;
    }

    @EventHandler
    public void onFish(PlayerFishEvent evt) {
        if (!evt.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            return;
        } else if (evt.getPlayer().hasPermission("naf.bypass")) {
            return;
        }
        Player p = evt.getPlayer();
        Location loc = evt.getPlayer().getLocation();
        NAFPlayer np = NAF.getNAFPlayers().getOrDefault(p.getUniqueId(), new NAFPlayer(loc, 0));
        np.score++;
        np.lastFishTime = System.currentTimeMillis();
        if (NAF.getConfig().getBoolean("debug")) {
            p.sendMessage("Score:" + np.score + ",SendHint:" + np.sendHint);
        }
        if (np.sendHint) {
            List<String> actions = NAF.getConfig().getStringList("action");
            double d = loc.distance(np.loc);
            if (d > NAF.getConfig().getDouble("move_least_distance")) {
                np.score = 0;
                np.sendHint = false;
                NAF.getNAFPlayers().put(p.getUniqueId(), np);
                return;
            }
            if (np.score >= NAF.getConfig().getInt("count.take_action")) {
                actions.forEach(a -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), a.replaceAll("%p%", p.getName())));
                np.score = 0;
                np.sendHint = false;
                NAF.getNAFPlayers().put(p.getUniqueId(), np);
                return;
            }
            p.sendTitle(NAF.getConfig().getString("hint_text").replaceAll("&", "§"), NAF.getConfig().getString("hint_text_sub").replaceAll("&", "§"), 60, 60, 20);
        }
        NAF.getNAFPlayers().put(p.getUniqueId(), np);
    }
}
