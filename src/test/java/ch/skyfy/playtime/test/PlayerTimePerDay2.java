package ch.skyfy.playtime.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class PlayerTimePerDay2 implements Serializable {

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

        public void add(ElapsedTime2 elapsedTime, PlayerTimePerDay2 instance){
            var v = get(instance);
            if(v != null)v.add(elapsedTime);
        }

        @SuppressWarnings("unchecked")
        public  List<ElapsedTime2> get(PlayerTimePerDay2 instance){
            for (var field : PlayerTimePerDay2.class.getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase(fieldName)) {
                    try {
                        return ((List<ElapsedTime2>) field.get(instance));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    private final List<ElapsedTime2> afk = new ArrayList<>();
    private final List<ElapsedTime2> sneak = new ArrayList<>();
    private final List<ElapsedTime2> walking = new ArrayList<>();
    private final List<ElapsedTime2> falling = new ArrayList<>();
    private final List<ElapsedTime2> running = new ArrayList<>();
    private final List<ElapsedTime2> swimming = new ArrayList<>();
    private final List<ElapsedTime2> mining = new ArrayList<>();
    private final List<ElapsedTime2> drinking = new ArrayList<>();
    private final List<ElapsedTime2> eating = new ArrayList<>();
    private final List<ElapsedTime2> crafting = new ArrayList<>();
    private final List<ElapsedTime2> flying = new ArrayList<>();

    public Long day;

    public PlayerTimePerDay2() {
        day = System.currentTimeMillis();
    }

    public void add(TimeType timeType, ElapsedTime2 elapsedTime) {
        timeType.add(elapsedTime, this);
    }

    public List<ElapsedTime2> getElapsedTime(TimeType timeType){
        return timeType.get(this);
    }

    public Long calculateTotal(List<ElapsedTime2> elapsedTimes) {
        if(elapsedTimes == null)return -1L;
        var totalTime = 0L;
        for (var timeElapsed : elapsedTimes)
            totalTime += timeElapsed.timeElapsed;
        return totalTime;
    }

}
