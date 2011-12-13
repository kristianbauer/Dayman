package com.bukkit.Kristian.Dayman;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Dayman for Bukkit
 *
 * @author Kristian
 */
public class Dayman extends JavaPlugin
{
    private final DaymanPlayerListener playerListener = new DaymanPlayerListener(this);
    private final HashMap<Player, Boolean> yays = new HashMap<Player, Boolean>();
    private boolean pollsOpen = false;

    public void onEnable()
    {
    	// KJB Reset the polls first
    	this.resetPolls();
    	
    	// KJB Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, this.playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_JOIN, this.playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, this.playerListener, Priority.Normal, this);
        
        // KJB Setup scheduler to handle spawning creatures
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new DaymanTimeChecker(this), 0, 100);

        // KJB Prints out debug information to the console
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }
    
    public void onDisable()
    {
    	// KJB Prints out debug information to the console
        System.out.println("Dayman is shutting down.");
    }
    
    // KJB Returns whether player has voted yes to skip night
    public boolean isYay(final Player player)
    {
    	if(yays.containsKey(player))
    	{
    		return yays.get(player);
    	}
    	else
    	{
    		return false;
    	}
    }
    
    // KJB Adds given player to the yay group
    public void setYay(final Player player, final boolean value)
    {
    	yays.put(player, value);
    	
    	// KJB Check to see if we should skip night
    	if(value)
    	{
    		this.tallyVotes();
    	}
    }
    
    // KJB Getter for pollsOpen
    public boolean arePollsOpen()
    {
    	return pollsOpen;
    }
    
    // KJB Setter for pollsOpen
    public void setPollsOpen(boolean newPollsOpen)
    {
    	pollsOpen = newPollsOpen;
    }
    
    // KJB Sends all players on the server a message
    public void sendAllPlayersMessage(String message)
    {
    	for(int i=0;i<getServer().getOnlinePlayers().length;i++)
    	{
    		Player curPlayer = getServer().getOnlinePlayers()[i];
    		this.sendMessageToPlayer(curPlayer, message);
    	}
    }
    
    // KJB Sends given player given message
    public void sendMessageToPlayer(Player player, String message)
    {
    	player.sendMessage(message);
    }
    
    // KJB Adds all online players to the nay list
    public void resetPolls()
    {
    	for(int i=0;i<getServer().getOnlinePlayers().length;i++)
    	{
    		Player curPlayer = getServer().getOnlinePlayers()[i];
    		this.setYay(curPlayer, false);
    	}
    }
    
    // KJB Tally the votes and see if it's okay to skip night
    public void tallyVotes()
    {
    	int yayCount = 0;
    	int votesToSkip = getServer().getOnlinePlayers().length/2;
    	for(int i=0;i<getServer().getOnlinePlayers().length;i++)
    	{
    		Player curPlayer = getServer().getOnlinePlayers()[i];
    		// KJB See if given player is a yay vote
    		if(this.isYay(curPlayer))
    		{
    			yayCount++;
    		}
    	}
    	// KJB See if we have a majority vote
    	if(yayCount > votesToSkip)
    	{
    		this.skipNight();
    	}
    	else
    	{
    		// KJB Calculate votes needed
    		int votesNeeded = votesToSkip+1-yayCount;
    		// KJB Alert players of votes needed to skip night
    		// KJB Correct grammar!
    		if(votesNeeded == 1)
    		{
    			this.sendAllPlayersMessage("Only " + votesNeeded + " more vote needed to skip night!");
    		}
    		else
    		{
    			this.sendAllPlayersMessage("Only " + votesNeeded + " more votes needed to skip night!");
    		}
    	}
    }
    
    // KJB Skips night!
    public void skipNight()
    {
    	if(getServer().getWorlds().size() > 0)
    	{
    		// KJB Make it morning
    		getServer().getWorlds().get(0).setTime(0);
    		// KJB Alert all players that morning has arrived
    		this.sendAllPlayersMessage("Dayman, aaaAAAaaa!");
    	}
    }
}

