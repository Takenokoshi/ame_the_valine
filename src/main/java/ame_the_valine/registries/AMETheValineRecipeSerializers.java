package ame_the_valine.registries;

import ame_the_valine.AMETheValineConstants;
import ame_the_valine.recipe.ValineReactorBaseRecipe;
import ame_the_valine.recipe.ValineReactorIRecipe;
import ame_the_valine.recipe.ValineReactorRecipeSerializer;
import mekanism.common.registration.impl.RecipeSerializerDeferredRegister;
import mekanism.common.registration.impl.RecipeSerializerRegistryObject;

public class AMETheValineRecipeSerializers {
    public static final RecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new RecipeSerializerDeferredRegister(
            AMETheValineConstants.MODID);

    public static final RecipeSerializerRegistryObject<ValineReactorBaseRecipe> VALINE_REACTOR = RECIPE_SERIALIZERS
            .register("valine_reactor", () -> new ValineReactorRecipeSerializer<>(ValineReactorIRecipe::new));
}
