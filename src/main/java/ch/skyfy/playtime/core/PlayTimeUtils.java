package ch.skyfy.playtime.core;

import net.minecraft.entity.player.PlayerEntity;

public class PlayTimeUtils {

    /**
     * Create new elapsed time with information data like the biome in wich the player is, the dimension, etc.
     */
    public static void createAndAddNewElapsedTime(PlayerEntity player, Long lastTime, PlayerTimePerDay.TimeType timeType) {
        var playerTime = getOrCreatePlayerTime(player.getUuidAsString());
        var playerTimePerDay = playerTime.getOrCreateToday();
        var elapsed = System.currentTimeMillis() - lastTime;
        var dimension = player.world.getDimension().getEffects().getPath();
        var biome = "";
        var opt = player.world.getBiomeKey(player.getBlockPos());
        if (opt.isPresent()) biome = opt.get().getValue().getPath();
        var timeElapsed = new ElapsedTime(dimension, biome, false, false, elapsed);
        playerTimePerDay.add(timeType, timeElapsed);
    }

    /**
     * From the list of playerTimes, retrieve a specific one by player uuid
     */
    public static PlayerTime getOrCreatePlayerTime(String uuid) {
        var playerTimes = PlayTimeManager.getInstance().playerTimes;
        for (var playerTime : playerTimes)
            if (playerTime.UUID.equalsIgnoreCase(uuid))
                return playerTime;
        var newPlayerTime = new PlayerTime(uuid);
        playerTimes.add(newPlayerTime);
        return newPlayerTime;
    }

}
