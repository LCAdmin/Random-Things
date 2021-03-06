package lumien.randomthings.Handler;

import static org.lwjgl.opengl.GL11.glPointSize;

import java.util.Arrays;
import java.util.HashSet;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static org.lwjgl.opengl.GL11.*;

import lumien.randomthings.RandomThings;
import lumien.randomthings.Blocks.ModBlocks;
import lumien.randomthings.Client.RenderUtils;
import lumien.randomthings.Client.Renderer.RenderBlut;
import lumien.randomthings.Configuration.ConfigItems;
import lumien.randomthings.Configuration.RTConfiguration;
import lumien.randomthings.Configuration.Settings;
import lumien.randomthings.Configuration.VanillaChanges;
import lumien.randomthings.Entity.EntityHealingOrb;
import lumien.randomthings.Entity.EntitySpirit;
import lumien.randomthings.Handler.Spectre.SpectreHandler;
import lumien.randomthings.Items.ItemDropFilter;
import lumien.randomthings.Items.ItemFilter;
import lumien.randomthings.Items.ItemSpectreArmor;
import lumien.randomthings.Items.ItemWhiteStone;
import lumien.randomthings.Items.ModItems;
import lumien.randomthings.Library.Colors;
import lumien.randomthings.Library.PotionEffects;
import lumien.randomthings.Network.PacketHandler;
import lumien.randomthings.Network.Messages.MessageBloodMoon;
import lumien.randomthings.Proxy.ClientProxy;
import lumien.randomthings.TileEntities.TileEntityFogGenerator;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.WorldEvent;

public class RTEventHandler
{
	@SubscribeEvent
	public void useHoe(UseHoeEvent event)
	{
		if (event.world.getBlock(event.x, event.y, event.z) == ModBlocks.fertilizedDirt)
		{
			event.setResult(Result.ALLOW);
			event.world.setBlock(event.x, event.y, event.z, ModBlocks.fertilizedDirtTilled);
			event.world.playSoundEffect(event.x + 0.5F, event.y + 0.5F, event.z + 0.5F, ModBlocks.fertilizedDirtTilled.stepSound.getStepResourcePath(), (ModBlocks.fertilizedDirtTilled.stepSound.getVolume() + 1.0F) / 2.0F, ModBlocks.fertilizedDirtTilled.stepSound.getPitch() * 0.8F);
		}
	}

