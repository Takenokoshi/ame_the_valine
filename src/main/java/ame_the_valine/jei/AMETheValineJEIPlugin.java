package ame_the_valine.jei;

import ame_the_valine.AMETheValineConstants;
import ame_the_valine.recipe.ValineReactorBaseRecipe;
import ame_the_valine.registries.AMETheValineMachines;
import ame_the_valine.registries.AMETheValineRecipeTypes;
import mekanism.api.providers.IItemProvider;
import mekanism.client.jei.CatalystRegistryHelper;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.client.jei.RecipeRegistryHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class AMETheValineJEIPlugin implements IModPlugin {

    public static final MekanismJEIRecipeType<ValineReactorBaseRecipe> VALINE_REACTOR_JEI_TYPE = new MekanismJEIRecipeType<>(
            AMETheValineMachines.VALINE_REACTOR, ValineReactorBaseRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return AMETheValineConstants.rl("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new IRecipeCategory[] {
                new ValineReactorRecipeCategory(guiHelper, VALINE_REACTOR_JEI_TYPE, AMETheValineMachines.VALINE_REACTOR)
        });
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeRegistryHelper.register(registration, VALINE_REACTOR_JEI_TYPE, AMETheValineRecipeTypes.VALINE_REACTOR);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        CatalystRegistryHelper.register(registration, VALINE_REACTOR_JEI_TYPE, new IItemProvider[] {
                AMETheValineMachines.VALINE_REACTOR
        });
    }

}
