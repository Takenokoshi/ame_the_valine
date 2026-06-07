package ame_the_valine.registries;

import ame_the_valine.AMETheValineConstants;
import ame_the_valine.recipe.ValineReactorBaseRecipe;
import ame_the_valine.recipe.ValineReactorInputCache;
import astral_mekanism.util.RecipeTypeUtils;
import mekanism.common.registration.impl.RecipeTypeDeferredRegister;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;

public class AMETheValineRecipeTypes {
    public static final RecipeTypeDeferredRegister RECIPE_TYPES = new RecipeTypeDeferredRegister(
            AMETheValineConstants.MODID);

    public static final RecipeTypeRegistryObject<ValineReactorBaseRecipe, ValineReactorInputCache> VALINE_REACTOR = RecipeTypeUtils
            .registerRecipeType(RECIPE_TYPES, "valine_reactor", AMETheValineConstants::rl, ValineReactorInputCache::new);
}
