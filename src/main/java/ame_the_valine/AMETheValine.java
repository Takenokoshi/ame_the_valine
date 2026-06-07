package ame_the_valine;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import ame_the_valine.registries.AMETheValineCreativeTabs;
import ame_the_valine.registries.AMETheValineMachines;
import ame_the_valine.registries.AMETheValineRecipeSerializers;
import ame_the_valine.registries.AMETheValineRecipeTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class AMETheValine {

    public static final Logger LOGGER = LogUtils.getLogger();

    public AMETheValine(){
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        IEventBus modEventBus = context.getModEventBus();
        AMETheValineMachines.MACHINES.register(modEventBus);
        AMETheValineRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        AMETheValineRecipeTypes.RECIPE_TYPES.register(modEventBus);
        AMETheValineCreativeTabs.CREATIVE_TABS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
