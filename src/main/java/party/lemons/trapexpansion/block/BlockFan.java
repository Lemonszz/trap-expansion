package party.lemons.trapexpansion.block;

/**
 * Created by Sam on 18/08/2018.
 */

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Particles;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import party.lemons.lemonlib.item.IItemModel;
import party.lemons.trapexpansion.block.tileentity.TileEntityFan;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockFan extends BlockDirectional implements IItemModel
{
	public static final BooleanProperty POWERED = BooleanProperty.create("powered");

	public BlockFan(Properties properties)
	{
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.SOUTH).with(POWERED, Boolean.valueOf(false)));
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(stateIn.get(POWERED) && rand.nextInt(3) == 0)
		{
			EnumFacing facing = stateIn.get(FACING);
			double xPos = pos.offset(facing).getX() + rand.nextFloat();
			double yPos = pos.offset(facing).getY() + rand.nextFloat();
			double zPos = pos.offset(facing).getZ() + rand.nextFloat();

			worldIn.spawnParticle(Particles.CLOUD, xPos, yPos, zPos, facing.getXOffset() / 2F, facing.getYOffset() / 2F, facing.getZOffset() / 2F);
		}
	}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		boolean powered = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());

		if (powered)
		{
			worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(worldIn));
			worldIn.setBlockState(pos, state.with(POWERED, true));
		}
		else
		{
			if(state.get(POWERED))
			{
				worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(worldIn));
				worldIn.setBlockState(pos, state.with(POWERED, false));
			}
		}
	}

	public void onBlockAdded(IBlockState state, World worldIn, BlockPos pos, IBlockState p_196259_4_)
	{
		if(worldIn.isBlockPowered(pos))
		{
			worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(worldIn));
			worldIn.setBlockState(pos, state.with(POWERED, true));
		}
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
		return new TileEntityFan();
	}

	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
	{
		builder.add(FACING, POWERED);
	}

	public IBlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
	}
}