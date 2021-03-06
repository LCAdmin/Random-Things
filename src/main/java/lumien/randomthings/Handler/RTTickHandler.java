package lumien.randomthings.Handler;

import lumien.randomthings.RandomThings;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class RTTickHandler
{
	@SubscribeEvent
	public void tick(TickEvent event)
	{
		switch (event.side)
		{
			case SERVER:
				serverTick(event);
				break;
			case CLIENT:
				clientTick(event);
				break;
			default:
				break;
		}
	}

	private void clientTick(TickEvent event)
	{
		switch (event.phase)
		{
			case START:
				RandomThings.instance.notificationHandler.update();
				break;
			case END:
				break;
		}
	}

	public static long lastTime = 0;

	private void serverTick(TickEvent event)
	{
		switch (event.phase)
		{
			case START:
				if (event.type == TickEvent.Type.SERVER)
				{
					MagneticForceHandler.INSTANCE.update();

					RandomThings.instance.letterHandler.update();
					if (RandomThings.instance.spectreHandler != null)
					{
						RandomThings.instance.spectreHandler.update();
					}
				}
				break;
			case END:
				break;

		}
	}
}
