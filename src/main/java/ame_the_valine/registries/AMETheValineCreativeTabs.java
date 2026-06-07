package ame_the_valine.registries;

import ame_the_valine.AMETheValineConstants;
import ame_the_valine.AMETheValineLang;
import mekanism.common.registration.impl.CreativeTabDeferredRegister;
import mekanism.common.registration.impl.CreativeTabRegistryObject;

public class AMETheValineCreativeTabs {
    public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(
            AMETheValineConstants.MODID);

    public static final CreativeTabRegistryObject TAB = CREATIVE_TABS.register("creative_tab",
            AMETheValineLang.AME_THE_VALINE,
            AMETheValineMachines.VALINE_REACTOR,
            builder -> builder.displayItems((displayParameters, output) -> {
                CreativeTabDeferredRegister.addToDisplay(AMETheValineMachines.MACHINES.blockRegister, output);
            }));
}
