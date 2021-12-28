package ch.skyfy.playtime.test;

import java.io.Serializable;

/**
 * This class use to register more information about a player when a time is elapsed
 *
 * Example
 *      player don't move for more than 20 second, -> couting time for afk begin (time is 09:00:20)
 *      after 5 minutes, player move -> afk time is (time is 09:05:20)
 *      we can now know total afk time, or also total afk time inside lava, total afk time in the_nether, total time in plains biome, etc.
 */
public class ElapsedTime implements Serializable {

    public Long timeElapsed;
    public String dimension; // the value is this: player.world.getDimension().getEffects().getPath(); (the_nether, the_end, overworld)
    public String biome;
    public boolean inWater;
    public boolean inLava;

    public ElapsedTime(String dimension, String biome, boolean inWater, boolean inLava, Long timeElapsed) {
        this.dimension = dimension;
        this.biome = biome;
        this.inWater = inWater;
        this.inLava = inLava;
        this.timeElapsed = timeElapsed;
    }
}
