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

        if (!(sender instanceof Player) || sender.hasPermission("BackupPrep.ctrl")) {
            if (args.length == 1) {
                if ("skip".equalsIgnoreCase(args[0])) {
                    plugin.isSkipOnce().set(true);
                    sender.sendMessage(plugin.getSkipSetMsg());
                } else if ("restore".equalsIgnoreCase(args[0])) {
                    plugin.isSkipOnce().set(false);
                    sender.sendMessage(plugin.getSkipCnlMsg());
                } else if ("block".equalsIgnoreCase(args[0])) {
                    plugin.isMaintain().set(true);
                    sender.sendMessage(plugin.getBlockSetMsg());
                } else if ("unblock".equalsIgnoreCase(args[0])) {
                    plugin.isMaintain().set(false);
                    sender.sendMessage(plugin.getBlockCnlMsg());
                } else {
                    sender.sendMessage(plugin.getIllCmdMsg());
                }
            } else {
                sender.sendMessage(plugin.getIllCmdMsg());
            }
        } else {
            sender.sendMessage(getPermissionMessage());
        }
        return true;
    }
}
