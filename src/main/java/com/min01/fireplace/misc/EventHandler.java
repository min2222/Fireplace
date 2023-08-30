package com.min01.fireplace.misc;

import com.min01.fireplace.Main;
import com.min01.fireplace.entity.EntityKaratFeng;
import com.min01.fireplace.init.ModEntityType;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler
{
    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) 
    {
    	event.put(ModEntityType.KARAT_FENG.get(), EntityKaratFeng.createAttributes().build());
    }
}
