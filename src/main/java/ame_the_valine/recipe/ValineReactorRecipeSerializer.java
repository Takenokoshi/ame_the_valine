package ame_the_valine.recipe;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import astral_mekanism.util.AMEJsonUtils;
import mekanism.api.JsonConstants;
import mekanism.api.SerializerHelper;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ValineReactorRecipeSerializer<RECIPE extends ValineReactorBaseRecipe> implements RecipeSerializer<RECIPE> {

    private final ValineReactorRecipeFactory<RECIPE> factory;

    public ValineReactorRecipeSerializer(ValineReactorRecipeFactory<RECIPE> factory) {
        this.factory = factory;
    }

    @Override
    public RECIPE fromJson(ResourceLocation id, JsonObject jsonObject) {
        return factory.create(id,
                IngredientCreatorAccess.gas().deserialize(AMEJsonUtils.read(jsonObject, JsonConstants.LEFT_INPUT)),
                IngredientCreatorAccess.gas().deserialize(AMEJsonUtils.read(jsonObject, JsonConstants.RIGHT_INPUT)),
                SerializerHelper.getFloatingLong(jsonObject, "energy_generation"),
                SerializerHelper.getGasStack(jsonObject, JsonConstants.LEFT_GAS_OUTPUT),
                SerializerHelper.getGasStack(jsonObject, JsonConstants.RIGHT_GAS_OUTPUT));
    }

    @Override
    public @Nullable RECIPE fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        return factory.create(id,
                IngredientCreatorAccess.gas().read(buf),
                IngredientCreatorAccess.gas().read(buf),
                FloatingLong.readFromBuffer(buf),
                GasStack.readFromPacket(buf),
                GasStack.readFromPacket(buf));
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, RECIPE recipe) {
        recipe.write(buf);
    }

    @FunctionalInterface
    public static interface ValineReactorRecipeFactory<RECIPE extends ValineReactorBaseRecipe> {

        RECIPE create(ResourceLocation id, GasStackIngredient ingredientA, GasStackIngredient ingredientB,
                FloatingLong energyGeneration, GasStack outputA, GasStack outputB);
    }

}
