package ch.skyfy.playtime.core.times;

import ch.skyfy.playtime.core.PlayerTimePerDay;

public class AFKTime extends TimeBase {
    public static final Long TIMEOUT = 60_000L;
    public AFKTime() {
        super(PlayerTimePerDay.TimeType.AFK);
    }
}
