package party.lemons.trapexpansion.item;

import net.minecraft.util.ResourceLocation;

/**
 * Created by Sam on 18/08/2018.
 */
public interface IModel
{
	default boolean hasModel(){
		return true;
	}

	ResourceLocation getModelLocation();
}
