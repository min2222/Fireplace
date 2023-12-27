package com.min01.fireplace;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.min01.fireplace.config.FireplaceConfig;
import com.min01.fireplace.init.FireplaceEffects;
import com.min01.fireplace.init.FireplaceEntities;
import com.min01.fireplace.init.FireplaceItems;
import com.min01.fireplace.network.FireplaceNetwork;
import com.min01.fireplace.proxy.ClientProxy;
import com.min01.fireplace.proxy.CommonProxy;
import com.min01.fireplace.sound.FireplaceSounds;

import net.minecraft.ChatFormatting;
import net.minecraft.world.BossEvent.BossBarColor;
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
	public static ChatFormatting ORANGE_COLOR;
	public static BossBarColor ORANGE;
	
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
		try 
		{
			Constructor<ChatFormatting> colorConstructor = ChatFormatting.class.getDeclaredConstructor(String.class, Character.class, Boolean.class, Integer.class, Integer.class);
			colorConstructor.setAccessible(true);
			ChatFormatting color = colorConstructor.newInstance("orange", 'g', false, 16, 16744448);
			ORANGE_COLOR = color;
			
			Constructor<BossBarColor> barConstructor = BossBarColor.class.getDeclaredConstructor(String.class, ChatFormatting.class);
			barConstructor.setAccessible(true);
			BossBarColor bar = barConstructor.newInstance("orange", ORANGE_COLOR);
			ORANGE = bar;
		} 
		catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
    }
	
	private void completeSetup(FMLLoadCompleteEvent event)
    {
		
    }
}
