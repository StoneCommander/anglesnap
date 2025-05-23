package me.contaria.anglesnap.mixin;

import me.contaria.anglesnap.AngleSnap;
import me.contaria.anglesnap.gui.camerasnap.CameraSnapScreen;
import me.contaria.anglesnap.gui.screen.AngleSnapScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    @Nullable
    public Screen currentScreen;

    @Shadow
    public abstract void setScreen(@Nullable Screen screen);

    @Inject(
            method = "handleInputEvents",
            at = @At("TAIL")
    )
    private void openMenu(CallbackInfo ci) {
        while (AngleSnap.openMenu.wasPressed()) {
            if (AngleSnap.CONFIG.hasAngles() && this.currentScreen == null) {
                this.setScreen(AngleSnapScreen.create(null));
            }
        }
        while (AngleSnap.cameraPositions.wasPressed()) {
            if (AngleSnap.currentCameraPos == null) {
                if (AngleSnap.CONFIG.hasCameraPositions() && this.currentScreen == null) {
                    this.setScreen(CameraSnapScreen.create(null));
                }
            } else {
                AngleSnap.currentCameraPos = null;
            }
        }
    }
}
