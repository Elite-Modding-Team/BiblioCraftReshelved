package jds.bibliocraft.jei;

import jds.bibliocraft.Config;
import jds.bibliocraft.blocks.blockitems.BlockItemClipboard;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;

@SuppressWarnings("deprecation")
@JEIPlugin
public class BiblioCraftJeiPlugin implements IModPlugin {
    @Override
    public void register(IModRegistry registry) {
        if (!Config.enableClipboard) {
            return;
        }

        registry.getJeiHelpers().getItemBlacklist().addItemToBlacklist(new ItemStack(BlockItemClipboard.instance));
    }
}
