package com.dev.mrtroxy.playtimeleaderboard.commands;

import com.dev.mrtroxy.playtimeleaderboard.managers.PlaytimeDataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.OfflinePlayer;

import java.util.Map;
import java.util.UUID;

/**
 * Handles the /leaderboard command to display all players' playtime.
 */
public class LeaderboardCommand implements CommandExecutor {

    // Reference to the playtime data manager
    private final PlaytimeDataManager dataManager;

    /**
     * Constructor initializes the data manager reference.
     *
     * @param dataManager The playtime data manager
     */
    public LeaderboardCommand(PlaytimeDataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * Executes the /leaderboard command.
     * Sends the playtime leaderboard to the command sender.
     *
     * @param sender  The command sender
     * @param command The command that was executed
     * @param label   The alias of the command which was used
     * @param args    The arguments passed to the command
     * @return true if the command was successfully executed
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        sender.sendMessage(ChatColor.GREEN + "Playtime Leaderboard:");
        for (Map.Entry<UUID, Long> entry : dataManager.getPlaytimeData().entrySet()) {
            OfflinePlayer player = sender.getServer().getOfflinePlayer(entry.getKey());
            String playerName = player.getName() != null ? player.getName() : "Unknown";
            sender.sendMessage(ChatColor.YELLOW + playerName + ": " + formatTime(entry.getValue()));
        }

        return true;
    }

    /**
     * Formats playtime from milliseconds to a readable string with hours, minutes, and seconds.
     *
     * @param millis Playtime in milliseconds
     * @return Formatted playtime string
     */
    private String formatTime(long millis) {
        long totalSeconds = millis / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        return String.format("%dh %dm %ds", hours, minutes, seconds);
    }
}