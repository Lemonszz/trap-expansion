package party.lemons.trapexpansion.block;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import party.lemons.trapexpansion.block.tileentity.TileEntityDetector;
import party.lemons.trapexpansion.item.IModel;

/**
 * Created by Sam on 20/08/2018.
 */
public class BlockDetector extends BlockDirectional implements IModel
{
	public static final PropertyBool POWERED = PropertyBool.create("powered");

	public BlockDetector()
	{
		super(Material.ROCK);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH).withProperty(POWERED, Boolean.valueOf(false)));
	}

	public boolean canProvidePower(IBlockState state)
	{
		return true;
	}

	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return blockState.getWeakPower(blockAccess, pos, side);
	}

	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return blockState.getValue(POWERED).booleanValue() && blockState.getValue(FACING) == side ? 15 : 0;
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityDetector();
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING, POWERED);
	}

	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
	}

	public int getMetaFromState(IBlockState state)
	{
		int i = 0;
		i = i | state.getValue(FACING).getIndex();

		if(state.getValue(POWERED).booleanValue())
		{
			i |= 8;
		}

		return i;
	}

	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta & 7)).withProperty(POWERED, Boolean.valueOf((meta & 8) > 0));
	}

	@Override
	public ResourceLocation getModelLocation()
	{
		return getRegistryName();
	}

}
