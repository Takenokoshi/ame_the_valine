package ame_the_valine;

import mekanism.api.text.ILangEntry;
import net.minecraft.Util;

public class AMETheValineLang implements ILangEntry {

    private final String key;

    private AMETheValineLang(String type, String path) {
        this.key = Util.makeDescriptionId(type, AMETheValineConstants.rl(path));
    }

    @Override
    public String getTranslationKey() {
        return key;
    }

    public static final AMETheValineLang VALINE_REACTOR_DESCRIPTION = new AMETheValineLang("description", "valine_reactor");
    public static final AMETheValineLang AME_THE_VALINE = new AMETheValineLang("title", "name");
}
