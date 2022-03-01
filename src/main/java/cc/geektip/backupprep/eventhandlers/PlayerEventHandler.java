package cc.geektip.backupprep.eventhandlers;

import cc.geektip.backupprep.BackupPrep;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;


/**
 * @author Fish
 */
public class PlayerEventHandler implements Listener {
    BackupPrep plugin;

    public PlayerEventHandler(BackupPrep plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerLoginEvent(PlayerLoginEvent e) {
        if (plugin.isBlockLogin().get()) {
            Player player = e.getPlayer();
            if (!player.hasPermission("BackupPrep.bypass")) {
                e.disallow(Result.KICK_OTHER, plugin.getKickInfoMsg());
            }
        }
    }
}
