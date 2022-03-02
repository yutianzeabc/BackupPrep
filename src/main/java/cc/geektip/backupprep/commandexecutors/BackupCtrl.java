package cc.geektip.backupprep.commandexecutors;

import cc.geektip.backupprep.BackupPrep;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getPermissionMessage;

/**
 * @author Fish
 */
public class BackupCtrl implements TabExecutor {
    BackupPrep plugin;

    public BackupCtrl(BackupPrep plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("BackupPrep.ctrl")) {
            if (args.length == 1) {
                if ("skip".equalsIgnoreCase(args[0])) {
                    if (sender.hasPermission("BackupPrep.ctrl.skip")) {
                        plugin.isSkipOnce().set(true);
                        sender.sendMessage(plugin.getSkipSetMsg());
                    } else {
                        sender.sendMessage(getPermissionMessage());
                    }
                } else if ("restore".equalsIgnoreCase(args[0])) {
                    if (sender.hasPermission("BackupPrep.ctrl.skip")) {
                        plugin.isSkipOnce().set(false);
                        sender.sendMessage(plugin.getSkipCnlMsg());
                    } else {
                        sender.sendMessage(getPermissionMessage());
                    }
                } else if ("block".equalsIgnoreCase(args[0])) {
                    if (sender.hasPermission("BackupPrep.ctrl.block")) {
                        plugin.isBlockLogin().set(true);
                        sender.sendMessage(plugin.getBlockSetMsg());
                    } else {
                        sender.sendMessage(getPermissionMessage());
                    }
                } else if ("unblock".equalsIgnoreCase(args[0])) {
                    if (sender.hasPermission("BackupPrep.ctrl.block")) {
                        plugin.isBlockLogin().set(false);
                        sender.sendMessage(plugin.getBlockCnlMsg());
                    } else {
                        sender.sendMessage(getPermissionMessage());
                    }
                } else if ("status".equalsIgnoreCase(args[0])) {
                    if (sender.hasPermission("BackupPrep.ctrl.status")) {
                        Component statusInfo = plugin.getMsgHeader().append(Component.text(String.format("[SkipOnce=%b, BlockLogin=%b, LastSucceed=%b]", plugin.isSkipOnce().get(), plugin.isBlockLogin().get(), plugin.isLastSucceed().get()), NamedTextColor.WHITE));
                        sender.sendMessage(statusInfo);
                    } else {
                        sender.sendMessage(getPermissionMessage());
                    }
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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> cmd = new ArrayList<>();
            if (sender.hasPermission("BackupPrep.ctrl.skip")) {
                cmd.add("skip");
                cmd.add("restore");
            }
            if (sender.hasPermission("BackupPrep.ctrl.block")) {
                cmd.add("block");
                cmd.add("unblock");
            }
            if (sender.hasPermission("BackupPrep.ctrl.status")) {
                cmd.add("status");
            }
            return cmd;
        }
        return null;
    }
}
