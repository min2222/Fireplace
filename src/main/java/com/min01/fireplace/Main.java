package com.min01.fireplace;

import com.min01.fireplace.config.FireplaceConfig;
import com.min01.fireplace.init.FireplaceEntities;
import com.min01.fireplace.init.FireplaceItems;
import com.min01.fireplace.proxy.ClientProxy;
import com.min01.fireplace.proxy.CommonProxy;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod(Main.MODID)
public class Main 
{
	public static final String MODID = "fireplace";
	public static IEventBus MOD_EVENT_BUS;
	public static SimpleChannel wrapper;
	public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	
	public Main() 
	{
		MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
		MOD_EVENT_BUS.addListener(this::CommonSetup);
		MOD_EVENT_BUS.addListener(this::CompleteSetup);
		FireplaceEntities.ENTITY_TYPES.register(MOD_EVENT_BUS);
		FireplaceItems.ITEMS.register(MOD_EVENT_BUS);
        FireplaceConfig.loadConfig(FireplaceConfig.common_config, FMLPaths.CONFIGDIR.get().resolve("fireplace-common.toml").toString());
	}
	
	private void CommonSetup(FMLCommonSetupEvent event)
    {
		
    }
	
	private void CompleteSetup(FMLLoadCompleteEvent event)
    {
		
    }
}
