package ch.skyfy.playtime.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract boolean isAlive();

    @Shadow protected abstract boolean shouldSetPositionOnLoad();

    @Shadow public float speed;
    private boolean isWalking;
    private boolean hasWalked;

    public EntityMixin() {
        isWalking = false;
        hasWalked = false;
    }

    @Inject(method = "updateVelocity", at = @At("HEAD"))
    private void updateVelocity(float speed, Vec3d movementInput, CallbackInfo ci) {
        if(0 == 0)return;
        var entity = ((Entity) (Object) this);
        if (entity instanceof PlayerEntity) {

//            System.out.println("speed: " + speed);
            System.out.println("movementInput X: " + movementInput.getX());
            System.out.println("movementInput Z: " + movementInput.getZ());

            if (speed > 0.1f) {
                if (!isWalking) {
                    System.out.println("Player start Walking");
                    hasWalked = true;
                } else System.out.println("Player still walking");
                isWalking = true;
            } else if (speed == 0.1f) {
                if (hasWalked) {
                    System.out.println("Player stop after walked");
                    hasWalked = false;
                    isWalking = false;
                }
            }

        }
    }
}