	@SubscribeEvent
	public void onConfigChange(OnConfigChangedEvent event)
	{
		RTConfiguration.onConfigChange(event);
	}

	
	// Body Width: 0.25
	// Body Height: 0.375
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderPlayerPost(RenderPlayerEvent.Post event)
	{
		RenderBlut.renderBlut(event);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void fogDensity(FogDensity event)
	{
		if (event.entity.worldObj.isRemote)
		{
			if (Minecraft.getMinecraft().thePlayer.getHealth() < 20f)
			{
				event.density = 10f;
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderPre(RenderLivingEvent.Pre event)
	{
		// GL11.glColor3f(0, 0, 1);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void toolTip(ItemTooltipEvent event)
	{
		if (event.itemStack.stackTagCompound != null)
		{
			if (event.itemStack.stackTagCompound.hasKey("customRTColor"))
			{
				int dyeColor = event.itemStack.stackTagCompound.getInteger("customRTColor");
				String dye = dyeColor + "";

				int indexDye;
				if ((indexDye = getDyeFromColor(dyeColor)) != -1)
				{
					dye = I18n.format("text.color." + ItemDye.field_150923_a[indexDye], new Object[] {});
				}

				event.toolTip.add(I18n.format("text.dyed", new Object[] { dye }));
			}
		}
	}

	private int getDyeFromColor(int color)
	{
		for (int i = 0; i < ItemDye.field_150922_c.length; i++)
		{
			if (ItemDye.field_150922_c[i] == color)
			{
				return i;
			}
		}

		return -1;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void soundPlayed(PlaySoundEvent17 event)
	{
		RandomThings.instance.soundRecorderHandler.soundPlayed(event);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderPlayerPre(RenderLivingEvent.Pre event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entity;

			ItemStack helmet = player.getCurrentArmor(0);
			ItemStack chestplate = player.getCurrentArmor(1);
			ItemStack leggings = player.getCurrentArmor(2);
			ItemStack boots = player.getCurrentArmor(3);

			if (helmet != null && chestplate != null && leggings != null && boots != null)
			{
				if (helmet.getItem() instanceof ItemSpectreArmor && chestplate.getItem() instanceof ItemSpectreArmor && leggings.getItem() instanceof ItemSpectreArmor && boots.getItem() instanceof ItemSpectreArmor)
				{
					glEnable(GL_BLEND);
					glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
					glColor4f(1, 1, 1, 0.6f);
				}
			}
		}
	}

	@SubscribeEvent
	public void worldLoad(WorldEvent.Load event)
	{
		if (event.world.isRemote && VanillaChanges.LOCKED_GAMMA)
		{
			Minecraft.getMinecraft().gameSettings.setOptionFloatValue(GameSettings.Options.GAMMA, 0);
			Minecraft.getMinecraft().gameSettings.gammaSetting = 0;
		}
	}

	@SubscribeEvent
	public void changedDimension(PlayerChangedDimensionEvent event)
	{
		if (event.toDim == Settings.SPECTRE_DIMENSON_ID)
		{
			EntityPlayer player = event.player;
			player.getEntityData().setInteger("oldDimension", event.fromDim);
			player.getEntityData().setDouble("oldPosX", player.posX);
			player.getEntityData().setDouble("oldPosY", player.posY);
			player.getEntityData().setDouble("oldPosZ", player.posZ);
		}
	}

	@SubscribeEvent
	public void loadWorld(WorldEvent.Load event)
	{
		if (!event.world.isRemote && event.world.provider.dimensionId == Settings.SPECTRE_DIMENSON_ID)
		{
			SpectreHandler spectreHandler = (SpectreHandler) event.world.mapStorage.loadData(SpectreHandler.class, "SpectreHandler");
			if (spectreHandler == null)
			{
				spectreHandler = new SpectreHandler();
				spectreHandler.markDirty();
			}

			spectreHandler.setWorld(event.world);

			event.world.mapStorage.setData("SpectreHandler", spectreHandler);
			RandomThings.instance.spectreHandler = spectreHandler;
		}
	}

	@SubscribeEvent
	public void itemPickUp(EntityItemPickupEvent event)
	{
		if (ConfigItems.dropFilter)
		{
			if (event.entityPlayer != null)
			{
				InventoryPlayer playerInventory = event.entityPlayer.inventory;
				if (playerInventory != null)
				{
					for (int slot = 0; slot < playerInventory.getSizeInventory(); slot++)
					{
						ItemStack is = playerInventory.getStackInSlot(slot);
						if (is != null && is.getItem() instanceof ItemDropFilter)
						{
							IInventory inventory = ItemDropFilter.getDropFilterInv(event.entityPlayer, is);
							if (inventory != null)
							{
								inventory.openInventory();
								ItemStack itemFilter = inventory.getStackInSlot(0);
								if (itemFilter != null)
								{
									if (ItemFilter.matchesItem(itemFilter, event.item.getEntityItem()))
									{
										switch (is.getItemDamage())
										{
											case 0:
												event.item.delayBeforeCanPickup = 20;
												break;
											case 1:
												event.item.delayBeforeCanPickup = 20;
												event.item.age = event.item.lifespan - 10;
												event.entityPlayer.onItemPickup(event.item, 0);
												event.item.worldObj.playSoundAtEntity(event.entityPlayer, "random.pop", 0.2F, ((event.entityPlayer.getRNG().nextFloat() - event.entityPlayer.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
												break;
										}
										event.setCanceled(true);
										return;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent()
	@SideOnly(Side.CLIENT)
	public void preTextureStitch(TextureStitchEvent.Pre event)
	{
		if (event.map.getTextureType() == 1)
		{
			ClientProxy.slimeParticleTexture = event.map.registerIcon("RandomThings:slimeParticle");
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	@SideOnly(Side.CLIENT)
	public void drawGameOverlay(RenderGameOverlayEvent.Post event)
	{
		if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR)
		{
			RandomThings.instance.notificationHandler.drawNotificationOverlay();
		}
	}

	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event)
	{
		if (VanillaChanges.THROWABLES_MOTION)
		{
			if (event.entity instanceof EntityThrowable)
			{
				EntityThrowable throwable = (EntityThrowable) event.entity;
				if (throwable != null && throwable.getThrower() != null)
				{
					throwable.motionX += throwable.getThrower().motionX;
					throwable.motionY += throwable.getThrower().motionY;
					throwable.motionZ += throwable.getThrower().motionZ;
				}
			}
			else if (event.entity instanceof EntityArrow)
			{
				EntityArrow arrow = (EntityArrow) event.entity;
				if (arrow.shootingEntity != null)
				{
					arrow.motionX += arrow.shootingEntity.motionX;
					arrow.motionY += arrow.shootingEntity.motionY;
					arrow.motionZ += arrow.shootingEntity.motionZ;
				}
			}
		}

		if (Settings.BLOOD_MOON && !event.world.isRemote && event.entity instanceof EntityPlayerMP)
		{
			boolean hasBloodMoon = BloodMoonHandler.INSTANCE.hasBloodMoon(event.world.provider.dimensionId);
			PacketHandler.INSTANCE.sendTo(new MessageBloodMoon().setBloodmoon(hasBloodMoon).setDimensionID(event.world.provider.dimensionId), (EntityPlayerMP) event.entity);
		}
	}

	@SubscribeEvent
	public void livingHurt(LivingHurtEvent event)
	{
		if (!event.entityLiving.worldObj.isRemote && event.ammount >= 7)
		{
			Entity attacker = event.source.getSourceOfDamage();
			if (attacker instanceof EntityArrow)
			{
				EntityArrow arrow = (EntityArrow) attacker;
				if (arrow.shootingEntity instanceof EntityPlayer)
				{
					attacker = arrow.shootingEntity;
				}
			}
			if (attacker instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) attacker;

				ItemStack helmet = player.getCurrentArmor(0);
				ItemStack chestplate = player.getCurrentArmor(1);
				ItemStack leggings = player.getCurrentArmor(2);
				ItemStack boots = player.getCurrentArmor(3);

				if (helmet != null && chestplate != null && leggings != null && boots != null)
				{
					if (helmet.getItem() instanceof ItemSpectreArmor && chestplate.getItem() instanceof ItemSpectreArmor && leggings.getItem() instanceof ItemSpectreArmor && boots.getItem() instanceof ItemSpectreArmor)
					{
						player.worldObj.spawnEntityInWorld(new EntityHealingOrb(player.worldObj, event.entityLiving.posX, event.entityLiving.posY + event.entityLiving.height / 2, event.entityLiving.posZ, event.ammount / 10));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void entityDeath(LivingDeathEvent event)
	{
		if (!event.entity.worldObj.isRemote)
		{
			if (ConfigItems.whitestone)
			{
				if (event.entityLiving instanceof EntityPlayer && !event.source.canHarmInCreative())
				{
					EntityPlayer player = (EntityPlayer) event.entityLiving;

					for (int slot = 0; slot < player.inventory.getSizeInventory(); slot++)
					{
						ItemStack is = player.inventory.getStackInSlot(slot);
						if (is != null && is.getItem() instanceof ItemWhiteStone && is.getItemDamage() == 1)
						{
							event.setCanceled(true);

							player.setHealth(1F);

							player.addPotionEffect(new PotionEffect(PotionEffects.REGENERATION, 200, 10, false));
							player.addPotionEffect(new PotionEffect(PotionEffects.RESISTANCE, 200, 5, false));
							player.addPotionEffect(new PotionEffect(PotionEffects.FIRERESISTANCE, 200, 1, false));

							is.setItemDamage(0);
							player.inventory.markDirty();
							player.inventoryContainer.detectAndSendChanges();
							player.worldObj.playSoundAtEntity(player, "randomthings:whiteStoneActivate", 2, 1);
							return;
						}
					}

				}
			}

			if (ConfigItems.spectreArmor || ConfigItems.spectreKey || ConfigItems.spectreSword)
			{
				if (event.source.getEntity() != null && !(event.source.getEntity() instanceof FakePlayer) && event.source.getEntity() instanceof EntityPlayer && !(event.entity instanceof EntitySpirit))
				{
					EntityPlayer player = (EntityPlayer) event.source.getEntity();
					double chance = Settings.SPIRIT_CHANCE;
					if (ConfigItems.spectreSword && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == ModItems.spectreSword)
					{
						chance = Settings.SPIRIT_CHANCE_SWORD;
					}
					double random = Math.random();
					if (random <= chance)
					{
						player.worldObj.spawnEntityInWorld(new EntitySpirit(player.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ));
					}
				}

			}
		}
	}
}
