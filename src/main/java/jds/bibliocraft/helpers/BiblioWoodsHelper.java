package jds.bibliocraft.helpers;

import jds.bibliocraft.blocks.BlockBookcase;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Mod;

public class BiblioWoodsHelper {
    public static CreativeTabs creativeTab;
    /*
    //id edition
    public BiblioWoodsHelper() {


    }

    */



    //  generic edition like done in ebt

    //  ModBlocks -> all the planks in the mod
    //  ModSlabs  -> all the slabs in the mod
    //  ModTextures -> all the textures in the mod
    //  CreativeTabName -> defines the Tabname String
    //  CreativeTabTexture  -> defines the texture used in the creative tab, pulled from ModTextures
    public BiblioWoodsHelper(Block[] ModBlocks, Block[] ModSlabs, String[] ModTextures, String CreativeTabName, int CreativeTabTexture) {


        // check if there are as many slabs as there are planks, if not it could cause lacking recipes
        if (ModBlocks.length != ModSlabs.length) return;

        //check if the blocks given even exsist, to again avoid lacking recipes or missing textures too
        for (int i = 0; i < ModBlocks.length; i++)
            if (ModBlocks[i] == null || ModSlabs[i] == null) return;

        ItemStack icon = new ItemStack(BlockBookcase.instance, 1, 6);
        NBTTagCompound tags = new NBTTagCompound();
        tags.setString("renderTexture", ModTextures[CreativeTabTexture]);
        icon.setTagCompound(tags);
        creativeTab = new BiblioWoodHelperTab("bibliowoodebttab", ModTextures, icon);
        for (int i = 0; i < ModTextures.length; i++)
        {
            RegisterCustomFramedBlocks reg = new RegisterCustomFramedBlocks(ModTextures[i]);
            reg.registerRecipies(new ItemStack(ModBlocks[i], 1), new ItemStack(ModSlabs[i], 1));
        }

    }


}
