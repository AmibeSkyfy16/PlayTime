package ch.skyfy.playtime.core.times;

import ch.skyfy.playtime.core.PlayerTimePerDay;
import ch.skyfy.playtime.events.PlayerSneakCallback;
import ch.skyfy.playtime.events.PlayerUnSneakCallback;
import net.minecraft.util.ActionResult;

import static ch.skyfy.playtime.core.times.TimeBase.SaveOrStartCountTime.SAVE_TIME;
import static ch.skyfy.playtime.core.times.TimeBase.SaveOrStartCountTime.START_COUNT_TIME;

public class SneakTime extends TimeBase {

    public SneakTime() {
        super(PlayerTimePerDay.TimeType.SNEAK);
    }

    @Override
    public void initialize() {
        playerSneakEvent();
    }

    private void playerSneakEvent() {
        PlayerSneakCallback.EVENT.register(player -> {
            createAndAddElapsedTime(player, START_COUNT_TIME);
            return ActionResult.PASS;
        });
        PlayerUnSneakCallback.EVENT.register(player -> {
            createAndAddElapsedTime(player, SAVE_TIME);
            return ActionResult.PASS;
        });
    }
}
