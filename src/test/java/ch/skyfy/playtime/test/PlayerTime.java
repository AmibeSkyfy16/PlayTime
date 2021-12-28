package ch.skyfy.playtime.test;

import ch.skyfy.playtime.test.PlayerTimePerDay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This class will contain all registered time for a specific player
 */
public class PlayerTime implements Serializable {
    public final String UUID;
    
    public final List<PlayerTimePerDay> playerTimePerDays;

    public PlayerTime(String UUID) {
        this.UUID = UUID;
        playerTimePerDays = new ArrayList<>();
    }

    public PlayerTimePerDay getOrCreateToday(){
        var current = Calendar.getInstance();
        current.setTimeInMillis(System.currentTimeMillis());
        var currentYear = current.get(Calendar.YEAR);
        var currentMonth = current.get(Calendar.MONTH);
        var currentDay = current.get(Calendar.DAY_OF_MONTH);
        var another = Calendar.getInstance();
        for (var playerTimePerDay : playerTimePerDays) {
            another.setTimeInMillis(playerTimePerDay.day);
            var year = another.get(Calendar.YEAR);
            var month = another.get(Calendar.MONTH);
            var day = another.get(Calendar.DAY_OF_MONTH);
            if(year == currentYear && month == currentMonth && day == currentDay) return playerTimePerDay;
        }
        var playerTimePerDay = new PlayerTimePerDay();
        playerTimePerDays.add(playerTimePerDay);
        return playerTimePerDay;
    }
}
