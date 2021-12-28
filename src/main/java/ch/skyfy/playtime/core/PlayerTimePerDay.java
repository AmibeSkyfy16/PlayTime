package ch.skyfy.playtime.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerTimePerDay implements Serializable {

    public enum TimeType implements Serializable {
        AFK,
        SNEAK,
        WALKING,
        FALLING,
        RUNNING,
        SWIMMING,
        MINING,
        DRINKING,
        EATING,
        CRAFTING,
        FLYING;
    }

    public final Map<TimeType, List<ElapsedTime>> map;

    public final Long day;

    public PlayerTimePerDay() {
        map = new HashMap<>();
        day = System.currentTimeMillis();
        for (var value : TimeType.values()) map.put(value, new ArrayList<>());
    }

    public void add(TimeType timeType, ElapsedTime elapsedTime) {
        map.get(timeType).add(elapsedTime);
    }

//    public @Nullable List<ElapsedTime> getElapsedTime(TimeType timeType){
//        return map.get(timeType);
//    }

//    public Long calculateTotal2(@Nullable List<ElapsedTime> elapsedTimes) {
//        if(elapsedTimes == null)return -1L;
//        var totalTime = 0L;
//        for (var timeElapsed : elapsedTimes)
//            totalTime += timeElapsed.timeElapsed;
//        return totalTime;
//    }

    public Long calculateTotal(TimeType timeType) {
        var list = map.get(timeType);
        if (list == null) return -1L;
        var totalTime = 0L;
        for (var timeElapsed : list)
            totalTime += timeElapsed.timeElapsed;
        return totalTime;
    }

}
