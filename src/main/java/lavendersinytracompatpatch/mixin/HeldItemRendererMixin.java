package lavendersinytracompatpatch.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.wispforest.lavender.book.LavenderBookItem;
import io.wispforest.lavender.client.LavenderBookScreen;
import io.wispforest.lavender.client.OffhandBookRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    @SuppressWarnings({"InvalidInjectorMethodSignature", "MixinAnnotationTarget"})
    @WrapOperation(
            method = "renderFirstPersonItem",
            constant = @Constant(classValue = FilledMapItem.class)
    )
    private boolean injectMap(Object obj, Operation<Boolean> originalOp, AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack stack) {
        var original = originalOp.call(obj);
        if (
                !(stack.getItem() instanceof LavenderBookItem)
                        || LavenderBookItem.bookOf(stack) == null
                        || hand == Hand.MAIN_HAND
                        || MinecraftClient.getInstance().currentScreen instanceof LavenderBookScreen
        ) {
            return original;
        }

        return true;
    }

//    @ModifyExpressionValue(
//            method = "renderFirstPersonItem",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
//                    ordinal = 0
//            )
//    )
//    private boolean injectMap(boolean original, AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack stack) {
//        if (
//                !(stack.getItem() instanceof LavenderBookItem)
//                        || LavenderBookItem.bookOf(stack) == null
//                        || hand == Hand.MAIN_HAND
//                        || MinecraftClient.getInstance().currentScreen instanceof LavenderBookScreen
//        ) {
//            return original;
//        }
//
//        return true;
//    }

    @Inject(
            method = "renderFirstPersonMap",
            at = @At("HEAD"),
            cancellable = true
    )
    private void injectBook(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ItemStack stack, CallbackInfo ci) {
        if (!(stack.getItem() instanceof LavenderBookItem)) return;
        ci.cancel();

        OffhandBookRenderer.render(matrices, light);
    }
}
