package ame_the_valine.jei;

import java.util.List;

import ame_the_valine.recipe.ValineReactorBaseRecipe;
import mekanism.api.providers.IItemProvider;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEI;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.tile.component.config.DataType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;

public class ValineReactorRecipeCategory extends BaseRecipeCategory<ValineReactorBaseRecipe> {

    private final GuiGasGauge inputA;
    private final GuiGasGauge inputB;
    private final GuiGasGauge outputA;
    private final GuiGasGauge outputB;

    public ValineReactorRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<ValineReactorBaseRecipe> recipeType,
            IItemProvider provider) {
        super(helper, recipeType, provider, 3, 10, 170, 80);
        inputA = addElement(GuiGasGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT_1), this, 3, 13));
        inputB = addElement(GuiGasGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT_2), this, 25, 13));
        outputA = addElement(GuiGasGauge.getDummy(GaugeType.STANDARD.with(DataType.OUTPUT_1), this, 111, 13));
        outputB = addElement(GuiGasGauge.getDummy(GaugeType.STANDARD.with(DataType.OUTPUT_2), this, 133, 13));
        addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
        addConstantProgress(ProgressType.SMALL_RIGHT, 64, 38);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ValineReactorBaseRecipe recipe, IFocusGroup focusGroup) {
        initChemical(builder, MekanismJEI.TYPE_GAS, RecipeIngredientRole.INPUT, inputA,
                recipe.ingredientA.getRepresentations());
        initChemical(builder, MekanismJEI.TYPE_GAS, RecipeIngredientRole.INPUT, inputB,
                recipe.ingredientB.getRepresentations());
        initChemical(builder, MekanismJEI.TYPE_GAS, RecipeIngredientRole.OUTPUT, outputA,
                List.of(recipe.getOutputA()));
        initChemical(builder, MekanismJEI.TYPE_GAS, RecipeIngredientRole.OUTPUT, outputB,
                List.of(recipe.getOutputB()));
    }

}
