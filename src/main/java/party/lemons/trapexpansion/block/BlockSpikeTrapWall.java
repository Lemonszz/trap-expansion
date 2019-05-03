package party.lemons.trapexpansion.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

/**
 * Created by Sam on 18/08/2018.
 */
public class BlockSpikeTrapWall extends BlockSpikeTrapVertical
{
	protected static final VoxelShape AABB_NORTH = VoxelShapes.create(0.0D, 0.0D, 1.0D, 1.0D, 1.0D, 0.9D);
	protected static final VoxelShape AABB_SOUTH = VoxelShapes.create(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1D);
	protected static final VoxelShape AABB_WEST = VoxelShapes.create(1D, 0D, 0.0D, 0.9D, 1.0D, 1.0D);
	protected static final VoxelShape AABB_EAST = VoxelShapes.create(0.0D, 0D, 0.0D, 0.1D, 1D, 1.0D);

	public static final DirectionProperty DIRECTION_WALL = DirectionProperty.create("direction", EnumFacing.Plane.HORIZONTAL);

	public BlockSpikeTrapWall(Properties properties)
	{
		super(properties, true);

		this.setDefaultState(this.stateContainer.getBaseState().with(OUT, 0).with(DIRECTION_WALL, EnumFacing.NORTH).with(WATERLOGGED, false));
	}

	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		switch(state.get(DIRECTION_WALL))
		{
			case NORTH:
				return AABB_NORTH;
			case SOUTH:
				return AABB_SOUTH;
			case WEST:
				return AABB_WEST;
			case EAST:
				return AABB_EAST;
			default:
				return AABB_EAST;
		}
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
	{
		builder.add(OUT, DIRECTION_WALL, WATERLOGGED);
	}

	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		if(face == state.get(DIRECTION_WALL).getOpposite())
			return BlockFaceShape.SOLID;

		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean hasModel()
	{
		return false;
	}
}
