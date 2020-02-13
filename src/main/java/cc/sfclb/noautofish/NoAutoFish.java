package cc.sfclb.noautofish;

import cc.sfclb.noautofish.event.Fish;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public final class NoAutoFish extends JavaPlugin {
    boolean enabled;
    @Getter
    private volatile HashMap<UUID, NAFPlayer> NAFPlayers = new HashMap<>();

    @Override
    public void onEnable() {
        //todo Custom Actions(More configure about scores)
        //todo Gui Click check
        if (!this.getDataFolder().exists()) {
            // If a player was hinted,how long distance should be walked by him for get pardon?
            getConfig().addDefault("move_least_distance", 3.2);
            //The score of hint by NAF.
            getConfig().addDefault("count.hint", 2);
            //The score of take action by NAF.
            getConfig().addDefault("count.take_action", 5);
            //Actions,command.
            getConfig().addDefault("action", Collections.singletonList("\"kick %p% AutoFish Hacks\""));
            //Check cycle(second)
            getConfig().addDefault("check_cycle", 10);
            //Hint text(title)
            getConfig().addDefault("hint_text", "Go away for continue fishing");
            getConfig().addDefault("hint_text", "It must be little far..");
            //The time of the player that should be clear the score by ScoreUpdater
            getConfig().addDefault("clear_time", 300);
            getConfig().addDefault("debug", false);
            getConfig().options().copyDefaults(true);
            saveConfig();
            reloadConfig();
        }
        if (enabled) {
            reloadNAFMap();
        }
        getServer().getPluginManager().registerEvents(new Fish(this), this);
        new ScoreUpdater(this).runTaskTimerAsynchronously(this, 0L, getConfig().getInt("check_cycle") * 20L);
        enabled = true;
        getLogger().info("N.A.F Working!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Good bye!");
    }

    public void reloadNAFMap() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (!NAFPlayers.containsKey(p.getUniqueId())) {
                Location loc = p.getLocation();
                NAFPlayers.put(p.getUniqueId(), new NAFPlayer(loc, 0));
            }
        });
    }
}
