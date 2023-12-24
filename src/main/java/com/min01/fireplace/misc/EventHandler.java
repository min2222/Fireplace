package com.min01.fireplace.misc;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.EntityCarrotFang;
import com.min01.fireplace.entity.EntityKaratFeng;
import com.min01.fireplace.entity.EntitySnowyFeng;
import com.min01.fireplace.init.FireplaceEntities;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Fireplace.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler
{
    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) 
    {
    	event.put(FireplaceEntities.KARAT_FENG.get(), EntityKaratFeng.createAttributes().build());
    	event.put(FireplaceEntities.CARROT_FANG.get(), EntityCarrotFang.createAttributes().build());
    	event.put(FireplaceEntities.SNOWY_FENG.get(), EntitySnowyFeng.createAttributes().build());
    }
}
