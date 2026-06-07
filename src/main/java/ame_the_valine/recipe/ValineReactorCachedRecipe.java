package ame_the_valine.recipe;

import java.util.function.BooleanSupplier;

import mekanism.api.chemical.gas.GasStack;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;

public class ValineReactorCachedRecipe extends CachedRecipe<ValineReactorBaseRecipe> {

    private final IInputHandler<GasStack> inputHandlerA;
    private final IInputHandler<GasStack> inputHandlerB;
    private final IOutputHandler<FloatingLong> energyOutputHandler;
    private final IOutputHandler<GasStack> outputHandlerA;
    private final IOutputHandler<GasStack> outputHandlerB;
    private GasStack inputA = GasStack.EMPTY;
    private GasStack inputB = GasStack.EMPTY;

    private final FloatingLong energy;
    private final GasStack outputA;
    private final GasStack outputB;

    public ValineReactorCachedRecipe(ValineReactorBaseRecipe recipe, BooleanSupplier recheckAllErrors,
            IInputHandler<GasStack> inputHandlerA, IInputHandler<GasStack> inputHandlerB,
            IOutputHandler<FloatingLong> energyOutputHandler, IOutputHandler<GasStack> outputHandlerA,
            IOutputHandler<GasStack> outputHandlerB) {
        super(recipe, recheckAllErrors);
        this.inputHandlerA = inputHandlerA;
        this.inputHandlerB = inputHandlerB;
        this.energyOutputHandler = energyOutputHandler;
        this.outputHandlerA = outputHandlerA;
        this.outputHandlerB = outputHandlerB;
        this.energy = this.recipe.getOutputEnergy();
        this.outputA = this.recipe.getOutputA();
        this.outputB = this.recipe.getOutputB();
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        // super method only calculate energy consume, so I can skip super.
        inputA = inputHandlerA.getRecipeInput(recipe.ingredientA);
        inputB = inputHandlerB.getRecipeInput(recipe.ingredientB);
        if (inputA.isEmpty() && inputB.isEmpty()) {
            inputA = inputHandlerA.getRecipeInput(recipe.ingredientB);
            inputB = inputHandlerB.getRecipeInput(recipe.ingredientA);
        }
        if (inputA.isEmpty() || inputB.isEmpty()) {
            tracker.mismatchedRecipe();
            return;
        }

        inputHandlerA.calculateOperationsCanSupport(tracker, inputA);
        inputHandlerB.calculateOperationsCanSupport(tracker, inputB);
        outputHandlerA.calculateOperationsCanSupport(tracker, outputA);
        outputHandlerB.calculateOperationsCanSupport(tracker, outputB);
    }

    @Override
    public boolean isInputValid() {
        return recipe.test(inputHandlerA.getInput(), inputHandlerB.getInput())
                || recipe.test(inputHandlerB.getInput(), inputHandlerA.getInput());
    }

    @Override
    protected void finishProcessing(int operations) {
        inputHandlerA.use(inputA, operations);
        inputHandlerB.use(inputB, operations);
        energyOutputHandler.handleOutput(energy, operations);
        outputHandlerA.handleOutput(outputA, operations);
        outputHandlerB.handleOutput(outputB, operations);
    }

}
