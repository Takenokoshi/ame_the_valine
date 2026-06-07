package ame_the_valine.recipe;

import java.util.function.BiPredicate;

import mekanism.api.chemical.gas.GasStack;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public abstract class ValineReactorBaseRecipe extends MekanismRecipe implements BiPredicate<GasStack, GasStack> {

    public final GasStackIngredient ingredientA;
    public final GasStackIngredient ingredientB;
    private final FloatingLong energyGeneration;
    private final GasStack outputA;
    private final GasStack outputB;

    protected ValineReactorBaseRecipe(ResourceLocation id, GasStackIngredient ingredientA,
            GasStackIngredient ingredientB, FloatingLong energyGeneration, GasStack outputA, GasStack outputB) {
        super(id);
        this.ingredientA = ingredientA;
        this.ingredientB = ingredientB;
        this.energyGeneration = energyGeneration;
        this.outputA = outputA;
        this.outputB = outputB;
    }

    public GasStackIngredient getInputA(){
        return ingredientA;
    }

    public GasStackIngredient getInputB(){
        return ingredientB;
    }

    @Override
    public boolean test(GasStack t, GasStack u) {
        return ingredientA.test(t) && ingredientB.test(u);
    }

    public GasStack getOutputA(){
        return outputA.copy();
    }

    public GasStack getOutputB(){
        return outputB.copy();
    }

    public FloatingLong getOutputEnergy(){
        return energyGeneration.copy();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        ingredientA.write(buffer);
        ingredientB.write(buffer);
        energyGeneration.writeToBuffer(buffer);
        outputA.writeToPacket(buffer);
        outputB.writeToPacket(buffer);
    }

    @Override
    public void logMissingTags() {
        ingredientA.logMissingTags();
        ingredientB.logMissingTags();
    }

    @Override
    public boolean isIncomplete() {
        return ingredientA.hasNoMatchingInstances() || ingredientB.hasNoMatchingInstances();
    }

}
