package lavendersinytracompatpatch;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.service.MixinService;

import java.util.List;

public class ModMixinCanceler implements MixinCanceller {
    private static final ILogger LOGGER = MixinService.getService().getLogger("LavenderSinytraCompatPatch");

    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        if (mixinClassName.equals("io.wispforest.lavender.mixin.HeldItemRendererMixin")) {
            LOGGER.info("Cancelling mixin " + mixinClassName + " via LavenderSinytraCompatPatch to match neo patch");
            return true;
        }
        return false;
    }
}
