package party.lemons.trapexpansion.sound;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import party.lemons.trapexpansion.TrapExpansion;

/**
 * Created by Sam on 18/08/2018.
 */
@Mod.EventBusSubscriber(modid = TrapExpansion.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(TrapExpansion.MODID)
public class TrapExpansionSounds
{
	public static final SoundEvent SPIKE_OUT_1 = SoundEvents.BLOCK_ANVIL_BREAK;
	public static final SoundEvent SPIKE_OUT_2 = SoundEvents.BLOCK_ANVIL_BREAK;

	@SubscribeEvent
	public static void onRegisterSound(RegistryEvent.Register<SoundEvent> event)
	{
		event.getRegistry().registerAll(
				new SoundEvent(new ResourceLocation(TrapExpansion.MODID, "spike_out_1")).setRegistryName(TrapExpansion.MODID, "spike_out_1"),
				new SoundEvent(new ResourceLocation(TrapExpansion.MODID, "spike_out_2")).setRegistryName(TrapExpansion.MODID, "spike_out_2")
		);
	}
}
