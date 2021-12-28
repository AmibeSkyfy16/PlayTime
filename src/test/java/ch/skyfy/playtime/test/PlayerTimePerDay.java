package ch.skyfy.playtime.test;

import ch.skyfy.playtime.test.ElapsedTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class PlayerTimePerDay implements Serializable {

    public enum TimeType {
        AFK("afk"),
        SNEAK("sneak"),
        WALKING("walking"),
        FALLING("falling"),
        RUNNING("running"),
        SWIMMING("swimming"),
        MINING("mining"),
        DRINKING("drinking"),
        EATING("eating"),
        CRAFTING("crafting"),
        FYLING("flying");

        private final String fieldName;
        TimeType(String fieldName) {
            this.fieldName = fieldName;
        }

        public void add(ElapsedTime elapsedTime, PlayerTimePerDay instance){
            var v = get(instance);
            if(v != null)v.add(elapsedTime);
        }

        @SuppressWarnings("unchecked")
        public  List<ElapsedTime> get(PlayerTimePerDay instance){
            for (var field : PlayerTimePerDay.class.getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase(fieldName)) {
                    try {
                        return ((List<ElapsedTime>) field.get(instance));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    private final List<ElapsedTime> afk = new ArrayList<>();
    private final List<ElapsedTime> sneak = new ArrayList<>();
    private final List<ElapsedTime> walking = new ArrayList<>();
    private final List<ElapsedTime> falling = new ArrayList<>();
    private final List<ElapsedTime> running = new ArrayList<>();
    private final List<ElapsedTime> swimming = new ArrayList<>();
    private final List<ElapsedTime> mining = new ArrayList<>();
    private final List<ElapsedTime> drinking = new ArrayList<>();
    private final List<ElapsedTime> eating = new ArrayList<>();
    private final List<ElapsedTime> crafting = new ArrayList<>();
    private final List<ElapsedTime> flying = new ArrayList<>();

    public Long day;

    public PlayerTimePerDay() {
        day = System.currentTimeMillis();
    }

    public void add(TimeType timeType, ElapsedTime elapsedTime) {
        timeType.add(elapsedTime, this);
    }

    public List<ElapsedTime> getElapsedTime(TimeType timeType){
        return timeType.get(this);
    }

    public Long calculateTotal(List<ElapsedTime> elapsedTimes) {
        if(elapsedTimes == null)return -1L;
        var totalTime = 0L;
        for (var timeElapsed : elapsedTimes)
            totalTime += timeElapsed.timeElapsed;
        return totalTime;
    }

}
