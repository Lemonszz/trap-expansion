package party.lemons.trapexpansion.block;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
/**
 * Created by Sam on 18/08/2018.
 */
public class BlockSpikeTrapWall extends BlockSpikeTrapVertical
{
	protected static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.0D, 0.0D, 1.0D, 1.0D, 1.0D, 0.9D);
	protected static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1D);
	protected static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(1D, 0D, 0.0D, 0.9D, 1.0D, 1.0D);
	protected static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.0D, 0D, 0.0D, 0.1D, 1D, 1.0D);

	public static final PropertyDirection DIRECTION_WALL = PropertyDirection.create("direction", EnumFacing.Plane.HORIZONTAL);

	public BlockSpikeTrapWall()
	{
		super(true);

		this.setDefaultState(this.blockState.getBaseState().withProperty(OUT, 0).withProperty(DIRECTION_WALL, EnumFacing.NORTH));
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		switch(state.getValue(DIRECTION_WALL))
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

	public IBlockState getStateFromMeta(int meta)
	{
		//TODO: work out better way to do this
		switch(meta)
		{
			default:
			case 0:
				return getDefaultState();
			case 1:
				return getDefaultState().withProperty(DIRECTION_WALL, EnumFacing.SOUTH);
			case 2:
				return getDefaultState().withProperty(DIRECTION_WALL, EnumFacing.EAST);
			case 3:
				return getDefaultState().withProperty(DIRECTION_WALL, EnumFacing.WEST);
			case 4:
				return getDefaultState().withProperty(OUT, 1);
			case 5:
				return getDefaultState().withProperty(DIRECTION_WALL, EnumFacing.SOUTH).withProperty(OUT, 1);
			case 6:
				return getDefaultState().withProperty(DIRECTION_WALL, EnumFacing.EAST).withProperty(OUT, 1);
			case 7:
				return getDefaultState().withProperty(DIRECTION_WALL, EnumFacing.WEST).withProperty(OUT, 1);
			case 8:
				return getDefaultState().withProperty(OUT, 2);
			case 9:
				return getDefaultState().withProperty(DIRECTION_WALL, EnumFacing.SOUTH).withProperty(OUT, 2);
			case 10:
				return getDefaultState().withProperty(DIRECTION_WALL, EnumFacing.EAST).withProperty(OUT, 2);
			case 11:
				return getDefaultState().withProperty(DIRECTION_WALL, EnumFacing.WEST).withProperty(OUT, 2);
		}
	}

	public int getMetaFromState(IBlockState state)
	{
		int meta = 0;

		//TODO: Better way of this
		switch(state.getValue(OUT))
		{
			default:
			case 0:
				meta = 0;
				break;
			case 1:
				meta = 4;
				break;
			case 2:
				meta = 8;
				break;
		}
		meta += getMetaForDirection(state.getValue(DIRECTION_WALL));

		return meta;
	}

	private int getMetaForDirection(EnumFacing direction)
	{
		switch(direction)
		{
			default:
			case NORTH:
				return 0;
			case SOUTH:
				return 1;
			case EAST:
				return 2;
			case WEST:
				return 3;
		}
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, OUT, DIRECTION_WALL);
	}

	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		if(face == state.getValue(DIRECTION_WALL).getOpposite())
			return BlockFaceShape.SOLID;

		return BlockFaceShape.UNDEFINED;
	}

	public boolean hasModel()
	{
		return false;
	}
}
