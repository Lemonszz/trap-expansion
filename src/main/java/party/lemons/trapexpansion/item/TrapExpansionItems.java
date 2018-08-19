package party.lemons.trapexpansion.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import party.lemons.trapexpansion.TrapExpansion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 18/08/2018.
 */
@Mod.EventBusSubscriber(modid = TrapExpansion.MODID)
@GameRegistry.ObjectHolder(TrapExpansion.MODID)
public class TrapExpansionItems
{
	public static List<Item> itemList = new ArrayList<>();

	@SubscribeEvent
	public static void onRegisterItem(RegistryEvent.Register<Item> event)
	{

	}

	public static void registerItem(IForgeRegistry<Item> registry, Item item, String name)
	{
		item.setTranslationKey(TrapExpansion.MODID + "." + name);
		item.setRegistryName(TrapExpansion.MODID, name);

		itemList.add(item);
		registry.register(item);
	}
}
