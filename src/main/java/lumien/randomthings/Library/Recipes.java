package lumien.randomthings.Library;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import lumien.randomthings.Blocks.ModBlocks;
import lumien.randomthings.Configuration.ConfigBlocks;
import lumien.randomthings.Configuration.ConfigItems;
import lumien.randomthings.Items.ModItems;

public class Recipes
{
	public static void init()
	{
		ItemStack iObsidian = new ItemStack(Block.getBlockFromName("obsidian"));
		ItemStack iEnderChest = new ItemStack(net.minecraft.init.Blocks.ender_chest);
		ItemStack iNetherstar = new ItemStack(Items.nether_star);
		ItemStack iEmerald = new ItemStack(Items.emerald);
		ItemStack iGlassPane = new ItemStack(Blocks.glass_pane);
		ItemStack iGlassBottle = new ItemStack(Items.glass_bottle);
		ItemStack iLapislazuli = new ItemStack(Items.dye, 1, 4);
		ItemStack iDirt = new ItemStack(Blocks.dirt);
		ItemStack iBonemeal = new ItemStack(Items.dye, 1, 15);
		ItemStack iRottenflesh = new ItemStack(Items.rotten_flesh);
		ItemStack iHopper = new ItemStack(Blocks.hopper);
		ItemStack iRedstone = new ItemStack(Items.redstone);
		ItemStack iEnderPearl = new ItemStack(Items.ender_pearl);
		ItemStack iGlowstone = new ItemStack(Items.glowstone_dust);
		ItemStack iDiamond = new ItemStack(Items.diamond);
		ItemStack iPaper = new ItemStack(Items.paper);
		ItemStack iStone = new ItemStack(Blocks.stone);
		ItemStack iRedstoneTorch = new ItemStack(Blocks.redstone_torch);
		ItemStack iRedstoneRepeater = new ItemStack(Items.repeater);
		ItemStack iLeather = new ItemStack(Items.leather);
		ItemStack iString = new ItemStack(Items.string);
		ItemStack iFlint = new ItemStack(Items.flint);
		ItemStack iSnowball = new ItemStack(Items.snowball);
		ItemStack iQuartz = new ItemStack(Items.quartz);
		ItemStack iGlass = new ItemStack(Blocks.glass);
		ItemStack iIronIngot = new ItemStack(Items.iron_ingot);
		ItemStack iSoulSand = new ItemStack(Blocks.soul_sand);
		ItemStack iRedstoneBlock = new ItemStack(Blocks.redstone_block);
		ItemStack iLever = new ItemStack(Blocks.lever);
		ItemStack iDiamondBlock = new ItemStack(Blocks.diamond_block);

		ItemStack iPlayerCore = new ItemStack(ModItems.ingredients, 1, 0);
		ItemStack iObsidianStick = new ItemStack(ModItems.ingredients, 1, 1);
		ItemStack iEnderFragment = new ItemStack(ModItems.ingredients,1,2);
		ItemStack iEctoplasm = new ItemStack(ModItems.ingredients,1,3);
		ItemStack iSpectreIron = new ItemStack(ModItems.ingredients,1,4);
		ItemStack iTransformationCore = new ItemStack(ModItems.ingredients,1,5);

		// Crafting Items
		GameRegistry.addRecipe(new ShapedOreRecipe(iPlayerCore, "xlx", "lel", "xlx", 'l', iLapislazuli, 'e', iEmerald));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ingredients, 3, 1), "o", "o", 'o', iObsidian));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ingredients, 4, 2), "fe", 'e', iEnderPearl,'f',iFlint));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ingredients,1,4),"ei",'e',iEctoplasm,'i',iIronIngot));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.ingredients,1,5), "dyeRed","dyeOrange","dyeYellow","dyeGreen","dyeCyan","dyePurple","dyeGray","dyeLime","dyeMagenta"));

		if (ConfigBlocks.playerInterface)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.playerInterface), "oeo", "omo", "ono", 'o', iObsidian, 'e', iEnderChest, 'm', iPlayerCore, 'n', iNetherstar));
		if (ConfigBlocks.fluidDisplay)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.fluidDisplay, 8), "ggg", "gbg", "ggg", 'g', iGlassPane, 'b', iGlassBottle));
		if (ConfigBlocks.fertilizedDirt)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.fertilizedDirt, 1), "brb", "rdr", "brb", 'b', iBonemeal, 'r', iRottenflesh, 'd', iDirt));
		if (ConfigBlocks.itemCollector)
		{
			ItemStack iItemCollector = new ItemStack(ModBlocks.itemCollector);
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.itemCollector, 1), "xhx", "rer", "xox", 'h', iHopper, 'r', iRedstone, 'e', iEnderPearl, 'o', "stickObsidian"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.advancedItemCollector, 1), "xdx", "gig", 'd', iDiamond, 'g', iGlowstone, 'i', iItemCollector));
		}
		if (ConfigBlocks.onlineDetector)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.onlineDetector, 1), "sts", "rer", "sts", 's', iStone, 't', iRedstoneTorch, 'r', iRedstone, 'e', iLapislazuli));
		if (ConfigBlocks.moonSensor)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.moonSensor,1),"ggg","lql","sss",'g',iGlass,'l',iLapislazuli,'s',"slabWood",'q',iQuartz));
		if (ConfigBlocks.lapisLamp)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.spectreLamp,4,0),"isi","sls","isi",'s',iGlass,'i',iIronIngot,'l',iLapislazuli));
		if (ConfigBlocks.wirelessLever)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.wirelessLever),"r","l",'r',iRedstoneBlock,'l',iLever));
		if (ConfigBlocks.dyeingMachine)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.dyeingMachine),"wpw","ptp","wpw",'w',new ItemStack(Blocks.wool,1,OreDictionary.WILDCARD_VALUE),'p',"plankWood",'t',iTransformationCore));
		
		
		if (ConfigItems.voidStone)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.voidStone, 1), "xox", "oeo", "xox", 'o', iStone, 'e', iEnderPearl));
		if (ConfigItems.dropFilter)
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.dropFilter, 1, 0), "lsl", "sfs", "lsl", 'l', iLeather, 's', iString, 'f', iFlint));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.dropFilter, 1, 1), "fe", 'f', new ItemStack(ModItems.dropFilter, 1, 0), 'e', ConfigItems.voidStone ? ModItems.voidStone : iEnderPearl));
		}
		if (ConfigItems.enderLetter)
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.enderLetter,1,0),"fpl",'f',iEnderFragment,'p',iPaper,'l',iLeather));
		}
		if (ConfigItems.spectreArmor)
			addArmorRecipes(iSpectreIron,new ItemStack(ModItems.spectreHelmet),new ItemStack(ModItems.spectreChestplate),new ItemStack(ModItems.spectreLeggings),new ItemStack(ModItems.spectreBoots));
		if (ConfigItems.spectreSword)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.spectreSword),"xsx","xsx","xox",'s',iSpectreIron,'o',"stickObsidian"));
		if (ConfigItems.spectreKey)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.spectreKey),"xsx","xtx",'s',iSpectreIron,'t',"stickWood"));
		if (ConfigItems.magneticForce)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.magneticForce),"xex","xmx","xpx",'e',iEnderPearl,'m',iEmerald,'p',iPaper));
		if (ConfigItems.spiritBinder)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.spiritBinder),"ses","ene","ses",'s',iSoulSand,'e',iEnderPearl,'n',iNetherstar));
		if (ConfigItems.soundRecorder)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.soundRecorder),"xrx","wiw","wiw",'r',iRedstoneTorch,'w',"plankWood",'i',iIronIngot));
		if (ConfigItems.biomeCapsule)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.biomeCapsule),"dte","qgq","ooo",'d',iDiamondBlock,'e',iEmerald,'t',iTransformationCore,'q',iQuartz,'g',iGlass,'o',iObsidian));
		if (ConfigItems.biomePainter)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.biomePainter),"xtx","xwx","xox",'t',iTransformationCore,'w',new ItemStack(Blocks.wool,1,OreDictionary.WILDCARD_VALUE),'o',"stickObsidian"));
		if (ConfigItems.ginto)
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ginto,16,0),"igi","iii",'i',iSpectreIron,'g',iGlass));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ginto,8,1),"ggg","geg","ggg",'g',new ItemStack(ModItems.ginto,1,0),'e',iEctoplasm));
		}
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.filter, 1, 0), "xrx", "rpr", "xrx", 'r', "dyeRed", 'p', iPaper));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.filter, 1, 1), "xrx", "rpr", "xrx", 'r', "dyeYellow", 'p', iPaper));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.filter, 1, 2), "xrx", "rpr", "xrx", 'r', "dyeBlue", 'p', iPaper));
	}
	
	private static void addArmorRecipes(ItemStack material,ItemStack helmet,ItemStack chestPlate,ItemStack leggings,ItemStack boots)
	{
		GameRegistry.addRecipe(helmet,"mmm","mxm",'m',material);
		GameRegistry.addRecipe(chestPlate,"mxm","mmm","mmm",'m',material);
		GameRegistry.addRecipe(leggings,"mmm","mxm","mxm",'m',material);
		GameRegistry.addRecipe(boots,"mxm","mxm",'m',material);
	}
}
