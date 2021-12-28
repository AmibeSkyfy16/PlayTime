package ch.skyfy.playtime.core.times;

import ch.skyfy.playtime.core.PlayTimeUtils;
import ch.skyfy.playtime.core.PlayerTimePerDay;
import net.minecraft.entity.player.PlayerEntity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class TimeBase implements Serializable {

    public enum SaveOrStartCountTime {
        SAVE_TIME,
        START_COUNT_TIME
    }

    protected final Map<String, Long> timeElapsed;
    protected final PlayerTimePerDay.TimeType timeType;

    public TimeBase(PlayerTimePerDay.TimeType timeType) {
        this.timeType = timeType;
        this.timeElapsed = new HashMap<>();
    }

    public void initialize() {}

    public void createAndAddElapsedTime(PlayerEntity player, SaveOrStartCountTime saveOrStartCountTime) {
        timeElapsed.compute(player.getUuidAsString(), (uuid, lastTime) -> {
            if (lastTime == null) {
                if (saveOrStartCountTime == SaveOrStartCountTime.START_COUNT_TIME)
                    lastTime = System.currentTimeMillis();
            } else {
                if (saveOrStartCountTime == SaveOrStartCountTime.SAVE_TIME) {
                    PlayTimeUtils.createAndAddNewElapsedTime(player, lastTime, timeType);
                    lastTime = null;
                }
            }
            return lastTime;
        });
    }

}
