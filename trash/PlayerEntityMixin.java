package ch.skyfy.playtime.mixin;

import ch.skyfy.playtime.PlayerManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiling.jfr.event.PacketReceivedEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    private static final Long TIMEOUT = 2000L;

    Vec3d vec3d;

    Long lastTime = null;
    Long elapsed;

    private List<Vec3d> elapsedList = new ArrayList<>();

    Timer timer = new Timer(true);

    boolean timerStarted = false;

    private void schedule(PlayerEntity player){
        timerStarted = true;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(lastTime == null){
                    lastTime = System.currentTimeMillis();

                }else{
                    elapsed = System.currentTimeMillis() - lastTime;
                    if(elapsed >= TIMEOUT){
                        if(elapsedList.stream().allMatch(vec3d1 -> vec3d1.getX() == 0 && vec3d1.getZ() == 0)){
                            PlayerManager.getInstance().getWalkingTime().createAndAddElapsedTime(player);
                            System.out.println("Player is standing");
                            elapsedList.clear();
                            lastTime = null;
                        }
                    }
                }
            }
        }, 500, 500);
    }

    int count = 0;

    @Inject(method = "travel", at = @At("HEAD"))
    private void onPlayerTravel(Vec3d movementInput, CallbackInfo ci){
        if(0 == 0)return;
        count++;
        var entity = ((PlayerEntity) (Object) this);



        if(!timerStarted)schedule(entity);

        elapsedList.add(movementInput);

        System.out.println("count : " + count);
        System.out.println("X: "+movementInput.getX());
        System.out.println("Z:" + movementInput.getZ());

        if(movementInput.getZ() > 0 || movementInput.getX() > 0){

            if(entity.isSprinting()){
//                PlayerManager.getInstance().getWalkingTime().playerStartWalking(entity);
//                System.out.println("player sprinting");
            }else{
                PlayerManager.getInstance().getWalkingTime().playerStartWalking(entity);
//                PlayerManager.getInstance().walking.pressed = true;
//                System.out.println("player walking");
            }
        }

        if(movementInput.getZ() == 0 && movementInput.getX() == 0){
//            PlayerManager.getInstance().getWalkingTime().playerStopWalking(entity);
        }

        // always 0
//        System.out.println("GetY " + movementInput.getY());
//        if(movementInput.getY() > 0){
//            System.out.println("entity is falling ");
//        }
    }
}
