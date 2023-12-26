package com.min01.fireplace;

import com.min01.fireplace.config.FireplaceConfig;
import com.min01.fireplace.init.FireplaceEffects;
import com.min01.fireplace.init.FireplaceEntities;
import com.min01.fireplace.init.FireplaceItems;
import com.min01.fireplace.network.FireplaceNetwork;
import com.min01.fireplace.proxy.ClientProxy;
import com.min01.fireplace.proxy.CommonProxy;
import com.min01.fireplace.sound.FireplaceSounds;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod(Fireplace.MODID)
public class Fireplace 
{
	public static final String MODID = "fireplace";
	public static IEventBus MOD_EVENT_BUS;
	public static SimpleChannel wrapper;
	public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	
	public Fireplace() 
	{
		MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
		MOD_EVENT_BUS.addListener(this::commonSetup);
		MOD_EVENT_BUS.addListener(this::completeSetup);
		FireplaceEntities.ENTITY_TYPES.register(MOD_EVENT_BUS);
		FireplaceItems.ITEMS.register(MOD_EVENT_BUS);
		FireplaceSounds.SOUNDS.register(MOD_EVENT_BUS);
		FireplaceEffects.EFFECTS.register(MOD_EVENT_BUS);
		FireplaceNetwork.registerMessages();
        FireplaceConfig.loadConfig(FireplaceConfig.CONFIG, FMLPaths.CONFIGDIR.get().resolve("fireplace.toml").toString());
	}
	
	private void commonSetup(FMLCommonSetupEvent event)
    {
		
    }
	
	private void completeSetup(FMLLoadCompleteEvent event)
    {
		
    }
}
