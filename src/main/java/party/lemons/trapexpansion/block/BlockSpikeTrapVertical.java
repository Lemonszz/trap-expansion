package party.lemons.trapexpansion.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import party.lemons.trapexpansion.item.IModel;
import party.lemons.trapexpansion.misc.DamageSourceSpike;
import party.lemons.trapexpansion.sound.TrapExpansionSounds;

import java.util.List;
import java.util.Random;

/**
 * Created by Sam on 18/08/2018.
 */
public class BlockSpikeTrapVertical extends Block implements IModel
{
	protected static final AxisAlignedBB AABB_UP = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1D, 1.0D);
	protected static final AxisAlignedBB AABB_DOWN = new AxisAlignedBB(0.0D, 0.9D, 0.0D, 1.0D, 1.0D, 1.0D);

	public static final PropertyInteger OUT = PropertyInteger.create("out", 0, 2);
	public static final PropertyDirection DIRECTION = PropertyDirection.create("direction", EnumFacing.Plane.VERTICAL);

	public BlockSpikeTrapVertical()
	{
		super(Material.IRON);
		this.setDefaultState(this.blockState.getBaseState().withProperty(OUT, 0).withProperty(DIRECTION, EnumFacing.UP));
	}

	public BlockSpikeTrapVertical(boolean child)
	{
		super(Material.IRON);
	}

	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if (!worldIn.isRemote && !entityIn.isDead)
		{
			int i = state.getValue(OUT);

			if (i == 0)
			{
				this.updateState(worldIn, pos, state, i);
			}

			if(i == 2 && worldIn.getTotalWorldTime() % 5 == 0)
			{
				entityIn.attackEntityFrom(DamageSourceSpike.SPIKE, 3);
			}
		}
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(TrapExpansionBlocks.SPIKE_TRAP);
	}

	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(TrapExpansionBlocks.SPIKE_TRAP);
	}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
	}

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			int i = state.getValue(OUT);

			if (i > 0 || worldIn.isBlockPowered(pos))
			{
				this.updateState(worldIn, pos, state, i);
			}
		}
	}

	protected void updateState(World worldIn, BlockPos pos, IBlockState state, int outValue)
	{
		int change = 0;
		boolean powered = worldIn.isBlockPowered(pos);

		if(!powered && !hasEntity(worldIn, pos, state))
		{
			change = -1;
		}
		else if(outValue < 2)
		{
			change = 1;
		}

		int endValue = Math.max(0, outValue + change);
		if(change != 0)
		{
			SoundEvent sound = TrapExpansionSounds.SPIKE_OUT_1;
			if(endValue == 2)
				sound = TrapExpansionSounds.SPIKE_OUT_2;

			worldIn.playSound(null, pos, sound, SoundCategory.BLOCKS, 1F, 0.5F + (worldIn.rand.nextFloat() / 2));
		}

		worldIn.setBlockState(pos, state.withProperty(OUT, endValue));
		worldIn.markBlockRangeForRenderUpdate(pos, pos);
		if(endValue != 2 || !powered)
			worldIn.scheduleUpdate(new BlockPos(pos), this, this.tickRate(worldIn));
	}

	protected boolean hasEntity(World worldIn, BlockPos pos, IBlockState state)
	{
		List<? extends Entity > list;
		list = worldIn.getEntitiesWithinAABB(Entity.class, FULL_BLOCK_AABB.offset(pos));
		if (!list.isEmpty())
		{
			for (Entity entity : list)
			{
				if (!entity.doesEntityNotTriggerPressurePlate())
				{
					return true;
				}
			}
		}
		return false;
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		if(state.getValue(OUT) > 0 || worldIn.isBlockPowered(pos))
			worldIn.scheduleUpdate(new BlockPos(pos), this, this.tickRate(worldIn));
	}

	public int tickRate(World worldIn)
	{
		return 5;
	}

	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}

	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return blockState.getValue(OUT);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		if(state.getValue(DIRECTION) == EnumFacing.UP)
			return AABB_UP;

		return AABB_DOWN;
	}

	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return getBoundingBox(blockState, worldIn, pos);
	}

	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	public IBlockState getStateFromMeta(int meta)
	{
		if(meta <= 2)
			return this.getDefaultState().withProperty(OUT, meta);

		return this.getStateFromMeta(meta - 3).withProperty(DIRECTION, EnumFacing.DOWN);
	}

	public int getMetaFromState(IBlockState state)
	{
		if(state.getValue(DIRECTION) == EnumFacing.UP)
			return state.getValue(OUT);
		else
			return 3 + state.getValue(OUT);
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		if(facing == EnumFacing.DOWN)
			return this.getDefaultState().withProperty(DIRECTION, EnumFacing.DOWN);

		switch(facing)
		{
			case NORTH:
			case WEST:
			case SOUTH:
			case EAST:
				return TrapExpansionBlocks.SPIKE_TRAP_WALL.getDefaultState().withProperty(BlockSpikeTrapWall.DIRECTION_WALL, facing);
		}

		return this.getDefaultState();
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, OUT, DIRECTION);
	}

	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		if(state.getValue(DIRECTION) == EnumFacing.UP && face == EnumFacing.DOWN)
			return BlockFaceShape.SOLID;

		if(state.getValue(DIRECTION) == EnumFacing.DOWN && face == EnumFacing.UP)
			return BlockFaceShape.SOLID;


		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public ResourceLocation getModelLocation()
	{
		return getRegistryName();
	}
}
