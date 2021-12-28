package ch.skyfy.playtime.core.times;

import ch.skyfy.playtime.core.PlayerTimePerDay;

public class WalkingTime extends TimeBase {
    public WalkingTime() {
        super(PlayerTimePerDay.TimeType.WALKING);
    }
}
