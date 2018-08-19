package party.lemons.trapexpansion.misc;

import net.minecraft.util.DamageSource;
import party.lemons.trapexpansion.TrapExpansion;

/**
 * Created by Sam on 19/08/2018.
 */
public class DamageSourceSpike extends DamageSource
{
	public static DamageSourceSpike SPIKE = new DamageSourceSpike();

	public DamageSourceSpike()
	{
		super(TrapExpansion.MODID + ".spike");
	}
}
