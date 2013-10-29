
package me.xxsniperzzxx_sd.infected.Messages;

import me.xxsniperzzxx_sd.infected.Tools.Files;

public class Time {

	// Method to take the time from seconds and convert it into a nice looking
	// message
	public static String getTime(Long Time) {
		String times = null;
		Long time = Time;
		Long seconds = time;
		long minutes = seconds / 60;
		seconds %= 60;
		if (seconds == 0)
		{
			if (minutes <= 1)
				times = minutes + " Minute";
			else
				times = minutes + " Minutes";
		} else if (minutes == 0)
		{
			if (seconds <= 1)
				times = seconds + " Second";
			else
				times = seconds + " Seconds";
		} else
		{
			times = minutes + " Minutes " + seconds + " Seconds";
		}
		return times;
	}
	public static String getOnlineTime(String player) {
		Long time = Files.getPlayers().getLong("Players." + player.toLowerCase() + ".Time");
		Long seconds = time;
		long minutes = seconds / 60;
		seconds %= 60;
		long hours = minutes / 60;
		minutes %= 60;
		long days = hours / 24;
		hours %= 24;
		String times = days + "D, " + hours + "H, " + minutes + "M " + seconds + "S";
		return times;
	}
}
