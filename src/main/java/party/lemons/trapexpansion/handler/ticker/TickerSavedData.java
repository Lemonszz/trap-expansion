package party.lemons.trapexpansion.handler.ticker;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import party.lemons.trapexpansion.TrapExpansion;

/**
 * Created by Sam on 6/05/2018.
 */
public class TickerSavedData extends WorldSavedData
{
	public static final String DATA_NAME = TrapExpansion.MODID + "_TickerData";

	public TickerSavedData(String name)
	{
		super(name);
	}

	public TickerSavedData()
	{
		super(DATA_NAME);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList tickers = nbt.getTagList("tickers", Constants.NBT.TAG_COMPOUND);
		TickerHandler.readFromNBT(tickers);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setTag("tickers", TickerHandler.writeToNBT());

		return compound;
	}

	public static TickerSavedData get(World world) {
		MapStorage storage = world.getPerWorldStorage();
		TickerSavedData instance = (TickerSavedData) storage.getOrLoadData(TickerSavedData.class, DATA_NAME);

		if (instance == null) {
			instance = new TickerSavedData();
			storage.setData(DATA_NAME, instance);
		}
		return instance;
	}
}