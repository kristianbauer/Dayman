package com.bukkit.Kristian.Dayman;

/**
 * Checks the time of day and enables/disables voting
 *
 * @author Kristian
 */
public class DaymanTimeChecker implements Runnable {
	
	private final Dayman plugin;
	
	public DaymanTimeChecker(Dayman instance)
	{
		plugin = instance;
	}

    public void run()
    {
    	// KJB Make sure we have a world
    	if(plugin.getServer().getWorlds().size() > 0)
    	{
    		// KJB Grab the time of day
        	long timeOfDay = plugin.getServer().getWorlds().get(0).getTime();
        	// KJB If the time of day is night time and the polls aren't open, open them
        	if(timeOfDay >= 12500 && timeOfDay <= 23500)
        	{
        		if(!plugin.arePollsOpen())
        		{
        			plugin.resetPolls();
        			plugin.setPollsOpen(true);
        			plugin.sendAllPlayersMessage("Would you like to skip night? Type /skipnight to skip!");
        		}
        	}
        	// KJB Close the polls at daytime
        	else
        	{
        		plugin.setPollsOpen(false);
        	}
    	}
    }
}

