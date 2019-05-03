package party.lemons.trapexpansion.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Sam on 18/08/2018.
 */
public class BlockSpiderProof extends Block
{
	private static final double OFFSET_AMT = 0.1D;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(OFFSET_AMT, OFFSET_AMT, OFFSET_AMT, 16.0D - OFFSET_AMT, 16.0D - OFFSET_AMT, 16.0D - OFFSET_AMT);
	protected static final VoxelShape SHAPE_FULL = Block.makeCuboidShape(0, 0, 0, 16.0D, 16.0D, 16.0D);


	public BlockSpiderProof(Properties properties)
	{
		super(properties);
	}

	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return SHAPE_FULL;
	}
	public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return SHAPE;
	}

	@Override
	public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		super.onEntityCollision(state, worldIn, pos, entityIn);

		if(entityIn instanceof EntitySpider)
		{
			((EntitySpider) entityIn).setBesideClimbableBlock(false);
		}
	}

	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.SOLID;
	}


	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		TextComponentTranslation translation = new TextComponentTranslation("trapexpansion.tip.spiderproof");
		translation.setStyle(new Style().setColor(TextFormatting.DARK_GRAY));

		tooltip.add(translation);
	}
}
