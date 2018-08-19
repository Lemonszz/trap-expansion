package party.lemons.trapexpansion.misc;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import party.lemons.trapexpansion.block.TrapExpansionBlocks;

/**
 * Created by Sam on 19/08/2018.
 */
public class TrapExpansionTab extends CreativeTabs
{
	public static final CreativeTabs INSTANCE = new TrapExpansionTab();

	public TrapExpansionTab()
	{
		super("trapexpansion");
	}

	@Override
	public ItemStack createIcon()
	{
		return new ItemStack(TrapExpansionBlocks.SPIKE_TRAP);
	}
}
