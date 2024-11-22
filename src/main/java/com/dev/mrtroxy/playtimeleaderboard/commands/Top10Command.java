package com.dev.mrtroxy.playtimeleaderboard.commands;

import com.dev.mrtroxy.playtimeleaderboard.managers.PlaytimeDataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.OfflinePlayer;

import java.util.*;
import java.util.Map.Entry;

/**
 * Handles the /top10 command to display the top 10 players by playtime.
 */
public class Top10Command implements CommandExecutor {

    // Reference to the playtime data manager
    private final PlaytimeDataManager dataManager;

    /**
     * Constructor initializes the data manager reference.
     *
     * @param dataManager The playtime data manager
     */
    public Top10Command(PlaytimeDataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * Executes the /top10 command.
     * Sends the top 10 players with the highest playtime to the command sender.
     *
     * @param sender  The command sender
     * @param command The command that was executed
     * @param label   The alias of the command which was used
     * @param args    The arguments passed to the command
     * @return true if the command was successfully executed
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        sender.sendMessage(ChatColor.AQUA + "Top 10 Players by Playtime:");

        // Create a list from the playtime data entries
        List<Map.Entry<UUID, Long>> sortedList = new ArrayList<Map.Entry<UUID, Long>>(dataManager.getPlaytimeData().entrySet());
        // Sort the list in descending order based on playtime
        Collections.sort(sortedList, new Comparator<Map.Entry<UUID, Long>>() {
            @Override
            public int compare(Map.Entry<UUID, Long> o1, Map.Entry<UUID, Long> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        int rank = 1;
        for (Map.Entry<UUID, Long> entry : sortedList) {
            if (rank > 10) break;
            OfflinePlayer player = sender.getServer().getOfflinePlayer(entry.getKey());
            String playerName = player.getName() != null ? player.getName() : "Unknown";
            sender.sendMessage(ChatColor.YELLOW + "#" + rank + " " + playerName + ": " + formatTime(entry.getValue()));
            rank++;
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