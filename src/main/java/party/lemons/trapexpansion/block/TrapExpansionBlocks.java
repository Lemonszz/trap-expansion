package party.lemons.trapexpansion.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import party.lemons.lemonlib.block.BlockRegistry;
import party.lemons.trapexpansion.TrapExpansion;
import party.lemons.trapexpansion.block.tileentity.TileEntityDetector;
import party.lemons.trapexpansion.block.tileentity.TileEntityFan;

import static net.minecraft.block.Block.Properties.create;


/**
 * Created by Sam on 18/08/2018.
 */
@Mod.EventBusSubscriber(modid = TrapExpansion.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(TrapExpansion.MODID)
public class TrapExpansionBlocks
{
	public static final Block SPIKE_TRAP = Blocks.AIR;
	public static final Block SPIKE_TRAP_WALL = Blocks.AIR;
	public static final Block SLIPPERY_STONE = Blocks.AIR;
	public static final Block FAN = Blocks.AIR;
	public static final Block DETECTOR = Blocks.AIR;

	//TODO: move these to another class or something
	public static TileEntityType<TileEntityDetector> DETECTOR_TYPE = null;
	public static TileEntityType<TileEntityFan> FAN_TYPE = null;

	@SubscribeEvent
	public static void onRegisterBlock(RegistryEvent.Register<Block> event)
	{
		BlockRegistry.setup(TrapExpansion.MODID, event.getRegistry());

		BlockRegistry.registerBlock(new BlockSpikeTrapVertical(create(Material.IRON).hardnessAndResistance(0.5F, 1.5F)), "spike_trap");
		BlockRegistry.registerBlock(new BlockSpikeTrapWall(create(Material.IRON).hardnessAndResistance(0.5F, 1.5F)), "spike_trap_wall");
		BlockRegistry.registerBlock(new BlockSlipperyStone(create(Material.IRON).slipperiness(0.97F).sound(SoundType.GLASS).hardnessAndResistance(0.5F, 1.5F)), "slippery_stone");
		BlockRegistry.registerBlock(new BlockFan(create(Material.IRON).hardnessAndResistance(0.5F, 1.5F)), "fan");
		BlockRegistry.registerBlock(new BlockDetector(create(Material.IRON).hardnessAndResistance(0.5F, 1.5F)), "detector");
	}

	@SubscribeEvent
	public static void onRegisterTileEntity(RegistryEvent.Register<TileEntityType<?>> event)
	{
		TileEntityType<TileEntityDetector> detector = TileEntityType.Builder.create(TileEntityDetector::new).build(null);
		detector.setRegistryName(new ResourceLocation(TrapExpansion.MODID, "detector"));
		DETECTOR_TYPE = detector;

		TileEntityType<TileEntityFan> fan = TileEntityType.Builder.create(TileEntityFan::new).build(null);
		fan.setRegistryName(new ResourceLocation(TrapExpansion.MODID, "fan"));
		FAN_TYPE = fan;

		event.getRegistry().registerAll(
			detector, fan
		);
	}
}
