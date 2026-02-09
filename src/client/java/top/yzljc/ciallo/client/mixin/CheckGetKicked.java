package top.yzljc.ciallo.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.yzljc.ciallo.client.steam.SteamLauncher;

@Mixin(MinecraftClient.class)
public class CheckGetKicked {

    @Inject(method = "setScreen", at = @At("HEAD"))
    private void onSetScreen(Screen screen, CallbackInfo ci) {
        if (screen instanceof DisconnectedScreen) {
            SteamLauncher.launchSenrenBanka();
        }
    }
}