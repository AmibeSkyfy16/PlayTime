package ch.skyfy.playtime.core;

import ch.skyfy.playtime.core.times.AFKTime;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.*;

import static ch.skyfy.playtime.core.times.TimeBase.SaveOrStartCountTime.SAVE_TIME;
import static ch.skyfy.playtime.core.times.TimeBase.SaveOrStartCountTime.START_COUNT_TIME;

public class MovementDetection {

    public static class MovementPlayerDetection {
        public static final int timerPeriod = 20;

        final Timer timer = new Timer(true);
        final Timer detectionTimer = new Timer(true);

        private final List<Vec3d> blockPosList = Collections.synchronizedList(new ArrayList<>());
        private final List<Boolean> flyingWithElytra = Collections.synchronizedList(new ArrayList<>());

        private Long startAFKTimeoutTime;

        private boolean isWalking;
        private boolean isFalling;
        private boolean isFlying;
        private boolean afk;
        private boolean startAFKTimeoutTimeOnce;

        final PlayerEntity player;

        public MovementPlayerDetection(PlayerEntity player) {
            this.player = player;
//            detectionTimer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    blockPosList.add(player.getPos());
//                    flyingWithElytra.add(player.isFallFlying());
//                }
//            }, 80, 80);
            schedule(player);
        }

        public void stop() {
            timer.cancel();
            timer.purge();
            detectionTimer.cancel();
            detectionTimer.purge();
        }

        private void schedule(PlayerEntity player) {
            timer.schedule(new TimerTask() {
                private int countInterval = 0;
                @Override
                public void run() {
                    // Getting some information about the player position during 320 millis
                    if(countInterval++ <= 16){ // every 320 millis
                        flyingWithElytra.add(player.isFallFlying());
                        blockPosList.add(player.getPos());
                        return;
                    }
                    countInterval = 0;

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
                        }
                    }

                    // All block register in last 1000 millis are the same, mean the player didn't move
                    if (blockPosList.stream().allMatch(pos -> pos.getX() == player.getPos().getX() && pos.getZ() == player.getPos().getZ())) {
                        // This is code executed once, when the player was moving and then stopped
                        if (!startAFKTimeoutTimeOnce) {
                            startAFKTimeoutTimeOnce = true;
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
                        if (elapsed > 20_000L) { // A player is condisidered AFK after a delay of 60 secondes
                            if(!afk) {
                                afk = true; // When the player will walk again, his time between the moment he started to be afk and now will be recorded
                                PlayTimeManager.getInstance().afkTime.createAndAddElapsedTime(player, START_COUNT_TIME);
                            }
                        }
                    } else {
                        // This code is executed once, when the player was standing without moving and starts to move
                        if (!isWalking) {
                            if (afk) { // Player is no more afk (only when the timeout for the afk time is exceeded)
                                afk = false;
                                startAFKTimeoutTimeOnce = false;
                                PlayTimeManager.getInstance().afkTime.createAndAddElapsedTime(player, SAVE_TIME);
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
            }, timerPeriod, timerPeriod);
        }
    }

    static List<MovementPlayerDetection> movementPlayerDetections = new ArrayList<>();

    public static void init() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            var mo = new MovementPlayerDetection(handler.player);
            movementPlayerDetections.add(mo);
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            for (MovementPlayerDetection movementPlayerDetection : movementPlayerDetections) {
                if (movementPlayerDetection.player.equals(handler.player)) {
                    movementPlayerDetection.stop();
                    break;
                }
            }
        });
    }
}
