package ch.skyfy.playtime.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public interface PlayerUnSneakCallback {
    Event<PlayerUnSneakCallback> EVENT = EventFactory.createArrayBacked(PlayerUnSneakCallback.class,
            (listeners) -> (player) -> {
                for (PlayerUnSneakCallback listener : listeners) {
                    ActionResult result = listener.interact(player);
                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact(PlayerEntity player);
}
