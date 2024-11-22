package com.dev.mrtroxy.playtimeleaderboard.listeners;

import com.dev.mrtroxy.playtimeleaderboard.managers.PlaytimeDataManager;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listens to player join and quit events.
 * Currently handles any necessary actions on player join and quit.
 */
public class PlayerEventListener implements Listener {

    // Reference to the playtime data manager
    private final PlaytimeDataManager dataManager;

    /**
     * Constructor initializes the data manager reference.
     *
     * @param dataManager The playtime data manager
     */
    public PlayerEventListener(PlaytimeDataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * Handles the PlayerJoinEvent.
     * Currently no additional actions are required on player join.
     *
     * @param event The player join event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // No action needed since playtime is updated via scheduler
    }

    /**
     * Handles the PlayerQuitEvent.
     * Currently no additional actions are required on player quit.
     *
     * @param event The player quit event
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // No action needed since playtime is updated via scheduler
    }
}