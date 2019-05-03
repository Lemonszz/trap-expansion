package party.lemons.trapexpansion.block;

import net.minecraft.block.Block;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.BlockStateContainer;
import party.lemons.lemonlib.item.IItemModel;
import party.lemons.trapexpansion.misc.DamageSourceSpike;
import party.lemons.trapexpansion.sound.TrapExpansionSounds;

import java.util.List;
import java.util.Random;

/**
 * Created by Sam on 18/08/2018.
 */
public class BlockSpikeTrapVertical extends Block implements IItemModel, ILiquidContainer, IBucketPickupHandler
{
	protected static final VoxelShape AABB_UP = VoxelShapes.create(0.0D, 0.0D, 0.0D, 1.0D, 0.1D, 1.0D);
	protected static final VoxelShape AABB_DOWN = VoxelShapes.create(0.0D, 0.9D, 0.0D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB FULL_BLOCK_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

	public static final IntegerProperty OUT = IntegerProperty.create("out", 0, 2);
	public static final DirectionProperty DIRECTION = DirectionProperty.create("direction", EnumFacing.Plane.VERTICAL);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public BlockSpikeTrapVertical(Properties properties)
	{
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(OUT, 0).with(DIRECTION, EnumFacing.UP).with(WATERLOGGED, false));
	}

	public BlockSpikeTrapVertical(Properties properties, boolean child)
	{
		super(properties);
	}

	public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		if (!worldIn.isRemote && entityIn.isAlive())
		{
			int i = state.get(OUT);

			if (i == 0)
			{
				this.updateState(worldIn, pos, state, i);
			}

			if(i == 2 && worldIn.getGameTime() % 5 == 0)
			{
				entityIn.attackEntityFrom(DamageSourceSpike.SPIKE, 3);
			}
		}
	}

	@Override
	public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune)
	{
		return Item.getItemFromBlock(TrapExpansionBlocks.SPIKE_TRAP);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(TrapExpansionBlocks.SPIKE_TRAP);
	}


	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(worldIn));
	}

	@Override
	public void tick(IBlockState state, World worldIn, BlockPos pos, Random random)
	{
		if (!worldIn.isRemote)
		{
			int i = state.get(OUT);

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

		worldIn.setBlockState(pos, state.with(OUT, endValue));
		worldIn.markBlockRangeForRenderUpdate(pos, pos);
		if(endValue != 2 || !powered)
			worldIn.getPendingBlockTicks().scheduleTick(new BlockPos(pos), this, this.tickRate(worldIn));
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

	@Override
	public void onBlockAdded(IBlockState state, World worldIn, BlockPos pos, IBlockState oldState)
	{
		if(state.get(OUT) > 0 || worldIn.isBlockPowered(pos))
			worldIn.getPendingBlockTicks().scheduleTick(new BlockPos(pos), this, this.tickRate(worldIn));
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
		return blockState.get(OUT);
	}

	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		if(state.get(DIRECTION) == EnumFacing.UP)
			return AABB_UP;

		return AABB_DOWN;
	}

	@Override
	public VoxelShape getRenderShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return super.getRenderShape(state, worldIn, pos);
	}

	public boolean isFullCube(IBlockState state)
	{
		return false;
	}


	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context)
	{
		EnumFacing facing = context.getFace();
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());

		if(facing == EnumFacing.DOWN)
			return this.getDefaultState().with(DIRECTION, EnumFacing.DOWN).with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));

		switch(facing)
		{
			case NORTH:
			case WEST:
			case SOUTH:
			case EAST:
				return TrapExpansionBlocks.SPIKE_TRAP_WALL.getDefaultState().with(BlockSpikeTrapWall.DIRECTION_WALL, facing).with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
		}



		return this.getDefaultState().with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
	}


	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
	{
		builder.add(OUT, DIRECTION, WATERLOGGED);
	}

	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		if(state.get(DIRECTION) == EnumFacing.UP && face == EnumFacing.DOWN)
			return BlockFaceShape.SOLID;

		if(state.get(DIRECTION) == EnumFacing.DOWN && face == EnumFacing.UP)
			return BlockFaceShape.SOLID;


		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public ResourceLocation getModelLocation()
	{
		return getRegistryName();
	}


	////// Water logging stuff

	public boolean receiveFluid(IWorld worldIn, BlockPos pos, IBlockState state, IFluidState fluidStateIn) {
		if (!state.get(WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER) {
			if (!worldIn.isRemote()) {
				worldIn.setBlockState(pos, state.with(WATERLOGGED, Boolean.valueOf(true)), 3);
				worldIn.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
			}

			return true;
		} else {
			return false;
		}
	}

	public Fluid pickupFluid(IWorld worldIn, BlockPos pos, IBlockState state) {
		if (state.get(WATERLOGGED)) {
			worldIn.setBlockState(pos, state.with(WATERLOGGED, Boolean.valueOf(false)), 3);
			return Fluids.WATER;
		} else {
			return Fluids.EMPTY;
		}
	}

	public IFluidState getFluidState(IBlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, IBlockState state, Fluid fluidIn) {
		return !state.get(WATERLOGGED) && fluidIn == Fluids.WATER;
	}

	public IBlockState updatePostPlacement(IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
}
