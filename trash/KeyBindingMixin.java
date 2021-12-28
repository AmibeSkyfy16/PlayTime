package ch.skyfy.playtime.mixin;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketDeflater;
import net.minecraft.network.PacketEncoder;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiling.jfr.event.PacketReceivedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
//@Mixin(PlayerEntity.class)
public class KeyBindingMixin {
//    @Inject(method = "onKeyPressed", at = @At("HEAD"))
//    private static void onKeyPressed(InputUtil.Key key, CallbackInfo ci) {
//        System.out.println("Key getTranslationKey: " + key.getTranslationKey());
//        System.out.println("key getCategory().name() " + key.getCategory().name());
//        System.out.println("key toString() " + key.toString());
//        System.out.println("key getLocalizedText().asString() " + key.getLocalizedText().asString());
//        System.out.println("key getCode() " + key.getCode());
//
//    }

//    @Inject(method = "tickMovement", at = @At("HEAD"))
//    private void tickMovement(CallbackInfo ci){
//        System.out.println("movement");
//        var entity = ((Entity) (Object) this);
//    }



}
