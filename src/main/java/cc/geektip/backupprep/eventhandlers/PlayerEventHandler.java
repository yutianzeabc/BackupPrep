package cc.geektip.backupprep.eventhandlers;

import cc.geektip.backupprep.BackupPrep;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import java.util.UUID;

/**
 * @author Fish
 */
public class PlayerEventHandler implements Listener {
    BackupPrep plugin;

    public PlayerEventHandler(BackupPrep plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        if (plugin.isMaintain().get()) {
            UUID uuid = e.getUniqueId();
            Player player = plugin.getServer().getPlayer(uuid);
            if (player == null || !player.hasPermission("BackupPrep.bypass")) {
                e.disallow(Result.KICK_OTHER, plugin.getKickInfoMsg());
            }
        }
    }
}
