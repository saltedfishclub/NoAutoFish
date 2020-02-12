package cc.sfclb.noautofish;

import org.bukkit.Location;

public class NAFPlayer {
    public int score;
    public long lastFishTime;
    public boolean sendHint;
    public Location loc;

    public NAFPlayer(Location loc, int score) {
        this.loc = loc;
        this.score = score;
        lastFishTime = 0;
        sendHint = false;
    }
}
