package party.lemons.trapexpansion.block;

import net.minecraft.util.ResourceLocation;
import party.lemons.lemonlib.item.IItemModel;

/**
 * Created by Sam on 18/08/2018.
 */
public class BlockSlipperyStone extends BlockSpiderProof implements IItemModel
{
	public BlockSlipperyStone(Properties properties)
	{
		super(properties);
	}

	@Override
	public ResourceLocation getModelLocation()
	{
		return getRegistryName();
	}
}
