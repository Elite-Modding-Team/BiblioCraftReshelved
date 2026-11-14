package jds.bibliocraft.bibliowoods.extrabiometravels;

import jds.bibliocraft.helpers.BiblioWoodsHelper;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BiblioWoodsEBT {
    public static CreativeTabs creativeTab;

    public static final Logger LOGGER = LogManager.getLogger("reshelved");

    public static void init()
    {


        if (Loader.isModLoaded("ebt"))
        {

            Block[] EBTPlanks =  {
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:autumnal_planks")),
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:bald_cypress_planks")),
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:cypress_planks")),
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:douglas_fir_planks")),
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:japanese_maple_planks")),
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:peach_sakura_planks")),
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:rainbow_eucalyptus_planks")),
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:scalewood_planks")),
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:wattle_planks")),
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:white_fir_planks"))
            };

            Block[] EBTSlabs =  {
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:autumnal_slab")),
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:bald_cypress_slab")),
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:cypress_slab")),
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:douglas_fir_slab")),
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:japanese_maple_slab")),
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:peach_sakura_slab")),
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:rainbow_eucalyptus_slab")),
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:scalewood_slab")),
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:wattle_slab")),
                    Block.REGISTRY.getObject(new ResourceLocation("ebt:white_fir_slab"))
            };



            //if (planks1 == null || planks2 == null || slabs1 == null || slabs2 == null || slabs3 == null) return;


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


            new BiblioWoodsHelper(EBTPlanks, EBTSlabs, textures, "bibliowoodebttab", 0);


        }
    }
}
