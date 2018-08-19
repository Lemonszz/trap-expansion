package party.lemons.trapexpansion.block;

/**
 * Created by Sam on 18/08/2018.
 */

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.trapexpansion.block.tileentity.TileEntityFan;
import party.lemons.trapexpansion.item.IModel;

import java.util.Random;

public class BlockFan extends BlockDirectional implements IModel
{
	public static final PropertyBool POWERED = PropertyBool.create("powered");

	public BlockFan()
	{
		super(Material.ROCK);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH).withProperty(POWERED, Boolean.valueOf(false)));
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(stateIn.getValue(POWERED) && rand.nextInt(3) == 0)
		{
			EnumFacing facing = stateIn.getValue(FACING);
			double xPos = pos.offset(facing).getX() + rand.nextFloat();
			double yPos = pos.offset(facing).getY() + rand.nextFloat();
			double zPos = pos.offset(facing).getZ() + rand.nextFloat();

			worldIn.spawnParticle(EnumParticleTypes.CLOUD, xPos, yPos, zPos, facing.getXOffset() / 2F, facing.getYOffset() / 2F, facing.getZOffset() / 2F);
		}
	}
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		boolean powered = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());

		if (powered)
		{
			worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
			worldIn.setBlockState(pos, state.withProperty(POWERED, true));
		}
		else
		{
			if(state.getValue(POWERED))
			{
				worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
				worldIn.setBlockState(pos, state.withProperty(POWERED, false));
			}
		}
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		if(worldIn.isBlockPowered(pos))
		{
			worldIn.scheduleUpdate(new BlockPos(pos), this, this.tickRate(worldIn));
			worldIn.setBlockState(pos, state.withProperty(POWERED, true));
		}
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityFan();
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING, POWERED);
	}

	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
	}

	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
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