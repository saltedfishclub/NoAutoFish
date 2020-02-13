package cc.sfclb.noautofish;

import org.bukkit.scheduler.BukkitRunnable;

public class ScoreUpdater extends BukkitRunnable {
    NoAutoFish naf;

    public ScoreUpdater(NoAutoFish naf) {
        this.naf = naf;
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        naf.getNAFPlayers().entrySet().forEach(e -> {
            if ((now - e.getValue().lastFishTime) / 1000 / 60 > naf.getConfig().getInt("clear_time")) {
                e.getValue().score = 0;
            } else {
                if (e.getValue().score >= naf.getConfig().getInt("count.hint")) {
                    e.getValue().sendHint = true;
                }
            }
        });
    }
}
