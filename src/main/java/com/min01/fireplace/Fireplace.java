package com.min01.fireplace;

import com.min01.fireplace.config.FireplaceConfig;
import com.min01.fireplace.init.FireplaceEffects;
import com.min01.fireplace.init.FireplaceEntities;
import com.min01.fireplace.init.FireplaceItems;
import com.min01.fireplace.network.FireplaceNetwork;
import com.min01.fireplace.sound.FireplaceSounds;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Fireplace.MODID)
public class Fireplace 
{
	public static final String MODID = "fireplace";
	
	public Fireplace() 
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		FireplaceEntities.ENTITY_TYPES.register(bus);
		FireplaceItems.ITEMS.register(bus);
		FireplaceSounds.SOUNDS.register(bus);
		FireplaceEffects.EFFECTS.register(bus);
		
		FireplaceNetwork.registerMessages();
        FireplaceConfig.loadConfig(FireplaceConfig.CONFIG, FMLPaths.CONFIGDIR.get().resolve("fireplace.toml").toString());
	}
}
