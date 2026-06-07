package ame_the_valine.blockentity;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ame_the_valine.recipe.EnergyOutputHandler;
import ame_the_valine.recipe.ValineReactorBaseRecipe;
import ame_the_valine.recipe.ValineReactorCachedRecipe;
import ame_the_valine.recipe.ValineReactorInputCache;
import ame_the_valine.registries.AMETheValineRecipeTypes;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableFloatingLong;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo;
import mekanism.common.tile.machine.TileEntityElectrolyticSeparator;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityValineReactor extends TileEntityRecipeMachine<ValineReactorBaseRecipe> {

    public static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(RecipeError.NOT_ENOUGH_LEFT_INPUT,
            RecipeError.NOT_ENOUGH_RIGHT_INPUT,
            TileEntityElectrolyticSeparator.NOT_ENOUGH_SPACE_LEFT_OUTPUT_ERROR,
            TileEntityElectrolyticSeparator.NOT_ENOUGH_SPACE_RIGHT_OUTPUT_ERROR);

    private IGasTank inputTankA;
    private IGasTank inputTankB;
    private BasicEnergyContainer energyContainer;
    private IGasTank outputTankA;
    private IGasTank outputTankB;

    private final IInputHandler<GasStack> inputHandlerA;
    private final IInputHandler<GasStack> inputHandlerB;
    private final IOutputHandler<FloatingLong> energyOutputHandler;
    private final IOutputHandler<GasStack> outputHandlerA;
    private final IOutputHandler<GasStack> outputHandlerB;

    private FloatingLong lastEnergyGenerated = FloatingLong.ZERO;

    public BlockEntityValineReactor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.ENERGY, TransmissionType.GAS);
        configComponent.setupOutputConfig(TransmissionType.ENERGY, energyContainer, RelativeSide.RIGHT);
        ConfigInfo gasConfigInfo = configComponent.getConfig(TransmissionType.GAS);
        gasConfigInfo.addSlotInfo(DataType.INPUT_1, new ChemicalSlotInfo.GasSlotInfo(true, false, inputTankA));
        gasConfigInfo.addSlotInfo(DataType.INPUT_2, new ChemicalSlotInfo.GasSlotInfo(true, false, inputTankB));
        gasConfigInfo.addSlotInfo(DataType.OUTPUT_1, new ChemicalSlotInfo.GasSlotInfo(false, true, outputTankA));
        gasConfigInfo.addSlotInfo(DataType.OUTPUT_2, new ChemicalSlotInfo.GasSlotInfo(false, true, outputTankB));
        gasConfigInfo.addSlotInfo(DataType.INPUT_OUTPUT,
                new ChemicalSlotInfo.GasSlotInfo(true, true, inputTankA, inputTankB, outputTankA, outputTankB));
        ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE, () -> 0, () -> FloatingLong.MAX_VALUE)
                .setOutputData(configComponent, TransmissionType.ENERGY, TransmissionType.GAS)
                .setCanTankEject(tank -> tank == outputTankA || tank == outputTankB);
        inputHandlerA = InputHelper.getInputHandler(inputTankA, RecipeError.NOT_ENOUGH_LEFT_INPUT);
        inputHandlerB = InputHelper.getInputHandler(inputTankB, RecipeError.NOT_ENOUGH_RIGHT_INPUT);
        energyOutputHandler = new EnergyOutputHandler(energyContainer);
        outputHandlerA = OutputHelper.getOutputHandler(outputTankA,
                TileEntityElectrolyticSeparator.NOT_ENOUGH_SPACE_LEFT_OUTPUT_ERROR);
        outputHandlerB = OutputHelper.getOutputHandler(outputTankB,
                TileEntityElectrolyticSeparator.NOT_ENOUGH_SPACE_RIGHT_OUTPUT_ERROR);
    }

    @Override
    protected @Nullable IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = BasicEnergyContainer.output(FloatingLong.MAX_VALUE, listener));
        return builder.build();
    }

    @Override
    protected @Nullable IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper
                .forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputTankA = ChemicalTankBuilder.GAS.create(Long.MAX_VALUE,
                (gas, type) -> type == AutomationType.MANUAL,
                (gas, type) -> AMETheValineRecipeTypes.VALINE_REACTOR.getInputCache().containsInput(getLevel(),
                        gas.getStack(Long.MAX_VALUE), inputTankB.getStack()),
                gas -> AMETheValineRecipeTypes.VALINE_REACTOR.getInputCache().containsInput(getLevel(),
                        gas.getStack(Long.MAX_VALUE)),
                ChemicalAttributeValidator.ALWAYS_ALLOW,
                recipeCacheListener));
        builder.addTank(inputTankB = ChemicalTankBuilder.GAS.create(Long.MAX_VALUE,
                (gas, type) -> type == AutomationType.MANUAL,
                (gas, type) -> AMETheValineRecipeTypes.VALINE_REACTOR.getInputCache().containsInput(getLevel(),
                        inputTankA.getStack(), gas.getStack(Long.MAX_VALUE)),
                gas -> AMETheValineRecipeTypes.VALINE_REACTOR.getInputCache().containsInput(getLevel(),
                        gas.getStack(Long.MAX_VALUE)),
                ChemicalAttributeValidator.ALWAYS_ALLOW,
                recipeCacheListener));
        builder.addTank(outputTankA = ChemicalTankBuilder.GAS.output(Long.MAX_VALUE, listener));
        builder.addTank(outputTankB = ChemicalTankBuilder.GAS.output(Long.MAX_VALUE, listener));
        return builder.build();
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<ValineReactorBaseRecipe, ValineReactorInputCache> getRecipeType() {
        return AMETheValineRecipeTypes.VALINE_REACTOR;
    }

    @Override
    public @Nullable ValineReactorBaseRecipe getRecipe(int cacheIndex) {
        return AMETheValineRecipeTypes.VALINE_REACTOR.getInputCache().findFirstRecipe(getLevel(),
                inputHandlerA.getInput(), inputHandlerB.getInput());
    }

    @Override
    public @NotNull CachedRecipe<ValineReactorBaseRecipe> createNewCachedRecipe(@NotNull ValineReactorBaseRecipe recipe,
            int cacheIndex) {
        return new ValineReactorCachedRecipe(recipe, recheckAllRecipeErrors, inputHandlerA, inputHandlerB,
                energyOutputHandler, outputHandlerA, outputHandlerB)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setBaselineMaxOperations(() -> 0x7fffffff)
                .setOnFinish(this::markForSave);
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        FloatingLong before = energyContainer.getEnergy().copy();
        recipeCacheLookupMonitor.updateAndProcess();
        lastEnergyGenerated = energyContainer.getEnergy().subtract(before);
    }

    public IGasTank getInputTankA() {
        return inputTankA;
    }

    public IGasTank getInputTankB() {
        return inputTankB;
    }

    public IGasTank getOutputTankA() {
        return outputTankA;
    }

    public IGasTank getOutputTankB() {
        return outputTankB;
    }

    public BasicEnergyContainer getEnergyContainer() {
        return energyContainer;
    }

    public FloatingLong getEnergyGenerated() {
        return lastEnergyGenerated;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableFloatingLong.create(this::getEnergyGenerated, v -> lastEnergyGenerated = v));
    }

}
