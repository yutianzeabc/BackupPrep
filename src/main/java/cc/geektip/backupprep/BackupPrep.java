package cc.geektip.backupprep;

import cc.geektip.backupprep.commandexecutors.BackupCtrl;
import cc.geektip.backupprep.eventhandlers.PlayerEventHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ratismal.drivebackup.DriveBackupApi;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Fish
 */
public final class BackupPrep extends JavaPlugin {
    BackupPrep plugin = this;

    AtomicBoolean isBlockLogin;
    AtomicBoolean isSkipOnce;
    AtomicBoolean isLastSucceed;

    Component msgHeader;
    Component illCmdMsg;
    Component skipSetMsg;
    Component skipCnlMsg;
    Component blockSetMsg;
    Component blockCnlMsg;
    Component kickInfoMsg;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        isBlockLogin = new AtomicBoolean(getConfig().getBoolean("status.isBlockLogin"));
        isSkipOnce = new AtomicBoolean(getConfig().getBoolean("status.isSkipOnce"));
        isLastSucceed = new AtomicBoolean(getConfig().getBoolean("status.isLastSucceed"));

        msgHeader = Component.text("[BP] ", NamedTextColor.GREEN);
        illCmdMsg = msgHeader.append(Component.text(getConfig().getString("msg.illCmd", "illCmd"), NamedTextColor.RED));
        skipSetMsg = msgHeader.append(Component.text(getConfig().getString("msg.skipSet", "skipSet")));
        skipCnlMsg = msgHeader.append(Component.text(getConfig().getString("msg.skipCnl", "skipCnl")));
        blockSetMsg = msgHeader.append(Component.text(getConfig().getString("msg.blockSet", "blockSet")));
        blockCnlMsg = msgHeader.append(Component.text(getConfig().getString("msg.blockCnl", "blockCnl")));
        kickInfoMsg = Component.text(getConfig().getString("msg.kickInfo", "kickInfo"));

        getCommand("backupctrl").setExecutor(new BackupCtrl(this));
        getServer().getPluginManager().registerEvents(new PlayerEventHandler(this), this);

        DriveBackupApi.beforeBackupStart(() -> {
            if (isSkipOnce.compareAndSet(true,false)) {
                return Boolean.FALSE;
            }
            isBlockLogin.getAndSet(true);
            Future<Object> future = getServer().getScheduler().callSyncMethod(plugin, () -> {
                for (Player player : getServer().getOnlinePlayers()) {
                    player.kick(kickInfoMsg);
                }
                return Boolean.TRUE;
            });
            future.get();
            return Boolean.TRUE;
        });

        DriveBackupApi.onBackupDone(() -> {
            isBlockLogin.getAndSet(false);
            isLastSucceed.getAndSet(true);
        });

        DriveBackupApi.onBackupError(() -> {
            isBlockLogin.getAndSet(false);
            isLastSucceed.getAndSet(false);
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getConfig().set("status.isBlockLogin", isBlockLogin.get());
        getConfig().set("status.isSkipOnce", isSkipOnce.get());
        getConfig().set("status.isLastSucceed", isLastSucceed.get());
        saveConfig();
    }

    public AtomicBoolean isBlockLogin() {
        return isBlockLogin;
    }

    public AtomicBoolean isSkipOnce() {
        return isSkipOnce;
    }

    public AtomicBoolean isLastSucceed() {
        return isLastSucceed;
    }

    public Component getMsgHeader() {
        return msgHeader;
    }

    public Component getIllCmdMsg() {
        return illCmdMsg;
    }

    public Component getSkipSetMsg() {
        return skipSetMsg;
    }

    public Component getSkipCnlMsg() {
        return skipCnlMsg;
    }

    public Component getBlockSetMsg() {
        return blockSetMsg;
    }

    public Component getBlockCnlMsg() {
        return blockCnlMsg;
    }

    public Component getKickInfoMsg() {
        return kickInfoMsg;
    }
}
