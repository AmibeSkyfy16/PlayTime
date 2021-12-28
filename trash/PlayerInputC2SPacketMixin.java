package ch.skyfy.playtime.mixin;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInputC2SPacket.class)
public class PlayerInputC2SPacketMixin {
    @Shadow @Final private float forward;

    @Inject(method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V", at=@At("RETURN"))
    private void init(PacketByteBuf buf, CallbackInfo info) {
        System.out.println("PacketByteBuf");
        // inject into a constructor
    }
    
    @Inject(method = "<init>(FFZZ)V", at = @At("RETURN"))
    void init(float sideways, float forward, boolean jumping, boolean sneaking, CallbackInfo info){
        System.out.println("sideways " + sideways);
    }

    @Inject(method = "apply(Lnet/minecraft/network/listener/ServerPlayPacketListener;)V", at = @At("HEAD"))
    public void apply(ServerPlayPacketListener serverPlayPacketListener, CallbackInfo ci){
        System.out.println("forward value is " + forward);
    }
}
