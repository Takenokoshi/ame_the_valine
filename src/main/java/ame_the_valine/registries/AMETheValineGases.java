package ame_the_valine.registries;

import ame_the_valine.AMETheValineConstants;
import mekanism.api.chemical.gas.Gas;
import mekanism.common.registration.impl.GasDeferredRegister;
import mekanism.common.registration.impl.GasRegistryObject;

public class AMETheValineGases {
    public static final GasDeferredRegister GASES = new GasDeferredRegister(AMETheValineConstants.MODID);

    public static final GasRegistryObject<Gas> VALINE_NAQUADAH_FUEL = GASES.register(
            "valine_naquadah_fuel", 0xC7C7CA);
    public static final GasRegistryObject<Gas> VALINE_URANIUM_FUEL = GASES.register(
            "valine_uranium_fuel", 0x8691B7);
}
