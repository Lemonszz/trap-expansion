package party.lemons.trapexpansion.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import party.lemons.lemonlib.item.IItemModel;
import party.lemons.trapexpansion.block.tileentity.TileEntityDetector;

import javax.annotation.Nullable;

/**
 * Created by Sam on 20/08/2018.
 */
public class BlockDetector extends BlockDirectional implements IItemModel
{
	public static final BooleanProperty POWERED = BooleanProperty.create("powered");

	public BlockDetector(Properties properties)
	{
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.SOUTH).with(POWERED, Boolean.valueOf(false)));
	}

	public boolean canProvidePower(IBlockState state)
	{
		return true;
	}

	public int getStrongPower(IBlockState blockState, IBlockReader blockAccess, BlockPos pos, EnumFacing side)
	{
		return blockState.getWeakPower(blockAccess, pos, side);
	}

	public int getWeakPower(IBlockState blockState, IBlockReader blockAccess, BlockPos pos, EnumFacing side)
	{
		return blockState.get(POWERED).booleanValue() && blockState.get(FACING) == side ? 15 : 0;
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(IBlockState state, IBlockReader world)
	{
		return new TileEntityDetector();
	}

	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(FACING, POWERED);
	}

	public IBlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
	}


	@Override
	public ResourceLocation getModelLocation()
	{
		return getRegistryName();
	}

}
