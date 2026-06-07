package ame_the_valine.recipe;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.common.capabilities.energy.BasicEnergyContainer;

public class EnergyOutputHandler implements IOutputHandler<FloatingLong> {

    private final BasicEnergyContainer energyContainer;

    public EnergyOutputHandler(BasicEnergyContainer energyContainer) {
        this.energyContainer = energyContainer;
    }

    @Override
    public void handleOutput(FloatingLong toOutput, int operations) {
        energyContainer.insert(toOutput.multiply(operations), Action.EXECUTE, AutomationType.INTERNAL);
    }

    @Override
    public void calculateOperationsCanSupport(OperationTracker tracker, FloatingLong toOutput) {
        tracker.updateOperations(energyContainer.getNeeded().divideToInt(toOutput));
    }

}
