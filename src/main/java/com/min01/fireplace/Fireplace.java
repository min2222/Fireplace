package com.min01.fireplace;

import com.min01.fireplace.config.FireplaceConfig;
import com.min01.fireplace.effect.FireplaceEffects;
import com.min01.fireplace.entity.FireplaceEntities;
import com.min01.fireplace.item.FireplaceItems;
import com.min01.fireplace.network.FireplaceNetwork;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Fireplace.MODID)
public class Fireplace 
{
	public static final String MODID = "fireplace";
	
	public Fireplace() 
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext ctx = ModLoadingContext.get();
		FireplaceEntities.ENTITY_TYPES.register(bus);
		FireplaceItems.ITEMS.register(bus);
		FireplaceEffects.EFFECTS.register(bus);
		
		FireplaceNetwork.registerMessages();
		ctx.registerConfig(Type.COMMON, FireplaceConfig.CONFIG_SPEC, "fireplace.toml");
	}
}
