package ame_the_valine;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(value = AMETheValineConstants.MODID)
public class AMETheValineBootstrap {
    public AMETheValineBootstrap() {
        DistExecutor.unsafeRunForDist(() -> AMETheValineClient::new, () -> AMETheValine::new);
    }
}
