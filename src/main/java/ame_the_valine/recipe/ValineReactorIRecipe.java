package ame_the_valine.recipe;

import ame_the_valine.registries.AMETheValineRecipeSerializers;
import ame_the_valine.registries.AMETheValineRecipeTypes;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ValineReactorIRecipe extends ValineReactorBaseRecipe {

    public ValineReactorIRecipe(ResourceLocation id, GasStackIngredient ingredientA, GasStackIngredient ingredientB,
            FloatingLong energyGeneration, GasStack outputA, GasStack outputB) {
        super(id, ingredientA, ingredientB, energyGeneration, outputA, outputB);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AMETheValineRecipeSerializers.VALINE_REACTOR.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AMETheValineRecipeTypes.VALINE_REACTOR.get();
    }
    
}
