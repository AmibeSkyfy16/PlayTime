package ch.skyfy.playtime.commands;

import ch.skyfy.playtime.core.PlayTimeManager;
import ch.skyfy.playtime.core.PlayerTime;
import ch.skyfy.playtime.core.PlayerTimePerDay;
import com.mojang.brigadier.Command;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public abstract class TimeCmd {
    public static int execute(PlayerEntity player, PlayerTimePerDay.TimeType timeType){
        for (PlayerTime playerTime : PlayTimeManager.getInstance().playerTimes) {
            if (playerTime.UUID.equalsIgnoreCase(player.getUuidAsString())) {
                var time = playerTime.getOrCreateToday().calculateTotal(timeType);
                player.sendMessage(Text.of(timeType.name()+" time is : " + time + " millis"), false);
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
