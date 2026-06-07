package ame_the_valine.gui;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import ame_the_valine.blockentity.BlockEntityValineReactor;
import ame_the_valine.jei.AMETheValineJEIPlugin;
import mekanism.api.math.FloatingLong;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.util.text.EnergyDisplay;
import mekanism.generators.common.GeneratorsLang;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiValineReactor
        extends GuiConfigurableTile<BlockEntityValineReactor, MekanismTileContainer<BlockEntityValineReactor>> {

    public GuiValineReactor(MekanismTileContainer<BlockEntityValineReactor> container, Inventory inv,
            Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiEnergyTab(this, () -> List.of(
                GeneratorsLang.PRODUCING_AMOUNT.translate(EnergyDisplay.of(tile.getEnergyGenerated())),
                MekanismLang.MAX_OUTPUT.translate(EnergyDisplay.of(FloatingLong.MAX_VALUE)))));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 164, 15));
        addRenderableWidget(new GuiGasGauge(tile::getInputTankA,
                () -> tile.getGasTanks(null),
                GaugeType.STANDARD, this, 3, 13));
        addRenderableWidget(new GuiGasGauge(tile::getInputTankB,
                () -> tile.getGasTanks(null),
                GaugeType.STANDARD, this, 25, 13));
        addRenderableWidget(new GuiGasGauge(tile::getOutputTankA,
                () -> tile.getGasTanks(null),
                GaugeType.STANDARD, this, 111, 13));
        addRenderableWidget(new GuiGasGauge(tile::getOutputTankB,
                () -> tile.getGasTanks(null),
                GaugeType.STANDARD, this, 133, 13));
        addRenderableWidget(new GuiProgress(tile::getActive, ProgressType.SMALL_RIGHT, this, 64, 38))
                .jeiCategories(AMETheValineJEIPlugin.VALINE_REACTOR_JEI_TYPE);
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        float widthThird = imageWidth / 3F;
        drawTextScaledBound(guiGraphics, title, widthThird - 7, titleLabelY, titleTextColor(), 2 * widthThird);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
