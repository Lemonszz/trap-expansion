package party.lemons.trapexpansion.block.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import party.lemons.trapexpansion.block.BlockDetector;

import java.util.List;

/**
 * Created by Sam on 20/08/2018.
 */
public class TileEntityDetector extends TileEntity implements ITickable
{
	private static final int STEP_TIME = 4;
	private static final int RANGE = 5;

	@Override
	public void update()
	{
		if(world.getTotalWorldTime() % STEP_TIME == 0 && !world.isRemote)
		{
			IBlockState state = world.getBlockState(pos);

			if(!(state.getBlock() instanceof BlockDetector))
				return;

			EnumFacing facing = state.getValue(BlockDetector.FACING);
			AxisAlignedBB bb = new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(pos.offset(facing)).expand(facing.getXOffset() * RANGE, facing.getYOffset() * RANGE, facing.getZOffset() * RANGE);
			List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, bb);

			int entityCount = entities.size();
			boolean hasEntity = entityCount > 0;

			if(hasEntity)
			{
				for(int i = 0; i < entities.size(); i++)
				{
					Entity e = entities.get(i);

					int xCheck = facing.getXOffset() * (MathHelper.floor(e.posX) - this.pos.getX());
					int yCheck = facing.getYOffset() * (MathHelper.floor(e.posY) - this.pos.getY());
					int zCheck = facing.getZOffset() * (MathHelper.floor(e.posZ) - this.pos.getZ());

					for(int b = 1; b < Math.abs(xCheck + yCheck + zCheck); b++)
					{
						if(world.isBlockFullCube(this.pos.offset(facing, b)))
						{
							entityCount--;
							if(entityCount <= 0)
							{
								hasEntity = false;
								break;
							}
						}
					}
				}
			}

			boolean powered = state.getValue(BlockDetector.POWERED);

			if(powered != hasEntity)
			{
				world.setBlockState(pos, state.withProperty(BlockDetector.POWERED, hasEntity));
			}
		}
	}
}