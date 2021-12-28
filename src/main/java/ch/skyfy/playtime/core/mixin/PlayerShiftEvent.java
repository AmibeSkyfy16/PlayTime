package ch.skyfy.playtime.core.mixin;

import ch.skyfy.playtime.events.PlayerSneakCallback;
import ch.skyfy.playtime.events.PlayerUnSneakCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class PlayerShiftEvent {

    @Inject(at = @At("HEAD"), method = "setSneaking(Z)V")
    private void sneakingActivate(boolean sneaking, CallbackInfo info) {
        var entity = ((Entity) (Object) this);
        if (sneaking) {
            System.out.println("player sneak");
            if (entity instanceof PlayerEntity player)
                PlayerSneakCallback.EVENT.invoker().interact(player);
        } else {
            if (entity instanceof PlayerEntity player)
                PlayerUnSneakCallback.EVENT.invoker().interact(player);
            System.out.println("player unsneak");
        }
    }

}
