package com.bukkit.Kristian.Dayman;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;

/**
 * Handle events for all Player related events
 * 
 * @author Kristian
 */
public class DaymanPlayerListener extends PlayerListener 
{
    private final Dayman plugin;

    public DaymanPlayerListener(Dayman instance)
    {
        plugin = instance;
    }

    // KJB Function called when a player types something
    public void onPlayerCommandPreprocess(PlayerChatEvent event)
    {
    	// KJB First check to see if the polls are open
    	if(plugin.arePollsOpen())
    	{
    		String[] split = event.getMessage().split(" ");
    		Player player = event.getPlayer();
    	
    		// KJB Check to see if player wants to skip night
    		if(split[0].equalsIgnoreCase("/skipnight"))
    		{
    			plugin.setYay(player, true);
    			event.setCancelled(true);
    		}
    	}
    }
    
    // KJB Function called whenever a player logins to the server
    public void onPlayerJoin(PlayerEvent event)
    {
    	// KJB Set this player's vote to no
    	plugin.setYay(event.getPlayer(), false);
    	// KJB If the polls are open, send this player a message about skipping night
    	if(plugin.arePollsOpen())
    	{
    		plugin.sendMessageToPlayer(event.getPlayer(), "Would you like to skip night? Type /skipnight to skip!");
    	}
    }
    
    // KJB Function called whenever a player logs out of the server
    public void onPlayerQuit(PlayerEvent event)
    {
    	// KJB Set this player's vote to no
    	plugin.setYay(event.getPlayer(), false);
    }
}

