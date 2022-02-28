package cc.geektip.backupprep.commandexecutors;

import cc.geektip.backupprep.BackupPrep;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getPermissionMessage;

/**
 * @author Fish
 */
public class BackupCtrl implements CommandExecutor {
    BackupPrep plugin;

    public BackupCtrl(BackupPrep plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("BackupPrep.ctrl")) {
                if (args.length == 1) {
                    if ("skip".equalsIgnoreCase(args[0])) {
                        plugin.isSkipOnce().set(true);
                        player.sendMessage(plugin.getSkipSetMsg());
                    } else if ("restore".equalsIgnoreCase(args[0])) {
                        plugin.isSkipOnce().set(false);
                        player.sendMessage(plugin.getSkipCnlMsg());
                    } else {
                        player.sendMessage(plugin.getIllCmdMsg());
                    }
                } else {
                    player.sendMessage(plugin.getIllCmdMsg());
                }
            } else {
                player.sendMessage(getPermissionMessage());
            }
        } else {
            plugin.getLogger().info("Only Players can use this command.");
        }
        return true;
    }
}
