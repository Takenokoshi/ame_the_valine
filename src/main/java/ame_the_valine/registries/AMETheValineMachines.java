package ame_the_valine.registries;

import java.util.Set;

import ame_the_valine.AMETheValineConstants;
import ame_the_valine.AMETheValineLang;
import ame_the_valine.blockentity.BlockEntityValineReactor;
import astral_mekanism.enums.AMEUpgrade;
import astral_mekanism.registration.MachineDeferredRegister;
import astral_mekanism.registration.MachineRegistryObject;
import mekanism.api.Upgrade;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.generators.common.registries.GeneratorsSounds;

public class AMETheValineMachines {
    public static final MachineDeferredRegister MACHINES = new MachineDeferredRegister(AMETheValineConstants.MODID);

    public static final MachineRegistryObject<BlockEntityValineReactor, ?, MekanismTileContainer<BlockEntityValineReactor>, ?> VALINE_REACTOR = MACHINES
            .registerSimple("valine_reactor",
                    BlockEntityValineReactor::new,
                    BlockEntityValineReactor.class,
                    AMETheValineLang.VALINE_REACTOR_DESCRIPTION,
                    builder -> builder
                            .withSupportedUpgrades(Set.of(Upgrade.MUFFLING, AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                            .withSound(GeneratorsSounds.GAS_BURNING_GENERATOR));
}
