package lumien.randomthings.Core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class RTCreativeTab extends CreativeTabs
{

	public RTCreativeTab()
	{
		super ("Random Things");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
		return Item.getItemFromBlock(Blocks.bedrock);
	}

}