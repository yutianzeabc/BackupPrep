package cc.geektip.backupprep;

import cc.geektip.backupprep.commandexecutors.BackupCtrl;
import cc.geektip.backupprep.eventhandlers.PlayerEventHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ratismal.drivebackup.DriveBackupApi;

import java.io.File;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Fish
 */
public final class BackupPrep extends JavaPlugin {
    BackupPrep plugin = this;
    AtomicBoolean isMaintain = new AtomicBoolean(false);
    AtomicBoolean isSkipOnce = new AtomicBoolean(false);
    AtomicBoolean isLastSucceed = new AtomicBoolean(true);

    Component msgHeader = Component.text("[BP] ", NamedTextColor.GREEN);
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
        File defaultConfig = new File(getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(defaultConfig);

        illCmdMsg = msgHeader.append(Component.text(config.getString("illCmdMsg"), NamedTextColor.RED));
        skipSetMsg = msgHeader.append(Component.text(config.getString("skipSetMsg")));
        skipCnlMsg = msgHeader.append(Component.text(config.getString("skipCnlMsg")));
        blockSetMsg = msgHeader.append(Component.text(config.getString("blockSetMsg")));
        blockCnlMsg = msgHeader.append(Component.text(config.getString("blockCnlMsg")));
        kickInfoMsg = Component.text(config.getString("kickInfoMsg"));

        this.getCommand("backupctrl").setExecutor(new BackupCtrl(this));
        getServer().getPluginManager().registerEvents(new PlayerEventHandler(this), this);

        DriveBackupApi.beforeBackupStart(() -> {
            if (isSkipOnce.get()) {
                isSkipOnce.set(false);
                return Boolean.FALSE;
            }
            isMaintain.set(true);
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
            isMaintain.set(false);
            isLastSucceed.set(true);
        });
        DriveBackupApi.onBackupError(() -> {
            isMaintain.set(false);
            isLastSucceed.set(false);
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public AtomicBoolean isMaintain() {
        return isMaintain;
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
