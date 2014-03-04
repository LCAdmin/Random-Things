package lumien.randomthings.Handler;

import java.util.ArrayList;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import lumien.randomthings.RandomThings;
import lumien.randomthings.Blocks.BlockFertilizedDirt;
import lumien.randomthings.Blocks.ModBlocks;
import lumien.randomthings.Network.Packets.PacketInfusedBlocks;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;

public class RTEventHandler
{
	@SubscribeEvent
	public void useHoe(UseHoeEvent event)
	{
		if (event.world.getBlock(event.x, event.y, event.z) == ModBlocks.fertilizedDirt)
		{
			event.setResult(Result.ALLOW);
			event.world.setBlock(event.x, event.y, event.z, ModBlocks.fertilizedDirtTilled);
			event.world.playSoundEffect((double) ((float) event.x + 0.5F), (double) ((float) event.y + 0.5F), (double) ((float) event.z + 0.5F), ModBlocks.fertilizedDirtTilled.stepSound.getStepResourcePath(), (ModBlocks.fertilizedDirtTilled.stepSound.getVolume() + 1.0F) / 2.0F, ModBlocks.fertilizedDirtTilled.stepSound.getPitch() * 0.8F);
		}
	}

	@SubscribeEvent
	@SideOnly(Side.SERVER)
	public void entityJoinedWorld(EntityJoinWorldEvent event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			ArrayList<Vector3d> toSend = new ArrayList<Vector3d>();
			
			for (Vector4d v:RedstoneHandler.poweredBlocks)
			{
				toSend.add(new Vector3d(v.x,v.y,v.z));
			}
			
			RandomThings.packetPipeline.sendTo(new PacketInfusedBlocks(player.dimension,true,toSend),(EntityPlayerMP) player);
		}
	}
}