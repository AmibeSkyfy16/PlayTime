package ch.skyfy.playtime.core.mixin;

import ch.skyfy.playtime.core.PlayTimeManager;
import ch.skyfy.playtime.core.times.AFKTime;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

import static ch.skyfy.playtime.core.times.TimeBase.SaveOrStartCountTime.SAVE_TIME;
import static ch.skyfy.playtime.core.times.TimeBase.SaveOrStartCountTime.START_COUNT_TIME;

@SuppressWarnings("UnusedMixin")
@Deprecated // Use MovementDetection instead of this
@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    private final Timer timer = new Timer(true);

    private final List<Vec3d> blockPosList = Collections.synchronizedList(new ArrayList<>());
    private final List<Boolean> flyingWithElytra = Collections.synchronizedList(new ArrayList<>());

    private Long startAFKTimeoutTime;

    private boolean timerStarted;
    private boolean isWalking;
    private boolean isFalling;
    private boolean isFlying;
    private boolean afk;

    private void schedule(PlayerEntity player) {
        timerStarted = true;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                var elapsed = -1L; // lastAFKTime will be null the first few times the code is executed
                if (startAFKTimeoutTime != null) elapsed = System.currentTimeMillis() - startAFKTimeoutTime;

                // If all boolean added in last 1000 millis, mean the player is flying with elytra
                if (flyingWithElytra.stream().allMatch(aBoolean -> aBoolean)) {
                    if (!isFlying) {
                        isFlying = true;
                        PlayTimeManager.getInstance().flyingTime.createAndAddElapsedTime(player, START_COUNT_TIME);
                    }
                }

                if (blockPosList.stream().allMatch(pos -> pos.getY() == player.getPos().getY())) {
                    if (isFalling) {
                        isFalling = false;
                        PlayTimeManager.getInstance().fallingTime.createAndAddElapsedTime(player, SAVE_TIME);
                    }
                } else {
                    // This code is executed once, when the player was standing without moving and starts to fall
                    if (!isFalling) {


                        // Check if player go down, not up
                        if (blockPosList.get(0).getY() > blockPosList.get(blockPosList.size() - 1).getY()) {
                            PlayTimeManager.getInstance().fallingTime.createAndAddElapsedTime(player, START_COUNT_TIME);
                            isFalling = true;
                        }

//                        for (Vec3d vec3d : blockPosList) {
//                            System.out.println("Y: " + vec3d.getY());
//                        }
                    }
                }

                // All block register in last 1000 millis are the same, mean the player didn't move
                if (blockPosList.stream().allMatch(pos -> pos.getX() == player.getPos().getX() && pos.getZ() == player.getPos().getZ())) {
                    // This is code executed once, when the player was moving and then stopped
                    if (!afk) {
                        afk = true;
                        startAFKTimeoutTime = System.currentTimeMillis(); // Use to know when the player has not moved for more than 60 seconds
                    }

                    // This is code executed once, when the player was moving and then stopped
                    if (!isFlying && isWalking) {
                        isWalking = false;
                        PlayTimeManager.getInstance().walkingTime.createAndAddElapsedTime(player, SAVE_TIME);
                        PlayTimeManager.getInstance().runningTime.createAndAddElapsedTime(player, SAVE_TIME);
                        System.out.println("Player was walking, he is now afk");
                    }

                    if (isFlying) {
                        isFlying = false;
                        PlayTimeManager.getInstance().flyingTime.createAndAddElapsedTime(player, SAVE_TIME);
                    }

                    // This code is repeated until the player moves again
                    if (elapsed > AFKTime.TIMEOUT) { // A player is condisidered AFK after a delay of 60 secondes
                        afk = true; // When the player will walk again, his time between the moment he started to be afk and now will be recorded
                        PlayTimeManager.getInstance().afkTime.createAndAddElapsedTime(player, START_COUNT_TIME);
                    }
                } else {
                    // This code is executed once, when the player was standing without moving and starts to move
                    if (!isWalking) {
                        if (afk) { // Player is no more afk (only when the timeout for the afk time is exceeded)
                            afk = false;
                            PlayTimeManager.getInstance().afkTime.createAndAddElapsedTime(player, SAVE_TIME); // #2
                        }
                        if (!player.isSprinting()) {
                            PlayTimeManager.getInstance().walkingTime.createAndAddElapsedTime(player, START_COUNT_TIME);
                        } else {
                            PlayTimeManager.getInstance().runningTime.createAndAddElapsedTime(player, START_COUNT_TIME);
                        }
                    }
                    isWalking = true;
                }
                flyingWithElytra.clear();
                blockPosList.clear();
            }
        }, 1000, 1000);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if(0 == 0)return;
        var entity = ((PlayerEntity) (Object) this);
        blockPosList.add(entity.getPos());
        flyingWithElytra.add(entity.isFallFlying());
        if (!timerStarted) schedule(entity);
    }
}
