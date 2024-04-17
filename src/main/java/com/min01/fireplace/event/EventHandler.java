package com.min01.fireplace.event;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.EntityAlienFeng;
import com.min01.fireplace.entity.EntityCarrotFang;
import com.min01.fireplace.entity.EntityEvokerFeng;
import com.min01.fireplace.entity.EntityFireFeng;
import com.min01.fireplace.entity.EntityKaratFeng;
import com.min01.fireplace.entity.EntityNecroFeng;
import com.min01.fireplace.entity.EntitySantaFeng;
import com.min01.fireplace.entity.EntitySnowyFeng;
import com.min01.fireplace.entity.EntityUFO;
import com.min01.fireplace.entity.EntityVampireFeng;
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
    	event.put(FireplaceEntities.EVOKER_FENG.get(), EntityEvokerFeng.createAttributes().build());
    	event.put(FireplaceEntities.SANTA_FENG.get(), EntitySantaFeng.createAttributes().build());
    	event.put(FireplaceEntities.VAMPIRE_FENG.get(), EntityVampireFeng.createAttributes().build());
    	event.put(FireplaceEntities.FIRE_FENG.get(), EntityFireFeng.createAttributes().build());
    	event.put(FireplaceEntities.NECRO_FENG.get(), EntityNecroFeng.createAttributes().build());
    	event.put(FireplaceEntities.ALIEN_FENG.get(), EntityAlienFeng.createAttributes().build());
    	event.put(FireplaceEntities.UFO.get(), EntityUFO.createAttributes().build());
    }
}
