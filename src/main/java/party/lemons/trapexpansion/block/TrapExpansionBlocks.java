package party.lemons.trapexpansion.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import party.lemons.trapexpansion.TrapExpansion;
import party.lemons.trapexpansion.block.tileentity.TileEntityDetector;
import party.lemons.trapexpansion.block.tileentity.TileEntityFan;
import party.lemons.trapexpansion.item.IModel;
import party.lemons.trapexpansion.item.TrapExpansionItems;
import party.lemons.trapexpansion.misc.TrapExpansionTab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 18/08/2018.
 */
@Mod.EventBusSubscriber(modid = TrapExpansion.MODID)
@GameRegistry.ObjectHolder(TrapExpansion.MODID)
public class TrapExpansionBlocks
{
	public static List<Block> blockList = new ArrayList<>();

	public static final Block SPIKE_TRAP = Blocks.AIR;
	public static final Block SPIKE_TRAP_WALL = Blocks.AIR;
	public static final Block SLIPPERY_STONE = Blocks.AIR;
	public static final Block FAN = Blocks.AIR;
	public static final Block DETECTOR = Blocks.AIR;

	@SubscribeEvent
	public static void onRegisterBlock(RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> r = event.getRegistry();

		setProperties(registerBlock(r, new BlockSpikeTrapVertical(), "spike_trap"), 0.5F, 1.5F, 0.0F);
		setProperties(registerBlock(r, new BlockSpikeTrapWall(), "spike_trap_wall"), 0.5F, 1.5F, 0.0F);
		setProperties(registerBlock(r, new BlockSlipperyStone(), "slippery_stone"), 0.5F, 1.5F, 0.0F);
		setProperties(registerBlock(r, new BlockFan(), "fan"), 0.5F, 1.5F, 0.0F);
		setProperties(registerBlock(r, new BlockDetector(), "detector"), 0.5F, 1.5F, 0.0F);

		GameRegistry.registerTileEntity(TileEntityFan.class, new ResourceLocation(TrapExpansion.MODID, "fan"));
		GameRegistry.registerTileEntity(TileEntityDetector.class, new ResourceLocation(TrapExpansion.MODID, "detector"));
	}

	@SubscribeEvent
	public static void onRegisterItem(RegistryEvent.Register<Item> event)
	{
		blockList.stream().filter(b-> (b instanceof IModel) && ((IModel) b).hasModel()).forEach(b -> registerItemBlock(event.getRegistry(), b));
	}

	public static void registerItemBlock(IForgeRegistry<Item> registry, Block block)
	{
		ItemBlock ib = new ItemBlock(block);
		ib.setRegistryName(block.getRegistryName());

		TrapExpansionItems.itemList.add(ib);
		registry.register(ib);
	}

	public static Block setProperties(Block block, float hardness, float resistence, float light)
	{
		return block.setHardness(hardness).setResistance(resistence).setLightLevel(light);
	}

	public static Block registerBlock(IForgeRegistry<Block> registry, Block block, String name)
	{
		return registerBlock(registry, block, name, TrapExpansion.MODID, true);
	}

	public static Block registerBlock(IForgeRegistry<Block> registry, Block block, String name, String domain, boolean addDomainToUnloc)
	{
		String unloc = addDomainToUnloc ? (domain + ".") : "";

		block.setTranslationKey(unloc + name);
		block.setRegistryName(domain, name);
		block.setCreativeTab(TrapExpansionTab.INSTANCE);

		blockList.add(block);
		registry.register(block);

		return block;
	}
}
