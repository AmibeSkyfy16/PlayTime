package ch.skyfy.playtime.commands;

import ch.skyfy.playtime.core.PlayerTimePerDay;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class WalkingTimeCmd {
    public static void initialize(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("WalkingTime").executes(context -> {
            return RunningTimeCmd.execute(context.getSource().getPlayer(), PlayerTimePerDay.TimeType.WALKING);
        }));
    }
}
