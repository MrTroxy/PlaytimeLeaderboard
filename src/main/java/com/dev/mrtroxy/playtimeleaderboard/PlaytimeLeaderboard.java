package com.dev.mrtroxy.playtimeleaderboard;

import com.dev.mrtroxy.playtimeleaderboard.commands.LeaderboardCommand;
import com.dev.mrtroxy.playtimeleaderboard.commands.Top10Command;
import com.dev.mrtroxy.playtimeleaderboard.listeners.PlayerEventListener;
import com.dev.mrtroxy.playtimeleaderboard.managers.PlaytimeDataManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Main class for the Playtime Leaderboard plugin.
 */
public class PlaytimeLeaderboard extends JavaPlugin {

    // Manages playtime data for all players
    private PlaytimeDataManager dataManager;

    /**
     * Called when the plugin is enabled.
     * Initializes data manager, registers event listeners and commands, and starts the playtime updater.
     */
    @Override
    public void onEnable() {
        dataManager = new PlaytimeDataManager(this);
        // Register event listeners for player join and quit
        Bukkit.getPluginManager().registerEvents(new PlayerEventListener(dataManager), this);
        // Set command executors for leaderboard and top10 commands
        if (getCommand("leaderboard") != null) {
            getCommand("leaderboard").setExecutor(new LeaderboardCommand(dataManager));
        } else {
            getLogger().severe("Command 'leaderboard' not found in plugin.yml");
        }

        if (getCommand("top10") != null) {
            getCommand("top10").setExecutor(new Top10Command(dataManager));
        } else {
            getLogger().severe("Command 'top10' not found in plugin.yml");
        }

        // Start a repeating task to update playtime every second
        new BukkitRunnable() {
            @Override
            public void run() {
                updatePlaytime();
            }
        }.runTaskTimer(this, 20L, 20L); // 20 ticks = 1 second
    }

    /**
     * Called when the plugin is disabled.
     * Saves all playtime data to disk.
     */
    @Override
    public void onDisable() {
        dataManager.savePlaytimeData();
    }

    /**
     * Updates the playtime for all online players by adding one second.
     */
    private void updatePlaytime() {
        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
            dataManager.addPlaytime(player.getUniqueId(), 1000L); // Add 1000 milliseconds (1 second)
        }
    }
}