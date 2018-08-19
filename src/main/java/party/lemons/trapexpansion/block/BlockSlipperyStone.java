package party.lemons.trapexpansion.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import party.lemons.trapexpansion.item.IModel;

/**
 * Created by Sam on 18/08/2018.
 */
public class BlockSlipperyStone extends BlockSpiderProof implements IModel
{
	public BlockSlipperyStone()
	{
		super(Material.ROCK);

		this.slipperiness = 0.97F;
		this.setSoundType(SoundType.GLASS);
	}

	@Override
	public ResourceLocation getModelLocation()
	{
		return getRegistryName();
	}
}
