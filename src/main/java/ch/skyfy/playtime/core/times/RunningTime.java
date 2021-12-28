package ch.skyfy.playtime.core.times;

import ch.skyfy.playtime.core.PlayerTimePerDay;

public class RunningTime extends TimeBase{
    public RunningTime() {
        super(PlayerTimePerDay.TimeType.RUNNING);
    }
}
