package com.dev.mrtroxy.playtimeleaderboard.managers;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Manages playtime data for all players.
 * Handles loading, saving, and updating playtime information.
 */
public class PlaytimeDataManager {

    // Reference to the main plugin instance
    private final JavaPlugin plugin;
    // Stores total playtime for each player by UUID
    private final Map<UUID, Long> playtimeData;
    // File to store playtime data
    private File dataFile;
    // Configuration object for the data file
    private FileConfiguration dataConfig;

    /**
     * Constructor initializes data structures and loads existing playtime data.
     *
     * @param plugin The main plugin instance
     */
    public PlaytimeDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.playtimeData = new HashMap<UUID, Long>();
        loadPlaytimeData();
    }

    /**
     * Loads playtime data from the YAML file.
     * If the file does not exist, it creates a new one.
     */
    public void loadPlaytimeData() {
        dataFile = new File(plugin.getDataFolder(), "playtime_data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create playtime_data.yml: " + e.getMessage());
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        for (String key : dataConfig.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                long time = dataConfig.getLong(key);
                playtimeData.put(uuid, time);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid UUID in playtime_data.yml: " + key);
            }
        }
    }

    /**
     * Saves all playtime data to the YAML file.
     * Handles any IOExceptions that may occur during the save process.
     */
    public void savePlaytimeData() {
        for (Map.Entry<UUID, Long> entry : playtimeData.entrySet()) {
            dataConfig.set(entry.getKey().toString(), entry.getValue());
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save playtime data: " + e.getMessage());
        }
    }

    /**
     * Adds a specified amount of playtime to a player's total.
     *
     * @param uuid   The UUID of the player
     * @param millis The amount of time to add in milliseconds
     */
    public void addPlaytime(UUID uuid, long millis) {
        long currentPlaytime = getPlaytime(uuid);
        playtimeData.put(uuid, currentPlaytime + millis);
    }

    /**
     * Retrieves the total playtime for a player.
     *
     * @param uuid The UUID of the player
     * @return Total playtime in milliseconds
     */
    public long getPlaytime(UUID uuid) {
        return playtimeData.getOrDefault(uuid, 0L);
    }

    /**
     * Provides an unmodifiable view of all playtime data.
     *
     * @return Map of player UUIDs to their total playtime
     */
    public Map<UUID, Long> getPlaytimeData() {
        return Collections.unmodifiableMap(playtimeData);
    }
}