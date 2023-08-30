package com.min01.fireplace.misc;

import com.min01.fireplace.Main;

import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandlerForge 
{
	@SubscribeEvent
	public static void onRenderLivingPre(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event)
	{
		
	}
}
