package ame_the_valine.recipe;

import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.EitherSideInputRecipeCache;
import mekanism.common.recipe.lookup.cache.type.ChemicalInputCache;

public class ValineReactorInputCache extends
        EitherSideInputRecipeCache<GasStack, ChemicalStackIngredient<Gas, GasStack>, ValineReactorBaseRecipe, ChemicalInputCache<Gas, GasStack, ValineReactorBaseRecipe>> {

    public ValineReactorInputCache(MekanismRecipeType<ValineReactorBaseRecipe, ?> recipeType) {
        super(recipeType, ValineReactorBaseRecipe::getInputA, ValineReactorBaseRecipe::getInputB,
                new ChemicalInputCache<>());
    }

}
