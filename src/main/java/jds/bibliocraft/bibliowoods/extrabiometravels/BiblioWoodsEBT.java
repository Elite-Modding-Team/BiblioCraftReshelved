package jds.bibliocraft.bibliowoods.extrabiometravels;

import jds.bibliocraft.blocks.BlockBookcase;
import jds.bibliocraft.helpers.BiblioWoodHelperTab;
import jds.bibliocraft.helpers.RegisterCustomFramedBlocks;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BiblioWoodsEBT {
    public static CreativeTabs creativeTab;

    public static final Logger LOGGER = LogManager.getLogger("reshelved");

    public static void init()
    {
        LOGGER.info("ebt is farting!!!!!");

        if (Loader.isModLoaded("ebt"))
        {

            Block planks1 = Block.REGISTRY.getObject(new ResourceLocation("natura:overworld_planks"));
            Block planks2 = Block.REGISTRY.getObject(new ResourceLocation("natura:nether_planks"));
            Block slabs1 = Block.REGISTRY.getObject(new ResourceLocation("natura:overworld_slab"));
            Block slabs2 = Block.REGISTRY.getObject(new ResourceLocation("natura:overworld_slab2"));
            Block slabs3 = Block.REGISTRY.getObject(new ResourceLocation("natura:nether_slab"));

            if (planks1 == null || planks2 == null || slabs1 == null || slabs2 == null || slabs3 == null) return;

            Block[] planks = {planks1, planks1, planks1, planks1, planks1, planks1, planks1, planks1, planks1, planks2, planks2, planks2, planks2};
            int[] plankID = {0,1,2,3,4,5,6,7,8,0,1,2,3};
            Block[] slabs = {slabs1, slabs1, slabs1, slabs1, slabs1, slabs2, slabs2, slabs2, slabs2, slabs3, slabs3, slabs3, slabs3};
            int[] slabID = {0,1,2,3,4,0,1,2,3,0,1,2,3};

            String[] textures = {
                    "ebt:blocks/autumnal_planks",
                    "ebt:blocks/bald_cypress_planks",
                    "ebt:blocks/cypress_planks",
                    "ebt:blocks/douglas_fir_planks",
                    "ebt:blocks/japanese_maple_planks",
                    "ebt:blocks/peach_sakura_planks",
                    "ebt:blocks/rainbow_eucalyptus_planks",
                    "ebt:blocks/scalewood_planks",
                    "ebt:blocks/wattle_planks",
                    "ebt:blocks/white_fir_planks",
            };

            ItemStack icon = new ItemStack(BlockBookcase.instance, 1, 6);
            NBTTagCompound tags = new NBTTagCompound();
            tags.setString("renderTexture", "ebt:blocks/autumnal_planks");
            icon.setTagCompound(tags);
            creativeTab = new BiblioWoodHelperTab("bibliowoodebttab", textures, icon);
            for (int i = 0; i < textures.length; i++)
            {
                RegisterCustomFramedBlocks reg = new RegisterCustomFramedBlocks(textures[i]);
                reg.registerRecipies(new ItemStack(planks[i], 1, plankID[i]), new ItemStack(slabs[i], 1, slabID[i]));
            }

        }
    }
}
